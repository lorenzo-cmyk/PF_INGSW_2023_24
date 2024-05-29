package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;

/**
 * This class is used to manage the message sent by the client when he wants to see the field of another player.
 */
public class RequestPlayerFieldMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to see the field of another player
     */
    private final String senderNickname;
    /**
     * The nickname of the player whose field the player wants to see
     */
    private final String playerNickname;

    public RequestPlayerFieldMessage(String senderNickname, String playerNickname) {
        this.senderNickname = senderNickname;
        this.playerNickname = playerNickname;
    }

    /**
     * This method is called when a player wants to see the field of another player.
     * Sends the field of the player to the client.
     * @param gameController the game controller of the game
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.sendPlayerField(senderNickname, playerNickname);
    }

    @Override
    public String toString() {
        return "RequestPlayerFieldMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                '}';
    }
}
