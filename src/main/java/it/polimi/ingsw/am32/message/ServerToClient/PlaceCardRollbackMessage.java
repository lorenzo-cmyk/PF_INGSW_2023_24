package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.Arrays;

public class PlaceCardRollbackMessage implements StoCMessage{
    private final String recipientNickname;
    private final String playerNickname;
    private final int removedCard;
    private final int playerPoints;
    private final int[] playerResources;

    public PlaceCardRollbackMessage(String recipientNickname, String playerNickname, int removedCard, int playerPoints, int[] playerResources) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.removedCard = removedCard;
        this.playerPoints = playerPoints;
        this.playerResources = playerResources;
    }

    @Override
    public void processMessage(View view) {
        // TODO: Not yet implemented
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PlaceCardRollbackMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                ", removedCard=" + removedCard +
                ", playerPoints=" + playerPoints +
                ", playerResources=" + Arrays.toString(playerResources) +
                '}' + "\n";
    }
}
