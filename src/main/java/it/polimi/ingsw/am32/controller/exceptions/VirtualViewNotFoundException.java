package it.polimi.ingsw.am32.controller.exceptions;

/**
 * Represents an exception that is thrown when a listener is not found
 */
public class VirtualViewNotFoundException extends Exception {
    /**
     * Constructor
     * @param message The message of the exception
     */
    public VirtualViewNotFoundException(String message) { super(message); }
}
