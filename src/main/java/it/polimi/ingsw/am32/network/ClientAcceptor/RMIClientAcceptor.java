package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientAcceptor extends UnicastRemoteObject implements RMIClientAcceptorInt {

    public RMIClientAcceptor() throws RemoteException {}

    @Override
    public GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message) throws RemoteException,
            GameAlreadyStartedException, FullLobbyException, InvalidPlayerNumberException, DuplicateNicknameException,
            GameNotFoundException, GameAlreadyEndedException, PlayerNotFoundException, PlayerAlreadyConnectedException,
            GameNotYetStartedException {

        RMIServerNode rmiServerNode = new RMIServerNode(node);

        GameController gameController = null;
        try {
            gameController = message.elaborateMessage(rmiServerNode);
        } catch (DuplicateNicknameException | InvalidPlayerNumberException | GameAlreadyStartedException |
                 GameNotYetStartedException | FullLobbyException | GameNotFoundException | GameAlreadyEndedException |
                 PlayerNotFoundException | PlayerAlreadyConnectedException e) {
            rmiServerNode.destroy();
            throw e;
        }

        rmiServerNode.setGameController(gameController);

        return new GameTuple(rmiServerNode, gameController.getId());
    }
}
