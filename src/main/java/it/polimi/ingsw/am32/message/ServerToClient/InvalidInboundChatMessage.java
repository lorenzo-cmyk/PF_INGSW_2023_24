package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent from the server to the player notifying them of an invalid chat message.
 */
public class InvalidInboundChatMessage implements StoCMessage {
    /**
     * The nickname of the sender of the invalid chat message.
     */
    private final String recipientNickname;
    /**
     * The reason why the chat message is invalid.
     */
    private final String reason;

    /**
     * The constructor of the class.
     * @param recipientNickname The nickname of the sender of the invalid chat message.
     * @param reason The reason why the chat message is invalid.
     */
    public InvalidInboundChatMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    /**
     * This method is used to get the nickname of the sender of the invalid chat message.
     * @return The nickname of the sender of the invalid chat message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method is used to notify the recipient of the invalid chat message.
     * @param view The view of the recipient.
     */
    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.CHAT_ERROR,reason);
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the InvalidInboundChatMessage object.
     */
    @Override
    public String toString() {
        return "InvalidInboundChatMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
