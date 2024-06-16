package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when the number of players in a game is invalid.
 */
public class InvalidPlayerNumberException extends LobbyMessageException {
    /**
     * Creates a new InvalidPlayerNumberException with the given message.
     * @param message The message of the exception.
     */
    public InvalidPlayerNumberException(String message) {
        super(
                LobbyMessageExceptionEnumeration.INVALID_PLAYER_NUMBER_EXCEPTION,
                message
        );
    }
}
