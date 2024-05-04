package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class DrawCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final int cardId;

    public DrawCardConfirmationMessage(String recipientNickname, int cardId) {
        this.recipientNickname = recipientNickname;
        this.cardId = cardId;
    }

    @Override
    public void processMessage() {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
