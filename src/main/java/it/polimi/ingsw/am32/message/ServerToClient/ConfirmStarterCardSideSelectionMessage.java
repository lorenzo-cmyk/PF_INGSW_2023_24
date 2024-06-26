package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is used to manage the message sent by the server to the client to confirm the selection of the starter
 * card side.
 */
public class ConfirmStarterCardSideSelectionMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the confirmation message for the selection of the starter card
     * side.
     */
    private final String recipientNickname;
    /**
     * The id of the starter card assigned to the player.
     */
    private final int startingCardId;
    /**
     * The side of the starter card selected by the player.
     */
    private final boolean side;
    /**
     * The available spaces after the placement of the starter card.
     */
    private final ArrayList<int[]> availableSpaces;
    /**
     * The resources of the player in the field after the placement of the starter card.
     */
    private final int[] playerResources;
    /**
     * The color identifier of the player in this game.
     */
    private final int playerColour;

    /**
     * The constructor of the class: it creates a new confirmStarterCardSideSelectionMessage with the nickname of the
     * recipient who will receive the confirmation message for the selection of the starter card side, the id of the
     * starter card, the side of the starter card, the available spaces and the resources of the player in the field
     * after the placement of the starter card and the color identifier of the player in this game.
     * @param recipientNickname who will receive the confirmation message for the selection of the starter card side.
     * @param startingCardId the id of the starter card assigned to the player.
     * @param side the side of the starter card selected by the player.
     * @param availableSpaces the available spaces after the placement of the starter card.
     * @param playerResources the resources of the player in the field after the placement of the starter card.
     * @param playerColour the color identifier of the player in this game.
     */
    public ConfirmStarterCardSideSelectionMessage(String recipientNickname, int startingCardId,
                                                  boolean side, ArrayList<int[]> availableSpaces,
                                                  int[] playerResources, int playerColour) {
        this.recipientNickname = recipientNickname;
        this.startingCardId = startingCardId;
        this.side = side;
        this.availableSpaces = availableSpaces;
        this.playerResources = playerResources;
        this.playerColour = playerColour;
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the confirmation message for the
     * selection of the starter card side.
     * @return the nickname of the player who will receive the message.
     */
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method is used to process the message to the client when the selection of the starter card side is
     * confirmed,
     * updating the view with the selected side of the starter card, the available spaces and resources
     * of the player in the field after the placement of the starter card.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateConfirmStarterCard(playerColour,startingCardId,side, availableSpaces, playerResources);
    }

    @Override
    public String toString() {
        return "ConfirmStarterCardSideSelectionMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", startingCardId=" + startingCardId +
                ", side=" + side +
                ", availableSpaces=[" + availableSpaces.stream().map(Arrays::toString).collect(Collectors.joining(", ")) +
                "], playerResources=" + Arrays.toString(playerResources) +
                ", playerColour=" + playerColour +
                '}';
    }
}
