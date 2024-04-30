package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.network.NodeInterface;

public class AccessGameMessage implements CtoSLobbyMessage {
    private final int matchId;
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    @Override
    public GameController elaborateMessage(NodeInterface nodeInterface) {
        return null;
        // TODO
    }
}
