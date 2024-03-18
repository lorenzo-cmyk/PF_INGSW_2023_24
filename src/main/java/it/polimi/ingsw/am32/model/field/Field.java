package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.Card;

import java.util.ArrayList;

public class Field {
    private final ArrayList<CardPlaced> fieldCards;
    private final int[] activeRes;

    public Field() {
        this.activeRes = new int[7];
        this.fieldCards = new ArrayList<CardPlaced>();
    }

    public boolean placeCardInField(Card card, int x, int y, boolean side) {
        // TODO
        return false;
    }
    public Card getCardFromPosition(int x, int y) {
        // TODO
        return null;
    }
    public boolean availableSpace(int x, int y) {
        // TODO
        return false;
    }
}
