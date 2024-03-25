package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

/**
 * Following strategy pattern to describe different strategies used by cards to calculate points
 * and for each type count how many times it is realized in the indicated field.
 *
 * @author jie
 */
public interface PointStrategy {
    /**
     * Calculates how many times the point strategy of the given card has been satisfied based on its specific field
     * and position.
     *
     * @param field a field of play where the card placed.
     * @param x the x coordinate of the card whose points are being calculated.
     * @param y the y coordinates of the card whose points are being calculated.
     * @return the number of times the card's point strategy has been met in this field.
     */
    int calculateOccurences(Field field, int x, int y);
}
