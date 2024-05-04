package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class PlayerDisconnectMessage implements StoCMessage {
    private final String recipientNickname;
    private final String disconnectedNickname;

    public PlayerDisconnectMessage(String recipientNickname, String disconnectedNickname) {
        this.recipientNickname = recipientNickname;
        this.disconnectedNickname = disconnectedNickname;
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
