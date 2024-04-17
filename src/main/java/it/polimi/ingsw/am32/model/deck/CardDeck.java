package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of Cards implemented as a stack.
 *
 * @author Lorenzo
 */
public class CardDeck {
    /**
     * The collection of Cards.
     */
    private final ArrayList<Card> cards;
    /**
     * The type of the deck.
     */
    private final DeckType deckType;

    protected CardDeck(ArrayList<Card> cards, DeckType deckType) {
        this.cards = cards;
        this.deckType = deckType;
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return Card object at the top of the deck if card is available, null otherwise.
     */
    public Card draw() {
        if (cards.isEmpty()) return null;
        else {
            Card lastCard = cards.getLast();
            cards.removeLast();
            return lastCard;
        }
    }

    /**
     * Randomly rearranges the cards.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Adds a card to the top of the deck.
     *
     * @param card Card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Returns the type of the deck.
     *
     * @return The DeckType enum constant that represents the type of the deck.
     */
    public DeckType getDeckType() {
        return deckType;
    }

    /**
     * Returns the ArrayList containing the Cards. It is used for testing purposes.
     *
     * @return The ArrayList containing the Cards.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

}
