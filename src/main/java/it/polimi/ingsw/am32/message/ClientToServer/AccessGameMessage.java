package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class AccessGameMessage implements CtoSMessage {
    private final int matchId;
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
