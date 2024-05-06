package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;

public class RequestPlayerFieldMessage implements CtoSMessage {
    private final String senderNickname;
    private final String playerNickname;

    public RequestPlayerFieldMessage(String senderNickname, String playerNickname) {
        this.senderNickname = senderNickname;
        this.playerNickname = playerNickname;
    }

    @Override
    public void elaborateMessage(GameController gameController) throws PlayerNotFoundException {
        gameController.sendPlayerField(senderNickname, playerNickname);
    }
}
