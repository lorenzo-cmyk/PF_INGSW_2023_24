package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class ConfirmStarterCardSideSelectionMessage implements StoCMessage {
    private final String recipientNickname;
    private final int startingCardId;
    private final boolean side;
    private final ArrayList<int[]> availableSpaces;
    private final int[] playerResources;
    private final int playerColour;

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

    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        // TODO
    }
}
