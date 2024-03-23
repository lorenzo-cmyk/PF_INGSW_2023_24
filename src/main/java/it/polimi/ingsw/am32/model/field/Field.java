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

    /**
     * Returns the card at the given position if available.
     *
     * @param x X position of the card in the field to return
     * @param y Y position of the card in the field to return
     * @return Card at given coordinates if present in the field, else null.
     */
    public NonObjectiveCard getCardFromPosition(int x, int y) {
        for (CardPlaced i : fieldCards) {
            if (i.getX() == x && i.getY() == y) {
                return i.getCard();
            }
        }
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
