package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class DrawCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final int[] playerHand;

    public DrawCardConfirmationMessage(String recipientNickname, int[] playerHand) {
        this.recipientNickname = recipientNickname;
        this.playerHand = playerHand;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
