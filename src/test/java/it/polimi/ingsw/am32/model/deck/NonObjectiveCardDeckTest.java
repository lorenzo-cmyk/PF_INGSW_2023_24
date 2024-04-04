package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NonObjectiveCardDeckTest {
    private NonObjectiveCardDeck nonObjectiveCardDeck;
    private NonObjectiveCard nonObjectiveCard;

    @BeforeEach
    void setUp() {
        ArrayList<NonObjectiveCard> cards = new ArrayList<>();
        int[] permRes = new int[]{0, 0, 0, 0, 0, 0, 0};
        int[] conditionCount = new int[]{0, 0, 0, 0, 0, 0, 0};
        nonObjectiveCard = new NonObjectiveCard(
                1,
                1,
                new Empty(),
                CornerType.PLANT,
                CornerType.ANIMAL,
                CornerType.FUNGI,
                CornerType.INSECT,
                CornerType.QUILL,
                CornerType.INKWELL,
                CornerType.MANUSCRIPT,
                CornerType.NON_COVERABLE,
                permRes,
                conditionCount,
                ObjectType.INSECT
        );
        cards.add(nonObjectiveCard);
        nonObjectiveCardDeck = new NonObjectiveCardDeck(cards, DeckType.RESOURCE);
    }

    @DisplayName("draw should return a card when the deck is not empty")
    @Test
    void drawReturnsCardWhenDeckIsNotEmpty() {
        assertEquals(nonObjectiveCard, nonObjectiveCardDeck.draw());
    }

    @DisplayName("draw should return null when the deck is empty")
    @Test
    void drawReturnsNullWhenDeckIsEmpty() {
        nonObjectiveCardDeck.draw(); // remove the only card in the deck
        assertNull(nonObjectiveCardDeck.draw());
    }

    @DisplayName("shuffle should return true when the deck is not empty")
    @Test
    void shuffleReturnsTrueWhenDeckIsNotEmpty() {
        assertTrue(nonObjectiveCardDeck.shuffle());
    }

    @DisplayName("shuffle should return false when the deck is empty")
    @Test
    void shuffleReturnsFalseWhenDeckIsEmpty() {
        nonObjectiveCardDeck.draw(); // remove the only card in the deck
        assertFalse(nonObjectiveCardDeck.shuffle());
    }

    @DisplayName("addCard should add a card to the deck")
    @Test
    void addCardAddsCardToDeck() {
        int[] permRes = new int[]{0, 0, 0, 0, 0, 0, 0};
        int[] conditionCount = new int[]{0, 0, 0, 0, 0, 0, 0};
        NonObjectiveCard newCard = new NonObjectiveCard(
                2,
                2,
                new Empty(),
                CornerType.PLANT,
                CornerType.ANIMAL,
                CornerType.FUNGI,
                CornerType.INSECT,
                CornerType.QUILL,
                CornerType.INKWELL,
                CornerType.MANUSCRIPT,
                CornerType.NON_COVERABLE,
                permRes,
                conditionCount,
                ObjectType.INSECT
        );
        assertTrue(nonObjectiveCardDeck.addCard(newCard));
        assertEquals(newCard, nonObjectiveCardDeck.draw());
    }

    @DisplayName("getDeckType should return the correct deck type")
    @Test
    void getDeckTypeReturnsCorrectDeckType() {
        assertEquals(DeckType.RESOURCE, nonObjectiveCardDeck.getDeckType());
    }
}
