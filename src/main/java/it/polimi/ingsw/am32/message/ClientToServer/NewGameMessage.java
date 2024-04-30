package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.GamesManager;
import it.polimi.ingsw.am32.network.NodeInterface;

public class NewGameMessage implements CtoSLobbyMessage {
    private final String senderNickname;
    private final int playerNum;

    public NewGameMessage(String senderNickname, int playerNum) {
        this.senderNickname = senderNickname;
        this.playerNum = playerNum;
    }

    @Override
    public void elaborateMessage(NodeInterface nodeInterface) {
        // TODO
    }
}
