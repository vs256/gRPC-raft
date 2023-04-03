package vidas.grpc.route.server.StateMachine;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.Engine;
import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;
import vidas.grpc.route.util.FileStorage;

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


   
    @Override
    public void sendLeaderFileWriteRequest(int serverPort, int serverID, ByteString content, String header) {
        Engine engine = Engine.getInstance();
        
        System.out.println("Sending file write request to server: " + serverID);
            long referenceID = engine.getNextMessageID();
            String path = "/storage/" + engine.serverTerm + "/" + engine.getServerPort();

            // DEBUG PRINT
            engine.debugHelper.debugPrintCustom(Integer.toString(engine.getServerPort()),
                    Integer.toString(serverID), path, "sendStorageRequest()",
                    "sending file storage write request");
            // DEBUG PRINT

            // Communications.sendNonBlockingRequest(l.getPort(), referenceID, l.getServerID(), engine.serverID, path,
            //         content);

            FileStorage.sendNonBlockingRequest(serverPort, referenceID, serverID, engine.serverID, path,
                    content, header);

        
    }
    
}
