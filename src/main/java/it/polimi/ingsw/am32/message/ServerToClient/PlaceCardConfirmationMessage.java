package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Override
    public String toString(){
        return "PlaceCardConfirmationMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                ", placedCard=" + placedCard +
                ", placedCardCoordinates=" + Arrays.toString(placedCardCoordinates) +
                ", placedSide=" + placedSide +
                ", playerPoints=" + playerPoints +
                ", playerResources=" + Arrays.toString(playerResources) +
                ", newAvailableFieldSpaces=[" + newAvailableFieldSpaces.stream().map(Arrays::toString).collect(Collectors.joining(", ")) +
                "]}";
    }
}
