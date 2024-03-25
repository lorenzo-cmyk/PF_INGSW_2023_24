package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.Field;

public interface PointStrategy {
    int calculateOccurences(Field field, int x, int y);
}
