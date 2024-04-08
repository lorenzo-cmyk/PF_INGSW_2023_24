package it.polimi.ingsw.am32.model.player;

/**
 * This enum represents the different colours a player can have in the game.
 * Each colour is associated with a unique integer value.
 */
public enum Colour {
    RED(0),    // Represents the colour red, associated with the value 0
    GREEN(1),  // Represents the colour green, associated with the value 1
    BLUE(2),   // Represents the colour blue, associated with the value 2
    YELLOW(3), // Represents the colour yellow, associated with the value 3
    BLACK(4);  // Represents the colour black, associated with the value 4

    private final int value;  // The unique integer value associated with the colour

    /**
     * Constructor for the Colour enum.
     * @param value The unique integer value to associate with the colour
     */
    Colour(int value) {
        this.value = value;
    }

    /**
     * Gets the unique integer value associated with the colour.
     * @return The integer value of the colour
     */
    public int getValue() {
        return value;
    }
}