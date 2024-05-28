package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientNode extends UnicastRemoteObject implements ClientNodeInterface, RMIClientNodeInt {
    private final View view;
    public RMIClientNode(View view) throws RemoteException {
        this.view = view;
    }
    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {
        //TODO
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException  {
        //TODO
    }

    @Override
    public void uploadStoC(StoCMessage message) {
        //TODO
    }

    public void run() {
        // TODO
    }

    @Override
    public void pongTimeOverdue() {

    }
}
