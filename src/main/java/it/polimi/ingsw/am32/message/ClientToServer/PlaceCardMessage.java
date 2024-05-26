package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to place a card.
 */
public class PlaceCardMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to place the card
     */
    private final String senderNickname;
    /**
     * The id of the card the player wants to place
     */
    private final int cardId;
    /**
     * The row where the player wants to place the card
     */
    private final int row;
    /**
     * The column where the player wants to place the card
     */
    private final int column;
    /**
     * True if the card is placed face up, false otherwise
     */
    private final boolean isUp;

    public PlaceCardMessage(String senderNickname, int cardId, int row, int column, boolean isUp) {
        this.senderNickname = senderNickname;
        this.cardId = cardId;
        this.row = row;
        this.column = column;
        this.isUp = isUp;
    }

    /**
     * This method is called when a player wants to place a card.
     * Places the card in the specified position.
     * @param gameController the game controller of the game the player is playing
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.placeCard(senderNickname, cardId, row, column, isUp);
    }

    @Override
    public String toString() {
        return "PlaceCardMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", cardId=" + cardId +
                ", row=" + row +
                ", column=" + column +
                ", isUp=" + isUp +
                '}';
    }
}
