package it.polimi.ingsw.am32.controller.exceptions;

/**
 * PlayerAlreadyConnectedException is thrown when a player tries to connect to the server but he is already connected
 */
public class PlayerAlreadyConnectedException extends Exception {
    /**
     * Constructor
     *
     * @param message the message to be shown
     */
    public PlayerAlreadyConnectedException(String message) {
        super(message);
    }
}
