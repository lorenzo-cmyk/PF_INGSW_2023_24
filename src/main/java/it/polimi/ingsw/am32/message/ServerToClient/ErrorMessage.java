package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class ErrorMessage implements StoCMessage{
    private final String message;
    private final String recipientNickname;
    private final int errorType;

    public ErrorMessage(String message, String recipientNickname, int errorType) {
        this.message = message;
        this.recipientNickname = recipientNickname;
        this.errorType = errorType;
    }

    @Override
    public void processMessage(View view) {
        view.handleFailureCase(view.getEvent(), message, errorType);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "ErrorMessage:{" +
                "message='" + message + '\'' +
                ", recipientNickname='" + recipientNickname + '\'' +
                ", errorType=" + errorType +
                '}';
    }
}
