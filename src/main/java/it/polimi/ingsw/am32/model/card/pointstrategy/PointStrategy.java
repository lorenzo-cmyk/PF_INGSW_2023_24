package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

public abstract class PointStrategy {
    public abstract int calculateOccurences(Field field, int x, int y);
}
