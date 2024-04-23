package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.message.StoCMessage;
import it.polimi.ingsw.am32.network.NodeInterface;

import java.util.ArrayList;

public class VirtualView implements VirtualViewInterface{
    private NodeInterface connectionNode;
    private ArrayList<StoCMessage> messages;
    private GameController gameController;

    public VirtualView(NodeInterface connectionNode){
        this.connectionNode = connectionNode;
        this.messages = new ArrayList();
    }
    public void changeNode(NodeInterface connectionNode){
        //TODO
    }
    public void addMessage(StoCMessage message) {

        this.messages.add(message);
        // TODO
    }
    public void processMessage() {
        // TODO
    }
}
