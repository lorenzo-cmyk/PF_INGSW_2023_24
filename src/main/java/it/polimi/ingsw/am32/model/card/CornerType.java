package it.polimi.ingsw.am32.model.card;

/**
 * Stores all the possible card corner object types. Each field is associated with an integer.
 * Used to indicate the type of resource present on the corners of Gold, Resource, and Start cards.
 *
 * @author Antony
 */
public enum CornerType {
    PLANT(0),
    FUNGI(1),
    ANIMAL(2),
    INSECT(3),
    QUILL(4),
    INKWELL(5),
    MANUSCRIPT(6),
    EMPTY(7),
    NON_COVERABLE(8);

    private final int value;

    CornerType(int value) {
        this.value = value;
    }

    /**
     * When an enumeration object is referenced, this method returns its value.
     * For example, when ObjectType.PLANT.getValue() is called, 0 is returned.
     *
     * @return Value of referenced enumeration object
     */
    public int getValue() {
        return value;
    }
}
