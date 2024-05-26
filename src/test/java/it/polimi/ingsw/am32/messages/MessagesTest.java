package it.polimi.ingsw.am32.messages;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesTest {
    @DisplayName("Print a PlayerConnectedMessage")
    @Test
    public void printPlayerGameStatusMessage() {
       // Initialize all the required parameters
        String recipientNickname = "player1";
        ArrayList<String> playerNicknames = new ArrayList<>(Arrays.asList("player1", "player2"));
        ArrayList<Boolean> playerConnected = new ArrayList<>(Arrays.asList(true, true));
        ArrayList<Integer> playerColours = new ArrayList<>(Arrays.asList(1, 2));
        ArrayList<Integer> playerHand = new ArrayList<>(Arrays.asList(1, 2));
        int playerSecretObjective = 1;
        int[] playerPoints = new int[]{1, 2};
        ArrayList<int[]> playerField1 = new ArrayList<>(Arrays.asList(new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}));
        ArrayList<int[]> playerField2 = new ArrayList<>(Arrays.asList(new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}, new int[]{1, 2, 3, 4}));
        ArrayList<ArrayList<int[]>> playerFields = new ArrayList<>(Arrays.asList(playerField1, playerField2));
        int[] playerResources = new int[]{1, 2, 3, 4, 5};
        ArrayList<Integer> gameCommonObjectives = new ArrayList<>(Arrays.asList(1, 2));
        ArrayList<Integer> gameCurrentResourceCards = new ArrayList<>(Arrays.asList(1, 2));
        ArrayList<Integer> gameCurrentGoldCards = new ArrayList<>(Arrays.asList(1, 2));
        int gameResourcesDeckSize = 1;
        int resourceCardDeckFacingKingdom = 1;
        int gameGoldDeckSize = 1;
        int goldCardDeckFacingKingdom = 1;
        int matchStatus = 1;
        ArrayList<ChatMessage> chatHistory = new ArrayList<>(); // Assuming ChatMessage is a valid class
        chatHistory.add(new ChatMessage("player1", "player2", true, "Hello, player2!"));
        chatHistory.add(new ChatMessage("player2", "player1", true, "Hello, player1!"));
        String currentPlayer = "player1";
        ArrayList<int[]> newAvailableFieldSpaces = new ArrayList<>(List.of(new int[]{1, 2, 3, 4}));

        // Create a new PlayerGameStatusMessage
        PlayerGameStatusMessage playerGameStatusMessage = new PlayerGameStatusMessage(recipientNickname, playerNicknames,
                playerConnected, playerColours, playerHand, playerSecretObjective, playerPoints, playerFields,
                playerResources, gameCommonObjectives, gameCurrentResourceCards, gameCurrentGoldCards,
                gameResourcesDeckSize, gameGoldDeckSize, matchStatus, chatHistory, currentPlayer,
                newAvailableFieldSpaces, resourceCardDeckFacingKingdom, goldCardDeckFacingKingdom);
        // System.out.println(playerGameStatusMessage);
    }
}
