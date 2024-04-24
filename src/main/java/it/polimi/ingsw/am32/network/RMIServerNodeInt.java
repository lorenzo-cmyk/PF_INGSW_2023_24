package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.message.CtoSMessage;

public interface RMIServerNodeInt {
    void uploadCtoS(CtoSMessage message);
}
