package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a game is already started and a player tries to join it.
 */
public class GameAlreadyStartedException extends LobbyMessageException {
    /**
     * Creates a new GameAlreadyStartedException with the given message.
     * @param message The message of the exception.
     */
    public GameAlreadyStartedException(String message) {
        super(
                LobbyMessageExceptionEnumeration.GAME_ALREADY_STARTED_EXCEPTION,
                message
        );
    }
}
