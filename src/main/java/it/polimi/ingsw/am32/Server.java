package it.polimi.ingsw.am32;

import it.polimi.ingsw.am32.Utilities.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        System.out.println("Server is starting");
    }

    private void startRMIServer() {
        System.out.println("Server is starting");
    }
}

