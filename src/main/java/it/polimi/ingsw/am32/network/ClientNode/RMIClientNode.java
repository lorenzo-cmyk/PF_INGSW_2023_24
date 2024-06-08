package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptorInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClientNode extends UnicastRemoteObject implements ClientNodeInterface, RMIClientNodeInt {


    private static final int PONGMAXCOUNT = 3;

    private final ExecutorService executorService;
    private final Logger logger;

    private GameTuple gameTuple;
    private final View view;
    private final String serverURL;
    private final int port;
    private int pongCount;
    private String nickname;

    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;

    private final Timer timer;
    private ClientPingTask clientPingTask;

    private boolean statusIsAlive;
    private final Object aliveLock;
    private final Object cToSProcessingLock;
    private final Object sToCProcessingLock;


    public RMIClientNode(View view, String serverURL, int port) throws RemoteException {
        this.view = view;
        this.serverURL = serverURL;
        this.port = port;
        logger = LogManager.getLogger("RMIClientNode");
        timer = new Timer();
        aliveLock = new Object();
        cToSProcessingLock = new Object();
        sToCProcessingLock = new Object();
        pongCount = PONGMAXCOUNT;
        executorService = Executors.newCachedThreadPool();
        clientPingTask = new ClientPingTask(this);
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        synchronized (aliveLock) {
            if(gameTuple == null) {

                // TODO aggiungere di dire errore alla view
                throw new UploadFailureException();
            }
        }

        synchronized (cToSProcessingLock) {

            try {
                gameTuple.getNode().uploadCtoS(message); // TODO il numero di partita in realtà non server
                logger.info("Message sent. Type: CtoSMessage");

            } catch (NodeClosedException e) { // TODO gestire eccezioni
                throw new RuntimeException(e);
            } catch (PlayerNotFoundException | RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {

        synchronized (aliveLock) {
            if(gameTuple != null) {

                // TODO aggiungere di dire errore alla view
                throw new UploadFailureException();
            }
        }

        synchronized (cToSProcessingLock) {

            try {
                // TODO ritorniamo solo l'interfaccia RMI e non il num di partita perchè non serve??
                gameTuple = rmiClientAcceptor.uploadToServer((RMIClientNodeInt) this, message);
                logger.info("Message sent. Type: CtoSLobbyMessage");


                timer.scheduleAtFixedRate(clientPingTask, 0, 5000);

            } catch (RemoteException e) { // TODO come gestisco queste exception??
                throw new RuntimeException(e);
            } catch (GameAlreadyStartedException | FullLobbyException | InvalidPlayerNumberException |
                     DuplicateNicknameException | GameNotFoundException | GameAlreadyEndedException |
                     PlayerNotFoundException | PlayerAlreadyConnectedException | GameNotYetStartedException e) {

                // view.failureCtoSLobby
            }
        }
    }

    @Override
    public void uploadStoC(StoCMessage message) {

        resetTimeCounter();

        if(message == null) {
            logger.info("Null message received");
            return;
        }

        if(message instanceof PongMessage) {
            logger.info("PongMessage received");
            return;
        }

        try {
            logger.info("Message received. Type: StoCMessage. Processing");
            message.processMessage(view);
        } catch (Exception e) {
            logger.fatal("Critical Runtime Exception:\nException Type: {}\nLocal Message: {}\nStackTrace: {}",
                    e.getClass(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    private void resetConnection () {

        gameTuple = null;
        clientPingTask.cancel();
        timer.purge();
        clientPingTask = new ClientPingTask(this);
        // TODO aggiungere alla view
        //TODO view.disconnected();


    }

    public void startConnection() {

        try {
            registry = LocateRegistry.getRegistry(serverURL, port);
            String remoteObjectName = "Server-CodexNaturalis";
            rmiClientAcceptor = (RMIClientAcceptorInt) registry.lookup(remoteObjectName);
            System.out.println("RMI Client Acceptor found");
        } catch (RemoteException | NotBoundException e) {
            //TODO handle exception
        }
    }

    @Override
    public void pongTimeOverdue() {

        boolean toReset = false;

        synchronized (aliveLock){
            if(!statusIsAlive)
                return;

            pongCount--;

            logger.info("Pong time overdue. Pong count: {}", pongCount);

            if(pongCount <= 0) {
                logger.info("Pong count reached minimum. Trying to check connection");
                toReset = true;
            }
        }

        if(toReset){
            // resetConnection(); // TODO risolvere
            return;
        }

        executorService.submit(() -> {
            try {
                uploadToServer(new PingMessage(nickname));
            } catch (UploadFailureException ignore) {}
        });
    }

    public void resetTimeCounter() {

        synchronized (aliveLock) {

            if (!statusIsAlive)
                return;

            pongCount = PONGMAXCOUNT; // TODO modificare se si aggiunge config
        }

        logger.info("Pong count reset");

    }
}
