package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class ErrorMessage implements StoCMessage{
    String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public void processMessage() {

    }

    @Override
    public String getRecipientNickname() {
        return null;
    }
}
