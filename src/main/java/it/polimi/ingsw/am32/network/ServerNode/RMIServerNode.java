package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;

public class RMIServerNode implements RMIServerNodeInt, NodeInterface {

    private GameController gameController;
    private int pingCount;
    public RMIServerNode(GameController gameController) {
        this.gameController = gameController;
    private RMIClientNodeInt clientNode;

    public RMIServerNode(RMIClientNodeInt clientNode) {
    }

    public void uploadCtoS(CtoSMessage message) {
        //TODO
    }

    public void uploadToClient(StoCMessage message) {
        //TODO
    }

    public void pingTimeOverdue() {
        //TODO
    }

    public void resetTimeCounter(){
        //TODO
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
