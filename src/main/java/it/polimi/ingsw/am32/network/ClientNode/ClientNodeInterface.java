package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

/**
 * This {@code ClientNodeInterface} interface is used to identify all ClientNodes through which communication with the
 * server can happen. <br>
 * It's objective is to mask behind the interface the complexity of managing the network part of the client, so that the
 * other classes of the client don't have to know how is the communication between server and client working. <br>
 * It contains all the methods that another class in the client can invoke on any ClientNode. <br>
 * It's important to know that it's necessary and also enough to create one instance of ClientNode per client for the
 * entire duration of the game.
 *
 * @author Matteo
 */
public interface ClientNodeInterface {

    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Send a {@link CtoSMessage} to the server that will process it. <br>
     * A {@link UploadFailureException} exception is thrown if the message could not reach the server. <br>
     * If the exception is thrown, the ClientNode might be in a corrupted/unusable state. The node will automatically
     * and independently reset itself and return to a normal state.
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message couldn't be sent
     */
    void uploadToServer(CtoSMessage message) throws UploadFailureException;

    /**
     * Send a {@link CtoSLobbyMessage} to the server that will process it. <br>
     * A {@link UploadFailureException} exception is thrown if the message could not reach the server. <br>
     * If the exception is thrown, the ClientNode might be in a corrupted/unusable state. The node will automatically
     * and independently reset itself and return to a normal state.
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message couldn't be sent
     */
    void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException;

    /**
     * Inform the ClientNode that the interval for pongs is over. <br>
     * This invocation can lead to the decrease of a pong counter. If the counter reach the minimum than the termination
     * process is started.
     * <br>
     * Invoking this method can lead, depending on the current state of the ClientNode, to verify if the connection to
     * the server is working through the emission of a ping to the server
     */
    void pongTimeOverdue();
}
