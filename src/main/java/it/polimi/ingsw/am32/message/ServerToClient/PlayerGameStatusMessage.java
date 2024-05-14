package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerGameStatusMessage implements StoCMessage {
    /**
     * Nickname of the recipient of the message
     */
    private final String recipientNickname;
    /**
     * List of nicknames of the players in the game (connected or disconnected)
     */
    private final ArrayList<String> playerNicknames;
    /**
     * List of booleans that represent the connection status of the players in the game
     */
    private final ArrayList<Boolean> playerConnected;
    /**
     * List of integers that represent the colours of the players in the game
     */
    private final ArrayList<Integer> playerColours;
    /**
     * List of integers that represent the ids of the cards in the hand of the player
     */
    private final ArrayList<Integer> playerHand;
    /**
     * Integer that represents the id of the chosen secret objective of the player
     */
    private final int playerSecretObjective;
    /**
     * Array of integers that represent the points held by each player
     */
    private final int[] playerPoints;
    /**
     * List of lists of arrays of integers that represent the fields of each player
     */
    private final ArrayList<ArrayList<int[]>> playerFields;
    /**
     * Array of integers that represent the resources of the player
     */
    private final int[] playerResources;
    /**
     * List of integers that represent the ids of the common objectives of the game
     */
    private final ArrayList<Integer> gameCommonObjectives;
    /**
     * List of integers that represent the ids of the place-up resource cards in the game
     */
    private final ArrayList<Integer> gameCurrentResourceCards;
    /**
     * List of integers that represent the ids of the place-up gold cards in the game
     */
    private final ArrayList<Integer> gameCurrentGoldCards;
    /**
     * Integer that represents the size of the resources deck
     */
    private final int gameResourcesDeckSize;
    /**
     * Integer that represents the size of the gold deck
     */
    private final int gameGoldDeckSize;
    /**
     * Integer that represents the status of the match
     */
    private final int matchStatus;
    /**
     * List of chat messages that represent the chat history of the game
     */
    private final ArrayList<ChatMessage> chatHistory;
    /**
     * String that represents the nickname of the current player
     */
    private final String currentPlayer;
    /**
     * List of arrays of integers that represent the available field spaces for the player to place a card
     */
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
            view.updatePlayerDate(playerNicknames, playerConnected,playerColours, playerHand, playerSecretObjective, playerPoints, playerFields, playerResources, gameCommonObjectives,
                    gameCurrentResourceCards, gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus, chatHistory, currentPlayer, newAvailableFieldSpaces);
        //TODO FOR RECONECTION
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "playerNicknames: " + playerNicknames.toString() + "\n";
        myString += "playerConnected: " + playerConnected.toString() + "\n";
        myString += "playerColours: " + playerColours.toString() + "\n";
        myString += "playerHand: " + playerHand.toString() + "\n";
        myString += "playerSecretObjective: " + playerSecretObjective + "\n";
        myString += "playerPoints: " + Arrays.toString(playerPoints) + "\n";
        myString += "playerFields: " + playerFields.toString() + "\n";
        myString += "playerResources: " + Arrays.toString(playerResources) + "\n";
        myString += "gameCommonObjectives: " + gameCommonObjectives.toString() + "\n";
        myString += "gameCurrentResourceCards: " + gameCurrentResourceCards.toString() + "\n";
        myString += "gameCurrentGoldCards: " + gameCurrentGoldCards.toString() + "\n";
        myString += "gameResourcesDeckSize: " + gameResourcesDeckSize + "\n";
        myString += "gameGoldDeckSize: " + gameGoldDeckSize + "\n";
        myString += "matchStatus: " + matchStatus + "\n";
        myString += "chatHistory: " + chatHistory.toString() + "\n";
        myString += "currentPlayer: " + currentPlayer + "\n";
        myString += "newAvailableFieldSpaces: " + newAvailableFieldSpaces.toString() + "\n";
        return myString;
    }
}
