package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class PongMessage implements StoCMessage {
    private final String recipientNickname;

    public PongMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
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
