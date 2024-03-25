package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for the point calculation of the only objective card which counts the number of left facing Ls made from 2
 * stacked vertical green cards and 1 purple card to the bottom left in the player's field.
 *
 * @author anto
 */
public class LConfigurationTwo implements PointStrategy {
    /**
     * Calculates the number of left facing L configurations present in the field made of 2 green and 1 purple cards.
     * The cards are extracted from the field parameter, then, by using streams, a filtered and ordered arraylist
     * containing only green (plant) and purple (insect) cards is generated. The ordering is done on the basis of the cards' x and y coordinates.
     * Cards are ordered by increasing x coordinate (primary ordering), and then by increasing y coordinates (secondary ordering).
     *
     * @param field Field object which the card belongs to
     * @param x Parameter not used
     * @param y Parameter not used
     * @return Number of found L configurations
     */
    public int calculateOccurences(Field field, int x, int y) {
        ArrayList<CardPlaced> fieldCards = field.getFieldCards(); // Get all the cards with their positions

        List<CardPlaced> orderedCardsTemp = fieldCards.stream() // Converts ArrayList to stream
                .filter(c -> c.getCard().getKingdom() == ObjectType.INSECT || c.getCard().getKingdom() == ObjectType.PLANT) // Filters all non-fungi or non-plants out
                .sorted((c1, c2) -> (c1.getX() < c2.getX() || (c1.getX() == c2.getX() && c1.getY() < c2.getY())) ? -1 : 1)
                .toList();
        ArrayList<CardPlaced> orderedCards = new ArrayList<CardPlaced>(orderedCardsTemp); // Ordered and filtered cards

        return recursiveOccurrences(orderedCards);
    }

    /**
     * Recursive function. Finds the number of occurrences of pattern in given list of cards.
     *
     * @param cards An array containing the list of cards with their coordinates
     * @return Occurrences of pattern in given cards list
     */
    private int recursiveOccurrences(ArrayList<CardPlaced> cards) {
        for (int i=0; i<cards.size(); i++) { // Scan all cards
            if (cards.get(i).getCard().getKingdom() == ObjectType.INSECT) { // Found purple card
                for (int j=i+1; j<cards.size(); j++) { // Scan all cards that follow purple card
                    if (cards.get(j).getCard().getKingdom() == ObjectType.PLANT && // Green card found
                        cards.get(j).getX() == cards.get(i).getX() + 1 &&
                        cards.get(j).getY() == cards.get(i).getY() + 1 && // At the top right of the purple card
                        j != cards.size()-1 && // Card is not last card of array
                        cards.get(j+1).getCard().getKingdom() == ObjectType.PLANT && // Second green card found at the correct spot
                        cards.get(j+1).getX() == cards.get(i).getX() + 1 &&
                        cards.get(j+1).getY() == cards.get(i).getY() + 2) {
                        // Pattern occurrence found

                        ArrayList<CardPlaced> tempCards = new ArrayList<CardPlaced>(cards); // Create copy of arraylist
                        tempCards.remove(j+1);
                        tempCards.remove(j);
                        tempCards.remove(i); // Remove cards used in found pattern

                        // Call same function on remaining cards in field
                        return 1 + recursiveOccurrences(tempCards);
                    }
                }
            }
        }
        // No pattern occurrences found
        return 0;
    }
}
