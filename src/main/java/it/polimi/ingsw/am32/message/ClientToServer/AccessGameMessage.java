package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;

public class AccessGameMessage implements CtoSLobbyMessage {
    private final int matchId;
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(GamesManager gamesManager) {
        // TODO
    }
}
