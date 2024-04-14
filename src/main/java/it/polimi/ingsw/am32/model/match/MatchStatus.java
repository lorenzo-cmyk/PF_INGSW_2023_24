package it.polimi.ingsw.am32.model.match;

/**
 * The MatchStatus enum represents the different states a match can be in.
 * Each state is associated with a specific integer value.
 * @author Lorenzo
 */
public enum MatchStatus {
    // The match is in the lobby state, waiting for players to join.
    LOBBY(0),
    // The match is in the preparation state, players are setting up their game.
    PREPARATION(1),
    // The match is in the playing state, the game is in progress.
    PLAYING(2),
    // The match is in the terminating state, the game is about to end.
    TERMINATING(3),
    // The match is in the last turn state, the last turn of the game is in progress.
    LAST_TURN(4),
    // The match is in the terminated state, the game has ended.
    TERMINATED(5);

    // The integer value associated with each state.
    private final int value;

    /**
     * Constructor for the MatchStatus enum.
     * @param value The integer value associated with the state.
     */
    MatchStatus(int value) {
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
