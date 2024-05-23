package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class PlaceCardFailedMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public PlaceCardFailedMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    @Override
    public void processMessage(View view) {
        view.handleFailureCase(Event.PLACE_CARD_FAILURE,reason);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PlaceCardFailedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", reason='" + reason + '\'' +
                '}' + "\n";
    }
}
