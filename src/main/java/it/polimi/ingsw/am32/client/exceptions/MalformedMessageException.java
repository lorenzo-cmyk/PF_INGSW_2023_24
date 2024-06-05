package it.polimi.ingsw.am32.client.exceptions;

/**
 * This class represents a custom exception that is thrown when a chat message is malformed.
 * A chat message could be considered malformed if it doesn't adhere to the expected format or structure.
 * This is a runtime exception, meaning it doesn't need to be declared in a method's or constructor's throws clause.
 */
public class MalformedMessageException extends RuntimeException {

    /**
     * Constructs a new MalformedMessageException with the specified detail message.
     * The detail message is saved for later retrieval by the Throwable.getMessage() method.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public MalformedMessageException(String message) {
        super(message);
    }
}
