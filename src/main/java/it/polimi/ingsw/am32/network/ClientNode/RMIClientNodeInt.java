package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientNodeInt extends Remote {
    void uploadStoC(StoCMessage message) throws RemoteException;
}
