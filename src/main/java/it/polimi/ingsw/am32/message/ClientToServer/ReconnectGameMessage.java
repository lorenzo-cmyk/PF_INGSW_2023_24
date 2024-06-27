package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.network.ServerNode.ServerNodeInterface;
/**
 * This class is used to manage the message sent by the client when he wants to reconnect to a game.
 */
public class ReconnectGameMessage implements CtoSLobbyMessage {
    /**
     * The nickname of the player who wants to reconnect to the game
     */
    private final String senderNickname;
    /**
     * The id of the game the player left before and wants to reconnect to
     */
    private final int matchId;

    /**
     * Constructor: a message containing the nickname of the player who wants to reconnect to the game and the id of
     * the game the player requests to reconnect to.
     * @param senderNickname the nickname of the player who wants to reconnect to the game
     * @param matchId the id of the game the player wants to reconnect to
     */
    public ReconnectGameMessage(String senderNickname, int matchId) {
        this.senderNickname = senderNickname;
        this.matchId = matchId;
    }
    /**
     * This method is called when a player wants to reconnect to a game.
     * Creates a new GameController and adds the player and updates the data of the player and the game.
     * @param  nodeInterface the serverNode of the player
     * @return the game controller of the game the player wants to join
     * @throws GameAlreadyEndedException if the game has already ended
     * @throws GameNotFoundException if the game was not found with given id
     * @throws PlayerAlreadyConnectedException if the player is already connected to the game
     * @throws GameNotYetStartedException if the game has not yet started, in lobby phase
     * @throws CTRPlayerNotFoundException if the player was not found with given nickname
     *
     */
    @Override
    public GameController elaborateMessage(ServerNodeInterface nodeInterface) throws GameAlreadyEndedException,
            GameNotFoundException, PlayerAlreadyConnectedException, GameNotYetStartedException, CTRPlayerNotFoundException {
        return GamesManager.getInstance().reconnectToGame(senderNickname, matchId, nodeInterface);
    }

   /**
    * This method overrides the default toString method.
    * It provides a string representation of a message object, which can be useful for debugging purposes.
    *
    * @return A string representation of the ReconnectGameMessage object.
    * The string includes the message type, the matchId and the senderNickname properties of the object.
    */
    @Override
    public String toString() {
        return "ReconnectGameMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", matchId=" + matchId +
                '}';
    }
}
