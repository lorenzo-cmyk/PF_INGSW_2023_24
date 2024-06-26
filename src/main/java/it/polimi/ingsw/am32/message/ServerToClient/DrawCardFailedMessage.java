package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent by the server to the client when his draw card action fails.
 */
public class DrawCardFailedMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the message when his draw card action fails.
     */
    private final String recipientNickname;
    /**
     * The reason why the draw card action failed.
     */
    private final String reason;

    /**
     * The constructor of the class: it creates a new DrawCardFailedMessage with the nickname of the recipient who will
     * receive the message when his draw card action fails and the reason why the draw card action failed.
     * @param recipientNickname the nickname of the player who tried to draw a card but failed.
     * @param reason the reason why the draw card action failed.
     */
    public DrawCardFailedMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    /**
     * This method is used to process the message to the client when the draw card action fails, updating the view with
     * the reason why the draw card action failed.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.DRAW_CARD_FAILURE, reason);
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the message when his draw card action
     * fails.
     * @return the nickname of the player who tried to draw a card but failed.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the DrawCardFailedMessage object.
     */
    @Override
    public String toString() {
        return "DrawCardFailedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
