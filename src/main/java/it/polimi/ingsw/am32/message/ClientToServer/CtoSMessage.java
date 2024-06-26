package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

import java.io.Serializable;

/**
 * This interface represents a message from the client to the server.
 * It contains a single method to elaborate the message with a game controller.
 */
public interface CtoSMessage extends Serializable {

    /**
     * Elaborates the message with the specified game controller.
     * @param gameController The game controller with which the message should be elaborated
     */
    void elaborateMessage(GameController gameController);
    /**
     * This method provides a string representation of a message object, which can be useful for debugging purposes.
     * It will be overridden by the classes that implement the StoCMessage interface.
     *
     * @return A string representation of the CtoSMessage object.
     */
    String toString(); // Used for debugging purposes
}
