package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class LobbyPlayerListMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> playerList;

    public LobbyPlayerListMessage(String recipientNickname, ArrayList<String> playerList) {
        this.recipientNickname = recipientNickname;
        this.playerList = playerList;
    }

    @Override
    public void processMessage(View view) {

        view.updatePlayerList(playerList); // update the player list in the view
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "LobbyPlayerListMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerList=" + playerList.toString() +
                '}';
    }
}
