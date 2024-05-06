package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

public class NewGameMessage implements CtoSLobbyMessage {
    private final String senderNickname;
    private final int playerNum;

    public NewGameMessage(String senderNickname, int playerNum) {
        this.senderNickname = senderNickname;
        this.playerNum = playerNum;
    }

    /**
     * Creates a new game with the given sender nickname and player count
     *
     * @param nodeInterface The server node associated with the given player
     * @throws InvalidPlayerNumberException If the player count is not between 2 and 4
     */
    @Override
    public void elaborateMessage(NodeInterface nodeInterface) throws InvalidPlayerNumberException {
        try {
            GameController gameController = GamesManager.getInstance().createGame(senderNickname, playerNum, nodeInterface);
            nodeInterface.setGameController(gameController);
        } catch (InvalidPlayerNumberException e) { // The player count is not between 2 and 4
            throw e;
        }
    }
}
