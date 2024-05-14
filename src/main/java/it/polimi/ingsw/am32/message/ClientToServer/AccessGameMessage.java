package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

/**
 * This class is used to manage the message sent by the client when he wants to join a game.
 */
public class AccessGameMessage implements CtoSLobbyMessage {
    /**
     * The id of the game the player wants to join
     */
    private final int matchId;
    /**
     * The nickname of the player who wants to join the game
     */
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    /**
     * This method is called when a player wants to join a game.
     * Creates a new GameController and adds the player to the game.
     * @param nodeInterface the serverNode of the player
     * @return the game controller of the game the player wants to join
     * @throws GameAlreadyStartedException if the game has already started
     * @throws FullLobbyException if the lobby is full
     * @throws DuplicateNicknameException if the nickname is already in use
     * @throws GameNotFoundException if the game was not found
     */
    @Override
    public GameController elaborateMessage(NodeInterface nodeInterface) throws GameAlreadyStartedException, FullLobbyException, DuplicateNicknameException, GameNotFoundException {
        return GamesManager.getInstance().accessGame(senderNickname, matchId, nodeInterface);
    }
}
