package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
     * ArrayList of arrays of integers that represent the resources of each player in the game
     */
    private final ArrayList<int[]> playersResourcesSummary;
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
     * Integer that represents the kingdom of the card facing up in the resources deck.
     */
    private final int resourceCardDeckFacingKingdom;
    /**
     * Integer that represents the size of the gold deck
     */
    private final int gameGoldDeckSize;
    /**
     * Integer that represents the kingdom of the card facing up in the gold deck.
     */
    private final int goldCardDeckFacingKingdom;
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
                                   ArrayList<Integer> playerHand, int playerSecretObjective, int[] playerPoints, ArrayList<int[]> playersResourcesSummary,
                                   ArrayList<ArrayList<int[]>> playerFields, int[] playerResources, ArrayList<Integer> gameCommonObjectives,
                                   ArrayList<Integer> gameCurrentResourceCards, ArrayList<Integer> gameCurrentGoldCards,
                                   int gameResourcesDeckSize, int gameGoldDeckSize, int matchStatus, ArrayList<ChatMessage> chatHistory, String currentPlayer,
                                   ArrayList<int[]> newAvailableFieldSpaces,
                                   int resourceCardDeckFacingKingdom, int goldCardDeckFacingKingdom) {
        this.recipientNickname = recipientNickname;
        this.playerNicknames = playerNicknames;
        this.playerConnected = playerConnected;
        this.playerColours = playerColours;
        this.playerHand = playerHand;
        this.playerSecretObjective = playerSecretObjective;
        this.playerPoints = playerPoints;
        this.playersResourcesSummary = playersResourcesSummary;
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
        this.resourceCardDeckFacingKingdom = resourceCardDeckFacingKingdom;
        this.goldCardDeckFacingKingdom = goldCardDeckFacingKingdom;
    }

    @Override
    public void processMessage(View view) {
            // FIXME: We should pass the playerResourcesSummary to the view according to the View demands
            view.updatePlayerData(playerNicknames, playerConnected,playerColours, playerHand, playerSecretObjective,
                    playerPoints, playerFields, playerResources, gameCommonObjectives, gameCurrentResourceCards,
                    gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus, chatHistory,
                    currentPlayer, newAvailableFieldSpaces, resourceCardDeckFacingKingdom, goldCardDeckFacingKingdom);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString(){
        String playerFieldsAsString = playerFields.stream()
            .map(innerList -> innerList.stream()
                .map(Arrays::toString)
                .collect(Collectors.joining(", ", "[", "]")))
            .collect(Collectors.joining(", ", "[", "]"));

        return "PlayerGameStatusMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNicknames=" + playerNicknames +
                ", playerConnected=" + playerConnected.toString() +
                ", playerColours=" + playerColours.toString() +
                ", playerHand=" + playerHand.toString() +
                ", playerSecretObjective=" + playerSecretObjective +
                ", playerPoints=" + Arrays.toString(playerPoints) +
                ", playersResourcesSummary=" + playersResourcesSummary.stream().map(Arrays::toString).toList() +
                ", playerFields=" + playerFieldsAsString +
                ", playerResources=" + Arrays.toString(playerResources) +
                ", gameCommonObjectives=" + gameCommonObjectives.toString() +
                ", gameCurrentResourceCards=" + gameCurrentResourceCards.toString() +
                ", gameCurrentGoldCards=" + gameCurrentGoldCards.toString() +
                ", gameResourcesDeckSize=" + gameResourcesDeckSize +
                ", resourceCardDeckFacingKingdom=" + resourceCardDeckFacingKingdom +
                ", gameGoldDeckSize=" + gameGoldDeckSize +
                ", goldCardDeckFacingKingdom=" + goldCardDeckFacingKingdom +
                ", matchStatus=" + matchStatus +
                ", chatHistory=" + chatHistory.stream().map(ChatMessage::toString).toList() +
                ", currentPlayer='" + currentPlayer + '\'' +
                ", newAvailableFieldSpaces=[" + newAvailableFieldSpaces.stream().map(Arrays::toString).collect(Collectors.joining(", ")) +
                "]}";
    }
}
