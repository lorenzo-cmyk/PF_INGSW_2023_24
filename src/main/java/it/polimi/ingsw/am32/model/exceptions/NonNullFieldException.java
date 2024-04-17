package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a non-null field is encountered where a null field was expected.
 * It extends the RuntimeException class, meaning it is an unchecked exception.
 */
public class NonNullFieldException extends RuntimeException {

    /**
     * Constructor for the NonNullFieldException class.
     *
     * @param message The detail message associated with the exception. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NonNullFieldException(String message) {
        super(message);
    }
}