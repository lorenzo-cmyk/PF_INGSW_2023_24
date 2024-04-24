package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class DestroyGameMessage implements CtoSMessage {
    private final String senderNickname;

    public DestroyGameMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
