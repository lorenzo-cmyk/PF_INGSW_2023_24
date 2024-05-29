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
    private String nickname;
    private int pongCount;

    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;

    private final Timer timer;
    private ClientPingTask clientPingTask;

    private boolean statusIsAlive;
    private boolean reconnectCalled;
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
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        while (true){

            try {
                gameTuple.getNode().uploadCtoS(message);
                logger.info("Message sent. Type: CtoSMessage");
                break;

            } catch (NodeClosedException e) { // TODO gestire eccezioni
                throw new RuntimeException(e);
            } catch (PlayerNotFoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException  {

        while (true) {
            try {
                gameTuple = rmiClientAcceptor.uploadToServer((RMIClientNodeInt) this, message);
                logger.info("Message sent. Type: CtoSLobbyMessage");

                timer.scheduleAtFixedRate(clientPingTask, 0, 5000);

                break;

            } catch (RemoteException e) { // TODO come gestisco queste exception??
                throw new RuntimeException(e);
            } catch (GameAlreadyStartedException e) {
                throw new RuntimeException(e);
            } catch (FullLobbyException e) {
                throw new RuntimeException(e);
            } catch (InvalidPlayerNumberException e) {
                throw new RuntimeException(e);
            } catch (DuplicateNicknameException e) {
                throw new RuntimeException(e);
            } catch (GameNotFoundException e) {
                throw new RuntimeException(e);
            } catch (GameAlreadyEndedException e) {
                throw new RuntimeException(e);
            } catch (PlayerNotFoundException e) {
                throw new RuntimeException(e);
            } catch (PlayerAlreadyConnectedException e) {
                throw new RuntimeException(e);
            } catch (GameNotYetStartedException e) {
                throw new RuntimeException(e);
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
                uploadToServer(new PingMessage(null));
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
