package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.VirtualView;

public class MatchStatusMessage implements StoCMessage {
    private final String recipientNickname;
    private final int matchStatus;

    public MatchStatusMessage(String recipientNickname, int matchStatus) {
        this.recipientNickname = recipientNickname;
        this.matchStatus = matchStatus;
    }

    @Override
    public void processMessage(View view) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
