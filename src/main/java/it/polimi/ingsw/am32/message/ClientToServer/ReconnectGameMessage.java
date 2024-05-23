package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyEndedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotYetStartedException;
import it.polimi.ingsw.am32.controller.exceptions.PlayerAlreadyConnectedException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
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
            PlayerNotFoundException, GameNotFoundException, PlayerAlreadyConnectedException, GameNotYetStartedException
    {
        return GamesManager.getInstance().reconnectToGame(senderNickname, matchId, nodeInterface);
    }

    @Override
    public String toString() {
        return "ReconnectGameMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", matchId=" + matchId +
                '}' + "\n";
    }
}
