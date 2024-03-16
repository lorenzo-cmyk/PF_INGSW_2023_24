package it.polimi.ingsw.am32.model;

import java.util.ArrayList;

public class Field {
    private ArrayList<CardPlaced> fieldCards;
    private int[] activeRes;

    Field() {
        this.activeRes = new int[7];
        this.fieldCards = new ArrayList<CardPlaced>();
    }

    public boolean placeCardInField(Card card, int x, int y, boolean side) {
        // TODO
    }
    public Card getCardFromPosition(int x, int y) {
        // TODO
    }
    public boolean availableSpace(int x, int y) {
        // TODO
    }
}
