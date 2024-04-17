package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a duplicate player is encountered.
 * It extends the Exception class, meaning it is a checked exception.
 * Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.
 */
public class DuplicateNicknameException extends Exception {
    /**
     * Constructs a new DuplicateNicknameException with the specified detail message.
     * The detail message is saved for later retrieval by the Throwable.getMessage() method.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public DuplicateNicknameException(String message) {
        super(message);
    }
}
