package it.polimi.ingsw.am32.model.exceptions;

/**
 * This class represents a custom exception that is thrown when a player is not found.
 * It extends the Exception class, meaning it is a checked exception.
 * Checked exceptions need to be declared in a method or constructor's throws clause or caught within the method.
 */
public class PlayerNotFoundException extends Exception {
    /**
     * Constructs a new PlayerNotFoundException with the specified detail message.
     * The detail message is saved for later retrieval by the Throwable.getMessage() method.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
