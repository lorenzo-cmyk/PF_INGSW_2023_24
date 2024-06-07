package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Used to manage the messages that are sent to the client.
 * Stays in a loop and waits for messages to be added to the queue.
 * When a new message is added, it sends it to the client through the connection node.
 *
 * @author Antony, Lorenzo
 */
public class VirtualView implements VirtualViewInterface, Runnable {
    /**
     * The Logger of the VirtualView class.
     */
    private static final Logger logger = LogManager.getLogger(VirtualView.class);
    /**
     * The connection node associated with the VirtualView.
     */
    private NodeInterface connectionNode;
    /**
     * The queue of messages that are to be sent to the client.
     */
    private final ArrayList<StoCMessage> messageQueue;
    /**
     * A boolean that indicates if the VirtualView is terminating.
     * The attribute is volatile because it is accessed by multiple threads.
     */
    private volatile boolean terminating;
    /**
     * An object used as a lock for the non-final attribute connectionNode.
     */
    private final Object connectionNodeLock;

    /**
     * Constructor for the VirtualView class.
     *
     * @param connectionNode The connection node associated with the VirtualView.
     */
    public VirtualView(NodeInterface connectionNode) {
        this.connectionNode = connectionNode;
        // Connection node cannot be null
        if (connectionNode == null) {
            throw new CriticalFailureException("Connection node cannot be null");
        }
        this.messageQueue = new ArrayList<>();
        this.terminating = false;
        this.connectionNodeLock = new Object();
    }

    /**
     * The run method of the VirtualView class.
     */
    public void run() {
        logger.debug("VirtualView thread has now started");
        while (!Thread.currentThread().isInterrupted() && !isTerminating()) {
            processMessage();
        }
        // If we get to this point, the VirtualView thread dies
        logger.debug("VirtualView thread shut down");
    }

    /**
     * Changes the connection node associated with the VirtualView. Used for reconnections
     *
     * @param node The new connection node to associate with the VirtualView.
     */
    public void changeNode(NodeInterface node) {
        synchronized (connectionNodeLock) {
            connectionNode = node;
        }
        logger.debug("connectionNode changed");
    }

    /**
     * Adds a message to the queue of messages to be sent to the client.
     *
     * @param message The message to be added to the queue.
     */
    public void addMessage(StoCMessage message) {
        // Message cannot be null
        if (message == null) {
            throw new CriticalFailureException("Message cannot be null");
        }
        synchronized (messageQueue) {
            messageQueue.add(message);
            logger.debug("Message added to the VirtualView queue: {}", message.getClass());
            messageQueue.notifyAll(); // Notify the processing thread that a message has been added to the queue
        }
    }

    /**
     * Processes the message queue.
     */
    protected void processMessage() {
        logger.debug("VirtualView thread awake and processing messages");

        // If there are no messages to be delivered to the client, the thread goes to sleep.
        // It will be woken up when a message is added to the queue or when the VirtualView is being shut down.

        synchronized (messageQueue) {
            // Keep waiting until there are messages to be delivered to the client or a termination signal is received
            while(messageQueue.isEmpty() && !isTerminating()) {
                try {
                    messageQueue.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("VirtualView thread interrupted while waiting for messages");
                    return;
                }
            }

            // If we woke up because of a termination signal, kill the thread.
            if (isTerminating()) {
                return;
            }
        }

        // If we get to this point, there are messages to be delivered to the client.
        // Keeps delivering messages until the queue is empty, or we are told to terminate.
        while(!Thread.currentThread().isInterrupted()){
            StoCMessage currentMessage;

            // Acquire the lock on the message queue to retrieve the next message to be delivered
            // If the queue is empty, return early so the run() method of the class will relaunch
            // the processMessage() method putting the thread back to sleep until a new message is added
            synchronized (messageQueue){
                if(messageQueue.isEmpty()){
                    return;
                }
                currentMessage = messageQueue.getFirst();
            }

            // We have a message, let's try to send it to the client
            try {
                // Acquire the lock on the connection node and try to send the message
                synchronized (connectionNodeLock) {
                    connectionNode.uploadToClient(currentMessage);
                }
                // If we successfully sent the message, remove it from the queue
                synchronized (messageQueue) {
                    messageQueue.removeFirst();
                }
                logger.debug("Message sent to the client: {}", currentMessage.getClass());
            } catch (UploadFailureException e) {
                logger.error("Failed to send message to the client: {}", e.getMessage());
                // If we failed to send the message, we lost connection with the client.
                // When the client will reconnect, the GameController will swap the old connectionNode
                // with a new working one. The GameController will also clean the message queue to avoid
                // sending outdated messages to the client. We just need to wait until a new message is added
                // to the queue (or until the VirtualView is being shut down) as it will represent a signal
                // that we are ready to start working again.
                synchronized (messageQueue) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        logger.error("VirtualView thread interrupted while awaiting a wake-up signal after a failed message upload");
                        return;
                    }
                    // If we woke up because of a termination signal, kill the thread.
                    if (isTerminating()) {
                        return;
                    }
                    // If we woke up because a new message was added to the queue, we will
                    // try to send it (and the whole loop will restart)
                }
            }
        }
    }

    /**
     * Flushes the message queue.
     */
    public void flushMessages() {
        synchronized (messageQueue) {
            messageQueue.clear();
        }
    }

    /**
     * Method used to retrieve the message queue. Used for testing purposes only.
     *
     * @return The message queue associated with the VirtualView.
     */
    protected ArrayList<StoCMessage> getMessageQueue() {
        synchronized (messageQueue) {
            return messageQueue;
        }
    }

    /**
     * Method used to retrieve the connection node. Used for testing purposes only.
     *
     * @return The connection node associated with the VirtualView.
     */
    protected NodeInterface getConnectionNode() {
        synchronized (connectionNodeLock) {
            return connectionNode;
        }
    }

    /**
     * Method used to terminate the thread running the VirtualView.
     * Used by the GameController, when the player's VirtualView must be destroyed
     */
    protected void setTerminating() {
        terminating = true;
        logger.debug("VirtualView thread is being shut down");
        synchronized (messageQueue) { // Needs to be executed in a synchronized block to avoid race conditions
            messageQueue.notifyAll(); // Notify the processing thread that the VirtualView is being shut down
        }
    }

    /**
     * Checks if the VirtualView is terminating.
     *
     * @return True if the VirtualView is terminating, false otherwise.
     */
    private boolean isTerminating() {
        return terminating;
    }
}
