package vidas.grpc.route.server.StateMachine;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.Engine;
import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;
import vidas.grpc.route.server.Types.Link;
import vidas.grpc.route.util.Communications;

public class Candidate implements ServerState {

    public String votedFor;

    public ServerStateMachine stateMachine;

    public ServerStateRoles stateRole = ServerStateRoles.Candidate;

    public int nominationVotes;

    public Candidate(ServerStateMachine sm) {
        nominationVotes = 0;
        votedFor = "";
        // if(Engine.getInstance().serverName.contains("B")) { votedFor = "test";}
        stateMachine = sm;
    }

    @Override
    public ServerStateRoles getStateRole() {
        return ServerStateRoles.Candidate;
    }

    @Override
    public void nextState() {
        stateMachine.changeState(new Leader(stateMachine));
    }

    @Override
    public void previousState() {
        stateMachine.changeState(new Follower(stateMachine));
    }

    @Override
    public void sendRequest() {
        sendNominateRequest();
    }

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

}
