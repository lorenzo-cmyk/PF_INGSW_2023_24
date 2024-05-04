package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.NodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

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
        messageQueue = new ArrayList<>();
    }

    /**
     * The run method of the VirtualView class.
     */
    public void run() {
        while (true) {
            processMessage();
        }
    }

    /**
     * Changes the connection node associated with the virtual view. Used for reconnections
     * @param node The new connection node to associate with the virtual view.
     */
    public void changeNode(NodeInterface node) {
        // TODO should we synchronize this?
        connectionNode = node;
    }

    /**
     * Adds a message to the queue of messages to be sent to the client.
     * @param message The message to be added to the queue.
     */
    public void addMessage(StoCMessage message) {
        synchronized(messageQueue) {
            messageQueue.add(message);
            notifyAll();
        }
    }

    /**
     * Processes the message queue.
     */
    public void processMessage() {
        synchronized(messageQueue) {
            if (messageQueue.isEmpty()) { // There is no message to be delivered to the client
                try {
                    wait(); // Enter sleep state, and what for a message to be added to the queue
                } catch (InterruptedException e) {
                    // TODO
                }
            }
            StoCMessage message = messageQueue.getLast(); // Pop message from queue
            messageQueue.removeLast();
            try {
                connectionNode.uploadToClient(message);
            }catch (UploadFailureException e) {
                // TODO
            }
        }
    }

    /**
     * Flushes the message queue.
     */
    public void flushMessage() {
        synchronized(messageQueue) {
            messageQueue.clear();
        }
    }
}
