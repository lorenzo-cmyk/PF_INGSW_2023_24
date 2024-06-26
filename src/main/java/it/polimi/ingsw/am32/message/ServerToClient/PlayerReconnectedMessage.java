package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to notify players that a player has reconnected to the game.
 **/
public class PlayerReconnectedMessage implements StoCMessage {
    /**
     * The nickname of the recipient who requested to reconnect to the game before.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player who reconnected to the game.
     */
    private final String disconnectedNickname;

    /**
     * The constructor of the class: it creates a new PlayerReconnectedMessage with the nickname of the recipient who
     * requested to reconnect to the game before and the nickname of the player who reconnected to the game.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param disconnectedNickname the nickname of the player who reconnected to the game.
     */
    public PlayerReconnectedMessage(String recipientNickname, String disconnectedNickname) {
        this.recipientNickname = recipientNickname;
        this.disconnectedNickname = disconnectedNickname;
    }

    /**
     * This method is used to process the message to the client when a player reconnects to the game,
     * updating the view based on the PLAYER_RECONNECTED event.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.handleEvent(Event.PLAYER_RECONNECTED, disconnectedNickname);
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
     * @return A string representation of the PlayerReconnectedMessage object.

     */
    @Override
    public String toString() {
        return "PlayerReconnectedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", disconnectedNickname='" + disconnectedNickname + '\'' +
                '}';
    }
}