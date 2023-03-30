package vidas.grpc.route.server.StateMachine;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.Engine;
import vidas.grpc.route.server.Types.Link;
import vidas.grpc.route.util.Communications;

public class ServerStateMachine {

	public enum ServerStateRoles { Follower, Candidate, Leader }

	public String votedFor;

	public ServerState state;

	public int nominationVotes;

	public ServerStateMachine() {
		nominationVotes = 0;
		votedFor = "";
		// if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
		state = new Follower(this);
	}

	public void changeState(ServerState state)
    {
        this.state = state;
    }

	public void resetStateToOriginal() {
		nominationVotes = 0;
		votedFor = "";
		state = new Follower(this);
	}



}
