package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to know the status of the game.
 */
public class RequestGameStatusMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to know the status of the game
     */
    private final String senderNickname;

    public RequestGameStatusMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    /**
     * This method is called when a player wants to know the status of the game.
     * Sends the status of the game to the player.
     * @param gameController the game controller of the game
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.sendGameStatus(senderNickname);
    }
}
