package vidas.grpc.route.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.protobuf.ByteString;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import route.RouteServiceGrpc.RouteServiceImplBase;


public class RouteServerImpl extends RouteServiceImplBase {
	private Server svr;

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

		
		Engine.logger.info("Starting Server " + engine.serverName + " | server.id = " + engine.getServerID() + " | server.port = "
				+ engine.getServerPort() + " |");
		svr.start();

		// //heartbeats
		// Engine.logger.info("starting heartbeats");
		// for(Link l : Engine.getInstance().links)
		// {
		// 	int i = 0;
		// 	ForwardMessage.forwardMessage(l.getPort(), i, l.getServerID(), (int) (long) Engine.getInstance().serverID, ByteString.copyFromUtf8("heartbeat"));
		// 	i++;
		// 	Engine.logger.info("sending heartbeat " + i + " to " + l.getServerID());
		// }
		// //

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

	/**
	 * server received a message!
	 */
	@Override
	public void request(route.Route request, StreamObserver<route.Route> responseObserver) {

		// TODO refactor to use RouteServer to isolate implementation from
		// transportation

		// ack work
		route.Route.Builder ack = null;
		if (verify(request)) {

			// delay work
			var w = new Work(request, responseObserver);
			if (MgmtWorker.isPriority(request))
				Engine.getInstance().mgmtQueue.add(w);
			else
				Engine.getInstance().workQueue.add(w);

			if (Engine.logger.isDebugEnabled())
				Engine.logger.debug("request() qsize = " + Engine.getInstance().workQueue.size());

			ack = route.Route.newBuilder();

			// routing/header information
			ack.setId(Engine.getInstance().getNextMessageID());
			ack.setOrigin(Engine.getInstance().getServerID());
			ack.setDestination(request.getOrigin());
			ack.setPath(request.getPath());

			// TODO ack of work
			ack.setPayload(ack(request));
		} else {
			// TODO rejecting the request - what do we do?
			// buildRejection(ack,request);
		}

		route.Route rtn = ack.build();
		responseObserver.onNext(rtn);
		responseObserver.onCompleted();
	}
}