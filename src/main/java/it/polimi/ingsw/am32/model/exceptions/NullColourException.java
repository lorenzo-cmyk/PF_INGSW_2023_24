package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a player's color is not set.
 * It extends the base RuntimeException class and provides a constructor to set a custom error message.
 */
public class NullColourException extends RuntimeException{
    /**
     * Constructs a new NullColourException with the specified detail message.
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NullColourException(String message) {
        super(message);
    }
}