package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

public class NewGameConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final int matchId;

    public NewGameConfirmationMessage(String recipientNickname, int matchId) {
        this.recipientNickname = recipientNickname;
        this.matchId = matchId;
    }

    @Override
    public void processMessage(View view){
        view.updateNewGameConfirm(matchId, recipientNickname);
        view.handleEvent(Event.GAME_CREATED,null); // print the message to notify the player that the game is created correctly
        view.updateCurrentEvent(Event.WAITING_FOR_START); // enter the waiting for start event
        view.updateStatus(Event.LOBBY);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "matchId: " + matchId + "\n";
        return myString;
    }
}
