package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.ClientAcceptor.RMIClientAcceptorInt;
import it.polimi.ingsw.am32.network.GameTuple;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientNode extends UnicastRemoteObject implements ClientNodeInterface, RMIClientNodeInt {

    private final Logger logger;
    private GameTuple gameTuple;
    private final View view;
    private final String serverURL;
    private final int port;
    private Registry registry;
    private RMIClientAcceptorInt rmiClientAcceptor;


    public RMIClientNode(View view, String serverURL, int port) throws RemoteException {
        this.view = view;
        this.serverURL = serverURL;
        this.port = port;
        logger = LogManager.getLogger("RMIClientNode");
    }

    public void run() {
        // TODO
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        while (true){

            try {
                gameTuple.getNode().uploadCtoS(message);
                logger.info("Message sent. Type: CtoSMessage");
                break;

            } catch (NodeClosedException e) { // TODO gestire eccezioni
                throw new RuntimeException(e);
            } catch (PlayerNotFoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException  {

        while (true) {
            try {
                gameTuple = rmiClientAcceptor.uploadToServer((RMIClientNodeInt) this, message);
                logger.info("Message sent. Type: CtoSLobbyMessage");
                break;

            } catch (RemoteException e) { // TODO come gestisco queste exception??
                throw new RuntimeException(e);
            } catch (GameAlreadyStartedException e) {
                throw new RuntimeException(e);
            } catch (FullLobbyException e) {
                throw new RuntimeException(e);
            } catch (InvalidPlayerNumberException e) {
                throw new RuntimeException(e);
            } catch (DuplicateNicknameException e) {
                throw new RuntimeException(e);
            } catch (GameNotFoundException e) {
                throw new RuntimeException(e);
            } catch (GameAlreadyEndedException e) {
                throw new RuntimeException(e);
            } catch (PlayerNotFoundException e) {
                throw new RuntimeException(e);
            } catch (PlayerAlreadyConnectedException e) {
                throw new RuntimeException(e);
            } catch (GameNotYetStartedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void uploadStoC(StoCMessage message) {
        //TODO
    }

    public void startConnection() {

        try {
            registry = LocateRegistry.getRegistry(serverURL, port);
            String remoteObjectName = "Server-CodexNaturalis";
            rmiClientAcceptor = (RMIClientAcceptorInt) registry.lookup(remoteObjectName);
            System.out.println("RMI Client Acceptor found");
        } catch (RemoteException | NotBoundException e) {
            //TODO handle exception
        }


    }

    @Override
    public void pongTimeOverdue() {

    }
}
