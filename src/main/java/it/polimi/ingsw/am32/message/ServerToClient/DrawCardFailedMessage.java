package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class DrawCardFailedMessage implements StoCMessage {
    private final String recipientNickname;
    private String reason;

    public DrawCardFailedMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
