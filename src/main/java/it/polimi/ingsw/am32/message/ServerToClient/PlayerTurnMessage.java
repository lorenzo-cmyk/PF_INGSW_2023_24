package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to notify players of whose turn it is
 */
public class PlayerTurnMessage implements StoCMessage {
    /**
     * The nickname of the recipient who requested to access the game before.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player whose turn it is
     */
    private final String playerNickname;

    /**
     * The constructor of the class: it creates a new PlayerTurnMessage with the nickname of the recipient who
     * requested to access the game before and the nickname of the player whose turn it is
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param playerNickname the nickname of the player whose turn it is
     */
    public PlayerTurnMessage(String recipientNickname, String playerNickname) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
    }

    /**
     * This method is used to process the message to the client when the player turn is notified,
     * updating the current event of the game and notifying the player that it is his turn.
     * @param view the view of the player who will receive the message.
     */
    @Override
    public void processMessage(View view) {

        view.updatePlayerTurn(playerNickname);
    }

    /**
     * This method is used to get the nickname of the recipient who requested to access the game before.
     * @return the nickname of the player who will receive the confirmation message for his request to access the game.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the PlayerTurnMessage object.
     */
    @Override
    public String toString() {
        return "PlayerTurnMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                '}';
    }
}
