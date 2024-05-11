package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.io.IOException;

public interface ClientNodeInterface {
    void uploadToServer(CtoSMessage message) throws UploadFailureException;
    void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException;
    void receiveFromServer() throws IOException, ClassNotFoundException;
}
