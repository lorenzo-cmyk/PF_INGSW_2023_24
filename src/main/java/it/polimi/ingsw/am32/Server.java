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

/**
 * The game server is started through the method {@link Server#main} included in this class.
 * <br>
 * This class main objective is managing the start the basic functions of the server
 * which includes the configs, the network protocols and the thread manager.
 * <p>
 * General description of the server: <br>
 * The server includes the "model" and the "controller" parts of the MVC pattern. <br>
 * At startup, an object {@link Configuration} is initialized containing all working parameters of the server.<br>
 * Afterwards an instance of {@link SKClientAcceptor} and {@link RMIClientAcceptor} are created. <br>
 * The first interaction of the client with the server is handled by one of the two classes written above and later on
 * the client communications will be handled by {@link it.polimi.ingsw.am32.network.SKServerNode SKServerNode} or
 * {@link it.polimi.ingsw.am32.network.RMIServerNode RMIServerNode}. <br>
 * The server and the client communicate through messages which are actual classes. Each message implements one of the
 * 3 interfaces: {@link it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage CtoSMessage},
 * {@link it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage CtoSLobbyMessage},
 * {@link it.polimi.ingsw.am32.message.ServerToClient.StoCMessage StoCMessage}. <br>
 * The server includes also a {@link it.polimi.ingsw.am32.controller.GamesManager GamesManager} (manages the creation
 * of new games and the access to those games by other players), a
 * {@link it.polimi.ingsw.am32.controller.GameController GameController} (manages all actions of the players on the game)
 * and a {@link it.polimi.ingsw.am32.controller.VirtualView VirtualView} (act as bridge in the communications from the
 * controller to the client). <br>
 * Incoming messages are directly handled by {@code SKServeNode} or {@code RMIServerNode}, on the contrary every message
 * used to notify the client following a change in the model are given by the {@code GameController} to a
 * {@code VirtualView} which duty will be to send those messages. <br>
 * </p>
 *
 * @author Matteo
 */
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

    /**
     * This method start the {@link Server} which will try to create the classes {@link SKClientAcceptor} and
     * {@link RMIClientAcceptor} used for handling new connections
     */
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
        Configuration.getInstance().getExecutorService().submit(new SKClientAcceptor(Configuration.getInstance().getSocketPort()));
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
            //FIXME problema se registro esiste gia
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