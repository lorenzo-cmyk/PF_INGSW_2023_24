package it.polimi.ingsw.am32.model.card;

/**
 * Stores all the possible card corner object types. Each field is associated with an integer.
 * Used to indicate the type of resource present on the corners of Gold, Resource, and Start cards.
 *
 * @author Antony
 */
public enum CornerType {
    /**
     * Plant corner type indicates that the corner of the card has a plant resource.
     */
    PLANT(0),
    /**
     * Fungi corner type indicates that the corner of the card has a fungi resource.
     */
    FUNGI(1),
    /**
     * Animal corner type indicates that the corner of the card has an animal resource.
     */
    ANIMAL(2),
    /**
     * Insect corner type indicates that the corner of the card has an insect resource.
     */
    INSECT(3),
    /**
     * Quill corner type indicates that the corner of the card has a quill object.
     */
    QUILL(4),
    /**
     * Inkwell corner type indicates that the corner of the card has an inkwell object.
     */
    INKWELL(5),
    /**
     * Manuscript corner type indicates that the corner of the card has a manuscript object.
     */
    MANUSCRIPT(6),
    /**
     * Empty corner type indicates that the corner of the card is empty.
     */
    EMPTY(7),
    /**
     * Non-coverable corner type indicates that the corner of the card is not coverable.
     */
    NON_COVERABLE(8);

    /**
     * The integer value associated with the corner type.
     */
    private final int value;

    /**
     * Constructor for the CornerType enum.
     *
     * @param value The integer value associated with the corner type.
     */
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
