package it.polimi.ingsw.am32.model.card.pointstrategy;

/**
 * Stores all the possible resource object types. Includes all resources and objects. Each field is associated with
 * an integer.
 * Used to indicate all the possible resources and special objects.
 * Can be considered a subset of the CornerType enumeration.
 *
 * @author Antony
 */
public enum ObjectType {
    /**
     * Plant object type indicates that the object is a plant resource.
     */
    PLANT(0),
    /**
     * Fungi object type indicates that the object is a fungi resource.
     */
    FUNGI(1),
    /**
     * Animal object type indicates that the object is an animal resource.
     */
    ANIMAL(2),
    /**
     * Insect object type indicates that the object is an insect resource.
     */
    INSECT(3),
    /**
     * Quill object type indicates that the object is a quill object.
     */
    QUILL(4),
    /**
     * Inkwell object type indicates that the object is an inkwell object.
     */
    INKWELL(5),
    /**
     * Manuscript object type indicates that the object is a manuscript object.
     */
    MANUSCRIPT(6);

    /**
     * The integer value associated with the object type.
     */
    private final int value;

    /**
     * Constructor for the ObjectType enum.
     * @param value The integer value associated with the object type.
     */
    ObjectType(int value) {
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
