package vidas.grpc.route.server;

import com.google.protobuf.ByteString;

public class ServerStateMachine {

    public String votedFor;
    
    ServerState state;




    protected int nominationVotes;

    ServerStateMachine() {
        nominationVotes = 0;
        votedFor = "";
        //if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
        state = ServerState.Follower;
    }

    public void resetStateToOriginal()
    {
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
                String str = " ** " + "Term: " + engine.serverTerm + " || State: "
                        + engine.serverStateMachine.state.toString() + " || votedFor: "
                        + engine.serverStateMachine.votedFor + " || nominationVotes: "
                        + engine.serverStateMachine.nominationVotes + " || Type: sendNominateRequest()|| Origin: "
                        + engine.getServerPort() + " || Destination: " + l.getServerID() + " || Path: "
                        + path + " || " + " || Reason: sending nominate vote request" + " ** \n";
                engine.gui.setLabel(str);
                // DEBUG PRINT

				Heartbeat.sendNonBlockingHeartbeat(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
						ByteString.copyFromUtf8("term: " + engine.serverTerm));

			}
			//
		}

        public void sendLeaderHeartbeat()
        {
            Engine engine = Engine.getInstance();
			// sending out nominate vote request to all other followers
			for (Link l : engine.links) {
				long referenceID = engine.getNextMessageID();
				String path = "/heartbeat/" + engine.serverTerm + "/" + engine.getServerPort();


                //DEBUG PRINT
				String str = " ** " + "Term: " + engine.serverTerm + " || State: "
                + engine.serverStateMachine.state.toString() + " || votedFor: "
                + engine.serverStateMachine.votedFor + " || nominationVotes: "
                + engine.serverStateMachine.nominationVotes + " || Type: sendLeaderHeartbeat()|| Origin: "
                + engine.getServerPort() + " || Destination: " + l.getServerID() + " || Path: "
                + path + " || " + " || Reason: sending leader heartbeat request" + " ** \n";
                engine.gui.setLabel(str);
                //DEBUG PRINT

				Heartbeat.sendNonBlockingHeartbeat(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
						ByteString.copyFromUtf8("term: " + engine.serverTerm));

			}
			//
        }

	}

}
