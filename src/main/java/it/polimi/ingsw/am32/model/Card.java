package it.polimi.ingsw.am32.model;

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
