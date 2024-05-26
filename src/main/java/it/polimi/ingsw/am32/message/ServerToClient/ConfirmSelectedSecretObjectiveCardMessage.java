package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ConfirmSelectedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final int chosenSecretObjectiveCard;

    public ConfirmSelectedSecretObjectiveCardMessage(String recipientNickname, int chosenSecretObjectiveCard) {
        this.recipientNickname = recipientNickname;
        this.chosenSecretObjectiveCard = chosenSecretObjectiveCard;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        view.updateConfirmSelectedSecretCard(chosenSecretObjectiveCard);
    }

    @Override
    public String toString() {
        return "ConfirmSelectedSecretObjectiveCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", chosenSecretObjectiveCard=" + chosenSecretObjectiveCard +
                '}';
    }
}
