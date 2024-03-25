package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

/**
 * Used for cards whose points are calculated on the basis of how many corners the card covers.
 *
 * @author anto
 */
public class AnglesCovered implements PointStrategy {
    /**
     * Calculates the number of angles covered by the given card.
     *
     * @param field Field object which the card belongs to
     * @param x X coordinate of card whose points are being calculated
     * @param y Y coordinates of card whose points are being calculated
     * @return Number of angles covered by card
     */
    public int calculateOccurences(Field field, int x, int y) {
        int count = 0;
        for (CardPlaced i : field.getFieldCards()) { // For each card on the field
            if (Math.abs(i.getX() - x) == 1 && Math.abs(i.getY() - y) == 1) { // Found card whose corners have been covered by the given card
                count++;
            }
        }
        return count;
    }
}
