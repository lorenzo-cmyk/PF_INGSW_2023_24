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
import java.util.ArrayList;

public abstract class UI implements View, EventHandler{
    ClientNodeInterface clientNode;
    protected String playerNickname;
    protected int gameID;
    protected int playerNum;
    protected String currentPlayer;
    protected String playerColour;
    protected Event currentEvent;
    protected ObjectiveCardFactory[] commonObjCards;
    protected ArrayList<String> players;
    protected ArrayList<NonObjCardFactory> hand;
    protected ArrayList<ArrayList<String>> FieldView = new ArrayList<>();
    protected NonObjCardFactory starterCard;
    protected static final ArrayList<ObjectiveCardFactory> objectiveCards = ObjectiveCardFactory.setObjectiveCardArray();
    protected static final ArrayList<NonObjCardFactory> nonObjCards = NonObjCardFactory.setNonObjCardArray();
    public UI() {
        this.playerNum = 0;
        this.currentPlayer = null;
        this.playerNickname = null;
        this.gameID = 0;
        this.playerColour = null;
        this.currentEvent = null;
        this.commonObjCards = null;
        this.players = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.FieldView = new ArrayList<>();
    }

    public abstract void showWelcome();

    public abstract void askSelectGameMode();

    public abstract void askNickname();

    public abstract void askCreateGame();

    public abstract void askJoinGame();

    public abstract void askReconnectGame();


    public abstract void chooseConnection();

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

    //-------------------Game start-----------------------
    public abstract void showInitialView();

    public abstract void showHelpInfo();

    //-------------------Game start-----------------------
    public abstract void requestSelectStarterCardSide();


    public abstract void requestSelectSecretObjCard();

    public abstract void requestPlaceCard();
}


