package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent from the server to the players notifying them of a new chat message.
 */
public class OutboundChatMessage implements StoCMessage {
    /**
     * The nickname of the recipient of the chat message.
     */
    private final String recipientString;
    /**
     * The nickname of the sender of the chat message.
     */
    private final String senderNickname;
    /**
     * The content of the chat message.
     */
    private final String content;

    /**
     * The constructor of the class.
     * @param recipientString The nickname of the recipient of the chat message.
     * @param senderNickname The nickname of the sender of the chat message.
     * @param content The content of the chat message.
     */
    public OutboundChatMessage(String recipientString, String senderNickname, String content) {
        this.recipientString = recipientString;
        this.senderNickname = senderNickname;
        this.content = content;
    }

    /**
     * This method is used to update the chat of the recipient.
     * @param view The view of the recipient.
     */
    @Override
    public void processMessage(View view) {
        view.updateChat(recipientString,senderNickname,content);
    }

    /**
     * This method is used to get the nickname of the sender of the chat message.
     * @return The nickname of the sender of the chat message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientString;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the OutboundChatMessage object.
     */
    @Override
    public String toString() {
        return "OutboundChatMessage:{" +
                "recipientString='" + recipientString + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
