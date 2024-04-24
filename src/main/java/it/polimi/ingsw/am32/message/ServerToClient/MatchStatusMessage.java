package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

public class MatchStatusMessage implements StoCMessage {
    private final String recipientNickname;
    private final String matchStatus;

    public MatchStatusMessage(String recipientNickname, String matchStatus) {
        this.recipientNickname = recipientNickname;
        this.matchStatus = matchStatus;
    }

    @Override
    public void processMessage(VirtualView virtualView) {
        // TODO
    }
}
