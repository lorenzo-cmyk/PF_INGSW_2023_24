package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import java.rmi.Remote;

public interface RMIServerNodeInt extends Remote{
    void uploadCtoS(CtoSMessage message);
}
