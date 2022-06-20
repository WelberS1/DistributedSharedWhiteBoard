package priv.welber.ds.assigenment1.client.windows;

import priv.welber.ds.assigenment1.client.DictionaryClient;
import priv.welber.ds.assigenment1.utils.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class ClientMainWindow extends AbstractDictionaryWindow {

    private JFrame frmClientMainWindow;
    private JTextField queryTextField;
    private final DictionaryClient client;
    private final String title = "Client";

    public ClientMainWindow(DictionaryClient client) {
        this.client = client;
        initialize();
    }

    private void initialize() {

        frmClientMainWindow = new JFrame();
        updateTitle(frmClientMainWindow, client.getConnectionStatus(), title);
        frmClientMainWindow.setVisible(true);
        frmClientMainWindow.setResizable(false);
        frmClientMainWindow.setBounds(100, 100, 408, 325);
        frmClientMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmClientMainWindow.getContentPane().setLayout(null);
        frmClientMainWindow.setLocationRelativeTo(null);


        queryTextField = new JTextField();
        queryTextField.setBounds(26, 6, 281, 29);
        frmClientMainWindow.getContentPane().add(queryTextField);
        queryTextField.setColumns(10);

        JButton queryButton = new JButton("Query");
        queryButton.setBounds(312, 7, 89, 29);
        frmClientMainWindow.getContentPane().add(queryButton);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(26, 47, 356, 214);
        frmClientMainWindow.getContentPane().add(scrollPane);

        JMenuBar menuBar = new JMenuBar();
        frmClientMainWindow.setJMenuBar(menuBar);

        JMenu mainMenu = new JMenu("Options");
        menuBar.add(mainMenu);

        JMenuItem addMenuItem = new JMenuItem("Add");
        mainMenu.add(addMenuItem);

        JMenuItem updateMenuItem = new JMenuItem("Update");
        mainMenu.add(updateMenuItem);

        JMenuItem removeMenuItem = new JMenuItem("Remove");
        mainMenu.add(removeMenuItem);

        JMenuItem clientDetailsMenuItem = new JMenuItem("Client Details");
        mainMenu.add(clientDetailsMenuItem);

        // Menu action: Add
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> {
                    try {
                        frmClientMainWindow.dispose();
                        client.setWindow(new ClientAddOrUpdateWindow(client, Commands.ADD));
                    } catch (Exception ex) {
                        System.out.println("Add menu error!");
                    }
                });
            }
        });

        // Menu action: Update
        updateMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> {
                    try {
                        frmClientMainWindow.dispose();
                        client.setWindow(new ClientAddOrUpdateWindow(client, Commands.UPDATE));
                    } catch (Exception ex) {
                        System.out.println("Update menu error!");
                    }
                });
            }
        });

        // Menu action: Remove
        removeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(() -> {
                    try {
                        frmClientMainWindow.dispose();
                        client.setWindow(new ClientRemoveWindow(client));
                    } catch (Exception ex) {
                        System.out.println("Remove menu error!");
                    }
                });
            }
        });

        // Menu action: Client Details
        clientDetailsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientDetailsDialog.invokeDialog(client.getHost(), client.getPort());
            }
        });

        // Button action: Query
        queryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!queryTextField.getText().equals("")){
                    String word = queryTextField.getText();
                    Message send = new Message();
                    send.setCommand(Commands.QUERY);
                    send.setWord(word);
                    if (client.sendMessage(send)){
                        textArea.setText(client.receiveMessage().getResponse());
                    } else {
                        ResponseDialog.invokeDialog("Connection error!");
                    }

                }
            }
        });
    }

    @Override
    public JFrame getFrame() {
        return frmClientMainWindow;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
