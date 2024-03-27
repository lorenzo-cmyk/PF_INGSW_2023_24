package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckBuilderTests {

    private CardDeckBuilder cardDeckBuilder;

    @BeforeEach
    void setUp() {
        cardDeckBuilder = new CardDeckBuilder();
    }

    @Test
    void buildCardDeckReturnsNullForNonObjectiveDeckType() {
        assertNull(cardDeckBuilder.buildCardDeck(DeckType.RESOURCE));
        assertNull(cardDeckBuilder.buildCardDeck(DeckType.GOLD));
        assertNull(cardDeckBuilder.buildCardDeck(DeckType.STARTING));
    }

    @Test
    void buildCardDeckReturnsCardDeckForObjectiveDeckType() {
        CardDeck cardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        assertNotNull(cardDeck);
        assertEquals(DeckType.OBJECTIVE, cardDeck.getDeckType());
    }

    @Test
    void buildCardDeckShouldShuffleDeckBeforeReturningIt() {
        CardDeck firstCardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        CardDeck secondCardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        assertNotEquals(firstCardDeck.draw().getId(), secondCardDeck.draw().getId());
    }

    @Test
    void buildCardDeckReturnsCardDeckWithCorrectCardsForObjectiveDeckType() {
        CardDeck cardDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        assertNotNull(cardDeck);
        // We iterate over each card in the deck. We expect the deck to contain 16 cards.
        // Each card should have a greater-than-zero ID, a non-negative Value, and a valid PointStrategy.
        for (int i = 0; i < 16; i++) {
            Card card = cardDeck.draw();
            assertNotNull(card);
            assertTrue(card.getId() > 0);
            assertTrue(card.getValue() >= 0);
            assertNotNull(card.getPointStrategy());
        }
        // The deck should be empty after drawing all the cards.
        assertNull(cardDeck.draw());
    }
}