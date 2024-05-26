package it.polimi.ingsw.am32.message.ServerToClient;

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
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "NewGameConfirmationMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", matchId=" + matchId +
                '}';
    }
}
