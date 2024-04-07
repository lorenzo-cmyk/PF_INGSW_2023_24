package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

/**
 * CountResource is one of the classes that implement the PointStrategy interface used to calculate the
 * objective cards and the gold cards, which count identical resources or objects visible in the play area (field)
 * of the player.
 * @author Jie
 */
public class CountResource implements PointStrategy {
    /**
     * type: the kingdom required by the objective card or the type of object required by the gold card.
     */
    private final ObjectType type;
    /**
     * count: the amount of resources or objects required by the card to get a certain value.
     */
    private final int count;

    /**
     * Calculate how many times the given objective card or gold card has been fulfilled based on its type of resources
     * (objects) and number of identical resources (objects).
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Number of times that objective card has been satisfied in this field.
     * @author Jie
     */
    public int calculateOccurences(Field field, int x, int y) {
        int times;
        // Get number of specific resources(objects) visible in the field,
        // then dived for the number of resources (objects) requested by objective card.
        times=field.getActiveRes(this.type)/this.count;
        return times;
    }

    /**
     * Constructor of the CountResource strategy.
     *
     * @param type  ObjectType to count.
     * @param count Minimum cardinality of a resource group to be reported.
     * @author Lorenzo
     */
    public CountResource(ObjectType type, int count) {
        this.type = type;
        this.count = count;
    }
}
