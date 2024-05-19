package it.polimi.ingsw.am32.controller;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

@SuppressWarnings("ALL")
public interface VirtualViewInterface {
    void addMessage(StoCMessage msg);
    void flushMessages();
    void changeNode(NodeInterface node);
}
