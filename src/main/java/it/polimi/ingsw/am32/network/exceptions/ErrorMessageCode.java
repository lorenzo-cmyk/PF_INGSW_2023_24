package it.polimi.ingsw.am32.network.exceptions;

/**
 * Enumerates the error message codes.
 * The error message codes are used to identify the type of error that occurred.
 * @see it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration
 * @see it.polimi.ingsw.am32.message.ServerToClient.ErrorMessage
 */
public enum ErrorMessageCode {
    STOCMESSAGE_SENT_BEFORE_STOCLOBBYMESSAGE(128),
    STOCLOBBYMESSAGE_SENT_BUT_GAMECONTROLLER_ALREADY_PRESENT(127),
    MESSAGE_TYPE_NOT_RECOGNIZED(126);

    /**
     * The code associated with the error type.
     */
    private final int code;

    /**
     * Constructor for the error type.
     * @param code The code associated with the error type.
     */
    ErrorMessageCode(int code) {
        this.code = code;
    }

    /**
     * Get the code associated with the error type.
     * @return The code associated with the error type.
     */
    public int getCode() {
        return code;
    }
}
