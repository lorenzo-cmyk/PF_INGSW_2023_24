package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.util.ArrayList;

public class AskListener implements AskListenerInterface, Runnable {
    private final ClientNodeInterface clientNode;
    private final ArrayList<CtoSLobbyMessage> lobbyMessagesBox;
    private final ArrayList<CtoSMessage> messagesBox;

    public AskListener(ClientNodeInterface clientNode) {
        this.clientNode = clientNode;
        if(clientNode == null){
            throw new RuntimeException("Connection node cannot be null");
        }
        this.lobbyMessagesBox = new ArrayList<>();
        this.messagesBox = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processLobbyMessages();
            processMessages();
        }
    }

    @Override
    public void addLobbyMessage(CtoSLobbyMessage message) {
        synchronized (lobbyMessagesBox) {
            if (message == null) {
                throw new RuntimeException("Message cannot be null");
            }
            lobbyMessagesBox.add(message);
            lobbyMessagesBox.notify();
        }
    }

    @Override
    public void addMessage(CtoSMessage message) {
        synchronized (messagesBox) {
            if (message == null) {
                throw new RuntimeException("Message cannot be null");
            }
            messagesBox.add(message);
            messagesBox.notify();
        }
    }

    @Override
    public void processLobbyMessages() {
        synchronized (lobbyMessagesBox) {
            if (lobbyMessagesBox.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            CtoSLobbyMessage message = lobbyMessagesBox.getFirst();
            try {
                clientNode.uploadToServer(message);
                lobbyMessagesBox.removeFirst();
            } catch (UploadFailureException e) {
                // If the message cannot be uploaded to the server
                try {
                    wait();
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void processMessages() {
        synchronized (messagesBox) {
            if (messagesBox.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            CtoSMessage message = messagesBox.getFirst();
            try {
                clientNode.uploadToServer(message);
                messagesBox.removeFirst();
            } catch (UploadFailureException e) {
                // If the message cannot be uploaded to the server
                try {
                    wait();
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
