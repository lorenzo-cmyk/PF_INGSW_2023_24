package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of cards implemented as a stack.
 */
public class Deck {
    /**
    * Card at top of deck found at last index of ArrayList.
    */
    private final ArrayList<Card> cards;

    Deck() {
        cards = new ArrayList<Card>();
        // TODO Constructor will need to invoke DecksFromDisk class object to load the cards
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return Card object at the top of the deck if card is available, else null
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
}
