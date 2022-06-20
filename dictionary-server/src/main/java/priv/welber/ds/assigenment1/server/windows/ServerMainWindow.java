package priv.welber.ds.assigenment1.server.windows;

import priv.welber.ds.assigenment1.server.DictionaryServer;

import javax.swing.*;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ServerMainWindow {

    private DictionaryServer server;
    private JFrame frame;
    private JLabel activeClientNumberLabel;

    public ServerMainWindow(DictionaryServer server) {
        this.server = server;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Server");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setBounds(100, 100, 510, 315);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(20, 40, 469, 224);
        frame.getContentPane().add(scrollPane);

        activeClientNumberLabel = new JLabel("Active Client Number: " + server.activeClientNumber);
        activeClientNumberLabel.setBounds(47, 12, 236, 16);
        frame.getContentPane().add(activeClientNumberLabel);

        new Thread(() -> {
            while (true){
                while (!server.messageQueue.isEmpty()){
                    textArea.append(server.messageQueue.pop());
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("Message queue error!");
                }
            }
        }).start();

    }

    public void setActiveClientNumberLabel(int clientNumber){
        activeClientNumberLabel.setText("Active Client Number: " + clientNumber);
    }
}