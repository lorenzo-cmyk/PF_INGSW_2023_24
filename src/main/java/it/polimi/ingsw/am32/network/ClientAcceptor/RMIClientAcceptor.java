package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    public RMIClientAcceptor() throws RemoteException {}

    @Override
    public GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message) throws RemoteException {
        return null;
    }
}
