package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a player tries to join a lobby with a nickname that is already in use.
 */
public class CTRPlayerNotFoundException extends LobbyMessageException {
    /**
     * Creates a new CTRPlayerNotFoundException with the given message.
     * @param message The message of the exception.
     */
    public CTRPlayerNotFoundException(String message) {
        super(
                LobbyMessageExceptionEnumeration.PLAYER_NOT_FOUND_EXCEPTION,
                message
        );
    }
}
