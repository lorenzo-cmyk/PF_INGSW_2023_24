package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNode;
import it.polimi.ingsw.am32.network.ClientNode.RMIClientNodeInt;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface {@link RMIServerNodeInt} exposes the RMI methods that a
 * {@link it.polimi.ingsw.am32.network.ClientNode.RMIClientNode} instance can invoke on the server. <br>
 * On the server a {@link RMIServerNode} will implement this interface to process the invocation of the method included.
 *
 * @author Matteo
 */
public interface RMIServerNodeInt extends Remote {

    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * The server will process the message according to its content. <br>
     * if a {@link RemoteException} or {@link NodeClosedException} is thrown, the ClientNode will reset automatically the
     * connection
     *
     * @param message a {@link CtoSMessage} that the server has to process
     * @throws RemoteException thrown if the method couldn't be invoked on the client
     * @throws NodeClosedException thrown if the ServerNode associated with the ClientNode is terminated server-side
     */
    void uploadCtoS(CtoSMessage message) throws RemoteException, NodeClosedException;
}
