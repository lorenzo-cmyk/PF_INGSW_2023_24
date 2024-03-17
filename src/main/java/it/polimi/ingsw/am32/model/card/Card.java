package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;

public class Card {
    private int id;
    private int value;
    private boolean validBack;
    private PointStrategy pointStrategy;

    Card(int id, int value, boolean validBack, PointStrategy pointStrategy) {
        this.id = id;
        this.value = value;
        this.validBack = validBack;
        this.pointStrategy = pointStrategy;
    }
}
