package vidas.grpc.route.client;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import route.Route;
import route.RouteServiceGrpc;

public class RouteClient {
	// when using server.conf
	private static long clientID = 501;
	private static int port = 10000;

	// private static long clientID = 100;
	// private static int port = 2100;

	private static final Route constructMessage(int mID, int toID, String path, String payload) {
		Route.Builder bld = Route.newBuilder();
		bld.setId(mID);
		bld.setDestination(toID);
		bld.setOrigin(RouteClient.clientID);
		bld.setPath(path);

		byte[] hello = payload.getBytes();
		bld.setPayload(ByteString.copyFrom(hello));

		return bld.build();
	}

	private static final void response(Route reply) {
		// TODO handle the reply/response from the server
		var payload = new String(reply.getPayload().toByteArray());
		System.out.println("reply: " + reply.getId() + ", from: " + reply.getOrigin() + ", payload: " + payload);
	}

	public static void main(String[] args) {
		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", RouteClient.port).usePlaintext().build();
		RouteServiceGrpc.RouteServiceBlockingStub stub = RouteServiceGrpc.newBlockingStub(ch);

		final int destID = 1000; // 1234;
		final int I = 5;
		for (int i = 0; i < I; i++) {

			// simulate different type of messages that can be sent
			// var path = (i % 5 == 0) ? "/election/manage-something" :
			// "/nominate/to-somewhere";
			String path = "/client";
			var msg = RouteClient.constructMessage(i, destID, path, "hello " + i);

			// blocking!
			var r = stub.blockingServerRequest(msg);
			response(r);

		}

		ch.shutdown();
	}
}
