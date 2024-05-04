package it.polimi.ingsw.am32.client;

import it.polimi.ingsw.am32.network.ClientNodeInterface;
import it.polimi.ingsw.am32.network.RMIClientAcceptor;
import it.polimi.ingsw.am32.network.RMIClientNode;
import it.polimi.ingsw.am32.network.SKClientNode;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public abstract class UI implements View, EventHandler{
    ClientNodeInterface clientNode;
    public UI() {
        //TODO
    }

    public abstract void InitializeViewElement();

    public abstract void showWelcome();

    public abstract void askSelectGameMode();

    public abstract void askNickname();

    public abstract void askCreateGame();

    public abstract void handleEvent(Event event, int Choice);

    public abstract void askJoinGame();

    public abstract void askReconnectGame();

    public abstract void askPlaceCard();
    public void setSocketClient(String ServerIP, int port) throws IOException {
        SKClientNode clientNode = new SKClientNode(this);
        this.clientNode = clientNode;
        clientNode.startConnection(ServerIP,port);
        //TODO verify if this is correct
    }
    public void setRMIClient(String ServerURL){
        //TODO verify if this is correct
        this.clientNode = new RMIClientNode(this);
        try{
            Registry registry = LocateRegistry.getRegistry(ServerURL);
            String remoteObjectName = "Server-CodexNaturalis";
            RMIClientAcceptor rmiClientAcceptor = (RMIClientAcceptor) registry.lookup(remoteObjectName);
            this.clientNode = new RMIClientNode(this);
            System.out.println("RMI Client Acceptor created");
        }catch (RemoteException | NotBoundException e) {
            //TODO
        }
    }
}


