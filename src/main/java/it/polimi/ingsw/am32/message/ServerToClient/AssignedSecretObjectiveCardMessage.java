package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class AssignedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<Integer> assignedSecretObjectiveCards;
    private final ArrayList<Integer> chosenCommonObjectiveCards;
    private final ArrayList<Integer> playerHand;

    public AssignedSecretObjectiveCardMessage(String recipientNickname, ArrayList<Integer> assignedSecretObjectiveCards,
                                              ArrayList<Integer> chosenCommonObjectiveCards, ArrayList<Integer> playerHand) {
        this.recipientNickname = recipientNickname;
        this.assignedSecretObjectiveCards = assignedSecretObjectiveCards;
        this.chosenCommonObjectiveCards = chosenCommonObjectiveCards;
        this.playerHand = playerHand;
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
