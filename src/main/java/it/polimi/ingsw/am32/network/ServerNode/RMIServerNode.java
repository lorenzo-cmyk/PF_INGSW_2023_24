package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
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
    private RMIClientNodeInt clientNode;
    private final ServerPingTask serverPingTask;
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

            message.elaborateMessage(gameController);
            logger.info("RMI CtoSMessage received and elaborated successfully: {}", message.toString());
        }
    }

    public void uploadToClient(StoCMessage message) throws UploadFailureException {

        try {
            clientNode.uploadStoC(message);
            logger.info("StoCMessage sent to client: {}", message.toString());

        } catch (RemoteException e) { // TODO gestire errore RMI interfaccia client??
            logger.error("Failed to send StoCMessage to client: {}",  e.getMessage());
            throw new UploadFailureException();
        }
    }

    public void pingTimeOverdue() {

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            if(pingCount <= 0){
                statusIsAlive = false;
                logger.debug("Ping time overdue, set statusIsAlive to false");
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
        }

        serverPingTask.cancel();

        synchronized (ctoSProcessingLock) {
            synchronized (stoCProcessingLock) {

                if(gameController != null) {
                    gameController.getTimer().purge();
                    gameController.disconnect(this);
                }

                try {
                    UnicastRemoteObject.unexportObject(this, true);
                } catch (NoSuchObjectException ignored) {}
            }
        }
        logger.info("RMIServerNode destroyed");
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        // TODO controllare se il timer può fallire a runtime (controllare con gamecontroller)
        gameController.getTimer().scheduleAtFixedRate(serverPingTask, 0, Configuration.getInstance().getPingTimeInterval());
    }

}
