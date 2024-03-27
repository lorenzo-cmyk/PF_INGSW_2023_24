package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTests {
    private CardDeck cardDeck;
    private Card card;

    @BeforeEach
    void setUp() {
        ArrayList<Card> cards = new ArrayList<>();
        card = new Card(0, 0, new Empty());
        cards.add(card);
        cardDeck = new CardDeck(cards, DeckType.OBJECTIVE);
    }

    @Test
    void drawReturnsCardWhenDeckIsNotEmpty() {
        assertEquals(card, cardDeck.draw());
    }

    @Test
    void drawReturnsNullWhenDeckIsEmpty() {
        cardDeck.draw(); // remove the only card in the deck
        assertNull(cardDeck.draw());
    }

    @Test
    void shuffleReturnsTrueWhenDeckIsNotEmpty() {
        assertTrue(cardDeck.shuffle());
    }

    @Test
    void shuffleReturnsFalseWhenDeckIsEmpty() {
        cardDeck.draw(); // remove the only card in the deck
        assertFalse(cardDeck.shuffle());
    }

    @Test
    void addCardAddsCardToDeck() {
        Card newCard = new Card(1, 1, new Empty());
        assertTrue(cardDeck.addCard(newCard));
        assertEquals(newCard, cardDeck.draw());
    }

    @Test
    void getDeckTypeReturnsCorrectDeckType() {
        assertEquals(DeckType.OBJECTIVE, cardDeck.getDeckType());
    }
}