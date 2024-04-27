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

    private final Logger logger;

    public static void main(String[] args){
        new Server(args).start();
    }

    public Server(String[] args) {
        Configuration.createInstance(args);
        logger = LogManager.getLogger("Server");
    }

    public void start() {
        startSocketServer();
        startRMIServer();
        logger.info("Server started");
    }

    private void startSocketServer() {
        Configuration.getInstance().getExecutorService().submit(new SKClientAcceptor());
    }

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