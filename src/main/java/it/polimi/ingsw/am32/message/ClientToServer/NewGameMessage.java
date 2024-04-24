package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class NewGameMessage implements CtoSMessage {
    private final String senderNickname;

    public NewGameMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
