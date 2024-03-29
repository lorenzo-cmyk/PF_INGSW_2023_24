package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

/**
 * The card class includes objective cards, resource cards, starting cards and gold cards. All types of cards have the
 * following attributes: each card has a unique ID and a value from zero to five points, and also each has a special
 * PointStrategy that describes the conditions that must be satisfied when using this card to score points.
 *
 * @author Jie
 */
public class Card {
    private final int id;
    private final int value;
    private final PointStrategy pointStrategy;

    /**
     * Constructor of Card
     *
     * @param id ID identify a card.
     * @param value Score value of a card.
     * @param pointStrategy The way or strategy to get points.
     * @author Jie
     */
    public Card(int id, int value, PointStrategy pointStrategy) {
        this.id = id;
        this.value = value;
        this.pointStrategy = pointStrategy;
    }

    /**
     * Getter of ID
     * @return id of a given card.
     * @author Jie
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of value
     * @return score value of a given card
     * @author Jie
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter of point strategy
     * @return the type of strategy used by a given card.
     * @author Jie
     */
    public PointStrategy getPointStrategy() {
        return pointStrategy;
    }
}
