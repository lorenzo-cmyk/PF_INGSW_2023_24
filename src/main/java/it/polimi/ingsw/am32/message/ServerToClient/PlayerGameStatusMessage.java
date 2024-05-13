package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class PlayerGameStatusMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> playerNicknames;
    private final ArrayList<Boolean> playerConnected;
    private final ArrayList<Integer> playerColours;
    private final ArrayList<Integer> playerHand;
    private final int playerSecretObjective;
    private final int[] playerPoints;
    private final ArrayList<ArrayList<int[]>> playerFields;
    private final int[] playerResources;
    private final ArrayList<Integer> gameCommonObjectives;
    private final ArrayList<Integer> gameCurrentResourceCards;
    private final ArrayList<Integer> gameCurrentGoldCards;
    private final int gameResourcesDeckSize;
    private final int gameGoldDeckSize;
    private final int matchStatus;
    private final ArrayList<ChatMessage> chatHistory;
    private final String currentPlayer;
    private final ArrayList<int[]> newAvailableFieldSpaces;

    public PlayerGameStatusMessage(String recipientNickname, ArrayList<String> playerNicknames, ArrayList<Boolean> playerConnected, ArrayList<Integer> playerColours,
                                   ArrayList<Integer> playerHand, int playerSecretObjective, int[] playerPoints,
                                   ArrayList<ArrayList<int[]>> playerFields, int[] playerResources, ArrayList<Integer> gameCommonObjectives,
                                   ArrayList<Integer> gameCurrentResourceCards, ArrayList<Integer> gameCurrentGoldCards,
                                   int gameResourcesDeckSize, int gameGoldDeckSize, int matchStatus, ArrayList<ChatMessage> chatHistory, String currentPlayer,
                                   ArrayList<int[]> newAvailableFieldSpaces) {
        this.recipientNickname = recipientNickname;
        this.playerNicknames = playerNicknames;
        this.playerConnected = playerConnected;
        this.playerColours = playerColours;
        this.playerHand = playerHand;
        this.playerSecretObjective = playerSecretObjective;
        this.playerPoints = playerPoints;
        this.playerFields = playerFields;
        this.playerResources = playerResources;
        this.gameCommonObjectives = gameCommonObjectives;
        this.gameCurrentResourceCards = gameCurrentResourceCards;
        this.gameCurrentGoldCards = gameCurrentGoldCards;
        this.gameResourcesDeckSize = gameResourcesDeckSize;
        this.gameGoldDeckSize = gameGoldDeckSize;
        this.matchStatus = matchStatus;
        this.chatHistory = chatHistory;
        this.currentPlayer = currentPlayer;
        this.newAvailableFieldSpaces = newAvailableFieldSpaces;
    }

    @Override
    public void processMessage(View view) {

    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
