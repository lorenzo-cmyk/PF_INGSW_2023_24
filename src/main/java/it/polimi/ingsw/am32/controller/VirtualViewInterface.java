package it.polimi.ingsw.am32.controller;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

public interface VirtualViewInterface {
    void addMessage(StoCMessage msg);
    void processMessage();
    void flushMessages();
    void changeNode(NodeInterface node);
}
