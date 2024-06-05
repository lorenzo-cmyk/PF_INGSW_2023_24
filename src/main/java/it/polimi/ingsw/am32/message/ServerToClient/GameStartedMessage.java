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
        view.updateCurrentEvent(Event.GAME_START);
        view.handleEvent(Event.GAME_START,null);  // notify the player that the game is started
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "GameStartedMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}';
    }
}
