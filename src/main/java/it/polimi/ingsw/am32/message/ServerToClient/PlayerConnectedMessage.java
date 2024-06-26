package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to notify players that a new player has joined the game during the lobby phase.
 */
public class PlayerConnectedMessage implements StoCMessage {
    /**
     * The nickname of the player that will receive the message.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player that has joined the game.
     */
    private final String connectedNickname;

    /**
     * Constructor.
     * @param recipientNickname The nickname of the player that will receive the message.
     * @param connectedNickname The nickname of the player that has joined the game.
     */
    public PlayerConnectedMessage(String recipientNickname, String connectedNickname) {
        this.recipientNickname = recipientNickname;
        this.connectedNickname = connectedNickname;
    }

    /**
     * This method is used to process the message to the client when a new player joins the game.
     * @param view the view of the player who will receive the message.
     */
    @Override
    public void processMessage(View view) {
        view.handleEvent(Event.NEW_PLAYER_JOIN, connectedNickname);
    }

    /**
     * This method is used to get the nickname of the player that will receive the message.
     * @return the nickname of the player that will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PlayerConnectedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", connectedNickname='" + connectedNickname + '\'' +
                '}';
    }
}

