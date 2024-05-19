package it.polimi.ingsw.am32.chat.exceptions;

/**
 * This class represents an exception that is thrown when a null message is encountered.
 */
public class NullMessageException extends RuntimeException {

    /**
     * Constructs a new NullMessageException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public NullMessageException(String message) {
        super(message);
    }
}
