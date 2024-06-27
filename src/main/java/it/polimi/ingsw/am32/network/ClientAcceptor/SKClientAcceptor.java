package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.utilities.Configuration;
import it.polimi.ingsw.am32.network.ServerNode.SKServerNode;
import it.polimi.ingsw.am32.network.exceptions.UninitializedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * The class {@code SKClientAcceptor} manages the new requests of connection to the server by the clients using socket
 * as network protocol. <br>
 * Only one instance of this class is required to be created when the server is run.
 * Because this class is an implementation of the interface {@link Runnable}, is preferable to assign its instance to a
 * new thread to maximise parallelization. <br>
 * Alternatively the method {@link SKClientAcceptor#run()} can be invoked directly. Doing this, however, will result in
 * the caller being stuck, waiting for new incoming connections.
 *
 * @author Matteo
 */
public class SKClientAcceptor implements Runnable {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private static final Logger logger = LogManager.getLogger(SKClientAcceptor.class);


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Invoking this method will lead to the creation of a {@link ServerSocket} that will wait for new incoming
     * connections. <br>
     * When a client attempt to establish a connection with the server, a new instance of {@link SKServerNode} will be
     * created and the connection will be handled by this instance.
     */
    public void run() {

        ExecutorService executorService = Configuration.getInstance().getExecutorService();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(Configuration.getInstance().getSocketPort());
        } catch (IOException e) {
            logger.fatal("Socket communications not available. ServerSocket initialization failed");
            return;
        }

        logger.debug("Server Socket Thread initialized successfully");

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                SKServerNode skServerNode = new SKServerNode(socket);
                logger.info("Accepted connection from: {}. SKServerNode created successfully", socket.getRemoteSocketAddress());
                executorService.submit(skServerNode);

            } catch (IOException e) {
                logger.error("Connection accept failed: {}", e.getMessage());
            } catch (UninitializedException e) {
                logger.error("SKServerNode initialization failed");
            }
        }
    }
}
