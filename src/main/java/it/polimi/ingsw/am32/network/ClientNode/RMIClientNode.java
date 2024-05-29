package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptorInt;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientNode extends UnicastRemoteObject implements ClientNodeInterface, RMIClientNodeInt {

    private final View view;
    public RMIClientNode(View view) throws RemoteException {
    private final String serverURL;
    private final int port;
    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;


    public RMIClientNode(View view, String serverURL, int port) throws RemoteException {
        this.view = view;
        this.serverURL = serverURL;
        this.port = port;
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
