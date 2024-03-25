package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;
/**
 * Empty is one of the classes implemented interface PointStrategy used to calculate for the
 * card, whose placement does not change the player's score points: all start cards and a part of the resources cards.
 */
public class Empty implements PointStrategy {
    /**
     * Calculate for all cards that have a constant value independent of other conditions.
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Always one.
     */
    public int calculateOccurences(Field field, int x, int y) {

        return 1; // for cards that have value constant
    }
}
