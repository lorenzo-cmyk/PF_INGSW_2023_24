package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.controller.GamesManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInterface {

    public RMIClientAcceptor() throws RemoteException {}

    public GameTuple createGame(RMIClientNodeInt node, int id, String creatorName) {
        //TODO
        return null;
    }
    public RMIServerNodeInt accessGame(RMIClientNodeInt node, int id, String playerName) {
        //TODO
        return null;
    }
    public RMIServerNodeInt ReconFromDeath(RMIClientNodeInt node, int id, String playerName) {
        //TODO
        return null;
    }

}
