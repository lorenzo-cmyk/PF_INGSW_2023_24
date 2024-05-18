package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class PlayerReconnectedMessage implements StoCMessage {
    private final String recipientNickname;
    private final String disconnectedNickname;

    public PlayerReconnectedMessage(String recipientNickname, String disconnectedNickname) {
        this.recipientNickname = recipientNickname;
        this.disconnectedNickname = disconnectedNickname;
    }

    @Override
    public void processMessage(View view) {
        view.updatePlayerReconnected(disconnectedNickname);
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