package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

import java.io.Serializable;

/**
 * This interface represents a message from the client to the server in the lobby phase.
 * It contains a single method to elaborate the message
 */
public interface CtoSLobbyMessage extends Serializable {
    /**
     * Elaborates the message associated with the specified nodeInterface.
     * @param nodeInterface the serverNode of the player
     * @return The game controller that will manage the game
     * @throws LobbyMessageException represents an exception that can be thrown during the elaboration of a generic
     * Lobby-Message.
     */
    GameController elaborateMessage(NodeInterface nodeInterface) throws LobbyMessageException;
    /**
     * This method provides a string representation of a message object, which can be useful for debugging purposes.
     * It will be overridden by the classes that implement the StoCMessage interface.
     *
     * @return A string representation of the CtoSLobbyMessage object.
     */
    String toString(); // Used for debugging purposes
}
