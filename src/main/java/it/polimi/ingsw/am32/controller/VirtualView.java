package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.NodeInterface;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VirtualView implements VirtualViewInterface, Runnable {
    private final NodeInterface connectionNode;
    private final BlockingQueue<StoCMessage> messages;

    public VirtualView(NodeInterface connectionNode) {
        this.connectionNode = connectionNode;
        this.messages = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                StoCMessage message = messages.take(); // This will block if the queue is empty
                processMessage(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupt status
                break;
            }
        }
    }

    public void changeNode(NodeInterface connectionNode) {
        //TODO: Implement this method
    }

    public void addMessage(StoCMessage message) {
        this.messages.add(message);
        // Wake up the observer thread if it was waiting for a message
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void processMessage() {
        // TODO: Fix this method
    }

    public void processMessage(StoCMessage message) {
        // TODO: Process the message
    }
}
