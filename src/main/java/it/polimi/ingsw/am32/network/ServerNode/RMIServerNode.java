package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerNode extends UnicastRemoteObject implements RMIServerNodeInt, NodeInterface {

    private GameController gameController;
    private int pingCount;
    private RMIClientNodeInt clientNode;

    public RMIServerNode(RMIClientNodeInt clientNode) {
    public RMIServerNode(RMIClientNodeInt clientNode) throws RemoteException {
    }

    public void uploadCtoS(CtoSMessage message) throws RemoteException {
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
