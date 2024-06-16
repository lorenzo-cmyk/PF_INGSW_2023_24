package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

public class ReconnectGameMessage implements CtoSLobbyMessage {
    private final String senderNickname;
    private final int matchId;

    public ReconnectGameMessage(String senderNickname, int matchId) {
        this.senderNickname = senderNickname;
        this.matchId = matchId;
    }

    @Override
    public GameController elaborateMessage(NodeInterface nodeInterface) throws GameAlreadyEndedException,
            GameNotFoundException, PlayerAlreadyConnectedException, GameNotYetStartedException, CTRPlayerNotFoundException {
        return GamesManager.getInstance().reconnectToGame(senderNickname, matchId, nodeInterface);
    }

    @Override
    public String toString() {
        return "ReconnectGameMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", matchId=" + matchId +
                '}';
    }
}
