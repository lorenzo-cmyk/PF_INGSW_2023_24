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
 * Each instance of class {@code RMIServerNode} handle une RMI connection with a client.
 * If, at some point, the connection were to go down, this instance while begin automatically a termination process
 *
 * @author Matteo
 */
public class RMIServerNode extends UnicastRemoteObject implements RMIServerNodeInt, ServerNodeInterface {

    private final static Logger logger = LogManager.getLogger(RMIServerNode.class);

    private final Configuration config;
    private GameController gameController;
    private int pingCount;
    private final String nickname;
    private final RMIClientNodeInt clientNode;
    private ServerPingTask serverPingTask;
    private boolean statusIsAlive;
    private boolean destroyCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;

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

    @Override
    public void resetTimeCounter() {

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
            logger.debug("Ping count reset");
        }
    }

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
