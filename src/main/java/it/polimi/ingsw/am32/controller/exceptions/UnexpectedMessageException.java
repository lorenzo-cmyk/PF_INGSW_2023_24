package it.polimi.ingsw.am32.controller.exceptions;

/**
 * The UnexpectedMessageException class represents an exception that is thrown when an unexpected message is received.
*/
public class UnexpectedMessageException extends Exception {
    /**
     * Constructor for the UnexpectedMessageException class.
     * @param message The message to be displayed when the exception is thrown.
     */
    public UnexpectedMessageException(String message) {
        super(message);
    }
}
