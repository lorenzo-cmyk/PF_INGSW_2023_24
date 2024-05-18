package it.polimi.ingsw.am32.client;

public enum Event {
    LOBBY(0),
    PREPARATION(1),
    PLAYING(2),
    TERMINATING(3),
    LAST_TURN(4),
    TERMINATED(5),
    CHOOSE_CONNECTION (6),
    SELECT_GAME_MODE(7),
    CREATE_GAME(8),
    JOIN_GAME(9),
    RECONNECT_GAME(10),
    GAME_CREATED (11),
    GAME_JOINED (12),
    GAME_RECONNECTED (13),
    NEW_PLAYER_JOIN (14),
    PLAYER_DISCONNECTED (15),
    WAITING_FOR_START (16),
    GAME_START (17),
    SELECT_STARTER_CARD_SIDE(18),
    SELECT_STARTER_CARD_SIDE_FAILURE (19),
    SELECT_SECRET_OBJ_CARD (20),
    SELECT_SECRET_OBJ_CARD_FAILURE (21),
    PLACE_CARD(22),
    PLACE_CARD_FAILURE (23),
    DRAW_CARD(24),
    PLAYER_RECONNECTED(25);
    private final int value;

    Event(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static Event getEvent(int value) {
        for (Event event : Event.values()) {
            if (event.getValue() == value) {
                return event;
            }
        }
        return null;
    }
}
