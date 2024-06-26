package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;

/**
 * This {@code ClientNodeInterface} interface is used to identify all ClientNodes through which communication with the
 * server can happen. <br>
 * It contains all the methods that another class in the client can invoke on any ClientNode.
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
     * Inform the ClientNode that the interval for pongs is over.
     * This invocation can lead to the decrease of a pong counter and if needed to the reset of the connection of the
     * ClientNode.
     * <br>
     * Invoking this method can lead, depending on the current state of the ClientNode, to verify if the connection to
     * the server is working through the emission of a ping to the server
     */
    void pongTimeOverdue();
}
