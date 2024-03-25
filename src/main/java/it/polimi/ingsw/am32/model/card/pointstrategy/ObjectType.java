package it.polimi.ingsw.am32.model.card.pointstrategy;

/**
 * Stores all the possible Card object types. Includes all resources and objects. Each field is associated with
 * an integer.
 */
public enum ObjectType {
    PLANT(0),
    FUNGI(1),
    ANIMAL(2),
    INSECT(3),
    QUILL(4),
    INKWELL(5),
    MANUSCRIPT(6);

    private final int value;

    ObjectType(int value) {

        this.value = value;
    }

    /**
     * When an enumeration object is referenced, this method returns its value.
     * For example, when ObjectType.PLANT is used, a value of 0 will be associated to it.
     *
     * @return Value of referenced enumeration object
     */
    public int getValue() {

        return value;
    }
}
