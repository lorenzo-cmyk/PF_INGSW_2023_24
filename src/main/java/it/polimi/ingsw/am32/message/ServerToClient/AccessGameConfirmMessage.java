package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;
/**
 * This class is used to manage the message sent by the server to the client to confirm the access to the game.
 */
public class AccessGameConfirmMessage implements StoCMessage {
    /**
     * The nickname of the recipient who requested to access the game before.
     */
    private final String recipientNickname;

    /**
     * The constructor of the class: it creates a new AccessGameConfirmMessage with the nickname of the recipient who
     * requested to access the game before.
     * @param recipientNickname the nickname of the player who will receive the confirmation message for his request
     *                          to access the game and should be updated.
     **/
    public AccessGameConfirmMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    /**
     * This method is used to process the message to the client when the access to the game is confirmed,
     *  updating the current event of the game and notifying the player that he joined the game successfully.
     * @param view the view of the player who will receive the message.
     */
    @Override
    public void processMessage(View view) {
        view.updateCurrentEvent(Event.GAME_JOINED);
        view.handleEvent(Event.GAME_JOINED,null); // notify the player that he joined the game successfully.
    }

    /**
     * This method is used to get the nickname of the recipient who requested to access the game before.
     * @return the nickname of the player who will receive the confirmation message for his request to access the game.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "AccessGameConfirmMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
