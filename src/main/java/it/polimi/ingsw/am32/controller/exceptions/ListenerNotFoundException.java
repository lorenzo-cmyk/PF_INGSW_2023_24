package it.polimi.ingsw.am32.controller.exceptions;

/**
 * Represents an exception that is thrown when a listener is not found
 */
public class ListenerNotFoundException extends Exception {
    /**
     * Constructor
     * @param message The message of the exception
     */
    public ListenerNotFoundException(String message) { super(message); }
}
