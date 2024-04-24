package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;
import it.polimi.ingsw.am32.message.ClientToServer.StartGameMessage;

public class StartGameConfirmationMessage implements StoCMessage {
    private final String recipientNickname;

    public StartGameConfirmationMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
