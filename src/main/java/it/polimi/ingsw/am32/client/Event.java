package it.polimi.ingsw.am32.client;

/**
 * Enumeration of the possible events that can occur during the game.
 */
public enum Event {
    /**
     * Identifiers for the Lobby phase: the phase in which the players are waiting for the game to start.
     * Associated with the number 0.
     */
    LOBBY(0),
    /**
     * Identifiers for the preparation phase: the phase in which the players are selecting their starting card side and
     * secret objective card.
     * Associated with the number 1.
     */
    PREPARATION(1),
    /**
     * Identifiers for the playing phase: the phase in which the players place their cards on the field and draw new
     * cards if it is their turn.
     * Associated with the number 2.
     */
    PLAYING(2),
    /**
     * Identifiers for the terminating phase: when one of the players has gained 20 points or more.
     * Associated with the number 3.
     */
    TERMINATING(3),
    /**
     * Identifiers for the last turn phase: in which phase each player has one last turn to place a card on the field
     * without drawing a new card.
     * Associated with the number 4.
     */
    LAST_TURN(4),
    /**
     * Identifiers for the game terminated phase: when the game is over and the final scores are displayed.
     * Associated with the number 5.
     */
    TERMINATED(5),
    /**
     * Indicates that the player should choose a connection type.
     * Associated with the number 6.
     */
    CHOOSE_CONNECTION (6),
    /**
     * Indicates that the player should choose a game mode.
     * Associated with the number 7.
     */
    SELECT_GAME_MODE(7),
    /**
     * Indicates that the player selected to create a new game.
     * Associated with the number 8.
     */
    CREATE_GAME(8),
    /**
     * Indicates that the player selected to join an existing game.
     * Associated with the number 9.
     */
    JOIN_GAME(9),
    /**
     * Indicates that the player selected to reconnect to a game.
     * Associated with the number 10.
     */
    RECONNECT_GAME(10),
    /**
     * Indicates that the game has been created successfully.
     * Associated with the number 11.
     */
    GAME_CREATED (11),
    /**
     * Indicates that the player has joined the game successfully.
     * Associated with the number 12.
     */
    GAME_JOINED (12),
    /**
     * Indicates that the player has reconnected to the game successfully.
     * Associated with the number 13.
     */
    GAME_RECONNECTED (13),
    /**
     * Indicates that a new player has joined the game.
     * Associated with the number 14.
     */
    NEW_PLAYER_JOIN (14),
    /**
     * Indicates that a player has disconnected from the game.
     * Associated with the number 15.
     */
    PLAYER_DISCONNECTED (15),
    /**
     * Indicates that the game is waiting for the start of the game, waiting for all players to join.
     * Associated with the number 16.
     */
    WAITING_FOR_START (16),
    /**
     * Indicates that the game has started with all players joined.
     * Associated with the number 17.
     */
    GAME_START (17),
    /**
     * Indicates that the player should select the side of the starter card.
     * Associated with the number 18.
     */
    SELECT_STARTER_CARD_SIDE(18),
    /**
     * Indicates that the player has selected the side of the starter card not successfully.
     * Associated with the number 19.
     */
    SELECT_STARTER_CARD_SIDE_FAILURE (19),
    /**
     * Indicates that the player should select the secret objective card.
     * Associated with the number 20.
     */
    SELECT_SECRET_OBJ_CARD (20),
    /**
     * Indicates that the player has selected the secret objective card not successfully.
     * Associated with the number 21.
     */
    SELECT_SECRET_OBJ_CARD_FAILURE (21),
    /**
     * Indicates that the player should place a card on the field.
     * Associated with the number 22.
     */
    PLACE_CARD(22),
    /**
     * Indicates that the player has placed the card on the field not successfully.
     * Associated with the number 23.
     */
    PLACE_CARD_FAILURE (23),
    /**
     * Indicates that the player should draw a card.
     * Associated with the number 24.
     */
    DRAW_CARD(24),
    /**
     * Indicates that the player has reconnected to the game successfully.
     * Associated with the number 25.
     */
    PLAYER_RECONNECTED(25),
    /**
     * Indicates that the player has drawn a card not successfully.
     * Associated with the number 26.
     */
    DRAW_CARD_FAILURE(26),
    /**
     * Indicates phase in which the player is asked to select a game mode.
     * Associated with the number 27.
     */
    WELCOME(27),
    /**
     * Indicates that the player has completed the selection of the starter card side.
     * Associated with the number 28.
     */
    SELECTED_STARTER_CARD_SIDE(28),
    /**
     * Indicates that the player has completed the selection of the secret objective card.
     * Associated with the number 29.
     */
    SELECTED_SECRET_OBJ_CARD(29),
    /**
     * Indicates that the player has completed the selection of the card he wants to place on the field and the
     * coordinates where he wants to place it.
     * Associated with the number 30.
     */
    CARD_PLACED(30),
    /**
     * Indicates that the player has completed the selection of the card he wants to draw.
     * Associated with the number 31.
     */
    CARD_DRAWN(31),
    /**
     * Indicates that the player is waiting for his turn to place a card on the field or the player is waiting for the
     * start of playing phase.
     * Associated with the number 32.
     */
    WAITING_FOR_TURN(32),
    /**
     * Indicates there was an error in the chat message.
     * Associated with the number 33.
     */
    CHAT_ERROR(33),
    /**
     * Indicates that the game creation failed.
     * Associated with the number 34.
     */
    CREATE_GAME_FAILURE(34),
    /**
     * Indicates that the player failed to join the game.
     * Associated with the number 35.
     */
    JOIN_GAME_FAILURE(35),
    /**
     * Indicates that the player failed to reconnect to the game.
     * Associated with the number 36.
     */
    RECONNECT_GAME_FAILURE(36);
    /**
     * The integer value associated with the event.
     */
    private final int value;

    /**
     * Constructor for the Event enum.
     * @param value The integer value associated with the event.
     */
    Event(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value associated with the event.
     * @return The integer value of the event.
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the event associated with the given integer value.
     * @param value The integer value of the event.
     * @return The event associated with the given integer value.
     */
    public static Event getEvent(int value) {
        for (Event event : Event.values()) {
            if (event.getValue() == value) {
                return event;
            }
        }
        return null;
    }
}
