package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class DrawCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final int[] playerHand;

    public DrawCardConfirmationMessage(String recipientNickname, int[] playerHand) {
        this.recipientNickname = recipientNickname;
        this.playerHand = playerHand;
    }

    @Override
    public void processMessage(View view) {
        view.updateAfterDrawCard(playerHand);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
