package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

public interface ServerNodeInterface {
    void uploadToClient(StoCMessage message) throws UploadFailureException;
    void pingTimeOverdue();
    void resetTimeCounter();
}
