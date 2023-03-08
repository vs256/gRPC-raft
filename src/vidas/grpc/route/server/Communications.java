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

public class Communications {

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

	public static void sendNonBlockingRequest(int serverPort, long referenceID, long destinationID, long origin,
			String path,
			ByteString payload) {

		ManagedChannel ch = ManagedChannelBuilder.forAddress("localhost", serverPort).usePlaintext().build();
		RouteServiceGrpc.RouteServiceStub stub = RouteServiceGrpc.newStub(ch);

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
					engine.debugHelper.debugPrint(response, "Heartbeat response()",
							"accept nominate request | incrementing nomination votes to "
									+ engine.serverStateMachine.nominationVotes);
					// DEBUG PRINT

					int majorityServerCount = Math.floorDiv(engine.links.size(), 2) + 1; // at least 50% + 1
					if (engine.serverStateMachine.nominationVotes >= majorityServerCount) {

						// DEBUG PRINT
						engine.debugHelper.debugPrint(response, "Heartbeat response()",
								" majority nomination votes received | becoming leader ");
						// DEBUG PRINT

						engine.serverStateMachine.state = engine.serverStateMachine.state.nextState(); // going from
																										// candidate
																										// to leader

						engine.election.electionTimerTask(0); // resetting timer after becomes leader

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
