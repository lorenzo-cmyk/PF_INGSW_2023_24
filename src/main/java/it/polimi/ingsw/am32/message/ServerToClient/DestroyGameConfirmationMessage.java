package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class DestroyGameConfirmationMessage implements StoCMessage {
    private final String recipientNickname;

    public DestroyGameConfirmationMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
