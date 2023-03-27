package vidas.grpc.route.server;

public class DebugHelper {

    public GUI gui;

    private Engine engine;

    DebugHelper(Engine eng) {
        engine = eng;
        System.out.println("gui");
        int offset = (int) (long) (Engine.getInstance().serverID / 1000);
        gui = new GUI(offset);
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
        String str = " " + "<b>Term</b>: " + engine.serverTerm + " || <b>State</b>: "
                + engine.serverStateMachine.state.toString() + " || <b>votedFor</b>: "
                + engine.serverStateMachine.votedFor + " || <b>nominationVotes</b>: "
                + engine.serverStateMachine.nominationVotes
                + " || <b>Type</b>: " + type + " || <b>Origin</b>: "
                + origin + " || <b>Destination</b>: " + destination
                + " || <b>Path</b>: "
                + path + " || "
                + " || <b>Reason</b>: " + reason + " "
                + " \n";
        gui.setLabel(str); // show up information to GUI
        // DEBUG PRINT
    }

    public void debugPrintCustom(String origin, String destination, String path, String type, String reason) {
        // DEBUG PRINT
        String str = " ** " + "<b>Term</b>: " + engine.serverTerm + " || <b>State</b>: "
                + engine.serverStateMachine.state.toString() + " || <b>votedFor</b>: "
                + engine.serverStateMachine.votedFor + " || <b>nominationVotes</b>: "
                + engine.serverStateMachine.nominationVotes
                + " || <b>Type</b>: " + type + " || <b>Origin</b>: "
                + origin + " || <b>Destination</b>: " + destination
                + " || <b>Path</b>: "
                + path + " || "
                + " || <b>Reason</b>: " + reason + " "
                + " ** \n";
        gui.setLabel(str); // show up information to GUI
        // DEBUG PRINT
    }

}
