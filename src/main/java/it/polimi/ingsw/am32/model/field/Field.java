package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;

import java.util.ArrayList;

public class Field {
    private final ArrayList<CardPlaced> fieldCards;
    private final int[] activeRes;

    public Field() {
        this.activeRes = new int[7];
        this.fieldCards = new ArrayList<CardPlaced>();
    }

    public boolean placeCardInField(NonObjectiveCard card, int x, int y, boolean side) {
        // TODO
        return false;
    }
    public NonObjectiveCard getCardFromPosition(int x, int y) {
        // TODO
        return null;
    }
    public boolean availableSpace(int x, int y) {
        // TODO
        return false;
    }

    public ArrayList<CardPlaced> getFieldCards() {
        return fieldCards;
    }

    public int[] getActiveRes() {
        return activeRes;
    }
}
