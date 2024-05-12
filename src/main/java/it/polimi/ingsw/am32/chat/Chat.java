package it.polimi.ingsw.am32.chat;

import it.polimi.ingsw.am32.chat.exceptions.NullMessageException;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class represents a chat system.
 * It contains a history of chat messages.
 *
 * @author Lorenzo
 * @author Anto
 */
public class Chat {
    private final ArrayList<ChatMessage> chatHistory;

    /**
     * Constructs a new Chat with an empty history.
     */
    public Chat() {
        chatHistory = new ArrayList<>();
    }

    /**
     * Adds a new message to the chat history.
     *
     * @param msg The message to be added
     * @exception NullMessageException If the message is null
     */
    public void addMessage(ChatMessage msg) {
        if (msg == null) throw new NullMessageException("Chat message cannot be null");
        chatHistory.add(msg);
    }

    /**
     * Returns an ArrayList of ChatMessage objects representing only the messages directed to the specified player
     * or multicast messages.
     *
     * @param playerNickname The nickname of the player
     * @return An ArrayList of ChatMessage objects
     */
    public ArrayList<ChatMessage> getPlayerChatHistory(String playerNickname) {
        // This method returns an ArrayList of ChatMessage objects representing only the message directed to that player
        // or multicast messages.
        return chatHistory.stream()
                .filter(msg -> msg.getRecipientNickname().equals(playerNickname) || msg.isMulticastFlag())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the chat history.
     *
     * @return An ArrayList of ChatMessage objects
     */
    public ArrayList<ChatMessage> getHistory() {
        return chatHistory;
    }
}
