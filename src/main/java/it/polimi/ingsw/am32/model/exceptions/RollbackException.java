package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception named RollbackException.
 * It extends the Exception class, hence it is a checked exception.
 * This exception is thrown when a rollback operation is required in the application.
 */
public class RollbackException extends Exception{

    /**
     * Constructor for the RollbackException class.
     * It calls the superclass constructor (Exception) and passes the error message to it.
     *
     * @param message The detail message. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     */
    public RollbackException(String message) {
        super(message);
    }
}
