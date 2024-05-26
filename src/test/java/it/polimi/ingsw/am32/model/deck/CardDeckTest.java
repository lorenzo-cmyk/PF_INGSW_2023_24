package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {
    private CardDeck cardDeck;
    private Card card;

    @BeforeEach
    void setUp() {
        ArrayList<Card> cards = new ArrayList<>();
        card = new Card(0, 0, new Empty());
        cards.add(card);
        cardDeck = new CardDeck(cards, DeckType.OBJECTIVE);
    }

    @DisplayName("draw should return a card when the deck is not empty")
    @Test
    void drawReturnsCardWhenDeckIsNotEmpty() {
        assertEquals(card, cardDeck.draw());
    }

    @DisplayName("draw should return null when the deck is empty")
    @Test
    void drawReturnsNullWhenDeckIsEmpty() {
        cardDeck.draw(); // remove the only card in the deck
        assertNull(cardDeck.draw());
    }

    @DisplayName("addCard should add a card to the deck")
    @Test
    void addCardAddsCardToDeck() {
        Card newCard = new Card(1, 1, new Empty());
        cardDeck.addCard(newCard);
        assertEquals(newCard, cardDeck.draw());
    }

    @DisplayName("getDeckType should return the correct deck type")
    @Test
    void getDeckTypeReturnsCorrectDeckType() {
        assertEquals(DeckType.OBJECTIVE, cardDeck.getDeckType());
    }

    @DisplayName("Getter getCards should return the ArrayList of cards")
    @Test
    void getCardsReturnsArrayListOfCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);
        assertEquals(cards, cardDeck.getCards());
    }
}
