package priv.welber.ds.assigenment1.client.windows;

import priv.welber.ds.assigenment1.client.DictionaryClient;
import priv.welber.ds.assigenment1.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ClientAddOrUpdateWindow extends AbstractDictionaryWindow {

    private JFrame frmAddOrUpdate;
    private JTextField wordTextField;
    private final DictionaryClient client;
    private final Commands command;

    public ClientAddOrUpdateWindow(DictionaryClient client, Commands command) {
        this.client = client;
        this.command = command;
        initialize();
    }

    private void initialize() {
        frmAddOrUpdate = new JFrame();
        updateTitle(frmAddOrUpdate, client.getConnectionStatus(), String.valueOf(command));
        frmAddOrUpdate.setVisible(true);
        frmAddOrUpdate.setResizable(false);
        frmAddOrUpdate.setBounds(100, 100, 358, 259);
        frmAddOrUpdate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAddOrUpdate.getContentPane().setLayout(null);
        frmAddOrUpdate.setLocationRelativeTo(null);

        JButton functionButton = new JButton(String.valueOf(command));
        functionButton.setBounds(74, 194, 117, 29);
        frmAddOrUpdate.getContentPane().add(functionButton);

        JLabel wordLabel = new JLabel("Word");
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordLabel.setBounds(24, 31, 61, 16);
        frmAddOrUpdate.getContentPane().add(wordLabel);

        JLabel meaningLabel = new JLabel("Meaning");
        meaningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        meaningLabel.setBounds(24, 116, 61, 16);
        frmAddOrUpdate.getContentPane().add(meaningLabel);

        wordTextField = new JTextField();
        wordTextField.setBounds(97, 26, 236, 26);
        frmAddOrUpdate.getContentPane().add(wordTextField);
        wordTextField.setColumns(10);

        JTextArea meaningTextArea = new JTextArea();
        meaningTextArea.setLineWrap(true);
        meaningTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(meaningTextArea);
        scrollPane.setBounds(97, 71, 236, 111);
        frmAddOrUpdate.getContentPane().add(scrollPane);

        JButton returnButton = new JButton("Return");
        returnButton.setBounds(203, 194, 117, 29);
        frmAddOrUpdate.getContentPane().add(returnButton);


        functionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!wordTextField.getText().equals("") || !meaningTextArea.getText().equals("")) {
                    Message send = new Message();
                    send.setCommand(command);
                    String response = "";
                    if(!wordTextField.getText().equals("") && meaningTextArea.getText().trim().equals("")) {
                        response = "Invalid meaning!";
                    } else if(wordTextField.getText().trim().equals("") && !meaningTextArea.getText().equals("")) {
                        response = "Invalid word!";
                    } else {
                        boolean isValidWord = true;
                        String word = wordTextField.getText();

                        if (command == Commands.ADD){
                            isValidWord = false;
                            if (word.charAt(0) != '-' && word.charAt(word.length()-1) != '-'){
                                Matcher matcher = Pattern.compile("[A-Za-z\\-]+").matcher(word);
                                Matcher matcher2 = Pattern.compile("[\\-]{2,}").matcher(word);
                                if (matcher.matches() && !matcher2.find()){
                                    isValidWord = true;
                                }
                            }
                        }

                        if (isValidWord){
                            send.setWord(word);
                            send.setMeaning(meaningTextArea.getText().trim());
                            if (client.sendMessage(send)){
                                response = client.receiveMessage().getResponse();
                            } else {
                                response = "Connection error!";
                            }
                        } else {
                            response = "Invalid word!";
                        }

                    }

                    try{
                        if (response.equals("")){
                            response = "Connection error!";
                        }
                    } catch (Exception ex){
                        response = "Connection error!";
                    }

                    String finalResponse = response;
                    ResponseDialog.invokeDialog(finalResponse);
                }

            }
        });

        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventQueue.invokeLater(() -> {
                    try {
                        frmAddOrUpdate.dispose();
                        client.setWindow(new ClientMainWindow(client));
                    } catch (Exception exp) {
                        System.out.println("Add return error!");
                    }
                });
            }
        });

    }

    @Override
    public JFrame getFrame() {
        return frmAddOrUpdate;
    }

    @Override
    public String getTitle() {
        return String.valueOf(command);
    }
}
