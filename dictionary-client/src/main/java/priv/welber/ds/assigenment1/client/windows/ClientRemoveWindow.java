package priv.welber.ds.assigenment1.client.windows;

import priv.welber.ds.assigenment1.client.DictionaryClient;
import priv.welber.ds.assigenment1.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ClientRemoveWindow extends AbstractDictionaryWindow {

    private JFrame frmRemove;
    private JTextField wordTextField;
    private final DictionaryClient client;
    private final String title = "REMOVE";

    public ClientRemoveWindow(DictionaryClient client) {
        this.client = client;
        initialize();
    }

    private void initialize() {
        frmRemove = new JFrame();
        updateTitle(frmRemove, client.getConnectionStatus(), title);
        frmRemove.setVisible(true);
        frmRemove.setResizable(false);
        frmRemove.setBounds(100, 100, 296, 168);
        frmRemove.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmRemove.getContentPane().setLayout(null);
        frmRemove.setLocationRelativeTo(null);

        JLabel wordLabel = new JLabel("Word");
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordLabel.setBounds(23, 34, 73, 21);
        frmRemove.getContentPane().add(wordLabel);

        wordTextField = new JTextField();
        wordTextField.setBounds(93, 31, 175, 26);
        frmRemove.getContentPane().add(wordTextField);
        wordTextField.setColumns(10);

        JButton removeButton = new JButton("REMOVE");
        removeButton.setBounds(32, 84, 117, 29);
        frmRemove.getContentPane().add(removeButton);

        JButton returnButton = new JButton("Return");
        returnButton.setBounds(161, 84, 117, 29);
        frmRemove.getContentPane().add(returnButton);



        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String response = "";
                if (!wordTextField.getText().equals("")) {
                    Message send = new Message();
                    send.setCommand(Commands.REMOVE);
                    send.setWord(wordTextField.getText());
                    if (client.sendMessage(send)){
                        response = client.receiveMessage().getResponse();
                    } else {
                        response = "Connection error!";
                    }

                    try {
                        if (response.equals("")){
                            response = "Connection error!";
                        }
                    } catch (Exception ex){
                        response = "Connection error!";
                    }

                    ResponseDialog.invokeDialog(response);
                }

            }
        });

        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventQueue.invokeLater(() -> {
                    try {
                        frmRemove.dispose();
                        client.setWindow(new ClientMainWindow(client));
                    } catch (Exception ex) {
                        System.out.println("Remove return error!");
                    }
                });
            }
        });
    }

    @Override
    public JFrame getFrame() {
        return frmRemove;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
