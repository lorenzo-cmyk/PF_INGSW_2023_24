package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent by the server to the client to confirm the reconnection to the game.
 *
 **/
public class ReconnectGameConfirmMessage implements StoCMessage {
    /**
     * The nickname of the recipient who requested to reconnect to the game before.
     */
    private final String recipientNickname;

    /**
     * The constructor of the class: it creates a new ReconnectGameConfirmMessage with the nickname of the recipient who
     * requested to reconnect to the game before.
     * @param recipientNickname the nickname of the player who will receive the confirmation message for his request
     *                          to reconnect to the game.
     */
    public ReconnectGameConfirmMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    /**
     * This method is used to process the message to the client when the reconnection to the game is confirmed,
     * modifying the view based on the GAME_RECONNECTED event.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.handleEvent(Event.GAME_RECONNECTED,null);
    }

    /**
     * This method is used to get the nickname of the recipient who requested to reconnect to the game before.
     * @return the nickname of the player who received the confirmation message for his request to reconnect to the game.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the ReconnectGameConfirmMessage object.
     */
    @Override
    public String toString() {
        return "ReconnectGameConfirmMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
