package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

/**
 * Creates objects used to represent Gold, Resource, and Start cards.
 *
 * @author Antony
 */
public class NonObjectiveCard extends Card {
    /**
     * Stores the type of resource displayed on the corner of the card.
     */
    private final CornerType topLeft, topRight, bottomLeft, bottomRight, topLeftBack, topRightBack, bottomLeftBack, bottomRightBack;
    /**
     * Permanent resources present on the back of the card. Index of each resource dictated by ObjectType enum.
     * Gold and Resource cards always have 1 permanent resource according to their kingdom, while start cards may have more.
     */
    private final int[] permRes;
    /**
     * Resources that need to be present on the field before the card can be played. Index of each resource dictated by ObjectType enum.
     */
    private final int[] conditionCount;
    /**
     * Kingdom (colour) the card belongs to.
     */
    private final ObjectType kingdom;

    /**
     * Constructor of NonObjectiveCard object
     *
     * @param id ID of card
     * @param value Value of card used for calculating points (number found on top of card)
     * @param pointStrategy Strategy for calculating card points
     * @param topLeft Top left corner object
     * @param topRight Top right corner object
     * @param bottomLeft Bottom left corner object
     * @param bottomRight Bottom right corner object
     * @param topLeftBack Top left corner object at the back
     * @param topRightBack Top right corner object at the back
     * @param bottomLeftBack Bottom left corner object at the back
     * @param bottomRightBack Bottom right corner object at the back
     * @param permRes Permanent resources at the back of the card
     * @param conditionCount Resources needed to place the card
     * @param kingdom Kingdom the card belongs to
     */
    public NonObjectiveCard(int id, int value, PointStrategy pointStrategy, CornerType topLeft,
                     CornerType topRight, CornerType bottomLeft, CornerType bottomRight, CornerType topLeftBack,
                     CornerType topRightBack, CornerType bottomLeftBack, CornerType bottomRightBack,
                     int[] permRes, int[] conditionCount, ObjectType kingdom) {
        super(id, value, pointStrategy);
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.topLeftBack = topLeftBack;
        this.topRightBack = topRightBack;
        this.bottomLeftBack = bottomLeftBack;
        this.bottomRightBack = bottomRightBack;
        this.permRes = permRes;
        this.conditionCount = conditionCount;
        this.kingdom = kingdom;
    }

    /**
     * Getter of topLeft
     *
     * @return Top left corner object
     */
    public CornerType getTopLeft() {
        return topLeft;
    }

    /**
     * Getter of topRight
     *
     * @return Top right corner object
     */
    public CornerType getTopRight() {
        return topRight;
    }

    /**
     * Getter of bottomLeft
     *
     * @return Bottom left corner object
     */
    public CornerType getBottomLeft() {
        return bottomLeft;
    }

    /**
     * Getter of bottomRight
     *
     * @return Bottom right corner object
     */
    public CornerType getBottomRight() {
        return bottomRight;
    }

    /**
     * Getter of topLeftBack
     *
     * @return Top left corner object at the back
     */
    public CornerType getTopLeftBack() {
        return topLeftBack;
    }

    /**
     * Getter of topRightBack
     *
     * @return Top right corner object at the back
     */
    public CornerType getTopRightBack() {
        return topRightBack;
    }

    /**
     * Getter of bottomLeftBack
     *
     * @return Bottom left corner object at the back
     */
    public CornerType getBottomLeftBack() {
        return bottomLeftBack;
    }

    /**
     * Getter of bottomRightBack
     *
     * @return Bottom right corner object at the back
     */
    public CornerType getBottomRightBack() {
        return bottomRightBack;
    }

    /**
     * Getter of permRes
     *
     * @return Permanent resources of the card
     */
    public int[] getPermRes() {
        return permRes;
    }

    /**
     * Getter of conditionCount
     *
     * @return Resources needed to place the card
     */
    public int[] getConditionCount() {
        return conditionCount;
    }

    /**
     * Getter of kingdom
     *
     * @return Kingdom of card
     */
    public ObjectType getKingdom() {
        return kingdom;
    }
}
