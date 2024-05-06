package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    public RMIClientAcceptor() throws RemoteException {}

    @Override
    public GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message) throws RemoteException, GameAlreadyStartedException, FullLobbyException, InvalidPlayerNumberException, DuplicateNicknameException, GameNotFoundException {

        RMIServerNode rmiServerNode = new RMIServerNode(node);

        GameController gameController = null;
        try {
            gameController = message.elaborateMessage(rmiServerNode);
        } catch (DuplicateNicknameException | InvalidPlayerNumberException | GameAlreadyStartedException |
                 FullLobbyException | GameNotFoundException e) {
            //TODO distruggere RMIServerNode
            throw e;
        }

        rmiServerNode.setGameController(gameController);

        return new GameTuple(rmiServerNode, gameController.getId());
    }
}
