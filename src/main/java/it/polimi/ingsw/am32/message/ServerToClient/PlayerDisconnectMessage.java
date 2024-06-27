package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to notify players when another player disconnects from the game during any stage.
 */
public class PlayerDisconnectMessage implements StoCMessage {
    /**
     * The nickname of the recipient of the message
     */
    private final String recipientNickname;
    /**
     * The nickname of the player who disconnected
     */
    private final String disconnectedNickname;

    /**
     * The constructor of the class: it creates a new PlayerDisconnectMessage with the nickname of the recipient of the message
     * and the nickname of the player who disconnected.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param disconnectedNickname the nickname of the player who disconnected from the game.
     */
    public PlayerDisconnectMessage(String recipientNickname, String disconnectedNickname) {
        this.recipientNickname = recipientNickname;
        this.disconnectedNickname = disconnectedNickname;
    }

    /**
     * This method is used to process the message to the client when a player disconnects from the game,
     * updating the view based on the PLAYER_DISCONNECTED event.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.handleEvent(Event.PLAYER_DISCONNECTED, disconnectedNickname);
    }

    /**
     * This method is used to get the nickname of the recipient of the message.
     * @return the nickname of the player who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the PlayerDisconnectMessage object.
     */
    @Override
    public String toString() {
        return "PlayerDisconnectMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", disconnectedNickname='" + disconnectedNickname + '\'' +
                '}';
    }
}
