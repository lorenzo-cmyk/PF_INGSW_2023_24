package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageExceptionEnumeration;

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
        // If the View is in the LOBBY state (or WELCOME state with JOIN_GAME as currentEvent) and the error is a
        // GAME_ALREADY_STARTED_EXCEPTION, we are going to put the View, again, in the WELCOME state with
        // RECONNECT_GAME as currentEvent in order to let the user choose if he wants to reconnect to the game.
        if(errorType == LobbyMessageExceptionEnumeration.GAME_ALREADY_STARTED_EXCEPTION.getValue() &&
                view.getEvent() == Event.JOIN_GAME) {
            view.updateCurrentEvent(Event.RECONNECT_GAME);
        }
        view.handleFailureCase(view.getEvent(), message);
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
