package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is used to manage the message sent from the server to the players notifying them that a player has placed a card.
 */
public class PlaceCardConfirmationMessage implements StoCMessage {
    /**
     * The nickname of the recipient.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player who placed the card successfully.
     */
    private final String playerNickname;
    /**
     * The card placed by the player.
     */
    private final int placedCard;
    /**
     * The coordinates where the card has been placed.
     */
    private final int[] placedCardCoordinates;
    /**
     * The side where the card has been placed.
     */
    private final boolean placedSide;
    /**
     * The points of the player who placed the card.
     */
    private final int playerPoints;
    /**
     * The resources of the player who placed the card.
     */
    private final int[] playerResources;
    /**
     * The new available field spaces.
     */
    private final ArrayList<int[]> newAvailableFieldSpaces;

    /**
     * The constructor of the class.
     * @param recipientNickname The nickname of the recipient.
     * @param playerNickname The nickname of the player who placed the card successfully.
     * @param placedCard The card placed by the player.
     * @param placedCardCoordinates The coordinates where the card has been placed.
     * @param placedSide The side where the card has been placed.
     * @param playerPoints The points of the player who placed the card.
     * @param playerResources The resources of the player who placed the card.
     * @param newAvailableFieldSpaces The new available field spaces.
     */
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

    /**
     * This method is used to update the view of the player who placed the card.
     * @param view The view of the player who placed the card.
     */
    @Override
    public void processMessage(View view) {
        view.updatePlacedCardConfirm(playerNickname, placedCard, placedCardCoordinates, placedSide, playerPoints, playerResources, newAvailableFieldSpaces);
    }

    /**
     * This method is used to get the nickname of the recipient.
     * @return The nickname of the recipient.
     */
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
