package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This exception is thrown when a game is already started and a player tries to join it.
 */
public class GameAlreadyStartedException extends Exception {
    /**
     * Constructor
     *
     * @param message The message to be shown
     */
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
