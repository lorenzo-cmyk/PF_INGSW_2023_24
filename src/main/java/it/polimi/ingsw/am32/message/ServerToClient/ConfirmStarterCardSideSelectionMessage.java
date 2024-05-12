package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ConfirmStarterCardSideSelectionMessage implements StoCMessage {
    private final String recipientNickname;
    private final int playerColour;

    public ConfirmStarterCardSideSelectionMessage(String recipientNickname, int playerColour) {
        this.recipientNickname = recipientNickname;
        this.playerColour = playerColour;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(View view) {

    }
}
