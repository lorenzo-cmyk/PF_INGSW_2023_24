package it.polimi.ingsw.am32.controller.exceptions.abstraction;

/**
 * The LobbyMessageException class represents an exception that can be thrown during
 * the elaboration of a generic Lobby-Message.
 * It contains the type of the exception and a message associated with it.
 * @see it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration
 */
public abstract class LobbyMessageException extends Exception {
    /**
     * The type of LobbyMessageException.
     */
    private final LobbyMessageExceptionEnumeration exceptionType;

    /**
     * Constructor for the LobbyMessageException class.
     * @param exceptionType The type of LobbyMessageException.
     * @param message The message associated with the exception.
     */
    public LobbyMessageException(LobbyMessageExceptionEnumeration exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    /**
     * Gets the enumeration type of the exception.
     * @return The enumeration type of the exception.
     */
    public LobbyMessageExceptionEnumeration getExceptionType() {
        return exceptionType;
    }
}
