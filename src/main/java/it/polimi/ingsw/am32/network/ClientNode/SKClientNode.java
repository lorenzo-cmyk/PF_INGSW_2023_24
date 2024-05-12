package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.io.*;
import java.net.Socket;

public class SKClientNode implements ClientNodeInterface, Runnable {
    private View view;
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    public SKClientNode(View view) {
        this.view = view;
    }

    public void run() {
        // Listen for incoming messages
        try{
            while(true) {
                receiveFromServer();
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
    public void receiveFromServer() throws IOException, ClassNotFoundException {
        StoCMessage message = (StoCMessage) socketIn.readObject();
        message.processMessage(view);
    }

    public void startConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        System.out.println("Socket Client Acceptor created");
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketIn = new ObjectInputStream(socket.getInputStream());

    }
}