package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    public RMIClientAcceptor() throws RemoteException {}

    @Override
    public GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message) throws RemoteException {

        RMIServerNode rmiServerNode = new RMIServerNode(node);

        GameController gameController = message.elaborateMessage(rmiServerNode);
        //TODO gestione errori se è da fare

        rmiServerNode.setGameController(gameController);

        return new GameTuple(rmiServerNode, gameController.getId());
    }
}
