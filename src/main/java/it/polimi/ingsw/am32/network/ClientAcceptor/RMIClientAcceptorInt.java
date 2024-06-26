package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientAcceptorInt extends Remote {

    RMIServerNodeInt uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message)
            throws RemoteException, LobbyMessageException;

    void extraPing() throws RemoteException;
}
