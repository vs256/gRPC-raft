package vidas.grpc.route.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {
    private int clicks = 0;
    private JLabel label = new JLabel("Number of clicks:  0     ");
    private JFrame frame = new JFrame();

    public String timer;

    public GUI() {

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 700));
        //frame.setLocation(new Point(600, 800));
        frame.setLayout(new BorderLayout());

        label = new JLabel("<html>Question:<br>What is love?<br>Baby don't hurt me<br>Don't hurt me<br>No more</html>");
        label.setFont(new Font("Serif", Font.BOLD, 15));
        label.setHorizontalAlignment(JLabel.CENTER);


        frame.add(label);

        frame.setVisible(true);
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {
        clicks++;
        label.setText("Number of clicks:  " + clicks);

    }

    public void setLabel(String str) {
        String[] stringArray = str.split("\\|\\|");
        String labelString = "<html>Server " + Engine.getInstance().serverName + "<br>";
        for (int i = 0; i < stringArray.length; i++) {
            labelString = labelString + stringArray[i] + "<br>";
        }
        labelString += timer + "</html>";
        label.setText(labelString);
    }

    public void setTimer(String str)
    {
        timer = "Timer: " + str;
        setLabel(label.getText());

    }

    // create one Frame
    // public static void main(String[] args) {
    // new GUI();
    // }
}