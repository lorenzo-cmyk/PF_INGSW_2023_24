package it.polimi.ingsw.am32.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameControllerStatusTest {

    @DisplayName("Check GameControllerStatus enum values - LOBBY")
    @Test
    public void lobbyStatusHasCorrectValue() {
        assertEquals(0, GameControllerStatus.LOBBY.getValue());
    }

    @DisplayName("Check GameControllerStatus enum values - WAITING_STARTER_CARD_CHOICE")
    @Test
    public void waitingStarterCardChoiceStatusHasCorrectValue() {
        assertEquals(1, GameControllerStatus.WAITING_STARTER_CARD_CHOICE.getValue());
    }

    @DisplayName("Check GameControllerStatus enum values - WAITING_SECRET_OBJECTIVE_CARD_CHOICE")
    @Test
    public void waitingSecretObjectiveCardChoiceStatusHasCorrectValue() {
        assertEquals(2, GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE.getValue());
    }

    @DisplayName("Check GameControllerStatus enum values - WAITING_CARD_PLACEMENT")
    @Test
    public void waitingCardPlacementStatusHasCorrectValue() {
        assertEquals(3, GameControllerStatus.WAITING_CARD_PLACEMENT.getValue());
    }

    @DisplayName("Check GameControllerStatus enum values - WAITING_CARD_DRAW")
    @Test
    public void waitingCardDrawStatusHasCorrectValue() {
        assertEquals(4, GameControllerStatus.WAITING_CARD_DRAW.getValue());
    }

    @DisplayName("Check GameControllerStatus enum values - GAME_ENDED")
    @Test
    public void gameEndedStatusHasCorrectValue() {
        assertEquals(5, GameControllerStatus.GAME_ENDED.getValue());
    }
}
