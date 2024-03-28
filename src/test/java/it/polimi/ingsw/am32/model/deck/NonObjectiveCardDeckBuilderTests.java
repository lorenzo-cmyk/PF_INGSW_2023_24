package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NonObjectiveCardDeckBuilderTests {
    private NonObjectiveCardDeckBuilder nonObjectiveCardDeckBuilder;

    @BeforeEach
    void setUp() {
        nonObjectiveCardDeckBuilder = new NonObjectiveCardDeckBuilder();
    }

    @DisplayName("buildNonObjectiveCardDeck should return null for the Objective deck")
    @Test
    void buildNonObjectiveCardDeckReturnsNullForObjectiveDeckType() {
        assertNull(nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.OBJECTIVE));
    }

    @DisplayName("buildNonObjectiveCardDeck should return a NonObjectiveCardDeck for the Resource deck")
    @Test
    void buildNonObjectiveCardDeckReturnsNonObjectiveCardDeckForResourceDeckType() {
        NonObjectiveCardDeck nonObjectiveCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        assertNotNull(nonObjectiveCardDeck);
        assertEquals(DeckType.RESOURCE, nonObjectiveCardDeck.getDeckType());
    }

    @DisplayName("buildNonObjectiveCardDeck should return a NonObjectiveCardDeck for the Gold deck")
    @Test
    void buildNonObjectiveCardDeckReturnsNonObjectiveCardDeckForGoldDeckType() {
        NonObjectiveCardDeck nonObjectiveCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);
        assertNotNull(nonObjectiveCardDeck);
        assertEquals(DeckType.GOLD, nonObjectiveCardDeck.getDeckType());
    }

    @DisplayName("buildNonObjectiveCardDeck should return a NonObjectiveCardDeck for the Starting deck")
    @Test
    void buildNonObjectiveCardDeckReturnsNonObjectiveCardDeckForStartingDeckType() {
        NonObjectiveCardDeck nonObjectiveCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        assertNotNull(nonObjectiveCardDeck);
        assertEquals(DeckType.STARTING, nonObjectiveCardDeck.getDeckType());
    }

    @DisplayName("buildNonObjectiveCardDeck should shuffle the deck before returning it")
    @Test
    void buildNonObjectiveCardDeckShouldShuffleDeckBeforeReturningIt() {
        // We will draw two cards from two different decks. The cards should not have the same ID.
        // The probability of the test failing is not null, but it is very low.
        // We will retry the test 3 times to reduce the probability of a false negative.
        for (int i = 0; true; i++) {
            try {
                NonObjectiveCardDeck firstNonObjectiveCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
                NonObjectiveCardDeck secondNonObjectiveCardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
                NonObjectiveCard firstCard = firstNonObjectiveCardDeck.draw();
                NonObjectiveCard secondCard = secondNonObjectiveCardDeck.draw();
                assertNotEquals(firstCard.getId(), secondCard.getId());
                break; // If the test passes, break the loop
            } catch (AssertionError e) {
                // If the test fails, catch the exception and retry
                if (i == 2) { // If this was the last attempt, rethrow the exception
                    throw e;
                }
            }
        }
    }

    @DisplayName("buildCardDeck should return a NonObjectiveCardDeck with the correct cards for the Resource deck")
    @Test
    void buildCardDeckReturnsCardDeckWithCorrectCardsForResourceDeckType() {
        NonObjectiveCardDeck cardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        assertNotNull(cardDeck);
        // We iterate over each card in the deck. We expect the deck to contain 40 cards.
        // Each card should have a greater-than-zero ID, a non-negative Value, and a valid PointStrategy.
        // Also, the card must have both the front and the back angles set.
        for (int i = 0; i < 40; i++) {
            NonObjectiveCard card = cardDeck.draw();
            // Check if the card is not null.
            assertNotNull(card);
            // Check if the card has a valid ID and Value.
            assertTrue(card.getId() > 0);
            assertTrue(card.getValue() >= 0);
            // Check if the card has a valid PointStrategy.
            assertNotNull(card.getPointStrategy());
            // Check if the card has both the front and the back angles set.
            assertNotNull(card.getTopLeft());
            assertNotNull(card.getTopRight());
            assertNotNull(card.getBottomLeft());
            assertNotNull(card.getBottomRight());
            assertNotNull(card.getTopLeftBack());
            assertNotNull(card.getTopRightBack());
            assertNotNull(card.getBottomLeftBack());
            assertNotNull(card.getBottomRightBack());
            // Both permRes and conditionCount must have 7 elements.
            assertEquals(7, card.getPermRes().length);
            assertEquals(7, card.getConditionCount().length);

            // Specific for Resource cards: permRes and conditionCount must be all-zero.
            assertEquals(0, Arrays.stream(card.getPermRes()).sum());
            assertEquals(0, Arrays.stream(card.getConditionCount()).sum());
            // Specific for Resource cards: the back of the card must have all the angles set to EMPTY.
            assertEquals(CornerType.EMPTY, card.getTopLeftBack());
            assertEquals(CornerType.EMPTY, card.getTopRightBack());
            assertEquals(CornerType.EMPTY, card.getBottomLeftBack());
            assertEquals(CornerType.EMPTY, card.getBottomRightBack());
            // Specific for Resource cards: the card must have a kingdom set.
            assertNotNull(card.getKingdom());
        }
        // The deck should be empty after drawing all the cards.
        assertNull(cardDeck.draw());
    }

    @DisplayName("buildCardDeck should return a NonObjectiveCardDeck with the correct cards for the Gold deck")
    @Test
    void buildCardDeckReturnsCardDeckWithCorrectCardsForGoldDeckType() {
        NonObjectiveCardDeck cardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);
        assertNotNull(cardDeck);
        // We iterate over each card in the deck. We expect the deck to contain 40 cards.
        // Each card should have a greater-than-zero ID, a non-negative Value, and a valid PointStrategy.
        // Also, the card must have both the front and the back angles set.
        for (int i = 0; i < 40; i++) {
            NonObjectiveCard card = cardDeck.draw();
            // Check if the card is not null.
            assertNotNull(card);
            // Check if the card has a valid ID and Value.
            assertTrue(card.getId() > 0);
            assertTrue(card.getValue() >= 0);
            // Check if the card has a valid PointStrategy.
            assertNotNull(card.getPointStrategy());
            // Check if the card has both the front and the back angles set.
            assertNotNull(card.getTopLeft());
            assertNotNull(card.getTopRight());
            assertNotNull(card.getBottomLeft());
            assertNotNull(card.getBottomRight());
            assertNotNull(card.getTopLeftBack());
            assertNotNull(card.getTopRightBack());
            assertNotNull(card.getBottomLeftBack());
            assertNotNull(card.getBottomRightBack());
            // Both permRes and conditionCount must have 7 elements.
            assertEquals(7, card.getPermRes().length);
            assertEquals(7, card.getConditionCount().length);

            // Specific for Gold cards: conditionCount must be not all-zero.
            assertNotEquals(0, Arrays.stream(card.getConditionCount()).sum());
            // Specific for Gold cards: permRes must be all-zero.
            assertEquals(0, Arrays.stream(card.getPermRes()).sum());
            // Specific for Gold cards: the back of the card must have all the angles set to EMPTY.
            assertEquals(CornerType.EMPTY, card.getTopLeftBack());
            assertEquals(CornerType.EMPTY, card.getTopRightBack());
            assertEquals(CornerType.EMPTY, card.getBottomLeftBack());
            assertEquals(CornerType.EMPTY, card.getBottomRightBack());
            // Specific for Gold cards: the card must have a kingdom set.
            assertNotNull(card.getKingdom());
        }
        // The deck should be empty after drawing all the cards.
        assertNull(cardDeck.draw());
    }

    @DisplayName("buildCardDeck should return a NonObjectiveCardDeck with the correct cards for the Starting deck")
    @Test
    void buildCardDeckReturnsCardDeckWithCorrectCardsForStartingDeckType() {
        NonObjectiveCardDeck cardDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        assertNotNull(cardDeck);
        // We iterate over each card in the deck. We expect the deck to contain 6 cards.
        // Each card should have a greater-than-zero ID, a non-negative Value, and a valid PointStrategy.
        // Also, the card must have both the front and the back angles set.
        for (int i = 0; i < 6; i++) {
            NonObjectiveCard card = cardDeck.draw();
            // Check if the card is not null.
            assertNotNull(card);
            // Check if the card has a valid ID and Value.
            assertTrue(card.getId() > 0);
            assertTrue(card.getValue() >= 0);
            // Check if the card has a valid PointStrategy.
            assertNotNull(card.getPointStrategy());
            // Check if the card has both the front and the back angles set.
            assertNotNull(card.getTopLeft());
            assertNotNull(card.getTopRight());
            assertNotNull(card.getBottomLeft());
            assertNotNull(card.getBottomRight());
            assertNotNull(card.getTopLeftBack());
            assertNotNull(card.getTopRightBack());
            assertNotNull(card.getBottomLeftBack());
            assertNotNull(card.getBottomRightBack());
            // Both permRes and conditionCount must have 7 elements.
            assertEquals(7, card.getPermRes().length);
            assertEquals(7, card.getConditionCount().length);

            // Specific for Starting cards: conditionCount must be all-zero.
            assertEquals(0, Arrays.stream(card.getConditionCount()).sum());
            // Specific for Starting cards: permRes must be not all-zero.
            assertNotEquals(0, Arrays.stream(card.getPermRes()).sum());
            // Specific for Starting cards: the back of the card must have all the angles occupied.
            assertNotEquals(CornerType.EMPTY, card.getTopLeftBack());
            assertNotEquals(CornerType.EMPTY, card.getTopRightBack());
            assertNotEquals(CornerType.EMPTY, card.getBottomLeftBack());
            assertNotEquals(CornerType.EMPTY, card.getBottomRightBack());
            // Specific for Starting cards: the card must not have a kingdom set.
            assertNull(card.getKingdom());
        }
        // The deck should be empty after drawing all the cards.
        assertNull(cardDeck.draw());
    }
}
