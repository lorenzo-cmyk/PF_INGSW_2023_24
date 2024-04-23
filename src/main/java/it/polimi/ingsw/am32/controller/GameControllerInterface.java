package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.network.NodeInterface;
import it.polimi.ingsw.am32.network.RMIServerNode;

public interface GameControllerInterface {
    void submitChatMessage(ChatMessage chatMessage);
    void addNode(NodeInterface node, String nickname, boolean connected);
    void removeListener(VirtualView listener);
    void disconnect(NodeInterface node);
    void reconnect(NodeInterface node);
    RMIServerNode reconFromDeath(String nickname);
}
