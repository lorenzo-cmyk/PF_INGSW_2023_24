// TODO: Rebuild method list according to the main GameController class
package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.network.NodeInterface;

public interface GameControllerInterface {
    void submitChatMessage(ChatMessage chatMessage);
    void disconnect(NodeInterface node);
    void reconnect(NodeInterface node);
}
