package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for the point calculation of the only objective card which counts the number
 * of Ls made of 2 vertical green cards and 1 purple card to the bottom left
 */
public class LConfigurationTwo {
    /**
     * Calculates the number of left facing L configurations present in the field made of 2 greens and 1 purple cards.
     * The cards are extracted from the field parameter, then, by using streams, a filtered and ordered arraylist
     * containing only green (plant) cards and red (fungi) cards is generated. The ordering is done on the basis of the cards' x and y coordinates.
     * If 2 cards have the same x coordinate, the card with the highest y coordinate comes first.
     * A simple triple for loop is used to scan the entire arraylist for the wanted pattern.
     *
     * @param field Field object which the card belongs to
     * @param x Parameter not used
     * @param y Parameter not used
     * @return Number of found L configurations
     */
    int calculateOccurences(Field field, int x, int y) {
        ArrayList<CardPlaced> fieldCards = field.getFieldCards(); // Get all the cards with their positions

        List<CardPlaced> orderedCardsTemp = fieldCards.stream() // Converts ArrayList to stream
                .filter(c -> c.getCard().getKingdom() == ObjectType.FUNGI || c.getCard().getKingdom() == ObjectType.PLANT) // Filters all non-fungi or non-plants out
                .sorted((c1, c2) -> (c1.getX() < c2.getX() || (c1.getX() == c2.getX() && c1.getY() > c2.getY())) ? -1 : 1)
                .toList();
        ArrayList<CardPlaced> orderedCards = new ArrayList<CardPlaced>(orderedCardsTemp);

        int count = 0; // Number of occurrences of L configuration in the field

        for (int i=0; i<orderedCards.size(); i++) { // Scan all the cards
            if (orderedCards.get(i).getCard().getKingdom() == ObjectType.INSECT) { // If the colour of the card is purple
                for (int j = i + 1; j < orderedCards.size(); j++) {
                    if (orderedCards.get(j).getCard().getKingdom() == ObjectType.PLANT &&
                        orderedCards.get(j).getX() == orderedCards.get(i).getX() + 1 &&
                        orderedCards.get(j).getY() == orderedCards.get(i).getY() + 1) { // If the card is a green card to the top right of the purple card
                        for (int k = j + 1; k < orderedCards.size(); k++) {
                            if (orderedCards.get(j).getCard().getKingdom() == ObjectType.PLANT &&
                                orderedCards.get(j).getX() == orderedCards.get(i).getX() + 1 &&
                                orderedCards.get(j).getY() == orderedCards.get(i).getY() + 1) { // If the card is a green card to the top right of the purple card
                                // Found single occurrence of pattern
                                count++;
                           }
                       }
                   }
               }
           }
       }

       return count;
    }
}
