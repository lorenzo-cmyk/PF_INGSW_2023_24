package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class AccessGameConfirmMessage implements StoCMessage {
    private final String recipientNickname;

    public AccessGameConfirmMessage(String recipientNickname) {
        this.recipientNickname = recipientNickname;
    }

    @Override
    public void processMessage(View view) {
        view.setCurrentEvent(Event.GAME_JOINED);
        view.handleEvent(Event.GAME_JOINED,null); // notify the player that he joined the game successfully.
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "AccessGameConfirmMessage{" +
                "recipientNickname='" + recipientNickname + '\'' +
                '}' + "\n";
    }
}
