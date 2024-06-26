package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent by the server to the client to notify the player that the game is
 * started, entering the preparation phase.
 */

public class GameStartedMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the message when the game enters the preparation phase.
     */
    private final String recipientNickname;

    /**
     * The constructor of the class: it creates a new GameStartedMessage with the nickname of the recipient who will
     * receive the message when the game enters the preparation phase.
     * @param recipientNickname the nickname of the player who will receive the message.
     */

    public GameStartedMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    /**
     * This method is used to process the message to the client when the game is started, updating the view with the
     * setup of players' data and notifying the player that the game entered the preparation phase.
     * @param view the view of the player who will receive the message and should be updated.
     */

    @Override
    public void processMessage(View view) {
        view.setUpPlayersData();
        view.updateCurrentEvent(Event.GAME_START);
        view.handleEvent(Event.GAME_START,null);  // notify the player that the game is started
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the message when the game enters the
     * preparation phase.
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
     * @return A string representation of the GameStartedMessage object.
     */
    @Override
    public String toString() {
        return "GameStartedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
