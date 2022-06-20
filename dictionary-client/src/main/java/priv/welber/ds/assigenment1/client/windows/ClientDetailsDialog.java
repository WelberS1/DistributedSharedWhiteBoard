package priv.welber.ds.assigenment1.client.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ClientDetailsDialog {
    private final JDialog jd = new JDialog();

    public ClientDetailsDialog(String host, int port) {

        jd.setResizable(false);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.setVisible(true);
        jd.setBounds(100, 100, 248, 179);
        jd.getContentPane().setLayout(null);
        jd.setLocationRelativeTo(null);

        {
            JButton btnNewButton = new JButton("OK");
            btnNewButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jd.dispose();
                }
            });
            btnNewButton.setBounds(88, 116, 75, 29);
            jd.getContentPane().add(btnNewButton);
        }

        JLabel hostLabel = new JLabel("Host: " + host);
        hostLabel.setBounds(63, 35, 148, 16);
        jd.getContentPane().add(hostLabel);

        JLabel portLabel = new JLabel("Port: " + port);
        portLabel.setBounds(63, 75, 148, 16);
        jd.getContentPane().add(portLabel);

    }

    public static void invokeDialog(String host, int port){
        EventQueue.invokeLater(() -> {
            try {
                new ClientDetailsDialog(host, port);
            } catch (Exception ex) {
                System.out.println("Dialog error!");
            }
        });
    }
}
