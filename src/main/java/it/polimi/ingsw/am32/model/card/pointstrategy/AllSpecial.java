package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

/**
 * Used for the point calculation of the only objective card which counts the number
 * of triplets of special type resources
 *
 * @author anto
 */
public class AllSpecial implements PointStrategy {
    /**
     * Calculates the number of full triplets of special type resources on the field.
     * If there are 1 quills, 3 inkwells, and 2 manuscripts for example, the number of occurrences should be 1
     *
     * @param field Field object which the card belongs to
     * @param x Parameter not used
     * @param y Parameter not used
     * @return Number of full triplets of special type resources
     */
    public int calculateOccurences(Field field, int x, int y) {
        int quill_num = field.getActiveRes(ObjectType.QUILL);
        int inkwell_num = field.getActiveRes(ObjectType.INKWELL);
        int manuscript_num = field.getActiveRes(ObjectType.MANUSCRIPT);

        return Math.min(Math.min(quill_num, inkwell_num), manuscript_num);
    }
}
