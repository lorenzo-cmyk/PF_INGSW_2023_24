package it.polimi.ingsw.am32.model.player;

/**
 * This enum represents the different colours a player can have in the game.
 * Each colour is associated with a unique integer value.
 * @author Matteo
 */
public enum Colour {
    /**
     * Represents the red color that a player can identify with and is associated with value 0.
     */
    RED(0),
    /**
     * Represents the green color that a player can identify with and is associated with value 1.
     */
    GREEN(1),
    /**
     * Represents the blue color that a player can identify with and is associated with value 2.
     */
    BLUE(2),
    /**
     * Represents the yellow color that a player can identify with and is associated with value 3.
     */
    YELLOW(3),
    /**
     * Represents the black color that a player can identify with and is associated with value 4.
     */
    BLACK(4);

    /**
     * The unique integer value associated with the color.
     */
    private final int value;

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