package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

import java.util.ArrayList;

public class PlaceCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final int[] playerResources;
    private final int points;
    private final ArrayList<int[]> newAvailableFieldSpaces;

    public PlaceCardConfirmationMessage(String recipientNickname, int[] playerResources, int points, ArrayList<int[]> newAvailableFieldSpaces) {
        this.recipientNickname = recipientNickname;
        this.playerResources= playerResources;
        this.points = points;
        this.newAvailableFieldSpaces = newAvailableFieldSpaces;
    }

    @Override
    public void processMessage() {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
