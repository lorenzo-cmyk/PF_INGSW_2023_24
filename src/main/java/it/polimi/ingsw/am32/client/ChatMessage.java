package it.polimi.ingsw.am32.client;

import it.polimi.ingsw.am32.client.exceptions.MalformedMessageException;

import java.util.Objects;

/**
 * This class represents a chat message in the system.
 * It contains the sender's nickname, the recipient's nickname, a flag indicating if the message is multicast,
 * and the content of the message.
 *
 * @author Lorenzo
 * @author Anto
 */
@SuppressWarnings("ClassCanBeRecord")
public class ChatMessage {
    private final String senderNickname;
    private final String recipientNickname;
    private final boolean multicastFlag;
    private final String messageContent;

    /**
     * Constructs a new ChatMessage with the given parameters.
     *
     * @param senderNickname    The nickname of the sender of the message
     * @param recipientNickname The nickname of the recipient of the message
     * @param multicastFlag     A flag indicating if the message is multicast or not
     * @param messageContent    The content of the message
     */
    public ChatMessage(String senderNickname, String recipientNickname, boolean multicastFlag, String messageContent) {
        this.senderNickname = senderNickname;
        // Sender nickname can be null if the message is sent by the system but cannot be empty
        if (Objects.equals(senderNickname, "")) {
            throw new MalformedMessageException("Sender nickname cannot be empty");
        }
        this.recipientNickname = recipientNickname;
        this.multicastFlag = multicastFlag;
        // Recipient nickname can be null if the message is multicast but cannot be empty
        if (Objects.equals(recipientNickname, "")) {
            throw new MalformedMessageException("Recipient nickname cannot be empty");
        }
        if (recipientNickname == null && !multicastFlag) {
            throw new MalformedMessageException("Recipient nickname cannot be null if the message is not multicast");
        }
        this.messageContent = messageContent;
        // Message content cannot be null or empty
        if (messageContent == null || messageContent.isEmpty()) {
            throw new MalformedMessageException("Message content cannot be empty or null");
        }
    }

    /**
     * Returns the sender's nickname.
     *
     * @return The sender's nickname
     */
    public String getSenderNickname() {
        return senderNickname;
    }

    /**
     * Returns the recipient's nickname.
     *
     * @return The recipient's nickname
     */
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * Returns the multicast flag.
     *
     * @return The multicast flag
     */
    public boolean isMulticastFlag() {
        return multicastFlag;
    }

    /**
     * Returns the content of the message.
     *
     * @return The content of the message
     */
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senderNickname='" + senderNickname + '\'' +
                ", recipientNickname='" + recipientNickname + '\'' +
                ", multicastFlag=" + multicastFlag +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}
