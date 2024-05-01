package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.AccessGameFailedMessage;
import it.polimi.ingsw.am32.network.NodeInterface;

public class AccessGameMessage implements CtoSLobbyMessage {
    private final int matchId;
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    @Override
    public void elaborateMessage(NodeInterface nodeInterface) {
        try {
            GameController gameController = GamesManager.getInstance().accessGame(senderNickname, matchId, nodeInterface);
            nodeInterface.setGameController(gameController);
            // Game was successfully joined
        } catch (GameNotFoundException e) { // Game with given id could not be found; must notify the player trying to join
            nodeInterface.uploadToClient(new AccessGameFailedMessage(senderNickname, "Game with id " + matchId + " not found"));
        }
    }
}
