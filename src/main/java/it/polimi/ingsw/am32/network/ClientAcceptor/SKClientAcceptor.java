package it.polimi.ingsw.am32.network.ClientAcceptor;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.network.ServerNode.SKServerNode;
import it.polimi.ingsw.am32.network.exceptions.UninitializedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SKClientAcceptor implements Runnable {

    private final Logger logger;

    public SKClientAcceptor() {
        this.logger = LogManager.getLogger("SKClientAcceptor");
    }

    public void run() {

        ExecutorService executorService = Configuration.getInstance().getExecutorService();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(Configuration.getInstance().getSocketPort());
        } catch (IOException e) {
            logger.error("Socket communications not available. ServerSocket initialization failed.");
            return;
        }

        logger.info("Server Socket initialized.");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executorService.submit(new SKServerNode(socket));
                logger.info("Accepted connection from: {}", socket.getRemoteSocketAddress());

            } catch (IOException e) {
                logger.error("Connection accept failed: {}", e.getMessage());

            }
        }
    }
}
