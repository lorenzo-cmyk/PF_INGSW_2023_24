package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class NewGameFailureMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public NewGameFailureMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
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
