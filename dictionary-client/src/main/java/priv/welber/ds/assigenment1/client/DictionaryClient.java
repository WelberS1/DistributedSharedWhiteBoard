package priv.welber.ds.assigenment1.client;

import priv.welber.ds.assigenment1.client.windows.AbstractDictionaryWindow;
import priv.welber.ds.assigenment1.client.windows.ClientMainWindow;
import priv.welber.ds.assigenment1.utils.*;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Wenbo Sun
 * 1137377
 * WENBOS1@student.unimelb.edu.au
 * */

public class DictionaryClient {
    private Socket client;
    private String host;
    private int port;
    private boolean connectionStatus = false;
    private boolean isFirstConnection = true;
    private AbstractDictionaryWindow window;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public DictionaryClient(String host, String port){
        this.host = host;
        try {
            this.port = Integer.parseInt(port);
        } catch (Exception e){
            System.out.println("Invalid port!");
            System.exit(0);
        }
        invokeWindow();
        initializeClient();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setWindow(AbstractDictionaryWindow window) {
        this.window = window;
    }

    public void invokeWindow(){
        EventQueue.invokeLater(() -> {
            try {
                window = new ClientMainWindow(this);
            } catch (Exception e) {
                System.out.println("Client window error!");
                System.exit(0);
            }
        });
    }

    public void initializeClient(){
        try {
            client = new Socket(host, port);
            this.updateConnectionStatus(true);
            isFirstConnection = false;
            new Thread(this::heartbeat).start();
        } catch (Exception e) {
            if (isFirstConnection){
                isFirstConnection = false;
                reconnect();
            }
        }
    }

    public boolean getConnectionStatus() {
        return connectionStatus;
    }

    public void updateConnectionStatus(boolean newStatus){
        connectionStatus = newStatus;
        if (window != null){
            window.updateTitle(window.getFrame(), newStatus, window.getTitle());
        } else {
            try {
                Thread.sleep(100);
                updateConnectionStatus(newStatus);
            } catch (Exception e) {
                //
            }
        }
    }

    public void heartbeat(){
        while (connectionStatus){
            try {
                Message send = new Message();
                send.setCommand(Commands.HEARTBEAT);
                send.setResponse("heartbeat");
                if (sendMessage(send)){
                    Thread.sleep(2 * 1000);
                } else {
                    closeClient();
                    reconnect();
                    break;
                }
            } catch (Exception e){
                System.out.println("Heartbeat error!");
            }
        }
    }

    public void reconnect(){
        while (!connectionStatus){
            this.initializeClient();
            try {
                Thread.sleep(3 * 1000);
            } catch (Exception e) {
                System.out.println("Reconnect error!");
            }
        }
    }

    public void closeClient(){
        updateConnectionStatus(false);

        try {
            if (in != null){
                in.close();
            }
        } catch (Exception e){
            System.out.println("Input stream error!");
        }

        try {
            if (out != null){
                out.close();
            }
        } catch (Exception e){
            System.out.println("Output stream error!");
        }

        try {
            client.close();
        } catch (Exception e){
            System.out.println("Close socket error!");
        }

        in = null;
        out = null;
        client = null;
    }

    public Message receiveMessage(){
        Message receive = new Message();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    closeClient();
                    reconnect();
                } catch (Exception e) {
                    //
                }
            }
        }, 1500);

        try {
            if (in == null){
                in = new ObjectInputStream(client.getInputStream());
            }
            receive = (Message) in.readObject();
            timer.cancel();
        } catch (Exception e) {
            if (connectionStatus) {
                System.out.println("Input stream error!");
            }
        }

        return receive;
    }

    public boolean sendMessage(Message send){
        try {
            if (out == null){
                out = new ObjectOutputStream(client.getOutputStream());
            }
            out.writeObject(send);
            out.flush();
            return true;
        } catch (Exception e) {
            if (connectionStatus) {
                System.out.println("Output stream error!");
            }
            return false;
        }
    }

}
