package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ConfirmStarterCardSideSelectionMessage implements StoCMessage {
    private final String recipientNickname;
    private final int playerColour;
    //FIXME private final boolean isUp;
    //FIXME private final arraylist<int[]> availablePos;
    //FIXME private final int[] resources;

    public ConfirmStarterCardSideSelectionMessage(String recipientNickname, int playerColour) {
        this.recipientNickname = recipientNickname;
        this.playerColour = playerColour;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        // TODO view.updateConfirmStarterCard(int colour,int cardID, boolean isUp,ArrayList<int[]> availablePos, int[] resources)
    }
}
