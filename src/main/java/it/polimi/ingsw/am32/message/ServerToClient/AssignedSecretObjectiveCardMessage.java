package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class AssignedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final int[] assignedCards;

    public AssignedSecretObjectiveCardMessage(String recipientNickname, int[] assignedCards) {
        this.recipientNickname = recipientNickname;
        this.assignedCards = assignedCards;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
