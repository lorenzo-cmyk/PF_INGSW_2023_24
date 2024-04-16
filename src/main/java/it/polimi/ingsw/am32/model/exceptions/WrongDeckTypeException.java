package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when an incorrect deck type is used.
 * It extends the Exception class, meaning it is a checked exception.
 * Checked exceptions need to be declared in a method or constructor's throws clause if they can be thrown by the
 * execution of the method or constructor and propagate outside the method or constructor boundary.
 */
public class WrongDeckTypeException extends RuntimeException {
    /**
     * Constructs a new WrongDeckTypeException.
     */
    public WrongDeckTypeException(String message) {
        super(message);
    }
}
