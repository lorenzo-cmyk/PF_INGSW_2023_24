package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
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
    private boolean pingTaskAvailable;

    public RMIServerNode(RMIClientNodeInt clientNode) throws RemoteException {
        this.clientNode = clientNode;
        pingTask = new PingTask(this);
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        pingTaskAvailable = true;

        logger = LogManager.getLogger("RMIServerNode");
    }

    public void uploadCtoS(CtoSMessage message) throws RemoteException {

        resetTimeCounter();
        message.elaborateMessage(gameController);
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
        synchronized (pingTask) {
            if(!pingTaskAvailable)
                return;

            pingCount--;

            if(pingCount == 0){
                //TODO il metodo seguente è ancora questo??

                destroy();
            }
        }
    }

    public void resetTimeCounter() {
        synchronized (pingTask){
            if(!pingTaskAvailable)
                return;

            pingCount = config.getMaxPingCount();
        }
    }

    public void destroy() {

        synchronized (pingTask){
            if(!pingTaskAvailable)
                return;

            pingTask.cancel();
            pingTaskAvailable = false;
        }

        // TODO eliminare pingTask da timer in controller


        //UnicastRemoteObject.unexportObject(this, false);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        // Aggiungi PingTask al controller
    }

}
