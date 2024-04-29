package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class NewGameMessage implements CtoSMessage {
    private final String senderNickname;
    private final int playerNum;

    public NewGameMessage(String senderNickname, int playerNum) {
        this.senderNickname = senderNickname;
        this.playerNum = playerNum;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
