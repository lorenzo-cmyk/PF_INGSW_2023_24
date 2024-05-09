package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.GameTuple;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientAcceptorInt extends Remote {
    GameTuple uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message) throws RemoteException, GameAlreadyStartedException, FullLobbyException, InvalidPlayerNumberException, DuplicateNicknameException, GameNotFoundException;
}
