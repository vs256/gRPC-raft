package vidas.grpc.route.server.DebugTools;

import javax.swing.*;

import vidas.grpc.route.server.Engine;

import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {
    private int clicks = 0;
    private JLabel label = new JLabel("Number of clicks:  0     ");
    private JLabel timerLabel = new JLabel("timer:  0     ");

    private JFrame frame = new JFrame();

    public String timer;

    static void setLocationToCorner(JFrame frame, int offset) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        


        int x = 0;
        int y = 0;
        if (offset == 1) { //top left
            x = 0;
            y = 0;
        }
        else if (offset == 2) { //top right
            x = (int) (screenSize.getWidth() - frame.getWidth());
            y = 0;
        }
        else if (offset == 3) { //bottom left
            x = 0;
            y = (int) (screenSize.getHeight() - frame.getHeight());
        }
        else if (offset == 4) {
            x = (int) (screenSize.getWidth() - frame.getWidth());
            y = (int) (screenSize.getHeight() - frame.getHeight());
        }
        frame.setLocation(x,y);
    }

    public GUI(int offset) {
        // // the clickable button
        // JButton button = new JButton("Click Me");
        // button.addActionListener(this);

        // // the panel with the button and text
        // JPanel panel = new JPanel();
        // panel.setBorder(BorderFactory.createEmptyBorder(300, 300, 500, 300));
        // panel.setLayout(new GridLayout(0, 1));
        // //panel.add(button);
        // panel.add(label);

        // // set up the frame and display it
        // frame.add(panel, BorderLayout.CENTER);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setTitle("GUI");
        // frame.pack();
        // frame.setVisible(true);

        final JFrame frame = new JFrame();
        // frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(700, 400));
        // frame.setLocation(new Point(400 + offsetLocationX, -300 + offsetLocationY));
        setLocationToCorner(frame, offset);
        // frame.setLocation(new Point(0,100));
        // frame.setLocation(0,0);
        // frame.setLocationRelativeTo(null);

        frame.setLayout(new FlowLayout());

        Engine engine = Engine.getInstance();
        String str = " <b>Server</b>: " + engine.getServerName() + " " + "<br><br> <b>Term</b>: " + engine.serverTerm
                + " <br> <b>State</b>: "
                + engine.serverStateMachine.state.getStateRole().toString() + " <br> <b>votedFor</b>: "
                + engine.serverStateMachine.votedFor + " <br> <b>nominationVotes</b>: "
                + engine.serverStateMachine.nominationVotes
                + " <br> <b>Type</b>: initializing <br> <b>Origin</b>: "
                + " " + " <br> <b>Destination</b>: " + " "
                + " <br> <b>Path</b>: "
                + " " + " <br> "
                + " <br> <b>Reason</b>: initializing server "
                + " \n";

        label = new JLabel("<html>" + str + "</html>");
        label.setFont(new Font("Tahoma", Font.PLAIN, 15));
        label.setHorizontalAlignment(JLabel.CENTER);

        timerLabel = new JLabel("<html><br><b>Election Timer</b>: Initializing<br></html>");
        timerLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        frame.add(label);
        frame.add(timerLabel);

        frame.setVisible(true);
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {
        clicks++;
        label.setText("Number of clicks:  " + clicks);

    }

    public void setLabel(String str) {
        String[] stringArray = str.split("\\|\\|");
        String labelString = "<html>Server " + Engine.getInstance().serverName + "<br><br>";
        for (int i = 0; i < stringArray.length; i++) {
            labelString = labelString + stringArray[i] + "<br>";
        }
        labelString += "</html>";
        label.setText(labelString);
    }

    public void setTimer(String str) {
        String labelString = "<html>Election Timer: ";
        labelString += str + "</html>";
        timerLabel.setText(labelString);

    }

}