package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

import java.util.ArrayList;

public class ResponseGameStatusMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> playerNicknames;
    private final ArrayList<Integer> playerColours;
    private final ArrayList<Integer> playerHand;
    private final int playerSecretObjective;
    private final int playerPoints;
    private final int playerColour;
    private final ArrayList<int[]> playerField;
    private final int[] playerResources;
    private final ArrayList<Integer> gameCommonObjectives;
    private final ArrayList<Integer> gameCurrentResourceCards;
    private final ArrayList<Integer> gameCurrentGoldCards;
    private final int gameResourcesDeckSize;
    private final int gameGoldDeckSize;
    private final int matchStatus;
    // private final ArrayList<ArrayList<String>> playerChatHistory;
    // TODO

    public ResponseGameStatusMessage(String recipientNickname, ArrayList<String> playerNicknames, ArrayList<Integer> playerColours,
                                     ArrayList<Integer> playerHand, int playerSecretObjective, int playerPoints, int playerColour,
                                     ArrayList<int[]> playerField, int[] playerResources, ArrayList<Integer> gameCommonObjectives,
                                     ArrayList<Integer> gameCurrentResourceCards, ArrayList<Integer> gameCurrentGoldCards,
                                     int gameResourcesDeckSize, int gameGoldDeckSize, int matchStatus) {
        this.recipientNickname = recipientNickname;
        this.playerNicknames = playerNicknames;
        this.playerColours = playerColours;
        this.playerHand = playerHand;
        this.playerSecretObjective = playerSecretObjective;
        this.playerPoints = playerPoints;
        this.playerColour = playerColour;
        this.playerField = playerField;
        this.playerResources = playerResources;
        this.gameCommonObjectives = gameCommonObjectives;
        this.gameCurrentResourceCards = gameCurrentResourceCards;
        this.gameCurrentGoldCards = gameCurrentGoldCards;
        this.gameResourcesDeckSize = gameResourcesDeckSize;
        this.gameGoldDeckSize = gameGoldDeckSize;
        this.matchStatus = matchStatus;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
