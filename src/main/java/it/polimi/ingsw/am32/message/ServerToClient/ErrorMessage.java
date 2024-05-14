package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ErrorMessage implements StoCMessage{
    private final String message;
    private final String recipientNickname;

    public ErrorMessage(String message, String recipientNickname) {
        this.message = message;
        this.recipientNickname = recipientNickname;
    }

    @Override

    public void processMessage(View view) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                ", recipientNickname='" + recipientNickname + '\'' +
                '}' + "\n";
    }
}
