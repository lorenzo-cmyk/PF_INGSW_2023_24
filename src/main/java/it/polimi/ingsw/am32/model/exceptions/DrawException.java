package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a draw operation fails.
 * It extends the base Exception class and provides a constructor to set a custom error message.
 */
public class DrawException extends Exception {
    /**
     * Constructs a new DrawException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public DrawException(String message) {
        super(message);
    }
}
