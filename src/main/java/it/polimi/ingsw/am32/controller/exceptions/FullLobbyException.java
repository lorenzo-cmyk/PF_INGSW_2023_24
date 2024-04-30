package it.polimi.ingsw.am32.controller.exceptions;

/**
 * This exception is thrown when a player tries to join a full lobby.
 */
public class FullLobbyException extends Exception {
    /**
     * Constructor for the exception.
     *
     * @param message The message to be displayed when the exception is thrown
     */
    public FullLobbyException(String message) { super(message); }
}
