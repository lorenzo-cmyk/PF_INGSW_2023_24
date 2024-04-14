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
    /**
     * id: Identifier of card.
     */
    private final int id;
    /**
     * value: Value of card from zero to five points.
     */
    private final int value;
    /**
     * pointStrategy: Strategy that should be fulfilled to get the points of card.
     */
    private final PointStrategy pointStrategy;

    /**
     * Constructor of Card
     *
     * @param id ID identify a card.
     * @param value Score value of a card.
     * @param pointStrategy The way or strategy to get points.
     */
    public Card(int id, int value, PointStrategy pointStrategy) {
        this.id = id;
        this.value = value;
        this.pointStrategy = pointStrategy;
    }

    /**
     * Getter of ID
     * @return id of a given card.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of value
     * @return score value of a given card
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter of point strategy
     * @return the type of strategy used by a given card.
     */
    public PointStrategy getPointStrategy() {
        return pointStrategy;
    }
}
