package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.network.NodeInterface;

import java.io.Serializable;

/**
 * This interface represents a message from the client to the server in the lobby phase.
 * It contains a single method to elaborate the message with a game manager.
 */
public interface CtoSLobbyMessage extends Serializable {
    /**
     * Elaborates the message with the specified game manager.
     *
     * @param nodeInterface The node interface that received the message.
     */
    void elaborateMessage(NodeInterface nodeInterface);
}
