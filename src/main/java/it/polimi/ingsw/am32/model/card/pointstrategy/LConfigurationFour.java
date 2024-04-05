package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Used for the point calculation of the only objective card which counts the number of right facing Ls made from 2
 * stacked vertical blue cards and 1 red card to the top right in the player's field.
 *
 * @author anto
 */
public class LConfigurationFour implements PointStrategy {
    /**
    * Calculates the number of right facing L configurations present in the field made of 2 blue and 1 red cards.
    * The cards are extracted from the field parameter, then, by using streams, a filtered and ordered arraylist
    * containing only blue (animal) and red (fungi) cards is generated. The ordering is done on the basis of the cards' x and y coordinates.
    * Cards are ordered by decreasing x coordinate (primary ordering), and then by decreasing y coordinates (secondary ordering).
    *
    * @param field Field object which the card belongs to
    * @param x Parameter not used
    * @param y Parameter not used
    * @return Number of found L configurations
    */
    public int calculateOccurences(Field field, int x, int y) {
        ArrayList<CardPlaced> fieldCards = field.getFieldCards(); // Get all the cards with their positions

        List<CardPlaced> orderedCardsTemp = fieldCards.stream() // Converts ArrayList to stream
                .filter(c -> c.getNonObjectiveCard().getKingdom() == ObjectType.FUNGI || c.getNonObjectiveCard().getKingdom() == ObjectType.ANIMAL) // Filters all non-fungi or non-plants out
                .sorted(new LConfigurationFourComparator())
                .toList();
        ArrayList<CardPlaced> orderedCards = new ArrayList<>(orderedCardsTemp);

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
            if (cards.get(i).getNonObjectiveCard().getKingdom() == ObjectType.FUNGI) { // Found red card
                for (int j=i+1; j<cards.size(); j++) { // Scan all cards that follow red card
                    if (cards.get(j).getNonObjectiveCard().getKingdom() == ObjectType.ANIMAL && // Blue card found
                        cards.get(j).getX() == cards.get(i).getX() - 1 &&
                        cards.get(j).getY() == cards.get(i).getY() - 1 && // At the bottom left of the red card
                        j != cards.size()-1 && // Card is not last card of array
                        cards.get(j+1).getNonObjectiveCard().getKingdom() == ObjectType.ANIMAL && // Second blue card found at the correct spot
                        cards.get(j+1).getX() == cards.get(i).getX() - 1 &&
                        cards.get(j+1).getY() == cards.get(i).getY() - 3) {
                        // Pattern occurrence found

                        ArrayList<CardPlaced> tempCards = new ArrayList<>(cards); // Create copy of arraylist
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

/**
 * Comparator used to sort CardPlaced objects according the position of the cards contained in them
 * Used in stream sorted() method
 */
class LConfigurationFourComparator implements Comparator<CardPlaced> {
    @Override
    public int compare(CardPlaced c1, CardPlaced c2) {
        if (c1.getX() == c2.getX() && c1.getY() == c2.getY()) return 0;
        else if (c1.getX() > c2.getX() || (c1.getX() == c2.getX() && c1.getY() > c2.getY())) return -1;
        else return 1;
    }
}
