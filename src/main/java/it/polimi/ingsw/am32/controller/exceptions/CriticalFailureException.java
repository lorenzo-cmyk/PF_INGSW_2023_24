package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This exception is thrown when a critical failure occurs.
 * A critical failure is an error that should never happen, and that is not recoverable.
 * When this exception is thrown, the program should be terminated.
 */
public class CriticalFailureException extends RuntimeException {
    /**
     * Constructor
     *
     * @param message The message to be shown
     */
    public CriticalFailureException(String message) { super(message); }
}
