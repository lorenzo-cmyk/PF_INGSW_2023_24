package it.polimi.ingsw.am32.model.deck.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTypeTest {
    @DisplayName("getDeckType should return the correct deck type for Resource deck")
    @Test
    void getJSONPathForResourceDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ResourceCards.json";
        assertEquals(expectedPath, DeckType.RESOURCE.getJSONPath());
    }

    @DisplayName("getJSONPath should return the correct path for Gold deck")
    @Test
    void getJSONPathForGoldDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/GoldCards.json";
        assertEquals(expectedPath, DeckType.GOLD.getJSONPath());
    }

    @DisplayName("getJSONPath should return the correct path for Starting deck")
    @Test
    void getJSONPathForStartingDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/StartingCards.json";
        assertEquals(expectedPath, DeckType.STARTING.getJSONPath());
    }

    @DisplayName("getJSONPath should return the correct path for Objective deck")
    @Test
    void getJSONPathForObjectiveDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json";
        assertEquals(expectedPath, DeckType.OBJECTIVE.getJSONPath());
    }
}
