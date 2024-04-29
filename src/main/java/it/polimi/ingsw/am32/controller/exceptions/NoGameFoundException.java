package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This class represents an exception that is thrown when no game is found.
 * It extends the Exception class, meaning it's a checked exception.
 * Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.
 */
public class NoGameFoundException extends Exception {
    /**
     * Constructs a new NoGameFoundException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public NoGameFoundException(String message) {
        super(message);
    }
}