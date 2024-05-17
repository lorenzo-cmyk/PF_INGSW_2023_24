package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.io.*;
import java.net.Socket;

public class SKClientNode implements ClientNodeInterface, Runnable {
    private View view;
    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;
    private String nickname;
    private ClientPingTask clientPingTask;

    public SKClientNode(View view, String ip, int port, String nickname) {
        this.view = view;
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
        clientPingTask = new ClientPingTask(this);
    }

    public SKClientNode(View view) {
        this.view = view;
    }

    public void run() {
        // Listen for incoming messages
        try{
            while(true) {
                listenForIncomingMessages();
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                if (!socket.isClosed()){
                    socketIn.close();
                    socketOut.close();
                    socket.close();
                }
            } catch (IOException ignored) {}
        }
    }

    public void listenForIncomingMessages() throws IOException, ClassNotFoundException {
        StoCMessage message = (StoCMessage) socketIn.readObject();
        System.out.println("Received"+message.getClass().getName()+" from server");
        System.out.println(message.toString());
        message.processMessage(view);
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {
        if (socket.isClosed()) {
            throw new UploadFailureException();
        }

        try {
            socketOut.writeObject(message);
        } catch (IOException e) {
            throw new UploadFailureException();
        }
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {
        if (socket.isClosed()) {
            throw new UploadFailureException();
        }

        try {
            socketOut.writeObject(message);
        } catch (IOException e) {
            throw new UploadFailureException();
        }
    }

    public void startConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        System.out.println("Socket Client Acceptor created");
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketIn = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void pingTimeOverdue() {
        try {
            uploadToServer(new PingMessage(nickname));
        } catch (UploadFailureException ignored) {}
    }
}