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

    /**
     * Constructor: a message representing a ping message sent by a player to the server.
     * @param senderNickname the nickname of the player who wants to ping the server
     */
    public PingMessage(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    /**
     * This method is called when a player wants to ping the server.
     * @param gameController The game controller with which the message should be elaborated
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.pongPlayer(senderNickname);
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the PingMessage object.
     * The string includes the message type and the senderNickname properties of the object.
     */
    @Override
    public String toString() {
        return "PingMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                '}';
    }
}
