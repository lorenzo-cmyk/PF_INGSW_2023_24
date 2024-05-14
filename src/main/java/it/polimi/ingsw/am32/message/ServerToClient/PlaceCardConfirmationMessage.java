package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class PlaceCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;

    private final String playerNickname;
    private final int placedCard;
    private final int[] placedCardCoordinates;
    private final boolean placedSide;
    private final int playerPoints;
    private final int[] playerResources;
    private final ArrayList<int[]> newAvailableFieldSpaces;

    public PlaceCardConfirmationMessage(String recipientNickname, String playerNickname,
                                        int placedCard, int[] placedCardCoordinates,
                                        boolean placedSide, int playerPoints,
                                        int[] playerResources, ArrayList<int[]> newAvailableFieldSpaces) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.placedCard = placedCard;
        this.placedCardCoordinates = placedCardCoordinates;
        this.placedSide = placedSide;
        this.playerPoints = playerPoints;
        this.playerResources = playerResources;
        this.newAvailableFieldSpaces = newAvailableFieldSpaces;
    }

    @Override
    public void processMessage(View view) {
        view.updatePlacedCardConfirm(playerNickname, placedCard, placedCardCoordinates, placedSide, playerPoints, playerResources, newAvailableFieldSpaces);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "playerNickname: " + playerNickname + "\n";
        myString += "placedCard: " + placedCard + "\n";
        myString += "placedCardCoordinates: " + placedCardCoordinates + "\n";
        myString += "placedSide: " + placedSide + "\n";
        myString += "playerPoints: " + playerPoints + "\n";
        myString += "playerResources: " + playerResources + "\n";
        myString += "newAvailableFieldSpaces: " + newAvailableFieldSpaces + "\n";
        return myString;
    }
}
