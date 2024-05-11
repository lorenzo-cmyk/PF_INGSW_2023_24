package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class DrawCardMessage implements CtoSMessage {
    private final String senderNickname;
    private final int deckType;
    private final int cardId;

    public DrawCardMessage(String senderNickname, int deckType, int cardId) {
        this.senderNickname = senderNickname;
        this.deckType = deckType;
        this.cardId = cardId;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.drawCard(senderNickname, deckType, cardId);
    }
}
