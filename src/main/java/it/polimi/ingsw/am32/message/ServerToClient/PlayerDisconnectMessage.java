package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class PlayerDisconnectMessage implements StoCMessage {
    private final String recipientNickname;
    private final String disconnectedNickname;

    public PlayerDisconnectMessage(String recipientNickname, String disconnectedNickname) {
        this.recipientNickname = recipientNickname;
        this.disconnectedNickname = disconnectedNickname;
    }

    @Override
    public void processMessage(View view) {
        view.handleEvent(Event.PLAYER_DISCONNECTED, disconnectedNickname);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "disconnectedNickname: " + disconnectedNickname + "\n";
        return myString;
    }
}
