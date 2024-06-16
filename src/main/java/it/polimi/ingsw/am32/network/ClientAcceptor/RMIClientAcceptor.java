package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    private static final Logger logger = LogManager.getLogger(RMIClientAcceptor.class);

    public RMIClientAcceptor() throws RemoteException {}

    // TODO export stuff
    @Override
    public GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message)
            throws RemoteException, LobbyMessageException
    {

        logger.info("Received a CtoSLobbyMessage from a RMI client: {}", message.toString());

        RMIServerNode rmiServerNode = new RMIServerNode(node);

        GameController gameController = null;
        try {
            gameController = message.elaborateMessage(rmiServerNode);
        } catch (LobbyMessageException e) {
            // TODO: Handle the exception, send ErrorMessage to the client instead
            rmiServerNode.destroy();
            logger.error("GameController access failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // We can't lose the visibility of the RuntimeExceptions that are thrown by the elaboration of the message.
            // The Server will not crash because how the thread is managed, but we need to know what happened to fix it in the future.
            // Do not remove this catch block. Remove the throws clause if needed but keep the logger.
            rmiServerNode.destroy();
            logger.fatal("GameController access failed due to a critical exception: {}", e.getMessage());
            throw e;
        }

        rmiServerNode.setGameController(gameController);

        logger.info("RMIServerNode successfully created and added to the GameController ID {}", gameController.getId());
        return new GameTuple(rmiServerNode, gameController.getId());
    }
}
