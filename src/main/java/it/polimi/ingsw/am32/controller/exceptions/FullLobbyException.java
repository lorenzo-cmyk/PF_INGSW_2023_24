package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a player tries to join a full lobby.
 */
public class FullLobbyException extends LobbyMessageException {
    /**
     * Creates a new FullLobbyException with the given message.
     * @param message The message of the exception.
     */
    public FullLobbyException(String message) {
        super(
                LobbyMessageExceptionEnumeration.FULL_LOBBY_EXCEPTION,
                message
        );
    }
}
