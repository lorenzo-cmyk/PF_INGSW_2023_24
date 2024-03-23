package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;
/**
 * Empty is one of the classed extended from the abstract class PointStrategy used to calculate for the
 * card, whose placement does not change the player's score points: all start cards and a part of the resources cards.
 */
public class Empty extends PointStrategy{
    /**
     * Calculates for all cards that have a value as zero.
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Always zero.
     */
    int calculateOccurences(Field field, int x, int y) {
        return 0; // Does not affect the score points.
    }
}
