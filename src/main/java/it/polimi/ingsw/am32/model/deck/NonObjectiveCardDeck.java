package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of NonObjectiveCards implemented as a stack.
 *
 * @author Lorenzo
 */
public class NonObjectiveCardDeck {
    /**
     * The collection of NonObjectiveCards.
     */
    private final ArrayList<NonObjectiveCard> cards;
    /**
     * The type of the deck.
     */
    private final DeckType deckType;

    protected NonObjectiveCardDeck(ArrayList<NonObjectiveCard> cards, DeckType deckType) {
        this.cards = cards;
        this.deckType = deckType;
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return NonObjectiveCard object at the top of the deck if card is available, null otherwise.
     */
    public NonObjectiveCard draw() {
        if (cards.isEmpty()) return null;
        else {
            NonObjectiveCard lastCard = cards.getLast();
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
     * @param card NonObjectiveCard to add
     */
    public void addCard(NonObjectiveCard card) {
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
     * Returns the ArrayList containing the NonObjectiveCards. It is used for testing purposes.
     *
     * @return The ArrayList containing the NonObjectiveCards.
     */
    public ArrayList<NonObjectiveCard> getCards() {
        return cards;
    }

}
