package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class PlayerConnectedMessage implements StoCMessage {
    private final String recipientNickname;
    private final String connectedNickname;

    public PlayerConnectedMessage(String recipientNickname, String connectedNickname) {
        this.recipientNickname = recipientNickname;
        this.connectedNickname = connectedNickname;
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

