package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptorInt;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;
import it.polimi.ingsw.am32.network.exceptions.ConnectionSetupFailedException;
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
    private static final int THREADSLEEPINTERVAL = 500;
    private static final int PINGINTERVAL = 5000;
    private static final String REMOTEOBJECTNAME = "Server-CodexNaturalis";


    private final ExecutorService executorService;
    private final Logger logger;

    private RMIServerNodeInt serverNode;
    private final View view;
    private final String ip;
    private final int port;
    private int pongCount;
    private String nickname;

    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;

    private final Timer timer;
    private ClientPingTask clientPingTask;

    private boolean statusIsAlive;
    private boolean reconnectCalled;
    private final Object aliveLock;
    private final Object cToSProcessingLock;
    private final Object sToCProcessingLock;


    public RMIClientNode(View view, String ip, int port) throws RemoteException, ConnectionSetupFailedException {
        this.view = view;
        this.ip = ip;
        this.port = port;
        pongCount = PONGMAXCOUNT;
        nickname = "Unknown";
        reconnectCalled = false;

        logger = LogManager.getLogger(RMIClientNode.class);

        try {

            logger.info("Attempting to connect to the server at {}:{}", ip, port);

            registry = LocateRegistry.getRegistry(ip, port);

            rmiClientAcceptor = (RMIClientAcceptorInt) registry.lookup(REMOTEOBJECTNAME);
            logger.info("RMI-Client-Acceptor found on the server. Connection established");

        } catch (RemoteException | NotBoundException e) {

            logger.info("Connection failed do to wrong parameters or inaccessible server");

            throw new ConnectionSetupFailedException();
        }

        timer = new Timer();
        aliveLock = new Object();
        cToSProcessingLock = new Object();
        sToCProcessingLock = new Object();
        executorService = Executors.newCachedThreadPool();
        clientPingTask = new ClientPingTask(this);
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        synchronized (cToSProcessingLock) {

            synchronized (aliveLock) {

                if(serverNode == null) {

                    logger.error("Attempt to send CtoSMessage before CtoSLobbyMessage. Upload rejected. Message: {}", message);
                    // TODO aggiungere di dire errore alla view
                    throw new UploadFailureException();
                }

                if(!statusIsAlive)
                    throw new UploadFailureException();
            }

            try {
                serverNode.uploadCtoS(message); // TODO il numero di partita in realtà non server
                logger.info("Message sent. Type: CtoSMessage: {}", message);

            } catch (NodeClosedException e) {

                logger.info("Failed to process CtoSMessage because server node is closed");

                requestReconnection();

                throw new UploadFailureException();

            } catch (RemoteException e) {

                logger.info("Failed to send CtoSMessage to server. RemoteException: {}", e.getMessage());

                requestReconnection();

                throw new UploadFailureException();
            }
        }
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {

        synchronized (cToSProcessingLock) {

            synchronized (aliveLock) {
                if(serverNode != null) {

                    logger.error("Attempt to send second CtoSLobbyMessage while RMIServerNode still valid. Upload rejected. Message: {}", message);
                    // TODO aggiungere di dire errore alla view
                    throw new UploadFailureException();
                }

                if(!statusIsAlive)
                    throw new UploadFailureException();
            }

            try {
                // TODO ritorniamo solo l'interfaccia RMI e non il num di partita perchè non serve??
                serverNode = rmiClientAcceptor.uploadToServer((RMIClientNodeInt) this, message).getNode();
                logger.info("Message sent. Type: CtoSLobbyMessage. Content: {}", message);

                timer.scheduleAtFixedRate(clientPingTask, 0, PINGINTERVAL);

            } catch (RemoteException e) {

                logger.info("Failed to send CtoSLobbyMessage to server. RemoteException: {}", e.getMessage());

                requestReconnection();

                throw new UploadFailureException();

            } catch (LobbyMessageException ignore) {}
        }
    }

    @Override
    public void uploadStoC(StoCMessage message) throws NodeClosedException {

        synchronized (sToCProcessingLock) {

            synchronized (aliveLock) {
                if(!statusIsAlive)
                    throw new NodeClosedException();

                resetTimeCounter();
            }

            if (message == null) {
                logger.error("Null message received");
                return;
            }

            if (message instanceof PongMessage) {
                logger.debug("PongMessage received");
                return;
            }

            try {
                logger.info("Message received. Type: StoCMessage. Processing: {}", message);
                message.processMessage(view);

            } catch (Exception e) {
                logger.fatal("Critical Runtime Exception:\nException Type: {}\nLocal Message: {}\nStackTrace: {}",
                        e.getClass(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void requestReconnection() {

        synchronized (aliveLock) {
            if(!statusIsAlive && reconnectCalled) {
                return;
            }

            reconnectCalled = true;
            statusIsAlive = false;
            serverNode = null;
            clientPingTask.cancel();
            timer.purge();
            clientPingTask = new ClientPingTask(this);
            view.nodeDisconnected();

            executorService.execute(this::resetConnection);
        }
    }

    private void resetConnection () {

        boolean notDone = true;

        while(notDone){

            try {
                rmiClientAcceptor = (RMIClientAcceptorInt) registry.lookup(REMOTEOBJECTNAME);
                notDone = false;
            } catch (RemoteException | NotBoundException e) {

                try {
                    Thread.sleep(THREADSLEEPINTERVAL);
                } catch (InterruptedException ignore) {}
            } // TODO mettiamo ricerca registy??
        }

        synchronized (aliveLock) {
            reconnectCalled = false;
            statusIsAlive = true;
            pongCount = PONGMAXCOUNT;
            view.nodeReconnected();
        }
    }

    public void startConnection() {

        logger.debug("RMIClientNode started");
    }

    @Override
    public void pongTimeOverdue() {

        boolean toReset = false;

        synchronized (aliveLock){
            if(!statusIsAlive)
                return;

            pongCount--;

            logger.debug("Pong time overdue. Pong count: {}", pongCount);

            if(pongCount <= 0) {
                logger.info("Pong count reached minimum. Trying to check connection");
                toReset = true;
            }
        }

        if(toReset){
            requestReconnection();
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

        logger.debug("Pong count reset");

    }
}
