package it.polimi.ingsw.am32.message;

import it.polimi.ingsw.am32.controller.GameController;

public class AccessMessage implements Message{
    private int gameID;
    private String nickname;

    public AccessMessage(int gameID, String nickname) {
        this.gameID = gameID;
        this.nickname = nickname;
    }

    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
