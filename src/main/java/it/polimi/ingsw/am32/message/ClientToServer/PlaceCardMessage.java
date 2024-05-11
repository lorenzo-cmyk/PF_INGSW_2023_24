package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class PlaceCardMessage implements CtoSMessage {
    private final String senderNickname;
    private final int cardId;
    private final int row;
    private final int column;
    private final boolean isUp;

    public PlaceCardMessage(String senderNickname, int cardId, int row, int column, boolean isUp) {
        this.senderNickname = senderNickname;
        this.cardId = cardId;
        this.row = row;
        this.column = column;
        this.isUp = isUp;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.placeCard(senderNickname, cardId, row, column, isUp);
    }
}
