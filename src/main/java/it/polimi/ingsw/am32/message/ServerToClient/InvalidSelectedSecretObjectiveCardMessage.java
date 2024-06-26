package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to the client when the selected secret objective card is invalid.
 */
public class InvalidSelectedSecretObjectiveCardMessage implements StoCMessage {
    /**
     * The nickname of the recipient.
     */
    private final String recipientNickname;
    /**
     * The reason why the selected secret objective card is invalid.
     */
    private final String reason;

    /**
     * The constructor of the class.
     * @param recipientNickname the nickname of the recipient.
     * @param reason the reason why the selected secret objective card is invalid.
     */
    public InvalidSelectedSecretObjectiveCardMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    /**
     * Getter method.
     * @return the nickname of the recipient.
     */
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * Getter method.
     * @return the reason why the selected secret objective card is invalid.
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method is used to manage the message sent to the client when the selected secret objective card is invalid.
     * @param view the view that will manage the message.
     */
    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.SELECT_SECRET_OBJ_CARD_FAILURE,reason);
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the InvalidSelectedSecretObjectiveCardMessage object.
     */
    @Override
    public String toString() {
        return "InvalidSelectedSecretObjectiveCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
