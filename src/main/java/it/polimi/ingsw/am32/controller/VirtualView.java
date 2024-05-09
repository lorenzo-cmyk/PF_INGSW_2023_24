package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

import java.util.ArrayList;

/**
 * Used to manage the messages that are sent to the client.
 * Stays in a loop and waits for messages to be added to the queue. When a new message is added, it sends it to the client through the connection node.
 *
 * @author Anto
 */
public class VirtualView implements VirtualViewInterface, Runnable {
    /**
     * The connection node associated with the virtual view.
     */
    private NodeInterface connectionNode;
    /**
     * The queue of messages that are to be sent to the client.
     */
    private final ArrayList<StoCMessage> messageQueue;

    /**
     * Constructor for the VirtualView class.
     * @param connectionNode The connection node associated with the virtual view.
     */
    public VirtualView(NodeInterface connectionNode) {
        this.connectionNode = connectionNode;
        // Connection node cannot be null
        if (connectionNode == null) {
            throw new CriticalFailureException("Connection node cannot be null");
        }
        messageQueue = new ArrayList<>();
    }

    /**
     * The run method of the VirtualView class.
     */
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processMessage();
        }
    }

    /**
     * Changes the connection node associated with the virtual view. Used for reconnections
     * @param node The new connection node to associate with the virtual view.
     */
    public synchronized void changeNode(NodeInterface node) {
        connectionNode = node;
    }

    /**
     * Adds a message to the queue of messages to be sent to the client.
     * @param message The message to be added to the queue.
     */
    public synchronized void addMessage(StoCMessage message) {
        // Message cannot be null
        if (message == null) {
            throw new CriticalFailureException("Message cannot be null");
        }
        messageQueue.add(message);
        notifyAll();
    }

    /**
     * Processes the message queue.
     */
    public synchronized void processMessage() {
        if (messageQueue.isEmpty()) { // There is no message to be delivered to the client
            try {
                wait(); // Enter sleep state, and what for a message to be added to the queue
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        StoCMessage message = messageQueue.removeFirst();
        connectionNode.uploadToClient(message);
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
}
