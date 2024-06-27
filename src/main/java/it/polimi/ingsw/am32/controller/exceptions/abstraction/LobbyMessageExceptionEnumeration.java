package it.polimi.ingsw.am32.controller.exceptions.abstraction;

/**
 * The LobbyMessageExceptionEnumeration enum represents the different exceptions that can be thrown during
 * the elaboration of a generic Lobby-Message.
 * Each exception is associated with a specific integer value.
 * @value The integer value associated with each exception.
 */
public enum LobbyMessageExceptionEnumeration {
    /**
     * This exception type indicates that the number of players in a game is invalid.
     */
    INVALID_PLAYER_NUMBER_EXCEPTION(0),
    /**
     * This exception type indicates that a game is already started and a player tries to join it.
     */
    GAME_ALREADY_STARTED_EXCEPTION(1),
    /**
     * This exception type indicates that a player tries to join a full lobby.
     */
    FULL_LOBBY_EXCEPTION(2),
    /**
     * This exception type indicates that a player tries to join a lobby with a nickname that is already in use.
     */
    DUPLICATE_NICKNAME_EXCEPTION(3),
    /**
     * This exception type indicates that the game was not found.
     */
    GAME_NOT_FOUND_EXCEPTION(4),
    /**
     * This exception type indicates that a game is already ended and a player tries to join it.
     */
    GAME_ALREADY_ENDED_EXCEPTION(5),
    /**
     * This exception type indicates that a player tries to join a lobby with a nickname that is already in use.
     */
    PLAYER_NOT_FOUND_EXCEPTION(6),
    /**
     * This exception type indicates that a player is already connected but tries to connect again somehow.
     */
    PLAYER_ALREADY_CONNECTED_EXCEPTION(7),
    /**
     * This exception type indicates that a player tries to connect to a game that has not yet started.
     */
    GAME_NOT_YET_STARTED_EXCEPTION(8);

    /**
     * The integer value associated with each state.
     */
    private final int value;

    /**
     * Constructor for the MatchStatus enum.
     * @param value The integer value associated with the state.
     */
    LobbyMessageExceptionEnumeration(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value associated with the state.
     * @return The integer value of the state.
     */
    public int getValue() {
        return value;
    }
}
