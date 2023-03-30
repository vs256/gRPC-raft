package vidas.grpc.route.server.StateMachine;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.Engine;
import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;
import vidas.grpc.route.server.Types.Link;
import vidas.grpc.route.util.Communications;

public class Leader implements ServerState {

    public String votedFor;

    public ServerStateMachine stateMachine;


    public int nominationVotes;

    public Leader(ServerStateMachine sm) {
        nominationVotes = 0;
        votedFor = "";
        // if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
        stateMachine = sm;
    }

    @Override
    public ServerStateRoles getStateRole() {
        return ServerStateRoles.Leader;
    }

    @Override
    public void nextState() {
        stateMachine.changeState(new Follower(stateMachine)); // or this?
    }

    @Override
    public void previousState() {
        stateMachine.changeState(new Candidate(stateMachine));
    }

    @Override
    public void sendRequest() {
        sendLeaderHeartbeat();
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