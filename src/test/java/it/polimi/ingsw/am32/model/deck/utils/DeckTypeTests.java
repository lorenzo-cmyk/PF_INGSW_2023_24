package it.polimi.ingsw.am32.model.deck.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckTypeTests {

    @Test
    void getJSONPathForResourceDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ResourceCards.json";
        assertEquals(expectedPath, DeckType.RESOURCE.getJSONPath());
    }

    @Test
    void getJSONPathForGoldDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/GoldCards.json";
        assertEquals(expectedPath, DeckType.GOLD.getJSONPath());
    }

    @Test
    void getJSONPathForStartingDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/StartingCards.json";
        assertEquals(expectedPath, DeckType.STARTING.getJSONPath());
    }

    @Test
    void getJSONPathForObjectiveDeck() {
        String expectedPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json";
        assertEquals(expectedPath, DeckType.OBJECTIVE.getJSONPath());
    }
}
