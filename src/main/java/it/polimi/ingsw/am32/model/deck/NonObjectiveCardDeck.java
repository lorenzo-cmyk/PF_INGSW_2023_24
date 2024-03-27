package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a collection of NonObjectiveCards implemented as a stack.
 * @author Lorenzo
 */
public class NonObjectiveCardDeck {
    private final ArrayList<NonObjectiveCard> cards;

    NonObjectiveCardDeck() {
        this.cards = new ArrayList<>();
        // TODO Constructor will need to invoke DecksFromDisk class object to load the cards
    }

    /**
     * Draws (and removes) the top card from the deck.
     *
     * @return NonObjectiveCard object at the top of the deck if card is available, null otherwise.
     * @author Lorenzo
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
     * @param card NonObjectiveCard to add
     * @return True if card was successfully added, false otherwise.
     * @author Lorenzo
     */
    public boolean addCard(NonObjectiveCard card) {
        cards.add(card);
        return true;
    }
}
