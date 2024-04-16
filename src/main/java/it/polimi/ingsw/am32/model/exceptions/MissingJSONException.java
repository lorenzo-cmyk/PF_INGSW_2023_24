package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a required JSON file is missing.
 * It extends the RuntimeException class, meaning it is an unchecked exception.
 * Unchecked exceptions do not need to be declared in a method or constructor's throws clause.
 */
public class MissingJSONException extends RuntimeException {
    /**
     * Constructs a new MissingJSONException with the specified detail message.
     * The detail message is saved for later retrieval by the Throwable.getMessage() method.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *                Throwable.getMessage() method.
     */
    public MissingJSONException(String message) {
        super(message);
    }
}
