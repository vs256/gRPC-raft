package vidas.grpc.route.server.StateMachine;

import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;

public class Follower implements ServerState {


    public String votedFor;

	public ServerStateMachine stateMachine;

    public ServerStateRoles stateRole = ServerStateRoles.Follower;

	public int nominationVotes;

    public Follower(ServerStateMachine sm) {
		nominationVotes = 0;
		votedFor = "";
		// if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
		stateMachine = sm;
	}


    @Override
    public ServerStateRoles getStateRole() {
        return ServerStateRoles.Follower;
    }


    @Override
    public void nextState() {
        stateMachine.changeState(new Candidate(stateMachine));
    }

    @Override
    public void previousState() {
        stateMachine.changeState(this);
    }

    @Override
    public void sendRequest() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'sendRequest'");
    }
    
}
