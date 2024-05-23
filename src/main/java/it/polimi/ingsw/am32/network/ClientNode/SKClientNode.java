package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.io.*;
import java.net.Socket;

/**
 * Used to communicate with the server. Will be run by a thread that listens for incoming messages, and changes the status of the view accordingly.
 * Is connected to the server's SKServerNode.
 * Its methods are called by the thread running the AskListener, to send messages to the server.
 */
public class SKClientNode implements ClientNodeInterface, Runnable {
    /**
     * The view that will be updated by the messages received from the server.
     */
    private View view;
    /**
     * The socket used to communicate with the server.
     */
    private Socket socket;
    /**
     * The output stream used to send messages to the server.
     */
    private ObjectOutputStream socketOut;
    /**
     * The input stream used to receive messages from the server.
     */
    private ObjectInputStream socketIn;

    /**
     * Constructor for the SKClientNode.
     * @param view The view that will be updated by the messages received from the server.
     */
    public SKClientNode(View view) {
        this.view = view;
    }

    /**
     * The run method of the thread. Will listen for incoming messages and process them.
     * If the connection is closed, it will try to reconnect until successful, or until the program is closed.
     */
    public void run() {
        // Listen for incoming messages
        try {
            while (true) {
                receiveFromServer();
            }
        } catch (IOException | ClassNotFoundException e) { // If the connection is closed, try to reconnect
            try {
                if (!socket.isClosed()){
                    socketIn.close();
                    socketOut.close();
                    socket.close();
                }
            } catch (IOException ignored) {}

            // Attempt to reconnect
        }
    }

    /**
     * Method used to send a standard message to the server.
     *
     * @param message The message to be sent.
     * @throws UploadFailureException If the message cannot be sent.
     */
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

    /**
     * Method used to send a lobby message to the server.
     *
     * @param message The message to be received.
     * @throws UploadFailureException the message cannot be received.
     */
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

    /**
     * Method used to receive a message from the server.
     *
     * @throws IOException If the message cannot be received.
     * @throws ClassNotFoundException If the message cannot be received.
     */
    @Override
    public void receiveFromServer() throws IOException, ClassNotFoundException {
        StoCMessage message = (StoCMessage) socketIn.readObject();
        System.out.println("Received "+message.getClass().getName()+" from server");
        System.out.println(message);
        message.processMessage(view);
    }

    /**
     * Method used to start the connection with the server.
     *
     * @param ip The IP address of the server.
     * @param port The port of the server.
     * @throws IOException If the connection cannot be established.
     */
    public void startConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        System.out.println("Socket Client Acceptor created");
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketIn = new ObjectInputStream(socket.getInputStream());

    }
}