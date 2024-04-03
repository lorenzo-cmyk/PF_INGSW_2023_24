package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;
/**
 * Empty is one of the classes that implement the PointStrategy interface used to calculate the
 * card whose placement returns always 1: all cards with a constant value regardless of other conditions.
 * @author Jie
 */
public class Empty implements PointStrategy {
    /**
     * Calculate for all cards that have a constant value independent of other conditions.
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Always one.
     * @author Jie
     */
    public int calculateOccurences(Field field, int x, int y) {

        return 1; // for cards that have value constant
    }
}
