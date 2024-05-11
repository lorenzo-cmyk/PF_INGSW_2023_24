package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class PingMessage implements CtoSMessage {
    private final String senderNickname;

    public PingMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.pongPlayer(senderNickname);
    }
}
