package vidas.grpc.route.server;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import route.Route;
import route.RouteServiceGrpc;
import vidas.grpc.route.server.Engine;



public class ForwardMessage {
	// when using server.conf

	// private static long clientID = 100;
	// private static int port = 2100;

	private static final Route constructMessage(int mID, int toID, int origin, String path, ByteString payload) {
		Route.Builder bld = Route.newBuilder();
		bld.setId(mID);
		bld.setDestination(toID);
		bld.setOrigin(origin);
		bld.setPath(path);

		//byte[] hello = payload.getBytes();
		//bld.setPayload(ByteString.copyFrom(hello));
		bld.setPayload(payload);

		return bld.build();
	}

	private static final void response(Route reply) {
		// TODO handle the reply/response from the server
		var payload = new String(reply.getPayload().toByteArray());
		System.out.println("reply: " + reply.getId() + ", from: " + reply.getOrigin());
	}

	public static void forwardMessage(int serverPort, int refID, int destID, int origin, ByteString payload) {
		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", serverPort).usePlaintext().build();
		RouteServiceGrpc.RouteServiceBlockingStub stub = RouteServiceGrpc.newBlockingStub(ch);

		final int destinationID = destID; // 1234;

		// simulate different type of messages that can be sent
		String sp = String.valueOf(Engine.getInstance().getServerID());
		var path = (refID % 5 == 0) ? "/election/" + sp : "/nomination/to-somewhere";
		var msg = ForwardMessage.constructMessage(refID, destinationID, origin, path, payload);

		// blocking!
		var r = stub.request(msg);
		response(r);

		ch.shutdown();
	}
}
