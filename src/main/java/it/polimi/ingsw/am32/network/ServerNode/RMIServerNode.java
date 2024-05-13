package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerNode extends UnicastRemoteObject implements RMIServerNodeInt, NodeInterface {

    private final Logger logger;
    private final Configuration config;
    private GameController gameController;
    private int pingCount;
    private RMIClientNodeInt clientNode;
    private final PingTask pingTask;
    private boolean statusIsAlive;
    private final Object aliveLock;
    private final Object processingLock;

    public RMIServerNode(RMIClientNodeInt clientNode) throws RemoteException {
        this.clientNode = clientNode;
        pingTask = new PingTask(this);
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        processingLock = new Object();

        logger = LogManager.getLogger("RMIServerNode");

        statusIsAlive = true;
    }

    public void uploadCtoS(CtoSMessage message) throws RemoteException {

        synchronized (aliveLock) {
            if (!statusIsAlive)
                // TODO aggiungere nuova exception
            resetTimeCounter();
        }

        synchronized (processingLock) {
            message.elaborateMessage(gameController);
        }
    }

    public void uploadToClient(StoCMessage message) throws UploadFailureException {

        try {
            clientNode.uploadStoC(message);
            logger.info("StoCMessage sent to client");

        } catch (RemoteException e) {
            logger.error("Failed to send StoCMessage to client: {}",  e.getMessage());
            throw new UploadFailureException();
        }
    }

    public void pingTimeOverdue() {

        boolean tmpDestroy = false;

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            if(pingCount == 0)
                tmpDestroy = true;

            if(tmpDestroy)
                destroy();

            return;
        }
    }

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
        }

        pingTask.cancel();

        // TODO eliminare pingTask da timer in controller

        synchronized (processingLock) {
            gameController.disconnect(this);
            // TODO eliminare pingTask da timer in controller

            // TODO UnicastRemoteObject.unexportObject(this, false);
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        // TODO Aggiungi PingTask al controller
    }

}
