package it.polimi.ingsw.am32.client;


import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptor;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNode;
import it.polimi.ingsw.am32.network.ClientNode.SKClientNode;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class UI implements View, EventHandler{
    protected ClientNodeInterface clientNode;
    protected String thisPlayerNickname;
    protected int gameID; //save the game ID received from the NewGameConfirmationMessage or AccessGameConfirmMessage.
    protected int playerNum; //number of players connected to the game, if the player is disconnected, the number will
                             // decrease.
    protected ArrayList<String> players; //save and update the players in the game.
    protected String currentPlayer; //save and update the current player by receiving the message from the server.
    protected Event currentEvent; //TODO: not sure if this is useful
    protected ArrayList<Integer> commonObjCards;
    protected int[] secretObjCards;
    protected int secretObjCardSelected;
    protected int starterCard;
    protected ArrayList<Integer> hand;
    protected ArrayList<Integer> currentResourceCards;
    protected ArrayList<Integer>  currentGoldCards;
    protected int resourceDeckSize;
    protected int goldDeckSize;
    protected String Status;
    protected HashMap<String,PlayerPub> publicInfo; //save the colour, nickname, points and resources of the player.
    protected static final ArrayList<ObjectiveCardFactory> objectiveCards = ObjectiveCardFactory.setObjectiveCardArray();
    protected static final ArrayList<NonObjCardFactory> nonObjCards = NonObjCardFactory.setNonObjCardArray();
    //TODO: add the attributes used by chat
    public UI() {
        this.playerNum = 0;
        this.currentPlayer = null;
        this.thisPlayerNickname = null;
        this.gameID = 0;
        this.currentEvent = null;
        this.commonObjCards = null;
        this.players = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.publicInfo = new HashMap<>();
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


    public abstract void updateAfterPlacedCard(String playerNickname, NonObjCardFactory card, int x, int y,
                                               boolean isUp, ArrayList<int[]> availablePos, int[] resources,
                                               int points);
}


