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

public class RMIServerNode extends UnicastRemoteObject implements RMIServerNodeInt, NodeInterface {

    private final static Logger logger = LogManager.getLogger(RMIServerNode.class);

    private final Configuration config;
    private GameController gameController;
    private int pingCount;
    private final RMIClientNodeInt clientNode;
    private ServerPingTask serverPingTask;
    private boolean statusIsAlive;
    private boolean destroyCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;

    public RMIServerNode(RMIClientNodeInt clientNode) throws RemoteException {
        this.clientNode = clientNode;
        serverPingTask = new ServerPingTask(this);
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        ctoSProcessingLock = new Object();
        stoCProcessingLock = new Object();

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

            if(message instanceof PingMessage) {
                config.getExecutorService().submit(() -> {
                    try {
                        logger.debug("PingMessage received");
                        uploadToClient(new PongMessage(null));
                    } catch (UploadFailureException e) {
                        logger.error("Failed to send PongMessage to client");
                    }
                });
                return;
            }

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

            logger.error("Failed to send StoCMessage to client: {}",  e.getMessage());
            synchronized (aliveLock) {
                statusIsAlive = false;
            }
            config.getExecutorService().submit(this::destroy);
            throw new UploadFailureException();

        } catch (NodeClosedException e) {

            synchronized (aliveLock) {
                statusIsAlive = false;
            }
            config.getExecutorService().submit(this::destroy);
            throw new UploadFailureException();
        }
    }

    public void pingTimeOverdue() {

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            logger.debug("Ping time overdue. Ping count: {}", pingCount);

            if(pingCount <= 0){
                statusIsAlive = false;
                logger.debug("Ping count reached minimum, starting destruction process");
            }

        }

        if(!statusIsAlive)
            destroy();

    }

    @Override
    public void resetTimeCounter() {

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
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

    public void setGameController(GameController gameController) {
        this.gameController = gameController;

        gameController.getTimer().scheduleAtFixedRate(serverPingTask, 0, Configuration.getInstance().getPingTimeInterval());
    }

}
