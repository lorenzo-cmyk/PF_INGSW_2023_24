package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.net.Socket;

public interface ClientNodeInterface {
    void uploadToServer(CtoSMessage message);

    void receiveFromServer();

    void uploadToServer(CtoSLobbyMessage message);

}
