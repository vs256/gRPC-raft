package vidas.grpc.route.server;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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

	private static final void response(Route reply) {
		// TODO handle the reply/response from the server
		var payload = new String(reply.getPayload().toByteArray());
		Engine engine = Engine.getInstance();

		// DEBUG PRINT
		System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
				+ engine.serverStateMachine.state.toString() + " || votedFor: "
				+ engine.serverStateMachine.votedFor + " || nominationVotes: "
				+ engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response() || Origin: "
				+ reply.getOrigin() + " || Destination: " + reply.getDestination() + " || Path: "
				+ reply.getPath() + " || " + " || Reason: Heartbeat response ack" + " ** \n");
		// DEBUG PRINT
		if (reply.getPath().contains("accept")) {

			engine.serverStateMachine.nominationVotes++;

			// DEBUG PRINT
			System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
					+ engine.serverStateMachine.state.toString() + " || votedFor: "
					+ engine.serverStateMachine.votedFor + " || nominationVotes: "
					+ engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response() || Origin: "
					+ reply.getOrigin() + " || Destination: " + reply.getDestination() + " || Path: "
					+ reply.getPath() + " || " + " || Reason: "
					+ "accept nominate request | incrementing nomination votes to "
					+ engine.serverStateMachine.nominationVotes + " ** \n");
			// DEBUG PRINT

			if (engine.serverStateMachine.nominationVotes >= 1) {

				// DEBUG PRINT
				System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
						+ engine.serverStateMachine.state.toString() + " || votedFor: "
						+ engine.serverStateMachine.votedFor + " || nominationVotes: "
						+ engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response() || Origin: "
						+ reply.getOrigin() + " || Destination: " + reply.getDestination() + " || Path: "
						+ reply.getPath() + " || " + " || Reason: majority nomination votes received | becoming leader "
						+ " ** \n");
				// DEBUG PRINT

				engine.serverStateMachine.state = engine.serverStateMachine.state.nextState(); // going from candidate
																								// to leader

			}
		} else if (reply.getPath().contains("reject")) {
			// System.out.println("rejected nominate request");
			String replyServerTerm = reply.getPath().split("/")[2];

			if (engine.serverTerm < Long.parseLong(replyServerTerm)) {
				engine.serverTerm = Long.parseLong(replyServerTerm);
				engine.serverStateMachine.resetStateToOriginal();

				// DEBUG PRINT
				System.out.println(" ** " + "Term: " + engine.serverTerm + " || State: "
						+ engine.serverStateMachine.state.toString() + " || votedFor: "
						+ engine.serverStateMachine.votedFor + " || nominationVotes: "
						+ engine.serverStateMachine.nominationVotes + " || Type: Heartbeat response() || Origin: "
						+ reply.getOrigin() + " || Destination: " + reply.getDestination() + " || Path: "
						+ reply.getPath() + " || " + " || Reason: "
						+ "serverTerm is less than reply server term, so setting this server term to = "
						+ engine.serverTerm
						+ " downgrading (if applicable) to follower | setting votedFor to none" + " ** \n");
				// DEBUG PRINT

			}

		}

		if (reply.getPath().contains("heartbeat")) {

		}
		// Need to send a heartbeat
	}

	public static void sendHeartbeat(int serverPort, long referenceID, long destinationID, long origin, String path,
			ByteString payload) {

		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", serverPort).usePlaintext().build();
		RouteServiceGrpc.RouteServiceBlockingStub stub = RouteServiceGrpc.newBlockingStub(ch);

		// simulate different type of messages that can be sent
		String sp = String.valueOf(Engine.getInstance().getServerID());
		// var path = (referenceID % 5 == 0) ? "/election/" + sp :
		// "/nomination/to-somewhere";
		var msg = constructMessage(referenceID, destinationID, origin, path, payload);

		// blocking!
		var r = stub.request(msg);
		response(r);

		ch.shutdown();
	}
}
