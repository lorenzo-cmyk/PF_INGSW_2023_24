package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;

import java.io.Serializable;

/**
 * This interface represents a message from the client to the server.
 * It contains a single method to elaborate the message with a game controller.
 */
public interface CtoSMessage extends Serializable {

    /**
     * Elaborates the message with the specified game controller.
     *
     * @param gameController The game controller with which the message should be elaborated
     */
    void elaborateMessage(GameController gameController);
}
