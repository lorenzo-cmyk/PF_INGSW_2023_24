package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to the client notifying them that the side of the starter card they have selected is invalid.
 */
public class InvalidStarterCardSideSelectionMessage implements StoCMessage {
    /**
     * The nickname of the player who has selected the invalid side of the starter card.
     */
    private final String recipientNickname;
    /**
     * The reason why the side of the starter card selected is invalid.
     */
    private final String reason;

    /**
     * The constructor of the class.
     * @param recipientNickname The nickname of the player who has selected the invalid side of the starter card.
     * @param reason The reason why the side of the starter card selected is invalid.
     */
    public InvalidStarterCardSideSelectionMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    /**
     * Getter method.
     * @return The nickname of the player who has selected the invalid side of the starter card.
     */
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * Getter method.
     * @return The reason why the side of the starter card selected is invalid.
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method is used to manage the message sent to the client notifying them that the side of the starter card they have selected is invalid.
     * @param view The view of the client.
     */
    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.SELECT_STARTER_CARD_SIDE_FAILURE,reason);
    }

    @Override
    public String toString() {
        return "InvalidStarterCardSideSelectionMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

}
