package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface {@link RMIClientNodeInt} exposes the RMI methods that a
 * {@link it.polimi.ingsw.am32.network.ServerNode.RMIServerNode} or {@link it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptor}
 * instance can invoke on the client. <br>
 * On the client a {@link RMIClientNode} will implement this interface to process the invocation of the method included
 * in this interface.
 *
 * @author Matteo
 */
public interface RMIClientNodeInt extends Remote {

    /**
     * The client will process the message according to its content.
     * if a {@link RemoteException} or {@link NodeClosedException} is thrown the ClientNode will reset automatically the
     * connection
     *
     * @param message a {@link StoCMessage} that the client has to process
     * @throws RemoteException thrown if the method couldn't be invoked on the client
     * @throws NodeClosedException thrown if the ServerNode associated with the ClientNode is terminated server-side
     */
    void uploadStoC(StoCMessage message) throws RemoteException, NodeClosedException;
}
