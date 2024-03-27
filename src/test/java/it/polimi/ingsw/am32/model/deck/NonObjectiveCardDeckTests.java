package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NonObjectiveCardDeckTests {
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

    @Test
    void drawReturnsCardWhenDeckIsNotEmpty() {
        assertEquals(nonObjectiveCard, nonObjectiveCardDeck.draw());
    }

    @Test
    void drawReturnsNullWhenDeckIsEmpty() {
        nonObjectiveCardDeck.draw(); // remove the only card in the deck
        assertNull(nonObjectiveCardDeck.draw());
    }

    @Test
    void shuffleReturnsTrueWhenDeckIsNotEmpty() {
        assertTrue(nonObjectiveCardDeck.shuffle());
    }

    @Test
    void shuffleReturnsFalseWhenDeckIsEmpty() {
        nonObjectiveCardDeck.draw(); // remove the only card in the deck
        assertFalse(nonObjectiveCardDeck.shuffle());
    }

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

    @Test
    void getDeckTypeReturnsCorrectDeckType() {
        assertEquals(DeckType.RESOURCE, nonObjectiveCardDeck.getDeckType());
    }
}
