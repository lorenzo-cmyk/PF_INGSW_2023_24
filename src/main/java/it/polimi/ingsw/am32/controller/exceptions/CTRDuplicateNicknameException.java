package it.polimi.ingsw.am32.controller.exceptions;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

/**
 * This exception is thrown when a player tries to join a lobby with a nickname that is already in use.
 */
public class CTRDuplicateNicknameException extends LobbyMessageException {
    /**
     * Creates a new CTRDuplicateNicknameException with the given message.
     * @param message The message of the exception.
     */
    public CTRDuplicateNicknameException(String message) {
        super(
                LobbyMessageExceptionEnumeration.DUPLICATE_NICKNAME_EXCEPTION,
                message
        );
    }
}
