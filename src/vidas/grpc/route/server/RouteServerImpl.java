package vidas.grpc.route.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.google.protobuf.ByteString;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.stub.StreamObserver;
import route.RouteServiceGrpc.RouteServiceImplBase;
import vidas.grpc.route.server.StateMachine.ServerStateMachine;
import vidas.grpc.route.server.Types.Link;
import vidas.grpc.route.server.Types.Work;


import route.RouteServiceGrpc.RouteServiceImplBase;
import route.FileUploadRequest;
import route.FileUploadResponse;
import route.RouteServiceGrpc;
import route.MetaData;
import route.FileContent;
import route.FileStatus;

public class RouteServerImpl extends RouteServiceImplBase {
	private Server svr;
	protected static Logger logger = LoggerFactory.getLogger("serverImpl");
	private final ConcurrentMap<route.Route, List<route.Route>> routes = new ConcurrentHashMap<route.Route, List<route.Route>>();
	
	private static final Path OUTPUT_PATH = Paths.get(
		"data-storage/server-output");

	/**
	 * Configuration of the server's identity, port, and role
	 */
	private static Properties getConfiguration(final File path) throws IOException {
		if (!path.exists())
			throw new IOException("missing file");

		Properties rtn = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			rtn.load(fis);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		return rtn;
	}

	protected ByteString ack(route.Route msg) {
		// TODO complete processing
		final String blank = msg.toString();// "accepted";
		byte[] raw = blank.getBytes();

		return ByteString.copyFrom(raw);
	}

	public static void main(String[] args) throws Exception {
		// TODO check args!

		String path = args[0];
		try {
			Properties conf = RouteServerImpl.getConfiguration(new File(path));
			Engine.configure(conf);
			Engine.getConf();

			/* Similar to the socket, waiting for a connection */
			final RouteServerImpl impl = new RouteServerImpl();
			impl.start();
			impl.blockUntilShutdown();

		} catch (IOException e) {
			// TODO better error message
			e.printStackTrace();
		}

	}

	private void start() throws Exception {

		Engine engine = Engine.getInstance();

		svr = ServerBuilder.forPort(engine.getServerPort()).addService(new RouteServerImpl()).build();

		Engine.logger.info(
				"Starting Server " + engine.serverName + " | server.id = " + engine.getServerID() + " | server.port = "
						+ engine.getServerPort() + " |");
		svr.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RouteServerImpl.this.stop();
			}
		});
	}

	protected void stop() {
		svr.shutdown();
	}

	private void blockUntilShutdown() throws Exception {
		/* TODO what clean up is required? */
		svr.awaitTermination();

	}

	private boolean verify(route.Route request) {
		return true;
	}



	
//FILE UPLOAD REQUESTS

