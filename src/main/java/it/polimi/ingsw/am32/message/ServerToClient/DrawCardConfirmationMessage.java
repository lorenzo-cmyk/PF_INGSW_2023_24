package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

/**
 * This class is used to manage the message sent by the server to the client to confirm the draw card action.
 */
public class DrawCardConfirmationMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the confirmation after he draws a card.
     */
    private final String recipientNickname;
    /**
     * The cards in the player's hand after the draw card action.
     */
    private final ArrayList<Integer> playerHand;

    /**
     * The constructor of the class: it creates a new DrawCardConfirmationMessage with the nickname of the recipient who
     * will receive the confirmation and the cards in the player's hand after his draw card action.
     * @param recipientNickname the nickname of the player who will receive the confirmation message.
     * @param playerHand the cards in the player's hand after the draw card action.
     */
    public DrawCardConfirmationMessage(String recipientNickname, ArrayList<Integer> playerHand) {
        this.recipientNickname = recipientNickname;
        this.playerHand = playerHand;
    }

    /**
     * This method is used to process the message to the client when the draw card action is confirmed, updating the
     * view with the cards in the player's hand.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateAfterDrawCard(playerHand);
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the confirmation after he draws a card.
     * @return  the nickname of the player who will receive the confirmation message.
     */

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the DrawCardConfirmationMessage object.
     */
    @Override
    public String toString() {
        return "DrawCardConfirmationMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerHand=" + playerHand.toString() +
                '}';
    }
}
