package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This class represents an exception that is thrown when no game is found.
 */
public class GameNotFoundException extends LobbyMessageException {
    /**
     * Constructs a new NoGameFoundException with the given message.
     * @param message The message of the exception.
     */
    public GameNotFoundException(String message) {
        super(
                LobbyMessageExceptionEnumeration.GAME_NOT_FOUND_EXCEPTION,
                message
        );
    }
}
