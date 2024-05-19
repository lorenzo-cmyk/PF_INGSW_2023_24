package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class AskListener implements AskListenerInterface, Runnable {
    private static final Logger logger = LogManager.getLogger("AskLogger");
    private final ClientNodeInterface clientNode;
    private final ArrayList<CtoSLobbyMessage> lobbyMessagesBox;
    private final ArrayList<CtoSMessage> messagesBox;
    //private boolean firstMessage=true;

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
            logger.info(message+"added to messagesBox");
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
            System.out.println("Adding message to messagesBox");
            logger.info(message+"added to messagesBox");
            messagesBox.notify();
        }
    }

    @Override
    public void processLobbyMessages() {
        synchronized (lobbyMessagesBox) {
            if (!lobbyMessagesBox.isEmpty()) { //TODO: check if this is the right way to do it
                /*try {
                    logger.info("MessagesBox is empty");
                    lobbyMessagesBox.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }*/
                CtoSLobbyMessage message = lobbyMessagesBox.getFirst();
                try {
                    clientNode.uploadToServer(message);
                    System.out.println(message + " upload to server successfully");
                    logger.info(message + " upload to server successfully");
                    lobbyMessagesBox.removeFirst();
                } catch (UploadFailureException e) {
                    // If the message cannot be uploaded to the server
                    try {
                        logger.info(message + " upload to server failed");
                        lobbyMessagesBox.wait();
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    @Override
    public void processMessages() {
        synchronized (messagesBox) {
            if (!messagesBox.isEmpty()) { //TODO: check if this is the right way to do it
               /* try {
                    logger.info("MessagesBox is empty");
                    messagesBox.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }*/
                CtoSMessage message = messagesBox.getFirst();
                try {
                    clientNode.uploadToServer(message);
                    System.out.println(message + " upload to server successfully");
                    logger.info(message + " upload to server successfully");
                    messagesBox.removeFirst();
                } catch (UploadFailureException e) {
                    // If the message cannot be uploaded to the server
                    try {
                        System.out.println("MessagesBox error");
                        messagesBox.wait();
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

}
