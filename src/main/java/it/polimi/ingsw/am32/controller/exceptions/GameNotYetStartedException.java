package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a player tries to connect to a game that has not yet started.
 */
public class GameNotYetStartedException extends LobbyMessageException {
    /**
     * Creates a new GameNotYetStartedException with the given message.
     * @param message The message of the exception.
     */
    public GameNotYetStartedException(String message) {
        super(
                LobbyMessageExceptionEnumeration.GAME_NOT_YET_STARTED_EXCEPTION,
                message
        );
    }
}
