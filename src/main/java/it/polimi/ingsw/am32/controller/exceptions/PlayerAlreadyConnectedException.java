package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a player is already connected but tries to connect again somehow.
 */
public class PlayerAlreadyConnectedException extends LobbyMessageException {
    /**
     * Creates a new PlayerAlreadyConnectedException with the given message.
     * @param message The message of the exception.
     */
    public PlayerAlreadyConnectedException(String message) {
        super(
                LobbyMessageExceptionEnumeration.PLAYER_ALREADY_CONNECTED_EXCEPTION,
                message
        );
    }
}
