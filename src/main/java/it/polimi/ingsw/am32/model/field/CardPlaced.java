package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;

/**
 * Used to store information about a currently placed card. Stores the position of the card in the player's field
 * along with its orientation.
 * The coordinates are those of the center of the card.
 */
public class CardPlaced {
    private final NonObjectiveCard placedCard;
    private final int x;
    private final int y;
    private final boolean isUp;

    /**
     * Record the position of the card placed and the face of the card selected by the player.
     *
     * @param placedCard the card that player decided to place.
     * @param x the x coordinate of the card placed.
     * @param y the y coordinate of the card placed.
     * @param isUp describe a face of the card that player want to use.
     */
    public CardPlaced(NonObjectiveCard placedCard, int x, int y, boolean isUp) {
        this.placedCard = placedCard;
        this.x = x;
        this.y = y;
        this.isUp = isUp;
    }

    /**
     * Returns the card.
     *
     * @return NonObjectiveCard object
     */
    public NonObjectiveCard getNonObjectiveCard() {
        return placedCard;
    }

    /**
     * Returns the x position of the card.
     *
     * @return X position of the card in the field.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y position of the card.
     *
     * @return Y position of the card in the field.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns card orientation.
     *
     * @return true if card is placed front side up, false otherwise
     */
    public boolean getIsUp() {
        return isUp;
    }
}
