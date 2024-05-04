package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class GameStartedMessage implements StoCMessage {
    private final String recipientNickname;

    public GameStartedMessage(String recipientNickname) {
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
