package vidas.grpc.route.server.StateMachine;

import com.google.protobuf.ByteString;

import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;

public interface ServerState {

    public ServerStateRoles getStateRole();

    public void nextState();
    public void previousState();

    public void sendRequest(); //sending the actual request

    public void sendLeaderFileWriteRequest(int serverPort, int serverID, ByteString content, String header);
}
