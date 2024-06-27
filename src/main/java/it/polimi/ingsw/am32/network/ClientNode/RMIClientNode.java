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

/**
 * RMIClientNode is the class that manage the connection with the server. <br>
 * It implements the {@link ClientNodeInterface} interface and extends {@link UnicastRemoteObject}. <br>
 * Is necessary and enough to instantiate one instance of this class to connect to the server. <br>
 * If the connection were to go down, the instance will automatically try to reset and reconnect. <br>
 *
 * @author Matteo
 */
public class RMIClientNode extends UnicastRemoteObject implements ClientNodeInterface, RMIClientNodeInt {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    /**
     * Constants used in the class
     */
    private static final int PONGMAXCOUNT = 3;
    private static final int THREADSLEEPINTERVAL = 1000;
    private static final int PINGINTERVAL = 5000;
    private static final String REMOTEOBJECTNAME = "Server-CodexNaturalis";

    /**
     * Variables used for service purposes
     */
    private final ExecutorService executorService;
    private final Logger logger;

    /**
     * Variables used to manage the connection with the server
     */
    private final String ip;
    private final int port;
    private int pongCount;
    private final String nickname;

    /**
     * Variables used to communicate with the view
     */
    private final View view;

    /**
     * Variables used to communicate with the server
     */
    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;
    private RMIServerNodeInt serverNode;

    /**
     * Variables used to verify and maintain active the connection with the server
     */
    private final Timer timer;
    private ClientPingTask clientPingTask;
    private ClientPingTask prePingTask;

    /**
     * Variables used to manage the state of the connection and the instance
     */
    private boolean statusIsAlive;
    private boolean nodePreState;
    private boolean reconnectCalled;
    private final Object aliveLock;
    private final Object cToSProcessingLock;
    private final Object sToCProcessingLock;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class. <br>
     * It will try to connect to search for the {@link Registry} and the {@link it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptor}
     * on the server. <br>
     * If any of the two is not found, it will throw a {@link ConnectionSetupFailedException} implying that the connection
     * is not possible. <br>
     * Additionally, it will schedule a {@link ClientPingTask} to periodically check the connection with the server. <br>
     *
     * @param view is the instance of {@link View} the {@link RMIClientNode} will use to process the messages received
     * @param ip is the ip of the server
     * @param port is the port of the server
     * @throws RemoteException thrown if, during the instantiation, there were some problems
     * @throws ConnectionSetupFailedException thrown if the connection is not possible
     */
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

        statusIsAlive = true;
        nodePreState = true;

