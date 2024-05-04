package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.rmi.Remote;
public interface RMIClientNodeInt extends Remote{
    public void uploadStoC(StoCMessage message);
    //TODO

}
