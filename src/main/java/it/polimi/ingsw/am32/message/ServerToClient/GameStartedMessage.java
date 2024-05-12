package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class GameStartedMessage implements StoCMessage {
    private final String recipientNickname;

    public GameStartedMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        view.setUpPlayersData();
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
