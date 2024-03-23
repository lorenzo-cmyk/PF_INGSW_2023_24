package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of cards implemented as a stack.
 */
public class NonObjectiveCardDeck {
    /**
    * Card at top of deck found at last index of ArrayList.
    */
    private final ArrayList<NonObjectiveCard> cards;

    NonObjectiveCardDeck() {
        cards = new ArrayList<NonObjectiveCard>();
        // TODO Constructor will need to invoke DecksFromDisk class object to load the cards
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return Card object at the top of the deck if card is available, else null
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
     *
     * @return true if deck is not empty, false if deck is empty
     */
    public boolean shuffle() {
        if (cards.isEmpty()) return false;
        else {
            Collections.shuffle(cards);
            return true;
        }
    }

    /**
     * Adds a card to the top of the deck.
     *
     * @param card Card to add
     */
     public void addCard(NonObjectiveCard card) {
        cards.add(card);
    }
}