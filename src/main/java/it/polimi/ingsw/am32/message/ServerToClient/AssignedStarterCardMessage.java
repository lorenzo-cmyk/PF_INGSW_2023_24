package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is a message sent to the player to notify them of their assigned starter card after the preparation phase is over.
 */
public class AssignedStarterCardMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the assigned starter card.
     */
    private final String recipientNickname;
    /**
     * The id of the assigned starter card.
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
     * This method is used to process the message to the client when the assigned starter card is received,
     * updating the current event of the game and notifying the player that he received the assigned starter card.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.setStarterCard(cardId); // store the starter card
        view.updateCurrentEvent(Event.SELECT_STARTER_CARD_SIDE);// set the current event
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the assigned starter card.
     * @return the nickname of the player who will receive the assigned starter card.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the AssignedStarterCardMessage object.
     */
    @Override
    public String toString() {
        return "AssignedStarterCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}
