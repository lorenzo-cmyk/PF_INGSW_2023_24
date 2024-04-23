package it.polimi.ingsw.am32.chat;

/**
 * This class represents a chat message in the system.
 * It contains the sender's nickname, the recipient's nickname, a flag indicating if the message is multicast,
 * and the content of the message.
 *
 * @author Lorenzo
 * @author Anto
 */
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
        this.recipientNickname = recipientNickname;
        this.multicastFlag = multicastFlag;
        this.messageContent = messageContent;
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
}
