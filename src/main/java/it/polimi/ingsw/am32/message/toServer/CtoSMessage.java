package it.polimi.ingsw.am32.message.toServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This interface represents a message from the client to the server.
 * It contains a single method to elaborate the message with a game controller.
 */
public interface CtoSMessage {

    /**
     * Elaborates the message with the specified game controller.
     *
     * @param gameController The game controller with which the message should be elaborated
     */
    void elaborateMessage(GameController gameController);
}
