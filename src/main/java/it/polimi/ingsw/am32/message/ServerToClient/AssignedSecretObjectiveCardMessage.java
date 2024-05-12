package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class AssignedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<Integer> assignedCards;

    public AssignedSecretObjectiveCardMessage(String recipientNickname, ArrayList<Integer> assignedCards) {
        this.recipientNickname = recipientNickname;
        this.assignedCards = assignedCards;
    }

    @Override
    public void processMessage(View view) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
