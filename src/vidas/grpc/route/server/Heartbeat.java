package vidas.grpc.route.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import route.Route;
import route.RouteServiceGrpc;
import vidas.grpc.route.server.Engine;

public class Heartbeat {
	// when using server.conf

	// private static long clientID = 100;
	// private static int port = 2100;

	private static final Route constructMessage(long msgID, long toID, long origin, String path, ByteString payload) {
		Route.Builder bld = Route.newBuilder();
		bld.setId(msgID);
		bld.setDestination(toID);
		bld.setOrigin(origin);
		bld.setPath(path);

		// byte[] hello = payload.getBytes();
		// bld.setPayload(ByteString.copyFrom(hello));
		bld.setPayload(payload);

		return bld.build();
	}

	// private static final void response(Route reply) {
	// // TODO handle the reply/response from the server
	// var payload = new String(reply.getPayload().toByteArray());
	// Engine engine = Engine.getInstance();

	// // DEBUG PRINT
	// String str = " ** " + "Term: " + engine.serverTerm + " || State: "
	// + engine.serverStateMachine.state.toString() + " || votedFor: "
	// + engine.serverStateMachine.votedFor + " || nominationVotes: "
	// + engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response()
	// || Origin: "
	// + reply.getOrigin() + " || Destination: " + reply.getDestination() + " ||
	// Path: "
	// + reply.getPath() + " || " + " || Reason: Heartbeat response ack" + " ** \n";
	// engine.gui.setLabel(str);
	// // DEBUG PRINT
	// if (reply.getPath().contains("accept")) {

	// engine.serverStateMachine.nominationVotes++;

	// // DEBUG PRINT
	// str = " ** " + "Term: " + engine.serverTerm + " || State: "
	// + engine.serverStateMachine.state.toString() + " || votedFor: "
	// + engine.serverStateMachine.votedFor + " || nominationVotes: "
	// + engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response()
	// || Origin: "
	// + reply.getOrigin() + " || Destination: " + reply.getDestination() + " ||
	// Path: "
	// + reply.getPath() + " || " + " || Reason: "
	// + "accept nominate request | incrementing nomination votes to "
	// + engine.serverStateMachine.nominationVotes + " ** \n";
	// engine.gui.setLabel(str);
	// // DEBUG PRINT

	// if (engine.serverStateMachine.nominationVotes >= 1) {

	// // DEBUG PRINT
	// str = " ** " + "Term: " + engine.serverTerm + " || State: "
	// + engine.serverStateMachine.state.toString() + " || votedFor: "
	// + engine.serverStateMachine.votedFor + " || nominationVotes: "
	// + engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response()
	// || Origin: "
	// + reply.getOrigin() + " || Destination: " + reply.getDestination() + " ||
	// Path: "
	// + reply.getPath() + " || " + " || Reason: majority nomination votes received
	// | becoming leader "
	// + " ** \n";
	// engine.gui.setLabel(str);
	// // DEBUG PRINT

	// engine.serverStateMachine.state =
	// engine.serverStateMachine.state.nextState(); // going from candidate
	// // to leader

	// //engine.election.electionTimerTask();

	// }
	// } else if (reply.getPath().contains("reject")) {
	// // System.out.println("rejected nominate request");
	// String replyServerTerm = reply.getPath().split("/")[2];

	// if (engine.serverTerm < Long.parseLong(replyServerTerm)) {
	// engine.serverTerm = Long.parseLong(replyServerTerm);
	// engine.serverStateMachine.resetStateToOriginal();

	// // DEBUG PRINT
	// str = " ** " + "Term: " + engine.serverTerm + " || State: "
	// + engine.serverStateMachine.state.toString() + " || votedFor: "
	// + engine.serverStateMachine.votedFor + " || nominationVotes: "
	// + engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response()
	// || Origin: "
	// + reply.getOrigin() + " || Destination: " + reply.getDestination() + " ||
	// Path: "
	// + reply.getPath() + " || " + " || Reason: "
	// + "serverTerm is less than reply server term, so setting this server term to
	// = "
	// + engine.serverTerm
	// + " downgrading (if applicable) to follower | setting votedFor to none" + "
	// ** \n";
	// engine.gui.setLabel(str);
	// // DEBUG PRINT

	// //engine.election.electionTimerTask();

	// }

	// }

	// if (reply.getPath().contains("heartbeat")) {

	// }
	// // Need to send a heartbeat
	// }

	// public static void sendHeartbeat(int serverPort, long referenceID, long
	// destinationID, long origin, String path,
	// ByteString payload) {

	// ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost",
	// serverPort).usePlaintext().build();
	// RouteServiceGrpc.RouteServiceBlockingStub stub =
	// RouteServiceGrpc.newBlockingStub(ch);
	// // RouteServiceGrpc.RouteServiceStub stub = RouteServiceGrpc.newStub(ch);

	// // simulate different type of messages that can be sent
	// String sp = String.valueOf(Engine.getInstance().getServerID());
	// // var path = (referenceID % 5 == 0) ? "/election/" + sp :
	// // "/nomination/to-somewhere";
	// var msg = constructMessage(referenceID, destinationID, origin, path,
	// payload);

	// // blocking!
	// // StreamObserver<route.Route> responseObserver = new
	// // StreamObserver<route.Route>();
	// // StreamObserver<route.Route> requestObserver =
	// // stub.biDirectionalRequest(responseObserver);
	// var r = stub.request(msg);
	// response(r);

	// ch.shutdown();
	// }

	public static void sendNonBlockingHeartbeat(int serverPort, long referenceID, long destinationID, long origin,
			String path,
			ByteString payload) {

		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", serverPort).usePlaintext().build();
		RouteServiceGrpc.RouteServiceStub stub = RouteServiceGrpc.newStub(ch);

		// simulate different type of messages that can be sent
		String sp = String.valueOf(Engine.getInstance().getServerID());
		// var path = (referenceID % 5 == 0) ? "/election/" + sp :
		// "/nomination/to-somewhere";

		final CountDownLatch finishLatch = new CountDownLatch(1);
		StreamObserver<route.Route> requestObserver = stub.biDirectionalRequest(new StreamObserver<route.Route>() {
			@Override
			public void onNext(Route response) {

				// response(note);
				System.out.println("Got path: " + response.getPath());
				Engine engine = Engine.getInstance();
				
					if (response.getPath().contains("/reject")) {

					} else if (response.getPath().contains("/accept")
							&& (engine.serverStateMachine.state == ServerStateMachine.ServerState.Candidate)) {

						engine.serverStateMachine.nominationVotes++;

						// DEBUG PRINT
						String str = " ** " + "Term: " + engine.serverTerm + " || State: "
								+ engine.serverStateMachine.state.toString() + " || votedFor: "
								+ engine.serverStateMachine.votedFor + " || nominationVotes: "
								+ engine.serverStateMachine.nominationVotes
								+ " || Type: Heartbeat response() || Origin: "
								+ response.getOrigin() + " || Destination: " + response.getDestination() + " || Path: "
								+ response.getPath() + " || " + " || Reason: "
								+ "accept nominate request | incrementing nomination votes to "
								+ engine.serverStateMachine.nominationVotes + " ** \n";
						engine.gui.setLabel(str);
						// DEBUG PRINT

						if (engine.serverStateMachine.nominationVotes >= 2) {

							// DEBUG PRINT
							str = " ** " + "Term: " + engine.serverTerm + " || State: "
									+ engine.serverStateMachine.state.toString() + " || votedFor: "
									+ engine.serverStateMachine.votedFor + " || nominationVotes: "
									+ engine.serverStateMachine.nominationVotes
									+ " || Type: Heartbeat response() || Origin: "
									+ response.getOrigin() + " || Destination: " + response.getDestination()
									+ " || Path: "
									+ response.getPath() + " || "
									+ " || Reason: majority nomination votes received | becoming leader "
									+ " ** \n";
							engine.gui.setLabel(str);
							// DEBUG PRINT

							engine.serverStateMachine.state = engine.serverStateMachine.state.nextState(); // going from
																											// candidate
																											// to leader

							engine.election.electionTimerTask(0);

						}
					}
				

			}

			@Override
			public void onCompleted() {
				// finished...
				System.out.println("finished");
				finishLatch.countDown();
			}

			@Override
			public void onError(Throwable arg0) {
				System.out.println("error");
				finishLatch.countDown();
			}

		});

		try {
			var msg = constructMessage(referenceID, destinationID, origin, path, payload);

			route.Route[] requests = { msg };

			for (route.Route request : requests) {
				System.out.println(
						"Sending message " + request.getPath() + " to" + " " + Long.toString(request.getDestination()));

				// stub.biDirectionalRequest(requestObserver);
				// response(request);
				requestObserver.onNext(request);

			}

		} catch (RuntimeException e) {
			// Cancel RPC
			requestObserver.onError(e);
			throw e;
		}
		// Mark the end of requests
		requestObserver.onCompleted();

		// Receiving happens asynchronously
		try {
			finishLatch.await(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
