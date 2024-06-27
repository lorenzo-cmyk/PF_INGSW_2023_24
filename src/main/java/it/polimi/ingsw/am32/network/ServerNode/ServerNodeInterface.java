package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

/**
 * This {@code ServerNodeInterface} interface is used to identify all ServerNodes through which communication, with
 * clients, can happen. <br>
 * It's objective is to mask behind the interface the complexity of managing the network part of the server, so that the
 * other classes of the server don't have to know how is the communication between server and client working. <br>
 * It contains all the methods that another class in the server can invoke on any ServerNode. <br>
 * It's important to know that each instance of ServerNode is assigned to a different client. If, at some point, the
 * communication to a client is impossible, the instance of ServerNode assigned to that client will follow a termination
 * process that will lead to the destruction of the instance itself and every object strictly linked to it. A new
 * instance will have to be created if the client were to reconnect later.
 *
 * @author Matteo
 */
public interface ServerNodeInterface {

    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Send a {@link StoCMessage} to the client that will process it. <br>
     * A {@link UploadFailureException} exception is thrown if the message could not reach the client. <br>
     * If the exception is thrown, the ServerNode will automatically and independently begin the termination process.
     *
     * @param message is the message that the server wants to send
     * @throws UploadFailureException if the message couldn't be sent
     */
    void uploadToClient(StoCMessage message) throws UploadFailureException;

    /**
     * Inform the ServerNode that the interval for pings is over. <br>
     * This invocation can lead to the decrease of a ping counter. If the counter reach the minimum than the termination
     * process is started.
     * <br>
     * Invoking this method can lead, depending on the current state of the ServerNode, to verify if the connection to
     * the client is working through the emission of a ping to the client.
     */
    void pingTimeOverdue();

    /**
     * The invocation of this method will lead to the reset of the ping counter, i.e. bringing it back to maximum,
     * associated with the ServerNode.Ad
     */
    void resetTimeCounter();
}
