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

public class ResponseDialog extends JDialog {

    private final JDialog jd = new JDialog();

    public ResponseDialog(String information) {

        jd.setResizable(false);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.setVisible(true);
        jd.setBounds(100, 100, 184, 120);
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
            btnNewButton.setBounds(55, 57, 75, 29);
            jd.getContentPane().add(btnNewButton);
        }
        {
            JLabel informationLabel = new JLabel(information);
            informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
            informationLabel.setBounds(6, 21, 172, 16);
            jd.getContentPane().add(informationLabel);
        }
    }

    public static void invokeDialog(String information){
        EventQueue.invokeLater(() -> {
            try {
                new ResponseDialog(information);
            } catch (Exception ex) {
                System.out.println("Dialog error!");
            }
        });
    }

}