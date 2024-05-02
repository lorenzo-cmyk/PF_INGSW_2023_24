package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class PlaceCardConfirmationMessage implements StoCMessage {
    private final String recipientNickname;
    private final String playingNickname;
    private final int points;

    public PlaceCardConfirmationMessage(String recipientNickname, String playingNickname, int points) {
        this.recipientNickname = recipientNickname;
        this.playingNickname = playingNickname;
        this.points = points;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}
