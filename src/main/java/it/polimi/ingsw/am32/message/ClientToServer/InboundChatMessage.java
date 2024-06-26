package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to send a chat message.
 */
public class InboundChatMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to send the message
     */
    private final String senderNickname;
    /**
     * The nickname of the player who will receive the message; will be ignored if the multicastFlag is true
     */
    private final String recipientNickname;
    /**
     * The flag that indicates if the message is for all the players in the game
     */
    private final boolean multicastFlag;
    /**
     * The content of the message
     */
    private final String content;

    /**
     * Constructor: a message representing a chat message sent by a player to another player or to all the players in
     *              the game.
     *
     * @param senderNickname the nickname of the player who wants to send the message
     * @param recipientNickname the nickname of the player who will receive the message
     * @param multicastFlag the flag that indicates if the message is for all the players in the game or for a single
     *                      player: true if the message is for all the players, false if the message is for a single
     *                      player
     * @param content the content of the message
     */
    public InboundChatMessage(String senderNickname, String recipientNickname, boolean multicastFlag, String content) {
        this.senderNickname = senderNickname;
        this.recipientNickname = recipientNickname;
        this.multicastFlag = multicastFlag;
        this.content = content;
    }

    /**
     * This method is called when a player wants to send a chat message.
     * Creates a new ChatMessage and sends it to the GameController to be processed.
     * @param gameController the game controller of the game the player is playing
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.submitChatMessage(new ChatMessage(senderNickname, recipientNickname, multicastFlag, content));
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the InboundChatMessage object.
     * The string includes the message type, the senderNickname, the recipientNickname, the multicastFlag and the
     * content properties of the object.
     */
    @Override
    public String toString() {
        return "InboundChatMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", recipientNickname='" + recipientNickname + '\'' +
                ", multicastFlag=" + multicastFlag +
                ", content='" + content + '\'' +
                '}';
    }
}
