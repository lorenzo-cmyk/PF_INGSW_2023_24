package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Each instance of class {@code RMIServerNode} handle une RMI connection with a client.<br>
 * If, at some point, the connection were to go down, this instance while begin automatically a termination process
 *
 * @author Matteo
 */
public class RMIServerNode extends UnicastRemoteObject implements RMIServerNodeInt, ServerNodeInterface {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    /**
     * Variables used for service purposes
     */
    private final static Logger logger = LogManager.getLogger(RMIServerNode.class);
    private final Configuration config;

    /**
     * Variables used to communicate with the {@code GameController}
     */
    private GameController gameController;

    /**
     * Variables used to manage the connection with the server
     */
    private int pingCount;
    private final String nickname;

    /**
     * Variables used to communicate with the client
     */
    private final RMIClientNodeInt clientNode;

    /**
     * Variables used to verify and maintain active the connection with the client
     */
    private ServerPingTask serverPingTask;

    /**
     * Variables used to manage the state of the connection and the instance
     */
    private boolean statusIsAlive;
    private boolean destroyCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class
     *
     * @param clientNode is the instance of {@code RMIClientNodeInt} the {@code RMIServerNode} will use to send messages
     *                   to che client
     * @throws RemoteException thrown if, during the instantiation, there were some problems
     */
    public RMIServerNode(RMIClientNodeInt clientNode) throws RemoteException {
        this.clientNode = clientNode;
        serverPingTask = new ServerPingTask(this);
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        ctoSProcessingLock = new Object();
        stoCProcessingLock = new Object();
        nickname = "Unknown";

        statusIsAlive = true;
        destroyCalled = false;
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Send a {@link CtoSMessage} to the {@link RMIServerNode}. The server will process it and send back a
     * {@link StoCMessage} according to the situation.<br>
     * If the ServerNode is not alive, a {@link NodeClosedException} will be thrown.
     *
     * @param message a {@link CtoSMessage} that the server has to process
     * @throws RemoteException if the method couldn't be invoked on the server
     * @throws NodeClosedException if the {@code RMIServerNode} is not alive
     */
    public void uploadCtoS(CtoSMessage message) throws RemoteException, NodeClosedException {

        synchronized (ctoSProcessingLock) {

            synchronized (aliveLock) {
                if (!statusIsAlive)
                    throw new NodeClosedException();

                resetTimeCounter();
            }

            if(message instanceof PingMessage) {return;}

            // We can't risk to lose the observability of potential RuntimeExceptions thrown by GameController and Model
            // The server will not crash if such exceptions are thrown, thanks to how the threads are managed, but
            // we need to log them to understand what went wrong and fix it.
            try{
                message.elaborateMessage(gameController);
                logger.info("RMI CtoSMessage received and elaborated successfully: {}", message.toString());
            } catch (Exception e) {
                logger.fatal("An error occurred while processing RMI CtoSMessage:", e);
                throw e;
            }
        }
    }

    /**
     * Send a {@link StoCMessage} to the client. <br>
     * If the client is not alive or the connection had issues and the message couldn't reach the client, a
     * {@link UploadFailureException} will be thrown. <br>
     * If any of the two exceptions is thrown, the {@code RMIServerNode} will start the destruction process.
     *
     * @param message is the message that the server wants to send
     * @throws UploadFailureException if the message couldn't be sent to the client
     */
    public void uploadToClient(StoCMessage message) throws UploadFailureException {

        try {
            clientNode.uploadStoC(message);
            logger.info("StoCMessage sent to client: {}", message.toString());

        } catch (RemoteException e) {

            synchronized (aliveLock) {
                statusIsAlive = false;
            }
            logger.error("Failed to send StoCMessage to client: {}",  e.getMessage());
            config.getExecutorService().submit(this::destroy);
            throw new UploadFailureException();

        } catch (NodeClosedException e) {

            synchronized (aliveLock) {
                statusIsAlive = false;
            }
            logger.info("Failed to send StoCMessage to client because client node is closed");
            config.getExecutorService().submit(this::destroy);
            throw new UploadFailureException();
        }
    }

    /**
     * If the {@code RMIServerNode} is not alive, the method will return immediately. <br>
     * If the {@code RMIServerNode} is alive, the ping count will be decremented. <br>
     * If the ping count reaches 0, the {@code RMIServerNode} will start the destruction process. <br>
     * On the other hand, if the ping count is still more than 0 after decrementing, the server will send a
     * {@link PongMessage} to the client.
     */
    public void pingTimeOverdue() {

        boolean tmpDestroy = false;

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            logger.debug("Ping time overdue. Ping count: {}", pingCount);

            if(pingCount <= 0){
                statusIsAlive = false;
                logger.debug("Ping count reached minimum, starting destruction process");
                tmpDestroy = true;
            }

        }

        if(tmpDestroy)
            destroy();
        else
            config.getExecutorService().submit(() -> {
                try {
                    uploadToClient(new PongMessage(nickname));
                } catch (UploadFailureException e) {
                    logger.error("Failed to send PongMessage to client");
                }
            });

    }

    /**
     * Reset the ping count to the maximum value if the server is alive. <br>
     */
    @Override
    public void resetTimeCounter() {

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
            logger.debug("Ping count reset");
        }
    }

    /**
     * Destroy the {@code RMIServerNode}. <br>
     * The method will stop the {@link ServerPingTask} and will disconnect the {@code RMIServerNode} from the
     * {@code GameController} if already assigned. <br>
     * The method will also unexport the {@code RMIServerNode} from the RMI registry.
     */
    public void destroy() {

        synchronized (aliveLock) {
            statusIsAlive = false;
            if(destroyCalled)
                return;
            destroyCalled = true;
            serverPingTask.cancel();
        }

        synchronized (ctoSProcessingLock) {
            synchronized (stoCProcessingLock) {

                if(gameController != null) {
                    gameController.getTimer().purge();
                    gameController.disconnect(this);
                }

                try {
                    UnicastRemoteObject.unexportObject(this, true);
                } catch (NoSuchObjectException ignored) {}

                serverPingTask = null;

                logger.info("RMIServerNode destroyed");
            }
        }

    }

    /**
     * Set the {@code GameController} associated with this {@code RMIServerNode}. All incoming messages will be
     * processed by this {@code GameController}.
     * Invoking this method will also add a new {@link ServerPingTask} to the {@code GameController} timer for pinging
     * the client
     *
     * @param gameController the instance of {@code GameController}
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;

        gameController.getTimer().scheduleAtFixedRate(serverPingTask,
                Configuration.getInstance().getPingTimeInterval(), Configuration.getInstance().getPingTimeInterval());
    }

}
