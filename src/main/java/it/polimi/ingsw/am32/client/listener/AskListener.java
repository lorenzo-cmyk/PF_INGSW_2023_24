package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * The AskListener class is responsible for listening for messages that need to be sent to the server.
 * It is a thread that runs continuously and waits for messages to be added to the queues.
 */
public class AskListener implements AskListenerInterface, Runnable {
    /**
     * The logger associated with the AskListener class.
     */
    private static final Logger logger = LogManager.getLogger("AskLogger");
    /**
     * The connection node associated with the listener.
     */
    private final ClientNodeInterface clientNode;
    /**
     * The queue of lobby messages that need to be sent to the server.
     */
    private final ArrayList<CtoSLobbyMessage> lobbyMessagesBox;
    /**
     * The queue of messages that need to be sent to the server.
     */
    private final ArrayList<CtoSMessage> messagesBox;

    /**
     * Constructor for the AskListener class.
     *
     * @param clientNode The connection node associated with the listener.
     */
    public AskListener(ClientNodeInterface clientNode) {
        if (clientNode == null){
            throw new RuntimeException("Connection node cannot be null");
        }

        this.clientNode = clientNode;
        this.lobbyMessagesBox = new ArrayList<>();
        this.messagesBox = new ArrayList<>();
    }

    /**
     * The run method of the AskListener class, that the thread will execute continuously.
     */
    @Override
    public synchronized void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processMessage();
        }
    }

    // Note: The addMessage method is overloaded, to allow for different types of messages to be added to the queues.

    /**
     * Adds a message to the queue associated with that message.
     *
     * @param message The message to be added to the queue.
     */
    @Override
    public synchronized void addMessage(CtoSLobbyMessage message) {
        if (message == null) {
            throw new RuntimeException("Message cannot be null");
        }

        lobbyMessagesBox.add(message);
        System.out.println("Adding message to lobbyMessagesBox");
        logger.info("{} added to messagesBox", message);
        notifyAll(); // Wake up the thread
    }

    /**
     * Adds a message to the queue associated with that message.
     *
     * @param message The message to be added to the queue.
     */
    @Override
    public synchronized void addMessage(CtoSMessage message) {
        if (message == null) {
            throw new RuntimeException("Message cannot be null");
        }

        messagesBox.add(message);
        System.out.println("Adding message to messagesBox");
        logger.info("{} added to messagesBox", message);
        notifyAll(); // Wake up the thread
    }

    /**
     * Checks if there are messages to be delivered to the server.
     * If there are no messages, the thread is put into a waiting state.
     * If there are messages, they are sent to the server and removed from the queues until they are empty.
     * Lobby messages are always sent before regular messages if present.
     */
    protected synchronized void processMessage() {
        if (lobbyMessagesBox.isEmpty() && messagesBox.isEmpty()) { // Both message queues are empty
            try {
                wait(); // Wait until a message is added to one of the queues
            } catch (InterruptedException e) { // Thread was somehow interrupted
                Thread.currentThread().interrupt();
                return; // Exit immediately
            }
        }

        // Messages are present in at least one of the queues

        while (!lobbyMessagesBox.isEmpty()) { // Empty the lobby message queue first
            CtoSLobbyMessage message = lobbyMessagesBox.getFirst(); // Get the first message from the lobby queue but don't remove it yet
            try {
                clientNode.uploadToServer(message); // Try to send the message to the server
                lobbyMessagesBox.removeFirst(); // The message was successfully sent, it can be removed from the queue

                System.out.println(message + " uploaded lobby msg to server successfully");
                logger.info("{} uploaded lobby msg to server successfully", message);
            } catch (UploadFailureException e) { // If the message cannot be uploaded to the server
                try {
                    logger.info("{} upload lobby msg to server failed", message);
                    wait(); // Wait for server to reconnect or for a new message to be added to a queue
                    return; // Exit immediately
                } catch (InterruptedException e1) { // Thread was somehow interrupted
                    Thread.currentThread().interrupt();
                    return; // Exit immediately
                }
            }
        }

        while (!messagesBox.isEmpty()) { // Empty the message queue
            CtoSMessage message = messagesBox.getFirst(); // Get the first message from the message queue but don't remove it yet
            try {
                clientNode.uploadToServer(message); // Try to send the message to the server
                messagesBox.removeFirst(); // The message was successfully sent, it can be removed from the queue

                System.out.println(message + " uploaded msg to server successfully");
                logger.info("{} uploaded msg to server successfully", message);
            } catch (UploadFailureException e) { // If the message cannot be uploaded to the server
                try {
                    logger.info("{} upload msg to server failed", message);
                    wait(); // Wait for server to reconnect or for a new message to be added to a queue
                    return; // Exit immediately
                } catch (InterruptedException e1) { // Thread was somehow interrupted
                    Thread.currentThread().interrupt();
                    return; // Exit immediately
                }
            }
        }

        // If we reach this point, all messages have been sent
    }
}
