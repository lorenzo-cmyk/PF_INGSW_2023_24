package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class PongMessage implements StoCMessage {
    private final String recipientNickname;

    public PongMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PongMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
