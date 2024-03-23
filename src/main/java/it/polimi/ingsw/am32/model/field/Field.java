package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class Field {
    private final ArrayList<CardPlaced> fieldCards;
    private final int[] activeRes;

    private static final int resourcesSize = 7;

    public static final int PLANT = 0;
    public static final int FUNGI = 1;
    public static final int ANIMAL = 2;
    public static final int INSECT = 3;
    public static final int QUILL = 4;
    public static final int INKWELL = 5;
    public static final int MANUSCRIPT = 6;


    /**
     * Initialize the field, put resources counter to 0 and insert the initial card
     *
     * @param initialCard id the card that was assigned
     * @param isUp denote the side of the card chosen by the player
     */
    public Field(Card initialCard, boolean isUp) {
        this.activeRes = new int[resourcesSize];
        for(int i = 0; i < resourcesSize; i++)
            activeRes[i] = 0;
        this.fieldCards = new ArrayList<CardPlaced>();
        CardPlaced cardPlaced = new CardPlaced(initialCard, 0, 0, isUp);
        fieldCards.addFirst(cardPlaced);
    }

    /**
     *
     * @param card
     * @param x
     * @param y
     * @param side
     * @return
     */
    public boolean placeCardInField(NonObjectiveCard card, int x, int y, boolean side) {
        // TODO
        return false;
    }

    /**
     * Returns the card at the given position if available.
     *
     * @param x X position of the card in the field to return
     * @param y Y position of the card in the field to return
     * @return NonObjectiveCard at given coordinates if present in the field, else null.
     */
    public NonObjectiveCard getCardFromPosition(int x, int y) {
        for (CardPlaced i : fieldCards) {
            if (i.getX() == x && i.getY() == y) {
                return i.getCard();
            }
        }

        return null;
    }

    /**
     * Verify if a specified space in the field is not already occupied by a card
     *
     * @param x is the coordinate on the horizontal axis for the space searched
     * @param y is the coordinate on the vertical axis for the space searched
     * @return true if the space is available, false if not
     */
    public boolean availableSpace(int x, int y) {

        for (CardPlaced fieldCard : fieldCards)
            if (fieldCard.getX() == x && fieldCard.getY() == y)
                return false;
        return false;
    }

    /**
     *
     * @return
     */
    public ArrayList<CardPlaced> getFieldCards() {
        return fieldCards;
    }

    /**
     *
     * @param type
     * @return
     */
    public int getActiveRes(ObjectType type) {

        return activeRes[type.getValue()];
    }
}
