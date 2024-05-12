package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class SelectedSecretObjectiveCardMessage implements CtoSMessage {
    private final String senderNickname;
    private final int cardId;

    public SelectedSecretObjectiveCardMessage(String senderNickname, int cardId) {
        this.senderNickname = senderNickname;
        this.cardId = cardId;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.chooseSecretObjectiveCard(senderNickname, cardId);
    }
}
