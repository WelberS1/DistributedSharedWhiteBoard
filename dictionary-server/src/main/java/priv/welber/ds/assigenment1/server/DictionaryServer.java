package priv.welber.ds.assigenment1.server;

import priv.welber.ds.assigenment1.server.windows.ServerMainWindow;
import priv.welber.ds.assigenment1.utils.*;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class DictionaryServer {

    // Server
    private int port;
    private ServerSocket server;
    private ServerMainWindow window;
    public int activeClientNumber;
    private int totalClientNumber;
    public LinkedList<String> messageQueue = new LinkedList<String>();

    // Dictionary
    private String dictionaryFilePath;
    private Dictionary dictionary;

    // Constructor
    public DictionaryServer(String dictionaryFilePath, String port) {
        try {
            this.port = Integer.parseInt(port);
        } catch (Exception e){
            System.out.println("Invalid port!");
            System.exit(0);
        }
        try {
            this.dictionaryFilePath = dictionaryFilePath;
            this.dictionary = new Dictionary(dictionaryFilePath);
            invokeWindow();
            initializeServer();
            connectClient();
        } catch (Exception e){
            System.out.println("Server class initialization error!");
            System.exit(0);
        }

    }

    // Initialize the server
    private void initializeServer(){
        try {
            server = new ServerSocket(port);
            autoSaveDictionary();
            setMessageQueue("========= Server start =========");
        } catch (Exception e) {
            System.out.println("Server initialization error!");
            System.exit(0);
        }
    }

    public void invokeWindow(){
        EventQueue.invokeLater(() -> {
            try {
                window = new ServerMainWindow(this);
            } catch (Exception e) {
                System.out.println("Server window error!");
                System.exit(0);
            }
        });
    }

    public synchronized void setMessageQueue(String message){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInfo = format.format(date);
        messageQueue.add(dateInfo + ": " + message + "\n");
    }

    // Connect clients
    public void connectClient(){
        try {
            while (true){
                Socket clientSocket = server.accept();
                setMessageQueue("New connection accepted. Try to connect...");
                updateActiveClientNumber(true);
                new Thread(() -> startService(clientSocket, totalClientNumber)).start();
            }
        } catch (Exception e) {
            setMessageQueue("Socket connection error!");
        }
    }

    public void updateActiveClientNumber(boolean isAdd){
        try {
            if (window != null){
                if (isAdd){
                    activeClientNumber ++;
                    totalClientNumber ++;
                    window.setActiveClientNumberLabel(activeClientNumber);
                } else {
                    if(activeClientNumber > 0){
                        activeClientNumber --;
                    }
                    window.setActiveClientNumberLabel(activeClientNumber);
                }
            }
        } catch (Exception e){
            setMessageQueue("Server window error!");
        }
    }

    public void autoSaveDictionary(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (dictionary.writeFile()){
                    setMessageQueue("Auto save succeeded");
                } else {
                    setMessageQueue("Auto save failed");
                }
            }
        }, 1000, 10 * 1000);
    }

    public void receiveMessage(Socket clientSocket, int clientID){
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            Timer heartbeat = new Timer();
            heartbeat.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        in.close();
                    } catch (Exception e) {
                        //
                    }
                }
            }, 5 * 1000);

            while (true){
                Message receive = (Message) in.readObject();
                Message send = new Message();
                if (receive.getCommand() != Commands.HEARTBEAT){
                    send.setResponse(dictionary.operation(receive));
                    sendMessage(out, send, clientID);
                    setMessageQueue("Client " + clientID + " " + receive.getCommand() + " Word: " + receive.getWord());
                } else {
                    heartbeat.cancel();
                    heartbeat = new Timer();
                    heartbeat.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                in.close();
                            } catch (Exception e) {
                                //
                            }
                        }
                    }, 5 * 1000);
                }
            }
        } catch (Exception e) {
            //
        }

    }

    public void sendMessage(ObjectOutputStream out, Message send, int clientID){
        try {
            out.writeObject(send);
            out.flush();
        } catch (Exception e) {
            setMessageQueue("Client " + clientID + " output stream error!");
        }
    }

    public void startService(Socket clientSocket, int clientID){
        setMessageQueue("Client " + clientID + " is connected");
        receiveMessage(clientSocket, clientID);

        stopService(clientSocket, clientID);
    }

    public void stopService(Socket clientSocket, int clientID){
        updateActiveClientNumber(false);
        try {
            clientSocket.close();
        } catch (Exception e) {
            setMessageQueue("Client " + clientID + " socket close error!");
        }

        setMessageQueue("Client " + clientID + " is disconnected. Try to save data...");

        if (dictionary.writeFile()){
            setMessageQueue("Client " + clientID + " data saved successfully");
        } else {
            setMessageQueue("Failed to save" + "Client " + clientID + "data");
        }
    }


}