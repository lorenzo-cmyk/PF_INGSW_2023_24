package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

public class Card {
    private int id;
    private int value;
    private PointStrategy pointStrategy;

    public Card(int id, int value, PointStrategy pointStrategy) {
        this.id = id;
        this.value = value;
        this.pointStrategy = pointStrategy;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public PointStrategy getPointStrategy() {
        return pointStrategy;
    }
}
