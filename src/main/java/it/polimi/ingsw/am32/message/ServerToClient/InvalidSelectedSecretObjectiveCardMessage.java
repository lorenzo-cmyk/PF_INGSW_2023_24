package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class InvalidSelectedSecretObjectiveCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public InvalidSelectedSecretObjectiveCardMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.SELECT_SECRET_OBJ_CARD_FAILURE,reason);
    }

    @Override
    public String toString() {
        return "InvalidSelectedSecretObjectiveCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
