package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

import java.util.ArrayList;

public class ResponsePlayerFieldMessage implements StoCMessage {
    private final String recipientNickname;
    private final String playerNickname;
    private final ArrayList<int[]> playerField;
    private final int[] playerResources;

    public ResponsePlayerFieldMessage(String recipientNickname, String playerNickname, ArrayList<int[]> playerField,
                                      int[] playerResources) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.playerField = playerField;
        this.playerResources = playerResources;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
