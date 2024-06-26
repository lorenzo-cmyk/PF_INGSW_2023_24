package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is used to manage the message sent to notify the player of the game status
 */
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
     * An Array of two integers that represent the server-assigned secret objective cards of the player
     */
    private final ArrayList<Integer> playerAssignedSecretObjectiveCards;
    /**
     * The ID of the starting card assigned by the server to the player
     */
    private final int playerStartingCard;
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
    private final ArrayList<String[]> chatHistory;
    /**
     * String that represents the nickname of the current player
     */
    private final String currentPlayer;
    /**
     * List of arrays of integers that represent the available field spaces for the player to place a card
     */
    private final ArrayList<int[]> newAvailableFieldSpaces;

    /**
     * Constructor: a message containing all the data necessary to update the view when the game enters a playing phase
     * or when the player reconnects to the game.
     * @param recipientNickname the nickname of the player who will receive the message and his view should be updated.
     * @param playerNicknames the nicknames of the players in the game.
     * @param playerConnected the connection status of the players in the game.
     * @param playerColours the colours of the players in the game.
     * @param playerHand the cards in the hand of the player.
     * @param playerAssignedSecretObjectiveCards the secret objective cards assigned to the player.
     * @param playerStartingCard the starting card assigned to the player.
     * @param playerSecretObjective the secret objective chosen by the player.
     * @param playerPoints the points of the players in the game.
     * @param playersResourcesSummary the resources of the players in the game.
     * @param playerFields the fields data of the players in the game.
     * @param playerResources the resources of the player who will receive the message.
     * @param gameCommonObjectives the common objectives of the game.
     * @param gameCurrentResourceCards the resource cards visible in the game.
     * @param gameCurrentGoldCards the gold cards visible in the game.
     * @param gameResourcesDeckSize the size of the resource deck.
     * @param gameGoldDeckSize the size of the gold deck.
     * @param matchStatus the status of the match.
     * @param chatHistory the chat history of players in the game.
     * @param currentPlayer the nickname of the current player.
     * @param newAvailableFieldSpaces the available field spaces for the player to place a card.
     * @param resourceCardDeckFacingKingdom the kingdom type of the card facing up in the resource deck.
     * @param goldCardDeckFacingKingdom the kingdom type of the card facing up in the gold deck.
     */
    public PlayerGameStatusMessage(String recipientNickname, ArrayList<String> playerNicknames,
                                   ArrayList<Boolean> playerConnected, ArrayList<Integer> playerColours,
                                   ArrayList<Integer> playerHand, ArrayList<Integer> playerAssignedSecretObjectiveCards,
                                   int playerStartingCard, int playerSecretObjective,
                                   int[] playerPoints, ArrayList<int[]> playersResourcesSummary,
                                   ArrayList<ArrayList<int[]>> playerFields, int[] playerResources,
                                   ArrayList<Integer> gameCommonObjectives, ArrayList<Integer> gameCurrentResourceCards,
                                   ArrayList<Integer> gameCurrentGoldCards, int gameResourcesDeckSize,
                                   int gameGoldDeckSize, int matchStatus,
                                   ArrayList<String[]> chatHistory, String currentPlayer,
                                   ArrayList<int[]> newAvailableFieldSpaces, int resourceCardDeckFacingKingdom,
                                   int goldCardDeckFacingKingdom) {
        this.recipientNickname = recipientNickname;
        this.playerNicknames = playerNicknames;
        this.playerConnected = playerConnected;
        this.playerColours = playerColours;
        this.playerHand = playerHand;
        this.playerAssignedSecretObjectiveCards = playerAssignedSecretObjectiveCards;
        this.playerStartingCard = playerStartingCard;
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

    /**
     * This method is used to process the message to the client when the game status is updated,
     * updating the view of the player with the new game status.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
            view.updatePlayerData(playerNicknames, playerConnected,playerColours, playerHand, playerSecretObjective,
                    playerPoints, playerFields, playerResources, gameCommonObjectives, gameCurrentResourceCards,
                    gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus, chatHistory,
                    currentPlayer, newAvailableFieldSpaces, resourceCardDeckFacingKingdom, goldCardDeckFacingKingdom,
                    playersResourcesSummary,playerAssignedSecretObjectiveCards,playerStartingCard );
    }

    /**
     * This method is used to get the nickname of the recipient of the message
     * @return the nickname of the player who will receive the message
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the PlayerGameStatusMessage object.
     */
    @Override
    public String toString(){
        String playerFieldsAsString = playerFields.stream()
                .map(innerList -> (innerList != null && !innerList.isEmpty())
                        ? innerList.stream()
                        .map(Arrays::toString)
                        .collect(Collectors.joining(", ", "[", "]"))
                        : "[]")
                .collect(Collectors.joining(", ", "[", "]"));

        return "PlayerGameStatusMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNicknames=" + playerNicknames +
                ", playerConnected=" + playerConnected.toString() +
                ", playerColours=" + playerColours.toString() +
                ", playerHand=" + playerHand.toString() +
                ", playerAssignedSecretObjectiveCards=" + playerAssignedSecretObjectiveCards.toString() +
                ", playerStartingCard=" + playerStartingCard +
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
                ", chatHistory=" + chatHistory.stream().map(Arrays::toString).toList() +
                ", currentPlayer='" + currentPlayer + '\'' +
                ", newAvailableFieldSpaces=[" + newAvailableFieldSpaces.stream().map(Arrays::toString).collect(Collectors.joining(", ")) +
                "]}";
    }
}
