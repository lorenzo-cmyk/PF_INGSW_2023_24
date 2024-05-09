package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

public class AccessGameMessage implements CtoSLobbyMessage {
    private final int matchId;
    private final String senderNickname;

    public AccessGameMessage(int matchId, String senderNickname) {
        this.matchId = matchId;
        this.senderNickname = senderNickname;
    }

    @Override
    public GameController elaborateMessage(NodeInterface nodeInterface) throws GameAlreadyStartedException, FullLobbyException, DuplicateNicknameException, GameNotFoundException {
        try {
            GameController gameController = GamesManager.getInstance().accessGame(senderNickname, matchId, nodeInterface);
            return gameController;
            // Game was successfully joined
        } catch (GameNotFoundException | FullLobbyException | GameAlreadyStartedException | DuplicateNicknameException e) { // The game was not found, the lobby is full, the game has already started, or the nickname is already in use
            throw e;
        }
    }
}
