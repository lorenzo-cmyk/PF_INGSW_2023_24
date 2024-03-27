package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.Card;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of Cards implemented as a stack.
 * @author Lorenzo
 */
public class CardDeck {
    private final ArrayList<Card> cards;

    CardDeck() {
        this.cards = new ArrayList<>();
        // TODO Constructor will need to invoke DecksFromDisk class object to load the cards
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return Card object at the top of the deck if card is available, null otherwise.
     * @author Lorenzo
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
     * @return True if deck is not empty, false if deck is empty.
     * @author Lorenzo
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
     * @return True if card was successfully added, false otherwise.
     * @author Lorenzo
     */
     public boolean addCard(Card card) {
        cards.add(card);
        return true;
    }
}
