package vidas.grpc.route.server;

import com.google.protobuf.ByteString;

public class ServerStateMachine {

	public String votedFor;

	ServerState state;

	protected int nominationVotes;

	ServerStateMachine() {
		nominationVotes = 0;
		votedFor = "";
		// if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
		state = ServerState.Follower;
	}

	public void resetStateToOriginal() {
		nominationVotes = 0;
		votedFor = "";
		state = ServerState.Follower;
	}

	public enum ServerState {

		Follower {

			@Override
			public ServerState nextState() {
				return Candidate;
			}

			@Override
			public ServerState previousState() {
				return this;
			}

		},
		Candidate {
			@Override
			public ServerState nextState() {
				return Leader;
			}

			@Override
			public ServerState previousState() {
				return Follower;
			}
		},
		Leader {
			@Override
			public ServerState nextState() {
				return this;
			}

			@Override
			public ServerState previousState() {
				return Candidate;
			}
		};

		public abstract ServerState previousState();

		public abstract ServerState nextState();

		public void sendNominateRequest() {
			Engine engine = Engine.getInstance();
			// sending out nominate vote request to all other followers
			for (Link l : engine.links) {
				long referenceID = engine.getNextMessageID();
				String path = "/nominate/" + engine.serverTerm + "/" + engine.getServerPort();

				// DEBUG PRINT
				engine.debugHelper.debugPrintCustom(Integer.toString(engine.getServerPort()),
						Integer.toString(l.getServerID()), path, "sendNominateRequest()",
						"sending nominate vote request");
				// DEBUG PRINT

				Communications.sendNonBlockingRequest(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
						ByteString.copyFromUtf8("term: " + engine.serverTerm));

			}
			//
		}

		public void sendLeaderHeartbeat() {
			Engine engine = Engine.getInstance();
			// sending out nominate vote request to all other followers
			for (Link l : engine.links) {
				long referenceID = engine.getNextMessageID();
				String path = "/heartbeat/" + engine.serverTerm + "/" + engine.getServerPort();

				// DEBUG PRINT
				engine.debugHelper.debugPrintCustom(Integer.toString(engine.getServerPort()),
						Integer.toString(l.getServerID()), path, "sendLeaderHeartbeat()",
						"sending leader heartbeat request");
				// DEBUG PRINT

				Communications.sendNonBlockingRequest(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
						ByteString.copyFromUtf8("term: " + engine.serverTerm));

			}
			//
		}

	}

}
