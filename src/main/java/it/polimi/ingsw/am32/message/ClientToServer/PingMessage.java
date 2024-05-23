package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to ping the server.
 */
public class PingMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to ping the server
     */
    private final String senderNickname;

    public PingMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    // TODO Need javadoc
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.pongPlayer(senderNickname);
    }

    @Override
    public String toString() {
        return "PingMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                '}' + "\n";
    }
}