        timer = new Timer();
        aliveLock = new Object();
        cToSProcessingLock = new Object();
        sToCProcessingLock = new Object();
        executorService = Executors.newCachedThreadPool();
        clientPingTask = new ClientPingTask(this);
        prePingTask = new ClientPingTask(this);
        timer.scheduleAtFixedRate(prePingTask, PINGINTERVAL, PINGINTERVAL);
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Check if the {@code RMIClientNode} is not alive or if the {@code RMIClientNode} is in pre-game state. <br>
     * In both cases, the method will throw a {@link UploadFailureException}. <br>
     * If those conditions are not met, the method will try to send the message to the server. <br>
     * If the server is not reachable or the respective {@link it.polimi.ingsw.am32.network.ServerNode.RMIServerNode} is
     * closed, the method will request a reset and reconnection process and throw a {@link UploadFailureException}. <br>
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message could not be sent
     */
    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        synchronized (cToSProcessingLock) {

            synchronized (aliveLock) {

                if(serverNode == null) {

                    logger.error("Attempt to send CtoSMessage before CtoSLobbyMessage. Upload rejected. Message: {}", message);
                    throw new UploadFailureException();
                }

                if(!statusIsAlive)
                    throw new UploadFailureException();
            }

            try {
                serverNode.uploadCtoS(message);
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

    /**
     * Check if the {@code RMIClientNode} is not alive or if the {@code RMIClientNode} is not in a pre-game state. <br>
     * In both cases, the method will throw a {@link UploadFailureException}. <br>
     * If those conditions are not met, the method will try to send the message to the
     * {@link it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptor} to process the {@link CtoSLobbyMessage}. <br>
     * If the processing is successful, the pre-existing {@link ClientPingTask} will be cancelled and a new one will be
     * scheduled. At the same time the {@code RMIClientNode} will switch from pre-game state to game state. <br>
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message could not be sent
     */
    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {

        synchronized (cToSProcessingLock) {

            synchronized (aliveLock) {
                if(serverNode != null) {

                    logger.error("Attempt to send second CtoSLobbyMessage while RMIServerNode still valid. Upload rejected. Message: {}", message);
                    throw new UploadFailureException();
                }

                if(!statusIsAlive)
                    throw new UploadFailureException();
            }

            try {

                serverNode = rmiClientAcceptor.uploadToServer((RMIClientNodeInt) this, message);

                synchronized (aliveLock){
                    nodePreState = false;
                    prePingTask.cancel();
                }

                logger.info("Message sent. Type: CtoSLobbyMessage. Content: {}", message);

                timer.scheduleAtFixedRate(clientPingTask, PINGINTERVAL, PINGINTERVAL);

            } catch (RemoteException e) {

                logger.info("Failed to send CtoSLobbyMessage to server. RemoteException: {}", e.getMessage());

                requestReconnection();

                throw new UploadFailureException();

            } catch (LobbyMessageException ignore) {}
        }
    }

    /**
     * If the {@code RMIClientNode} is not alive, the method will return immediately. <br>
     * If the {@code RMIClientNode} is alive, the method will invoke {@link #resetTimeCounter()}
     * Finally, after checking if the message is valid, the method will process it by calling the
     * {@link StoCMessage#processMessage(View)} method. <br>
     *
     * @param message a {@link StoCMessage} that the client has to process
     * @throws NodeClosedException if the {@code RMIClientNode} is not alive
     */
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

    /**
     * If the {@code RMIClientNode} is not alive or this method is called for more than one time before the termination
     * of the reset and reconnection process, the method will return immediately. <br>
     * If not, all {@link ClientPingTask} will be cancelled, the timer will be purged and the {@code RMIClientNode} will
     * be set to not alive. <br>
     * Finally, the thread will inform the {@link View} that the node has been disconnected and will task another thread
     * to carry out the reset and reconnection process. <br>
     */
    private void requestReconnection() {

        synchronized (aliveLock) {

            if(!statusIsAlive && reconnectCalled) {
                return;
            }

            reconnectCalled = true;
            statusIsAlive = false;
            nodePreState = false;
            serverNode = null;
            clientPingTask.cancel();
            prePingTask.cancel();
            timer.purge();
            clientPingTask = new ClientPingTask(this);
            prePingTask = new ClientPingTask(this);
            view.nodeDisconnected();

            executorService.submit(this::resetConnection);
        }
    }

    /**
     * tries infinitely to find the {@link Registry} and the {@link RMIClientAcceptorInt} on the server. <br>
     * If the {@code  Registry} and the {@code  RMIClientAcceptorInt} is not found, the method will wait for a certain
     * amount of time before trying again. <br>
     * Once the {@code RMIClientAcceptorInt} is found, the ClientNode will be set to alive and to a pre-game state. <br>
     * Then a new {@link ClientPingTask} will be scheduled to periodically check the connection with the server and set
     * the pong count to its maximum. <br>
     * Finally, the method will inform the {@link View} that the node has been reconnected. <br>
     */
    private void resetConnection () {

        while(true){

            try {
                rmiClientAcceptor = (RMIClientAcceptorInt) registry.lookup(REMOTEOBJECTNAME);
                logger.debug("RMI-Client-Acceptor found on the server. Connection established");
                break;
            } catch (RemoteException | NotBoundException e) {

                logger.debug("RMI-Client-Acceptor not found on the server. searching for registry");

                while (true) {

                    try {
                        registry = LocateRegistry.getRegistry(ip, port);
                        logger.debug("Registry found on the server. Searching for RMIClientAcceptor");
                        break;
                    } catch (RemoteException ignored) {}

                    logger.debug("Registry not found. Trying again later");

                    try {
                        Thread.sleep(THREADSLEEPINTERVAL);
                    } catch (InterruptedException ignore) {}
                }
            }
        }

        synchronized (aliveLock) {
            reconnectCalled = false;
            nodePreState = true;
            statusIsAlive = true;
            pongCount = PONGMAXCOUNT;
            timer.scheduleAtFixedRate(prePingTask, PINGINTERVAL, PINGINTERVAL);
            logger.info("Connection established");
            view.nodeReconnected();
        }
    }

    /**
     * Start the connection with the server. <br>
     */
    public void startConnection() {

        logger.debug("RMIClientNode started");
    }

    /**
     * If the {@code RMIClientNode} is not alive, the method will return immediately. <br>
     * If the {@code RMIClientNode} is alive, the method will check whether the ClientNode is in pre-game state or not. <br>
     * If the ClientNode is in pre-game state, the method will assign to another thread the task of calling the
     * {@link RMIClientAcceptorInt#extraPing()} method. <br>
     * IF the ClientNode is not in pre-game state, the method will decrement the pong count. <br>
     * If the pong count reaches 0, the {@code RMIClientNode} will start the reset and reconnection process. <br>
     * On the other hand, if the pong count is still more than 0 after decrementing, the client will send a
     * {@link PingMessage} to the server.
     */
    @Override
    public void pongTimeOverdue() {

        boolean toReset = false;
        boolean preState = false;

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            if(nodePreState){
                preState = true;
                logger.debug("Pong time overdue node pre-game");

            } else {
                pongCount--;

                logger.debug("Pong time overdue. Pong count: {}", pongCount);

                if (pongCount <= 0) {
                    logger.info("Pong count reached minimum. Trying to check connection");
                    toReset = true;
                }
            }
        }

        if(toReset){
            requestReconnection();
            return;
        }

        if(preState){
            executorService.submit(() -> {
                try {
                    rmiClientAcceptor.extraPing();
                    logger.debug("Calling extraPing method on RMIClientAcceptor");
                } catch (RemoteException e) {
                    requestReconnection();
                }
            });

        } else {
            executorService.submit(() -> {
                try {
                    uploadToServer(new PingMessage(nickname));
                } catch (UploadFailureException ignore) {}
            });
        }
    }

    /**
     * Reset the pong count to the maximum value if the {@code RMIClientNode} is alive.
     */
    public void resetTimeCounter() {

        synchronized (aliveLock) {

            if (!statusIsAlive)
                return;

            pongCount = PONGMAXCOUNT;
        }

        logger.debug("Pong count reset");

    }
}
