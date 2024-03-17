package it.polimi.ingsw.am32.model.field;

public class CardPlaced {
    private Card placedCard;
    private int x;
    private int y;
    private boolean isUp;

    CardPlaced(Card placedCard, int x, int y, boolean isUp) {
        this.placedCard = placedCard;
        this.x = x;
        this.y = y;
        this.isUp = isUp;
    }

    public Card getCard() {
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
