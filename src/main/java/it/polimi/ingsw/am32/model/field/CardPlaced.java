package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;

public class CardPlaced {
    private NonObjectiveCard placedCard;
    private int x;
    private int y;
    private boolean isUp;

    public CardPlaced(NonObjectiveCard placedCard, int x, int y, boolean isUp) {
        this.placedCard = placedCard;
        this.x = x;
        this.y = y;
        this.isUp = isUp;
    }

    public NonObjectiveCard getCard() {
        return placedCard;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean getIsUp() {
        return isUp;
    }
}
