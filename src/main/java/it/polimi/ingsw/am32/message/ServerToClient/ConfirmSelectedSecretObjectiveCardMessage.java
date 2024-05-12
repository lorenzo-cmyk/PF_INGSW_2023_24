package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ConfirmSelectedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;

    public ConfirmSelectedSecretObjectiveCardMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        view.updateConfirmSelectedSecretCard();
    }
}
