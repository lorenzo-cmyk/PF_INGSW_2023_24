package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This class represents a custom exception that is thrown when a game operation is attempted
 * before the game has officially started. It extends the Exception class, thereby inheriting
 * its methods and can be used in a try-catch block.
 */
public class GameNotYetStartedException extends Exception {
    /**
     * Constructor for the GameNotYetStartedException class.
     *
     * @param message The detail message which is saved for later retrieval by the Throwable.getMessage() method.
     */
    public GameNotYetStartedException(String message) {
        super(message);
    }
}
