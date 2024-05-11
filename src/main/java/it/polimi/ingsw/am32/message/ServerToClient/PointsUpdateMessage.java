package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class PointsUpdateMessage implements StoCMessage {
    private final String recipientNickname;
    private final String playerNickname;
    private final int points;

    public PointsUpdateMessage(String recipientNickname, String playerNickname, int points) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.points = points;
    }

    @Override
    public void processMessage() {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
