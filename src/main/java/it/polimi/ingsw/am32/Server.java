package it.polimi.ingsw.am32;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.network.RMIClientAcceptor;
import it.polimi.ingsw.am32.network.SKClientAcceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private final Logger logger;

    //---------------------------------------------------------------------------------------------
    // Static Main

    /**
     * When the program is started a new {@link Server} object is crated and started.
     *
     * @param args usual startup arguments
     */
    public static void main(String[] args){
        new Server(args).start();
    }

    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * The {@link Server} class attempt to establish RMI and Socket connection acceptors.
     * <br>
     * Furthermore, an instance of {@link Configuration} is created using the extra parameters given in the constructor
     *
     * @param args are the parameters to be used for the {@code Configuration} class
     */
    public Server(String[] args) {
        Configuration.createInstance(args);
        logger = LogManager.getLogger("Server");
    }

    //---------------------------------------------------------------------------------------------
    // Methods

    public void start() {
        startSocketServer();
        startRMIServer();
        logger.info("Server started");
    }

    /**
     * This method is used to expose to the outside a socket to accept incoming connections.
     * <br>
     * Create an instance of {@link SKClientAcceptor} and submit it to the server {@link java.util.concurrent.ExecutorService}
     */
    private void startSocketServer() {
        Configuration.getInstance().getExecutorService().submit(new SKClientAcceptor());
    }

    /**
     * This method is used to expose to the outside an invokable RMI interface.
     * <br>
     * Create the RMI {@link Registry} and an instance of {@link RMIClientAcceptor}, finally bind the latter to the former
     */
    private void startRMIServer() {
        try {
            System.setProperty("java.rmi.server.hostname", Configuration.getInstance().getServerIp());
            Registry registry = LocateRegistry.createRegistry(Configuration.getInstance().getRmiPort());
            RMIClientAcceptor rmiClientAcceptor = new RMIClientAcceptor();
            registry.bind("Server-CodexNaturalis", rmiClientAcceptor);
            logger.info("RMI Client Acceptor created");

        } catch (RemoteException e) {
            logger.error("RMI communications not available. RMI Client Acceptor creation failed");
        } catch (AlreadyBoundException e) {
            logger.error("RMI communications not available. RMI Client Acceptor binding failed");
        } catch (Exception e) {
            logger.error("RMI communications not available. Not listed error: {}", e.getMessage());
        }
    }
}