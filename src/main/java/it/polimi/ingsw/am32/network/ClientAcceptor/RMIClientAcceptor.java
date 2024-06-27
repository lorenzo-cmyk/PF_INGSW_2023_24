package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ServerToClient.ErrorMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import it.polimi.ingsw.am32.utilities.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The class {@code RMIClientAcceptor} is the implementation of the RMI methods that the interface
 * {@link RMIClientAcceptorInt} expose to the clients. <br>
 * an instance of {@link it.polimi.ingsw.am32.network.ClientNode.RMIClientNode} invoke those methods on the server
 * when the client hasn't joined yet a game. <br>
 *
 * @author Matteo
 */
public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private static final Logger logger = LogManager.getLogger(RMIClientAcceptor.class);


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class.
     */
    public RMIClientAcceptor() throws RemoteException {}


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Invoking this method will result in the server using the content of the message to either create a new game and
     * add the sender as its first player or make him join an already existing one. <br>
     * This process is done by making use of the class {@link it.polimi.ingsw.am32.controller.GamesManager}. <br>
     * If the previous part is successful, i.e. nothing went wrong with joining or creating a game, a new instance of
     * {@link RMIServerNode} will be created and assigned to the client that is invoking this method.
     * <br>
     * If a {@code LobbyMessageException} is thrown, an {@link ErrorMessage} will be sent to the client by invoking
     * the method {@link RMIClientNodeInt#uploadStoC(StoCMessage)} on the {@code RMIClientNodeInt} given as parameter.
     *
     * @param node an instance of {@code RMIClientNodeInt} that exports methods that the server can invoke on the client
     * @param message a {@link CtoSLobbyMessage} that the server has to process
     * @return an instance of {@link RMIServerNodeInt} on which the client can invoke other methods
     * @throws RemoteException thrown if the method couldn't be invoked on the server
     * @throws LobbyMessageException thrown if the client that invoked this method couldn't create or join a game
     */
    @Override
    public RMIServerNodeInt uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message)
            throws RemoteException, LobbyMessageException
    {

        logger.info("Received a CtoSLobbyMessage from a RMI client: {}", message.toString());

        RMIServerNode rmiServerNode = new RMIServerNode(node);

        GameController gameController = null;
        try {
            gameController = message.elaborateMessage(rmiServerNode);
        } catch (LobbyMessageException e) {

            Configuration.getInstance().getExecutorService().submit(() -> {
                try {
                    rmiServerNode.uploadToClient(new ErrorMessage(e.getMessage(), "PLAYER", e.getExceptionType().getValue()));
                } catch (UploadFailureException ignore) {}
                rmiServerNode.destroy();
            });

            logger.error("GameController access failed: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            // We can't lose the visibility of the RuntimeExceptions that are thrown by the elaboration of the message.
            // The Server will not crash because how the thread is managed, but we need to know what happened to fix it in the future.
            // Do not remove this catch block. Remove the throws clause if needed but keep the logger.

            // TODO mandiamo ErrorMessage anche qui?

            rmiServerNode.destroy();
            logger.fatal("GameController access failed due to a critical exception: {}", e.getMessage());
            throw e;
        }

        rmiServerNode.setGameController(gameController);

        logger.info("RMIServerNode successfully created and added to the GameController ID {}", gameController.getId());
        return (RMIServerNodeInt) rmiServerNode;
    }

    /**
     * A simple method used only to verify if the server is reachable by the client.
     *
     * @throws RemoteException thrown if the method couldn't be invoked on the server
     */
    public void extraPing() throws RemoteException {
        logger.trace("extraPing");
    }
}
