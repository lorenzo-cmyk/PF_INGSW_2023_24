package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class DrawCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<Integer> playerHand;

    public DrawCardConfirmationMessage(String recipientNickname, ArrayList<Integer> playerHand) {
        this.recipientNickname = recipientNickname;
        this.playerHand = playerHand;
    }

    @Override
    public void processMessage(View view) {
        view.updateAfterDrawCard(playerHand);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "DrawCardConfirmationMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerHand=" + playerHand.toString() +
                '}' + "\n";
    }
}
