package it.polimi.ingsw.am32.message.ServerToClient;

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

    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "reason: " + reason + "\n";
        return myString;
    }
}
