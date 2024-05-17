package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class AssignedStarterCardMessage implements StoCMessage {
    private final String recipientNickname;
    private final int cardId;

    public AssignedStarterCardMessage(String recipientNickname, int cardId) {
        this.recipientNickname = recipientNickname;
        this.cardId = cardId;
    }

    @Override
    public void processMessage(View view) {
        view.setCurrentEvent(Event.SELECT_STARTER_CARD_SIDE);// set the current event
        view.requestSelectStarterCardSide(cardId); // request the client to select the side of the starter card
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "AssignedStarterCardMessage{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", cardId=" + cardId +
                '}' + "\n";
    }
}
