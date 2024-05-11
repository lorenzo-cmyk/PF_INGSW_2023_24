package it.polimi.ingsw.am32.chat;

import it.polimi.ingsw.am32.chat.exceptions.NullMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    private Chat chat;

    @BeforeEach
    void setUp() {
        chat = new Chat();
    }

    @DisplayName("addMessage should add message to chat history when message is not null")
    @Test
    void addMessageShouldAddMessageToChatHistoryWhenMessageIsNotNull() {
        ChatMessage message = new ChatMessage("sender", "recipient", false, "Hello");
        chat.addMessage(message);
        ArrayList<ChatMessage> history = chat.getHistory();
        assertTrue(history.contains(message));
    }

    @DisplayName("addMessage should throw NullMessageException when message is null")
    @Test
    void addMessageShouldThrowNullMessageExceptionWhenMessageIsNull() {
        assertThrows(NullMessageException.class, () -> chat.addMessage(null));
    }

    @DisplayName("getPlayerChatHistory should return an empty list when there are no messages")
    @Test
    void getPlayerChatHistoryShouldReturnOnlyMessagesForSpecifiedPlayer() {
        ChatMessage message1 = new ChatMessage("sender1", "player1", false, "Hello");
        ChatMessage message2 = new ChatMessage("sender2", "player2", false, "Hi");
        ChatMessage message3 = new ChatMessage("sender3", "player1", false, "Hola");
        chat.addMessage(message1);
        chat.addMessage(message2);
        chat.addMessage(message3);
        ArrayList<ChatMessage> playerChatHistory = chat.getPlayerChatHistory("player1");
        assertTrue(playerChatHistory.contains(message1));
        assertTrue(playerChatHistory.contains(message3));
        assertFalse(playerChatHistory.contains(message2));
    }

    @DisplayName("getPlayerChatHistory should return also multicast messages along with messages for specified player")
    @Test
    void getPlayerChatHistoryShouldReturnMulticastMessages() {
        ChatMessage message1 = new ChatMessage("sender1", "player1", false, "Hi");
        ChatMessage message2 = new ChatMessage("sender2", "player2", true, "Hello");
        chat.addMessage(message1);
        chat.addMessage(message2);
        ArrayList<ChatMessage> playerChatHistory = chat.getPlayerChatHistory("player1");
        assertTrue(playerChatHistory.contains(message1));
        assertTrue(playerChatHistory.contains(message2));
    }

    @DisplayName("getHistory should return the chat history")
    @Test
    void getHistoryShouldReturnTheChatHistory() {
        ChatMessage message1 = new ChatMessage("sender1", "player1", false, "Hi");
        ChatMessage message2 = new ChatMessage("sender2", "player2", true, "Hello");
        chat.addMessage(message1);
        chat.addMessage(message2);
        ArrayList<ChatMessage> history = chat.getHistory();
        assertTrue(history.contains(message1));
        assertTrue(history.contains(message2));
    }
    
}