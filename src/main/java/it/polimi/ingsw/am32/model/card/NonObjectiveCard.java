package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

public class NonObjectiveCard extends Card{
    private CornerType topLeft;
    private CornerType topRight;
    private CornerType bottomLeft;
    private CornerType bottomRight;
    private CornerType topLeftBack;
    private CornerType topRightBack;
    private CornerType bottomLeftBack;
    private CornerType bottomRightBack;
    private int[] permRes;
    private int permResCount;
    private int[] conditionCount;

    public NonObjectiveCard(int id, int value, boolean validBack, PointStrategy pointStrategy, CornerType topLeft,
                     CornerType topRight, CornerType bottomLeft, CornerType bottomRight, CornerType topLeftBack,
                     CornerType topRightBack, CornerType bottomLeftBack, CornerType bottomRightBack,
                     int[] permRes, int permResCount, int[] conditionCount) {
        super(id, value, validBack, pointStrategy);
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.topLeftBack = topLeftBack;
        this.topRightBack = topRightBack;
        this.bottomLeftBack = bottomLeftBack;
        this.bottomRightBack = bottomRightBack;
        this.permRes = permRes;
        this.permResCount = permResCount;
        this.conditionCount = conditionCount;
        // FIXME Need to properly manage permRes, permResCount, and conditionCount initialization
    }
}
