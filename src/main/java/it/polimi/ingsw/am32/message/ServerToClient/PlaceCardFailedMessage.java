package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent a player notifying them that a card placement has failed.
 */
public class PlaceCardFailedMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the message.
     */
    private final String recipientNickname;
    /**
     * The reason why the card placement has failed.
     */
    private final String reason;

    /**
     * The constructor of the class: it creates a new PlaceCardFailedMessage with the nickname of the recipient who will receive the message
     * and the reason why the card placement has failed.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param reason the reason why the card placement has failed.
     */
    public PlaceCardFailedMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    /**
     * This method is used to process the message to the client when a card placement has failed,
     * updating the view of the player who will receive the message.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateCurrentEvent(Event.PLACE_CARD_FAILURE);
        view.handleFailureCase(Event.PLACE_CARD_FAILURE,reason);
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the message.
     * @return the nickname of the recipient who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PlaceCardFailedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
