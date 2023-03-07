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

			// if (MgmtWorker.isPriority(request))
			// Engine.getInstance().mgmtQueue.add(w);
			// else
			// if (request.getPath().contains("/nominate")) {
			// if (Engine.getInstance().getServerRole() == "follower") {
			// Election worker = Engine.getInstance().workers.get(0);
			// System.out.println(
			// "* request path contains /nominate & this server is a follower therefore
			// interrupting server worker & setting resetTimer to true | ");
			// worker.resetTimer = true;
			// worker.interrupt();
			// }
			// if (Engine.getInstance().getServerRole() == "candidate") { // got a nominate
			// vote. TODO CHECK IF ACCECPT
			// // OR REJECT
			// {
			// Engine.getInstance().nominationVotes++;
			// // then if majority nomination votes proceed to become a leader
			// if (Engine.getInstance().nominationVotes >= 2) {
			// Engine.getInstance().nominationVotes = 0; // reset nomination votes
			// Engine.getInstance().serverRole = "leader"; // become leader
			// System.out.println(
			// "this candidate server nominationVotes is >= 2 therefore this becomes a
			// leader");
			// // might need to interrupt timer for leader role now
			// }
			// }
			// }
			// if (request.getPath().contains("/election")) {
			// }
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

	@Override
	public StreamObserver<route.Route> biDirectionalRequest(final StreamObserver<route.Route> responseObserver) {
		return new StreamObserver<route.Route>() {
			@Override
			public void onNext(route.Route request) {
				//request(note, responseObserver);
				// List<route.Route> notes = getOrCreateNotes(request);

				// // Respond with all previous notes at this location.
				// for (route.Route prevNote : notes.toArray(new route.Route[0])) {
				// responseObserver.onNext(prevNote);
				// }

				// // Now add the new note to the list
				// notes.add(request);

				// ack work
				route.Route.Builder ack = null;
				if (true) {

					// delay work
					var w = new Work(request, responseObserver);

					// if (MgmtWorker.isPriority(request))
					// Engine.getInstance().mgmtQueue.add(w);
					// else
					// if (request.getPath().contains("/nominate")) {
					// if (Engine.getInstance().getServerRole() == "follower") {
					// Election worker = Engine.getInstance().workers.get(0);
					// System.out.println(
					// "* request path contains /nominate & this server is a follower therefore
					// interrupting server worker & setting resetTimer to true | ");
					// worker.resetTimer = true;
					// worker.interrupt();
					// }
					// if (Engine.getInstance().getServerRole() == "candidate") { // got a nominate
					// vote. TODO CHECK IF ACCECPT
					// // OR REJECT
					// {
					// Engine.getInstance().nominationVotes++;
					// // then if majority nomination votes proceed to become a leader
					// if (Engine.getInstance().nominationVotes >= 2) {
					// Engine.getInstance().nominationVotes = 0; // reset nomination votes
					// Engine.getInstance().serverRole = "leader"; // become leader
					// System.out.println(
					// "this candidate server nominationVotes is >= 2 therefore this becomes a
					// leader");
					// // might need to interrupt timer for leader role now
					// }
					// }
					// }
					// if (request.getPath().contains("/election")) {
					// }
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

						if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Follower) {
							String serverWhoIsAskingForVote = request.getPath().split("/")[3];
							// String requestServerTerm = request.getPath().split("/")[2];
							if (engine.serverStateMachine.votedFor == "") {
								ack.setPath(request.getPath() + "/accept");
								engine.serverStateMachine.votedFor = serverWhoIsAskingForVote;
								
								engine.election.electionTimerTask(2000L);

							} else if (engine.serverStateMachine.votedFor == serverWhoIsAskingForVote) {
								ack.setPath(request.getPath() + "/accept");
								
								engine.election.electionTimerTask(2000L);
							} else {
								ack.setPath(request.getPath() + "/reject");
							}
						} 
						else if (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate) {
							ack.setPath(request.getPath() + "/reject");
						}
					} else if (request.getPath().contains("/heartbeat")) {
						ack.setPath(request.getPath() + "/success");
						
						engine.election.electionTimerTask(2000L);
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
	 * Get the notes list for the given location. If missing, create it.
	 */
	private List<route.Route> getOrCreateNotes(route.Route route) {
		List<route.Route> notes = Collections.synchronizedList(new ArrayList<route.Route>());
		List<route.Route> prevNotes = routes.putIfAbsent(route, notes);
		return prevNotes != null ? prevNotes : notes;
	}

}
