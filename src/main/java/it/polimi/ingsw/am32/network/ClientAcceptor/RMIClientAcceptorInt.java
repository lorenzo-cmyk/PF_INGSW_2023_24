package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface {@code RMIClientAcceptorInt} exposes the RMI methods that a
 * {@link it.polimi.ingsw.am32.network.ClientNode.RMIClientNode} instance can invoke on the server when the client hasn't
 * joined yet a game. <br>
 * On the server a {@link RMIClientAcceptor} will implement this interface to process the invocation of the methods
 * included.
 *
 * @author Matteo
 */
public interface RMIClientAcceptorInt extends Remote {

    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * The server will process the message according to its content. This can lead to the client creating a new game or
     * accessing an existing game
     * <br>
     * if a {@link LobbyMessageException} is thrown, the server will notify the client through the {@link RMIClientNodeInt}
     *
     * @param node an instance of {@code RMIClientNodeInt} that exports methods that the server can invoke on the client
     * @param message a {@link CtoSLobbyMessage} that the server has to process
     * @return an instance of {@link RMIServerNodeInt} on which the client can invoke other methods
     * @throws RemoteException thrown if the method couldn't be invoked on the server
     * @throws LobbyMessageException thrown if the client that invoked this method couldn't create or access a game
     */
    RMIServerNodeInt uploadToServer(RMIClientNodeInt node, CtoSLobbyMessage message)
            throws RemoteException, LobbyMessageException;

    /**
     * A simple method used only to verify if the server is reachable.
     *
     * @throws RemoteException thrown if the method couldn't be invoked on the server
     */
    void extraPing() throws RemoteException;
}
