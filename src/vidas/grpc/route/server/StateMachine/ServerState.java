package vidas.grpc.route.server.StateMachine;

import vidas.grpc.route.server.StateMachine.ServerStateMachine.ServerStateRoles;

public interface ServerState {

    public ServerStateRoles getStateRole();

    public void nextState();
    public void previousState();

    public void sendRequest(); //sending the actual request
}