/**
	 * server received a upload message!
	 */
	@Override
	public StreamObserver<FileUploadRequest> forwardUpload(StreamObserver<FileUploadResponse> responseObserver) {
		logger.info("Receiving file upload request...");
		return new StreamObserver<FileUploadRequest>() {
			// upload context variables
			OutputStream writer = null;
			FileStatus fileStatus = FileStatus.IN_PROGRESS;
			boolean appendOnNextOpen = true;
			long t1 = System.currentTimeMillis();
			String[] cols;
			int cntOfChunks;
			Long cntOfLines = 0L;

			String headerOfFile;

			@Override
			public void onNext(FileUploadRequest fileUploadRequest) {
				if (fileUploadRequest.hasMetadata()) {
					headerOfFile = fileUploadRequest.getMetadata().getHeader();
					cols = headerOfFile.split(",", -1);

					// logger.info("---- Get metadata (Header of the file) ----");
					// logger.info(headerOfFile);
				} else {
					long t3 = System.currentTimeMillis();
					logger.info("---- Received No." + cntOfChunks + " chunk ----");

					// Long lines = writeFile(fileUploadRequest.getFile().getContent(), cols.length);
					// cntOfLines = lines + cntOfLines;

					
					int j = cntOfChunks%3;
					int port = Engine.getInstance().links.get(j).getPort();
					int serverID = Engine.getInstance().links.get(j).getServerID();
					System.out.println("Sending to server " + serverID + " on port " + port + "");
					Engine.getInstance().serverStateMachine.state.sendLeaderFileWriteRequest(port, serverID, fileUploadRequest.getFile().getContent(), headerOfFile);
					
					cntOfChunks++;

					long t4 = System.currentTimeMillis();
					logger.info(
							"---- Finished written No." + cntOfChunks + " chunk (Time: " + (t4 - t3) + ") ----");
				}
			}

			@Override
			public void onError(Throwable t) {
				fileStatus = FileStatus.FAILED;
				logger.info("Request Failed: {}", Status.fromThrowable(t));
				this.onCompleted();
			}

			@Override
			public void onCompleted() {
				logger.info("---- Received " + cntOfChunks + " chunks in total ----");
				logger.info("---- Wrote " + cntOfLines + " lines in total ----");
				// closeFile(writer);
				fileStatus = FileStatus.IN_PROGRESS.equals(fileStatus) ? FileStatus.SUCCESS : fileStatus;
				logger.info("fileStatus: " + fileStatus);
				FileUploadResponse response = FileUploadResponse.newBuilder()
						.setStatus(fileStatus)
						.build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				long t2 = System.currentTimeMillis();
				System.out.println("File receive and partition | Time: " + (t2 - t1) + "ms");
			}
		};
	}



	/**
	 * server received a upload message!
	 */
	@Override
	public StreamObserver<FileUploadRequest> upload(StreamObserver<FileUploadResponse> responseObserver) {
		logger.info("Receiving file upload request...");
		return new StreamObserver<FileUploadRequest>() {
			// upload context variables
			OutputStream writer = null;
			FileStatus fileStatus = FileStatus.IN_PROGRESS;
			boolean appendOnNextOpen = true;
			long t1 = System.currentTimeMillis();
			String[] cols;
			int cntOfChunks;
			Long cntOfLines = 0L;

			@Override
			public void onNext(FileUploadRequest fileUploadRequest) {
				if (fileUploadRequest.hasMetadata()) {
					var headerOfFile = fileUploadRequest.getMetadata().getHeader();
					cols = headerOfFile.split(",", -1);

					// logger.info("---- Get metadata (Header of the file) ----");
					// logger.info(headerOfFile);
					System.out.println("header: " + headerOfFile);
				} else {
					long t3 = System.currentTimeMillis();
					logger.info("---- Received No." + cntOfChunks + " chunk ----");

					Long lines = null;
					try {
						
						lines = writeFile(fileUploadRequest.getFile().getContent(), cols.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cntOfLines = lines + cntOfLines;

					
					// int j = cntOfChunks%4;
					// int port = Engine.getInstance().links.get(j).getPort();
					// int serverID = Engine.getInstance().links.get(j).getServerID();
					// Engine.getInstance().serverStateMachine.state.sendLeaderFileWriteRequest(port, serverID, fileUploadRequest.getFile().getContent(), cols.length);
					
					cntOfChunks++;

					long t4 = System.currentTimeMillis();
					logger.info(
							"---- Finished written No." + cntOfChunks + " chunk (Time: " + (t4 - t3) + ") ----");
				}
			}

			@Override
			public void onError(Throwable t) {
				fileStatus = FileStatus.FAILED;
				logger.info("Request Failed: {}", Status.fromThrowable(t));
				this.onCompleted();
			}

			@Override
			public void onCompleted() {
				logger.info("---- Received " + cntOfChunks + " chunks in total ----");
				logger.info("---- Wrote " + cntOfLines + " lines in total ----");
				// closeFile(writer);
				fileStatus = FileStatus.IN_PROGRESS.equals(fileStatus) ? FileStatus.SUCCESS : fileStatus;
				logger.info("fileStatus: " + fileStatus);
				FileUploadResponse response = FileUploadResponse.newBuilder()
						.setStatus(fileStatus)
						.build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
				long t2 = System.currentTimeMillis();
				System.out.println("File receive and partition | Time: " + (t2 - t1) + "ms");
			}
		};
	}

	private Long writeFile(ByteString content, int colsLength) throws IOException {
		byte[] byteArray = content.toByteArray();

		ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
		InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
		BufferedReader bufferedReader = new BufferedReader(streamReader);

		String line;
		Long numOfLines = 0L;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
			//
			String[] colStrings = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			String vc = colStrings[5];

			// discard records of which the number of fields doesn't equal to number of
			// columns
			if (colStrings.length != colsLength) {
				System.out.println("Drop this line: \n" + line);
				continue;
			}

			// split "Issue Date" and extract month, day and year
			String[] timeArray = colStrings[4].split("/");

			var newDirectory = timeArray[2] + "/" + timeArray[1] + "/" + timeArray[0];
			Path directoryPath = OUTPUT_PATH.resolve(newDirectory);

			// make a new directory if not exist
			File directory = new File(directoryPath.toString());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			directory.setWritable(true);
			Path filePath = directoryPath.resolve(vc + ".csv");

			// write a line to the csv file
			try {
				OutputStream writer = Files.newOutputStream(filePath, StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				writer.write((line + "\n").getBytes());
				writer.flush();
				writer.close();

				numOfLines++;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return numOfLines;
	}
//ENDOF FILE UPLOAD REQUESTS



//ELECTION REQUEST
	/**
	 * Server received a message! 
	 * bi-directional request - there is no blocking
	 */
	@Override
	public StreamObserver<route.Route> biDirectionalRequest(final StreamObserver<route.Route> responseObserver) {
		return new StreamObserver<route.Route>() {
			@Override
			public void onNext(route.Route request) {

				// ack work
				route.Route.Builder ack = null;
				if (verify(request)) {

					// delay work
					var w = new Work(request, responseObserver);
					Engine.getInstance().workQueue.add(w);

					if (Engine.logger.isDebugEnabled())
						Engine.logger.debug("request() qsize = " + Engine.getInstance().workQueue.size());

					ack = route.Route.newBuilder();

					// routing/header information
					ack.setId(Engine.getInstance().getNextMessageID());
					ack.setOrigin(Engine.getInstance().getServerID());
					ack.setDestination(request.getOrigin());

					//handle logic for incoming election request
					handleIncomingElection(request, ack);
					//ENDOF election logic

					//handle logic for incoming storage write request
					handleIncomingStorageWrite(request, ack);
					//ENDOF STORAGE WRITE REQUEST

					// TODO ack of work
					ack.setPayload(ack(request));
				} else {
					// TODO rejecting the request - what do we do?
					// buildRejection(ack,request);
				}

				route.Route rtn = ack.build();
				responseObserver.onNext(rtn); // sends back response

			}

			@Override
			public void onError(Throwable t) {
				// logger.log(Level.WARNING, "Encountered error in routeChat", t);
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}

	
	public void handleIncomingStorageWrite(route.Route request, route.Route.Builder ack)
	{
		Engine engine = Engine.getInstance();
		if (request.getPath().contains("/storage")) {
			int colsLength = Integer.parseInt(request.getPath().split("/")[1]);

			try {
				Long lines = writeFile(request.getPayload(), colsLength);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void handleIncomingElection(route.Route request, route.Route.Builder ack)
	{
		Engine engine = Engine.getInstance();
		String serverWhoIsAskingForVote = request.getPath().split("/")[3];
		if (request.getPath().contains("/nominate")) {

			if (engine.serverStateMachine.state.getStateRole() == ServerStateMachine.ServerStateRoles.Follower) {

				// String requestServerTerm = request.getPath().split("/")[2];
				if (engine.serverStateMachine.votedFor == "") {
					ack.setPath(request.getPath() + "/accept");
					engine.serverStateMachine.votedFor = serverWhoIsAskingForVote;

					engine.election.electionTimerTask(4000L);

				} else {
					ack.setPath(request.getPath() + "/reject");
				}
			} else if (engine.serverStateMachine.state.getStateRole() == ServerStateMachine.ServerStateRoles.Candidate) {
				if (engine.serverStateMachine.votedFor == "") {
					ack.setPath(request.getPath() + "/accept");
					engine.serverStateMachine.votedFor = serverWhoIsAskingForVote;

					engine.election.electionTimerTask(4000L);

				} else {
					ack.setPath(request.getPath() + "/reject");
				}
			}
		} else if (request.getPath().contains("/heartbeat")) {
			ack.setPath(request.getPath() + "/success");

			if (engine.serverStateMachine.state.getStateRole() != ServerStateMachine.ServerStateRoles.Leader) {
				engine.serverTerm = Long.parseLong(request.getPath().split("/")[2]); // make sure server
																						// term is same as
																						// leader just in
																						// case
				engine.election.electionTimerTask(4000L);
			}
		}
	}
//ENDOF ELECTION REQUEST

	

	

//BLOCKING REQUEST
	/**
	 * Server received a message! 
	 * This is a blocking message meaning server is stuck waiting for a response
	 */
	@Override
	public void blockingServerRequest(route.Route request, StreamObserver<route.Route> responseObserver) {

		// TODO refactor to use RouteServer to isolate implementation from
		// transportation

		// ack work
		route.Route.Builder ack = null;
		if (verify(request)) {

			// delay work
			var w = new Work(request, responseObserver);

			Engine.getInstance().workQueue.add(w);

			if (Engine.logger.isDebugEnabled())
				Engine.logger.debug("request() qsize = " + Engine.getInstance().workQueue.size());

			ack = route.Route.newBuilder();

			// routing/header information
			ack.setId(Engine.getInstance().getNextMessageID());
			ack.setOrigin(Engine.getInstance().getServerID());
			ack.setDestination(request.getOrigin());

			Engine engine = Engine.getInstance();
			
			if (request.getPath().contains("/nominate")) {

				String serverWhoIsAskingForVote = request.getPath().split("/")[3];
				// String requestServerTerm = request.getPath().split("/")[2];
				if (engine.serverStateMachine.votedFor == "") {
					ack.setPath(request.getPath() + "/accept");
					engine.serverStateMachine.votedFor = serverWhoIsAskingForVote;
					// engine.election.electionTimerTask();
				} else if (engine.serverStateMachine.votedFor == serverWhoIsAskingForVote) {
					ack.setPath(request.getPath() + "/accept");
					// engine.election.electionTimerTask();
				} else {
					ack.setPath(request.getPath() + "/reject");
				}
			} else if (request.getPath().contains("/heartbeat")) {
				// engine.election.electionTimerTask();
				ack.setPath(request.getPath() + "/success");
			}

			// TODO ack of work
			ack.setPayload(ack(request));
		} else {
			// TODO rejecting the request - what do we do?
			// buildRejection(ack,request);
		}

		route.Route rtn = ack.build();
		responseObserver.onNext(rtn); // sends back response
		responseObserver.onCompleted(); // closes the call

	}
}
//ENDOF BLOCKING REQUEST