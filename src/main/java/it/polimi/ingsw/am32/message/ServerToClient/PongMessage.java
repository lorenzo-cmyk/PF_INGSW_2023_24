package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to notify the client that a ping has been received.
 */
public class PongMessage implements StoCMessage {
    /**
     * The nickname of the recipient who sent the ping.
     */
    private final String recipientNickname;

    /**
     * The constructor of the class: it creates a new PongMessage with the nickname of the recipient who sent the ping.
     * @param recipientNickname the nickname of the player who will receive the pong message.
     */
    public PongMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    /**
     * This method is used to process the message to the client when a pong is received.
     * @param view the view of the player who will receive the message.
     */
    @Override
    public void processMessage(View view) {
        // TODO
    }

    /**
     * This method is used to get the nickname of the recipient who sent the ping.
     * @return the nickname of the player who will receive the pong message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the PongMessage object.
     */
    @Override
    public String toString() {
        return "PongMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
