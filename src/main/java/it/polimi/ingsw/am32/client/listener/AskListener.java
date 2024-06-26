package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Used to manage the messages that are sent to the server.
 * Stays in a loop and waits for messages to be added to the queues (according to their type).
 * When a new message is added, it sends it to the server through the clientNode.
 *
 * @author Jie, Antony, Lorenzo
 */
public class AskListener implements AskListenerInterface, Runnable {
    /**
     * The Logger of the AskListener class.
     */
    private static final Logger logger = LogManager.getLogger(AskListener.class);
    /**
     * The clientNode associated with the AskListener.
     */
    private final ClientNodeInterface clientNode;
    /**
     * The queue of messages that are to be sent to the server.
     */
    private final ArrayList<CtoSMessage> messageQueue;
    /**
     * The queue of messages that are to be sent to the server.
     */
    private final ArrayList<CtoSLobbyMessage> lobbyMessageQueue;
    /**
     * An object used to synchronize access to the queues.
     */
    private final Object queuesLock;

    /**
     * Constructor for the AskListener class.
     *
     * @param clientNode The clientNode associated with the AskListener.
     */
    public AskListener(ClientNodeInterface clientNode) {
        this.clientNode = clientNode;
        // clientNode cannot be null
        if (clientNode == null) {
            throw new RuntimeException("clientNode cannot be null");
        }
        this.messageQueue = new ArrayList<>();
        this.lobbyMessageQueue = new ArrayList<>();
        this.queuesLock = new Object();
    }

    /**
     * The run method of the AskListener class.
     */
    public void run() {
        logger.debug("AskListener thread has now started");
        while (!Thread.currentThread().isInterrupted()) {
            processMessage();
        }
        // If we get to this point, the AskListener thread dies
        logger.error("AskListener thread shut down");
    }

    /**
     * Adds a message to the queue of messages to be sent to the server.
     *
     * @param message The message to be added to the queue.
     */
    public void addMessage(CtoSMessage message) {
        // Message cannot be null
        if (message == null) {
            throw new RuntimeException("Message cannot be null");
        }
        synchronized (queuesLock) {
            messageQueue.add(message);
            logger.debug("Message added to the AskListener queue: {}", message.getClass());
            queuesLock.notifyAll(); // Notify the processing thread that a message has been added to the queue
        }
    }

    /**
     * Adds a lobbyMessage to the queue of lobbyMessage to be sent to the server.
     *
     * @param lobbyMessage The lobbyMessage to be added to the queue.
     */
    public void addMessage(CtoSLobbyMessage lobbyMessage) {
        // lobbyMessage cannot be null
        if (lobbyMessage == null) {
            throw new RuntimeException("lobbyMessage cannot be null");
        }
        synchronized (queuesLock) {
            lobbyMessageQueue.add(lobbyMessage);
            logger.debug("lobbyMessage added to the AskListener queue: {}", lobbyMessage.getClass());
            queuesLock.notifyAll(); // Notify the processing thread that a message has been added to the queue
        }
    }

    /**
     * Flushes both the message and lobbyMessage queues.
     */
    public void flushMessages() {
        synchronized (queuesLock) {
            lobbyMessageQueue.clear();
            messageQueue.clear();
        }
    }

    /**
     * Processes the messages in the queues.
     * The method will keep running until both queues are empty.
     * If the queues are empty, the method will wait until a message is added to one of the queues.
     */
    private void processMessage() {
        logger.debug("AskListener thread awake and processing messages");

        // If there are no messages to be delivered to the server, the thread goes to sleep.
        // It will be woken up when a message is added to one of the queues.

        synchronized (queuesLock) {
            // Keep waiting until there are messages to be delivered to the server
            while (messageQueue.isEmpty() && lobbyMessageQueue.isEmpty()) {
                try {
                    queuesLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("AskListener thread interrupted while waiting for messages");
                    return;
                }
            }
        }

        // If we get to this point, there are messages to be delivered to the server.
        // Keeps delivering messages until both queues are empty.

        processLobbyMessages();
        processRegularMessage();
    }

    /**
     * Send the regular messages to the server.
     * The method will keep running until the message queue is empty.
     * If the queue is empty, the method will return.
     */
    private void processRegularMessage() {
        while(!Thread.currentThread().isInterrupted()){
            CtoSMessage currentMessage;
            // Acquire the lock on the message queue to retrieve the next message to be delivered.
            // If the queue is empty, return early since our work is done.
            synchronized (queuesLock){
                if(messageQueue.isEmpty()){
                    return;
                }
                currentMessage = messageQueue.getFirst();
            }
            // We have a message, let's try to send it to the server.
            try {
                // Acquire the lock on the clientNode and try to send the message.
                synchronized (clientNode) {
                    clientNode.uploadToServer(currentMessage);
                }
                // If we successfully sent the message, remove it from the queue.
                synchronized (queuesLock) {
                    messageQueue.removeFirst();
                }
                logger.debug("Message sent to the server: {}", currentMessage.getClass());
            } catch (UploadFailureException e) {
                logger.error("Failed to send message to the server: {}", e.getMessage());
                // If we failed to send the message, we lost connection with the server.
                // If we want to reconnect we will flush the message queue and send a reconnection message.
                // Adding a reconnection message to the AskListener queue will wake up the thread.
                synchronized (queuesLock) {
                    try {
                        queuesLock.wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        logger.error("AskListener thread interrupted while awaiting a wake-up signal " +
                                "after a failed message upload");
                        return;
                    }
                }
            }
        }
    }

    /**
     * Send the lobby messages to the server.
     * The method will keep running until the lobbyMessage queue is empty.
     * If the queue is empty, the method will return.
     */
    private void processLobbyMessages() {
        while(!Thread.currentThread().isInterrupted()){
            CtoSLobbyMessage currentLobbyMessage;
            // Acquire the lock on the lobbyMessage queue to retrieve the next message to be delivered.
            // If the lobbyMessage queue is empty, return early since our work is done.
            synchronized (queuesLock){
                if(lobbyMessageQueue.isEmpty()){
                    return;
                }
                currentLobbyMessage = lobbyMessageQueue.getFirst();
            }
            // We have a message, let's try to send it to the server.
            try {
                // Acquire the lock on the clientNode and try to send the message.
                synchronized (clientNode) {
                    clientNode.uploadToServer(currentLobbyMessage);
                }
                // If we successfully sent the message, remove it from the queue.
                synchronized (queuesLock) {
                    if(!lobbyMessageQueue.isEmpty()) {
                        lobbyMessageQueue.removeFirst();
                    }
                }
                logger.debug("LobbyMessage sent to the server: {}", currentLobbyMessage.getClass());
            } catch (UploadFailureException e) {
                logger.error("Failed to send lobbyMessage to the server: {}", e.getMessage());
                // If we failed to send the message, we lost connection with the server.
                // If we want to reconnect we will flush the message queue and send a reconnection message.
                // Adding a reconnection message to the AskListener queue will wake up the thread.
                synchronized (queuesLock) {
                    try {
                        queuesLock.wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        logger.error("AskListener thread interrupted while awaiting a wake-up signal " +
                                "after a failed lobbyMessage upload");
                        return;
                    }
                }
            }
        }
    }
}
