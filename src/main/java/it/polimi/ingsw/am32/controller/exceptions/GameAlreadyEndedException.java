package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This exception is thrown when a game is already ended and a player tries to join it.
 */
public class GameAlreadyEndedException extends Exception {
    /**
     * Constructor
     *
     * @param message The message to be shown
     */
    public GameAlreadyEndedException(String message) {
        super(message);
    }
}
