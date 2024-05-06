package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.am32.network.NodeInterface;

/**
 * This class represents a message that is sent from the client to the server when a new game is created.
 * The message contains the nickname of the player that created the game and the number of players that the game will have.
 * The message is used to create a new game and add the player to it.
 */
public class NewGameMessage implements CtoSLobbyMessage {
    /**
     * The nickname of the player that created the game
     */
    private final String senderNickname;
    /**
     * The number of players that the game will have
     */
    private final int playerNum;

    /**
     * Creates a new NewGameMessage with the given sender nickname and player count
     *
     * @param senderNickname The nickname of the player that created the game
     * @param playerNum The number of players that the game will have
     */
    public NewGameMessage(String senderNickname, int playerNum) {
        this.senderNickname = senderNickname;
        this.playerNum = playerNum;
    }

    /**
     * Creates a new game with the given sender nickname and player count
     *
     * @param nodeInterface The server node associated with the given player
     */
    @Override
    public void elaborateMessage(NodeInterface nodeInterface) throws InvalidPlayerNumberException {
        GameController gameController = null;
        try {
            gameController = GamesManager.getInstance().createGame(senderNickname, playerNum, nodeInterface);
        } catch (InvalidPlayerNumberException e) {
            throw e;
        }
        nodeInterface.setGameController(gameController);
    }
}
