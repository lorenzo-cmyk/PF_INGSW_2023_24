package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class SelectedStarterCardSideMessage implements CtoSMessage {
    private final String senderNickname;
    private final boolean isUp;

    public SelectedStarterCardSideMessage(String senderNickname, boolean isUp) {
        this.senderNickname = senderNickname;
        this.isUp = isUp;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
