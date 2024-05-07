package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class InvalidInboundChatMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public InvalidInboundChatMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
