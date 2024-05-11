package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SKClientNode implements ClientNodeInterface {
    private final View view;
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;
    private Scanner in;


    public SKClientNode(View view) {
        this.view = view;
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {
        try {
            socketOut.writeObject(message);
        } catch (IOException e) {
            //TODO
        }
    }
    @Override
    public void receiveFromServer() {
        try {
            StoCMessage message = (StoCMessage) socketIn.readObject();
            //TODO
        } catch (IOException| ClassNotFoundException e) {
         //TODO
        }
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {
        //TODO

    }
    public void startConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        System.out.println("Socket Client Acceptor created");
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketIn = new ObjectInputStream(socket.getInputStream());
    }
}