package vidas.grpc.route.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Properties;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.ByteString;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import fileStream.FileServiceGrpc.FileServiceImplBase;
import fileStream.FileUploadRequest;
import fileStream.FileUploadResponse;
import fileStream.FileServiceGrpc;
import fileStream.MetaData;
import fileStream.FileContent;
import fileStream.FileStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileServerImpl extends FileServiceImplBase {
	private Server svr;
	protected static Logger logger = LoggerFactory.getLogger("serverImpl");
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

	public static void main(String[] args) throws Exception {
		// TODO check args!

		String path = args[0];
		try {
			Properties conf = FileServerImpl.getConfiguration(new File(path));
			Engine.configure(conf);
			Engine.getConf();

			/* Similar to the socket, waiting for a connection */
			final FileServerImpl impl = new FileServerImpl();
			impl.start();
			impl.blockUntilShutdown();

		} catch (IOException e) {
			// TODO better error message
			e.printStackTrace();
		}
	}

	private void start() throws Exception {
		svr = ServerBuilder.forPort(Engine.getInstance().getServerPort()).addService(new FileServerImpl())
				.build();

		Engine.logger.info("-------starting server-------");
		svr.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				FileServerImpl.this.stop();
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

	/**
	 * server received a message!
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
				try {
					if (fileUploadRequest.hasMetadata()) {
						var headerOfFile = fileUploadRequest.getMetadata().getHeader();
						cols = headerOfFile.split(",", -1);

						// logger.info("---- Get metadata (Header of the file) ----");
						// logger.info(headerOfFile);
					} else {
						long t3 = System.currentTimeMillis();
						logger.info("---- Received No." + cntOfChunks + " chunk ----");

						Long lines = writeFile(fileUploadRequest.getFile().getContent(), cols);
						cntOfLines = lines + cntOfLines;
						cntOfChunks++;

						long t4 = System.currentTimeMillis();
						logger.info(
								"---- Finished written No." + cntOfChunks + " chunk (Time: " + (t4 - t3) + ") ----");
					}
				} catch (IOException e) {
					logger.info("IOException: " + e.toString());
					this.onError(e);
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

	private Long writeFile(ByteString content, String[] cols) throws IOException {
		byte[] byteArray = content.toByteArray();

		ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
		InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
		BufferedReader bufferedReader = new BufferedReader(streamReader);

		String line;
		Long numOfLines = 0L;
		while ((line = bufferedReader.readLine()) != null) {
			//System.out.println(line);
			//
			String[] colStrings = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			String vc = colStrings[5];

			// discard records of which the number of fields doesn't equal to number of
			// columns
			if (colStrings.length != cols.length) {
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
}
