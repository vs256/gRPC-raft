package vidas.grpc.route.server;

public class DebugHelper {

    public GUI gui;

    private Engine engine;

    DebugHelper(Engine eng) {
        engine = eng;
        System.out.println("gui");
        int offset = (int) (long) (Engine.getInstance().serverID / 1000);
        gui = new GUI(0, (300 * offset));
    }

    public void debugPrint(route.Route route, String type, String reason) {
        
        String origin = "N/A";
        String destination = "N/A";
        String path = "N/A";
        if (route != null) {
            origin = Long.toString(route.getOrigin());
            destination = Long.toString(route.getDestination());
            path = route.getPath();
        }
        // DEBUG PRINT
        String str = " ** " + "Term: " + engine.serverTerm + " || State: "
                + engine.serverStateMachine.state.toString() + " || votedFor: "
                + engine.serverStateMachine.votedFor + " || nominationVotes: "
                + engine.serverStateMachine.nominationVotes
                + " || Type: " + type + " || Origin: "
                + origin + " || Destination: " + destination
                + " || Path: "
                + path + " || "
                + " || Reason: " + reason + " "
                + " ** \n";
        gui.setLabel(str); // show up information to GUI
        // DEBUG PRINT
    }


    public void debugPrintCustom(String origin, String destination, String path, String type, String reason) {
        // DEBUG PRINT
        String str = " ** " + "Term: " + engine.serverTerm + " || State: "
                + engine.serverStateMachine.state.toString() + " || votedFor: "
                + engine.serverStateMachine.votedFor + " || nominationVotes: "
                + engine.serverStateMachine.nominationVotes
                + " || Type: " + type + " || Origin: "
                + origin + " || Destination: " + destination
                + " || Path: "
                + path + " || "
                + " || Reason: " + reason + " "
                + " ** \n";
        gui.setLabel(str); // show up information to GUI
        // DEBUG PRINT
    }

}
