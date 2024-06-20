package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a game is already ended and a player tries to join it.
 */
public class GameAlreadyEndedException extends LobbyMessageException {
    /**
     * Creates a new GameAlreadyEndedException with the given message.
     * @param message The message of the exception.
     */
    public GameAlreadyEndedException(String message) {
        super(
                LobbyMessageExceptionEnumeration.GAME_ALREADY_ENDED_EXCEPTION,
                message
        );
    }
}
