package it.polimi.ingsw.am32.chat;

import it.polimi.ingsw.am32.chat.exceptions.MalformedMessageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    @DisplayName("Should create a ChatMessage when parameters are valid")
    @Test
    void shouldCreateChatMessageWhenParametersAreValid() {
        ChatMessage chatMessage = new ChatMessage("sender", "recipient", false, "Hello, World!");
        assertEquals("sender", chatMessage.getSenderNickname());
        assertEquals("recipient", chatMessage.getRecipientNickname());
        assertFalse(chatMessage.isMulticastFlag());
        assertEquals("Hello, World!", chatMessage.getMessageContent());
    }

    @DisplayName("Should throw exception when sender nickname is empty")
    @Test
    void shouldThrowExceptionWhenSenderNicknameIsEmpty() {
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("", "recipient", false, "Hello, World!"));
    }

    @DisplayName("Should throw exception when recipient nickname is empty both in multicast and unicast messages")
    @Test
    void shouldThrowExceptionWhenRecipientNicknameIsEmpty() {
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("sender", "", false, "Hello, World!"));
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("sender", "", true, "Hello, World!"));
    }

    @DisplayName("Should throw exception when recipient nickname is null and not multicast")
    @Test
    void shouldThrowExceptionWhenRecipientNicknameIsNullAndNotMulticast() {
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("sender", null, false, "Hello, World!"));
    }

    @DisplayName("Should create a ChatMessage when recipient nickname is null and multicast")
    @Test
    void shouldCreateChatMessageWhenRecipientNicknameIsNullAndMulticast() {
        ChatMessage chatMessage = new ChatMessage("sender", null, true, "Hello, World!");
        assertEquals("sender", chatMessage.getSenderNickname());
        assertNull(chatMessage.getRecipientNickname());
        assertTrue(chatMessage.isMulticastFlag());
        assertEquals("Hello, World!", chatMessage.getMessageContent());
    }

    @DisplayName("Should throw exception when message content is empty")
    @Test
    void shouldThrowExceptionWhenMessageContentIsEmpty() {
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("sender", "recipient", false, ""));
    }

    @DisplayName("Should throw exception when message content is null")
    @Test
    void shouldThrowExceptionWhenMessageContentIsNull() {
        assertThrows(MalformedMessageException.class, () -> new ChatMessage("sender", "recipient", false, null));
    }
}