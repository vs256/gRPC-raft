package vidas.grpc.route.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.protobuf.ByteString;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import route.RouteServiceGrpc.RouteServiceImplBase;

public class RouteServerImpl extends RouteServiceImplBase {
	private Server svr;

	private final ConcurrentMap<route.Route, List<route.Route>> routes = new ConcurrentHashMap<route.Route, List<route.Route>>();

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

					Engine engine = Engine.getInstance();
					String serverWhoIsAskingForVote = request.getPath().split("/")[3];
					if (request.getPath().contains("/nominate")) {

						if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {

							// String requestServerTerm = request.getPath().split("/")[2];
							if (engine.serverStateMachine.votedFor == "") {
								ack.setPath(request.getPath() + "/accept");
								engine.serverStateMachine.votedFor = serverWhoIsAskingForVote;

								engine.election.electionTimerTask(4000L);

							} else {
								ack.setPath(request.getPath() + "/reject");
							}
						} else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate) {
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

						if (engine.serverStateMachine.state != ServerStateMachine.ServerState.Leader) {
							engine.serverTerm = Long.parseLong(request.getPath().split("/")[2]); // make sure server
																									// term is same as
																									// leader just in
																									// case
							engine.election.electionTimerTask(4000L);
						}
					}

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


	

	
	/**
	 * server received a message!
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
