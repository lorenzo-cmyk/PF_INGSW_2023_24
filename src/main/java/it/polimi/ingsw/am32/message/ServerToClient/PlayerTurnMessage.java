package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class PlayerTurnMessage implements StoCMessage {
    private final String recipientNickname;
    private final String playerNickname;

    public PlayerTurnMessage(String recipientNickname, String playerNickname) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
    }

    @Override
    public void processMessage(View view) {

        view.updatePlayerTurn(playerNickname);
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "PlayerTurnMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                '}';
    }
}
