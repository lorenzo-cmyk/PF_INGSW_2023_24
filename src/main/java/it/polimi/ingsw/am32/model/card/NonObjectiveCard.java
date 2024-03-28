package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

/**
 * Used to represent gold cards, resource cards, and start card.
 *
 * @author anto
 */
public class NonObjectiveCard extends Card {
    private final CornerType topLeft;
    private final CornerType topRight;
    private final CornerType bottomLeft;
    private final CornerType bottomRight;
    private final CornerType topLeftBack;
    private final CornerType topRightBack;
    private final CornerType bottomLeftBack;
    private final CornerType bottomRightBack;
    private final int[] permRes;
    private final int[] conditionCount;
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

    public CornerType getTopLeft() {
        return topLeft;
    }

    public CornerType getTopRight() {
        return topRight;
    }

    public CornerType getBottomLeft() {
        return bottomLeft;
    }

    public CornerType getBottomRight() {
        return bottomRight;
    }

    public CornerType getTopLeftBack() {
        return topLeftBack;
    }

    public CornerType getTopRightBack() {
        return topRightBack;
    }

    public CornerType getBottomLeftBack() {
        return bottomLeftBack;
    }

    public CornerType getBottomRightBack() {
        return bottomRightBack;
    }

    public int[] getPermRes() {
        return permRes;
    }

    public int[] getConditionCount() {
        return conditionCount;
    }

    public ObjectType getKingdom() {
        return kingdom;
    }
}
