package it.polimi.ingsw.am32.model.card.pointstrategy;

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

    public int getValue() {
        return value;
    }
}
