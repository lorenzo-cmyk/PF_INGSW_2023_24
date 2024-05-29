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
 * Stays in a loop and waits for messages to be added to the queue. When a new message is added, it sends it to the client through the connection node.
 *
 * @author Anto, Lorenzo
 */
public class VirtualView implements VirtualViewInterface, Runnable {
    /**
     * The Logger of the VirtualView class.
     */
    private static final Logger logger = LogManager.getLogger(VirtualView.class);
    /**
     * The connection node associated with the virtual view.
     */
    private NodeInterface connectionNode;
    /**
     * The queue of messages that are to be sent to the client.
     */
    private final ArrayList<StoCMessage> messageQueue;
    /**
     * A boolean that indicates if the virtual view is terminating.
     */
    private boolean terminating;

    /**
     * Constructor for the VirtualView class.
     *
     * @param connectionNode The connection node associated with the virtual view.
     */
    public VirtualView(NodeInterface connectionNode) {
        this.connectionNode = connectionNode;
        // Connection node cannot be null
        if (connectionNode == null) {
            throw new CriticalFailureException("Connection node cannot be null");
        }
        this.messageQueue = new ArrayList<>();
        this.terminating = false;
    }

    /**
     * The run method of the VirtualView class.
     */
    public synchronized void run() {
        logger.debug("VirtualView thread has now started");
        while (!Thread.currentThread().isInterrupted() && !terminating) {
            processMessage();
        }
        // If we get to this point, the virtualView thread dies
        logger.debug("VirtualView thread shut down");
    }

    /**
     * Changes the connection node associated with the virtual view. Used for reconnections
     *
     * @param node The new connection node to associate with the virtual view.
     */
    public synchronized void changeNode(NodeInterface node) {
        connectionNode = node;
    }

    /**
     * Adds a message to the queue of messages to be sent to the client.
     *
     * @param message The message to be added to the queue.
     */
    public synchronized void addMessage(StoCMessage message) {
        // Message cannot be null
        if (message == null) {
            throw new CriticalFailureException("Message cannot be null");
        }
        messageQueue.add(message);
        logger.debug("Message added to the VirtualView queue: {}", message.getClass());
        notifyAll(); // Wake up early threads waiting to be synchronized to this
    }

    /**
     * Processes the message queue.
     */
    protected synchronized void processMessage() { // Synchronized statement might not be necessary, but it is kept as it is considered "more correct"
        logger.debug("VirtualView thread awake and processing messages");
        if (messageQueue.isEmpty()) { // There is no message to be delivered to the client
            try {
                wait(); // Enter sleep state, and what for a message to be added to the queue
                if (terminating) { // If the virtual view is being shutdown, return early
                    return;
                }
                else if (connectionNode == null) { // Player was disconnected, and the server node destroyed
                    return; // WARNING: This should never happen, as the connection node should never be null.
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // There are messages to be delivered to the client
        while (!messageQueue.isEmpty()) { // Keep delivering until there are no more messages to send to the client
            StoCMessage message = messageQueue.getFirst(); // Get the message from the queue but don't delete it yet
            try {
                connectionNode.uploadToClient(message);
                messageQueue.removeFirst(); // Remove the delivered message from the queue
            } catch (UploadFailureException e) {
                // If the message cannot be uploaded to the client, the connection is lost. The thread is put on wait().
                try {
                    wait();
                    if (terminating) { // If the virtual view is being shutdown, return early
                        return; // Probably not necessary, but it's here for clarity.
                    }
                    else if (connectionNode == null) { // Player was disconnected, and the server node destroyed
                        return; // WARNING: This should never happen, as the connection node should never be null.
                    }
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /**
     * Flushes the message queue.
     */
    public synchronized void flushMessages() {
        messageQueue.clear();
    }

    /*
     * Method used to retrieve the message queue. Used for testing purposes only.
     */
    protected synchronized ArrayList<StoCMessage> getMessageQueue() {
        return messageQueue;
    }

    /*
     * Method used to retrieve the connection node. Used for testing purposes only.
     */
    protected synchronized NodeInterface getConnectionNode() {
        return connectionNode;
    }

    /*
     * Method used to terminate the thread running the virtual view.
     * Used by the GameController, when the player's virtual view must be destroyed
     */
    protected synchronized void setTerminating() {
        terminating = true;
        logger.debug("VirtualView thread is being shut down");
        notifyAll(); // Wake up early threads waiting to be synchronized to this
    }
}
