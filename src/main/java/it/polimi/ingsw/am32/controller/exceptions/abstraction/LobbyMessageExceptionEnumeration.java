package it.polimi.ingsw.am32.controller.exceptions.abstraction;

/**
 * The LobbyMessageExceptionEnumeration enum represents the different exceptions that can be thrown during
 * the elaboration of a generic Lobby-Message.
 * Each exception is associated with a specific integer value.
 * @value The integer value associated with each exception.
 */
public enum LobbyMessageExceptionEnumeration {
    INVALID_PLAYER_NUMBER_EXCEPTION(0),
    GAME_ALREADY_STARTED_EXCEPTION(1),
    FULL_LOBBY_EXCEPTION(2),
    DUPLICATE_NICKNAME_EXCEPTION(3),
    GAME_NOT_FOUND_EXCEPTION(4),
    GAME_ALREADY_ENDED_EXCEPTION(5),
    PLAYER_NOT_FOUND_EXCEPTION(6),
    PLAYER_ALREADY_CONNECTED_EXCEPTION(7),
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
