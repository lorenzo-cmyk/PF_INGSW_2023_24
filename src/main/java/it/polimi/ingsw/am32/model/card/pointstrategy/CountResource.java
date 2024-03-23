package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

/**
 * CountResource is one of the classed extended from the abstract class PointStrategy used to calculate for the
 * objective cards, which count identical resources or objects visible in the play area (field) of the player.
 */
public class CountResource extends PointStrategy{
    private ObjectType type;
    private int count;

    /**
     * Calculate how many times the given objective card has been fulfilled based on its type of resources (objects)
     * and number of identical resources (objects).
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Number of times that objective card has been satisfied in this field.
     */
    int calculateOccurences(Field field, int x, int y) {
        int times;
        // Get number of specific resources(objects) visible in the field,
        // then dived for the number of resources (objects) requested by objective card.
        times=field.getActiveRes()[this.type.getValue()]/this.count;
        return times;
    }
}
