package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

import java.util.ArrayList;

public class LobbyPlayerListMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> playerList;

    public LobbyPlayerListMessage(String recipientNickname, ArrayList<String> playerList) {
        this.recipientNickname = recipientNickname;
        this.playerList = playerList;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
