package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

/**
 * Used for the point calculation of the only objective card which counts the number
 * of triplets of special type resources
 */
public class AllSpecial implements PointStrategy {
    /**
     * Calculates the number of full triplets of special type resources on the field.
     * If there are 5 quills, 6 inkwells, and 8 manuscripts for example, the number of occurrences should be 1
     *
     * @param field Field object which the card belongs to
     * @param x Parameter not used
     * @param y Parameter not used
     * @return Number of full triplets of special type resources
     */
    public int calculateOccurences(Field field, int x, int y) {
        int[] activeRes = field.getActiveRes();

        int quill_num = ObjectType.QUILL.getValue();
        int inkwell_num = ObjectType.INKWELL.getValue();
        int manuscript_num = ObjectType.MANUSCRIPT.getValue();

        int quill_full_triplets = quill_num/3;
        int inkwell_full_triplets = inkwell_num/3;
        int manuscript_full_triplets = manuscript_num/3;

        return Math.min(Math.min(quill_full_triplets, inkwell_full_triplets), manuscript_full_triplets);
    }
}
