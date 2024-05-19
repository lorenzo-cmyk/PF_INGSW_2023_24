package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerNodeInt extends Remote {
    void uploadCtoS(CtoSMessage message) throws RemoteException, PlayerNotFoundException, NodeClosedException;
}
