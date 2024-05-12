package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.VirtualView;

import java.util.ArrayList;

public class MatchWinnersMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> winners;

    public MatchWinnersMessage(String recipientNickname, ArrayList<String> winners) {
        this.recipientNickname = recipientNickname;
        this.winners = winners;
    }

    @Override
    public void processMessage(View view) {
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
