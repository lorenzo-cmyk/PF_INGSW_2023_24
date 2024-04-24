package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class StartGameMessage implements CtoSMessage {
    private final String senderMessage;

    public StartGameMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
