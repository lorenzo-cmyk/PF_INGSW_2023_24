package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;
/**
 * This class is used to manage the message sent by the server to the client to confirm the creation of a new game.
 */
public class NewGameConfirmationMessage implements StoCMessage {
    /**
     * The nickname of the recipient who requested to create a new game and received the confirmation message.
     */
    private final String recipientNickname;
    /**
     * The id of the match created.
     */
    private final int matchId;

    /**
     * The constructor of the class: it creates a new NewGameConfirmationMessage with the nickname of the recipient who
     * requested to create a new game and the id of the match returned by the server.
     * @param recipientNickname the nickname of the player who will receive the confirmation message for his request to
     *                          create a new game.
     * @param matchId the id of the match created and returned by the server.
     */
    public NewGameConfirmationMessage(String recipientNickname, int matchId) {
        this.recipientNickname = recipientNickname;
        this.matchId = matchId;
    }

    /**
     * This method is used to process the message to the client when the creation of a new game is confirmed,
     * updating view with data related to the new game created and the current event of the game, notifying the player
     * that the game is created correctly.
     * @param view the view of the player who will receive the message and should be updated.
     */

    @Override
    public void processMessage(View view){
        view.updateNewGameConfirm(matchId, recipientNickname);
        view.handleEvent(Event.GAME_CREATED,null); // print the message to notify the player that the game is created correctly
        view.updateCurrentEvent(Event.WAITING_FOR_START); // enter the waiting for start event
    }

    /**
     * This method is used to get the nickname of the player that will receive the message.
     * @return the nickname of the player that will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the NewGameConfirmationMessage object.
     */
    @Override
    public String toString() {
        return "NewGameConfirmationMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", matchId=" + matchId +
                '}';
    }
}
