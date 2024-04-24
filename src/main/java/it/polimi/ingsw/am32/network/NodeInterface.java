package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.message.toClient.StoCMessage;

public interface NodeInterface {
    void uploadToClient(StoCMessage message);

    void pingTimeOverdue();

    void resetTimeCounter();
}