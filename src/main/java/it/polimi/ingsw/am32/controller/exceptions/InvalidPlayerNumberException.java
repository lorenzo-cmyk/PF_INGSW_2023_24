package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This exception is thrown when the number of players in a game is invalid.
 */
public class InvalidPlayerNumberException extends Exception {
    /**
     * Creates a new InvalidPlayerNumberException with the given message.
     *
     * @param message The message of the exception
     */
    public InvalidPlayerNumberException(String message) {
        super(message);
    }
}
