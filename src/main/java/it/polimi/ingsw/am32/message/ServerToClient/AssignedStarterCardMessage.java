package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent by the server to the client to assign the starter card to the player.
 * The player will receive the starter card and will be able to select the side of the card.
 */

public class AssignedStarterCardMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the assignment of the starter card.
     */
    private final String recipientNickname;
    /**
     * The id of the starter card assigned to the player.
     */
    private final int cardId;

    /**
     * The constructor of the class: it creates a new AssignedStarterCardMessage with the nickname of the recipient who
     * will receive the assignment of the starter card and the id of the card assigned to the player.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param cardId the id of the starter card assigned to the player.
     */
    public AssignedStarterCardMessage(String recipientNickname, int cardId) {
        this.recipientNickname = recipientNickname;
        this.cardId = cardId;
    }

    /**
     * This method is used to process the message to the client when the starter card is assigned to the player,
     * setting the starter card received by the server and updating the current event of the game.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.setStarterCard(cardId); // store the starter card
        view.updateCurrentEvent(Event.SELECT_STARTER_CARD_SIDE);// set the current event
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the assignment of the starter card.
     * @return the nickname of the player who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "AssignedStarterCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}
