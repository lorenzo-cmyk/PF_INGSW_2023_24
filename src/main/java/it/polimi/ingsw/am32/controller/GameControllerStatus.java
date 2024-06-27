package it.polimi.ingsw.am32.controller;

/**
 * The GameControllerStatus enum represents the different states a game controller can be in.
 * Each state is associated with a specific integer value.
 * @author Anto
 */
public enum GameControllerStatus {
    /**
     * The game controller is in the lobby state, waiting for players to join.
     */
    LOBBY(0),
    /**
     * The game controller is waiting for all cards to make a selection for the starter card side.
     */
    WAITING_STARTER_CARD_CHOICE(1),
    /**
     * The game controller is waiting for all players to make a selection for the secret objective card.
     */
    WAITING_SECRET_OBJECTIVE_CARD_CHOICE(2),
    /**
     * The game controller is waiting for the current player to place a card on the board.
     */
    WAITING_CARD_PLACEMENT(3),
    /**
     * The game controller is waiting for the current player to draw a card from the deck.
     */
    WAITING_CARD_DRAW(4),
    /**
     * The game controller is in the ended state, meaning that the game has ended.
     */
    GAME_ENDED(5);

    /**
     * The integer value associated with the state.
     */
    private final int value;

    /**
     * Constructor for the GameControllerStatus enum.
     * @param value The integer value associated with the state.
     */
    GameControllerStatus(int value) {
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
