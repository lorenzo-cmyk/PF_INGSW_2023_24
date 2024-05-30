package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.ErrorMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UninitializedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SKServerNode implements Runnable, NodeInterface {

    private final Logger logger;
    private final Configuration config;
    private GameController gameController;
    private final ObjectInputStream inputObtStr;
    private final ObjectOutputStream outputObtStr;
    private final Socket socket;
    private int pingCount;
    private final ServerPingTask notLinkedPingTask;
    private final ServerPingTask serverPingTask;
    private boolean statusIsAlive;
    private boolean destroyCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;

    public SKServerNode(Socket socket) throws UninitializedException {
        this.gameController = null;
        this.socket = socket;
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        ctoSProcessingLock = new Object();
        stoCProcessingLock = new Object();

        this.logger = LogManager.getLogger("SKServerNode");

        try {
            outputObtStr = new ObjectOutputStream(socket.getOutputStream());
            outputObtStr.flush();

        } catch (IOException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("Could not open output stream: {} . Socket Closed", e.getMessage());

            throw new UninitializedException();
        }

        try {
            inputObtStr = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {

            try {
                outputObtStr.close();
            } catch (IOException ignored) {}

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("Could not open input stream: {} . Socket Closed", e.getMessage());

            throw new UninitializedException();
        }

        try {
            socket.setSoTimeout(config.getSocketReadTimeout());
            logger.info("Socket timeout successfully set");

        } catch (SocketException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("InputTimeout Error: {} . Socket Closed", e.getMessage());

            throw new UninitializedException();
        }

        logger.info("SKServerNode ready");

        statusIsAlive = true;
        destroyCalled = false;
        serverPingTask = new ServerPingTask(this);
        notLinkedPingTask = new ServerPingTask(this);
        config.addTimerTask(notLinkedPingTask);
    }

    public void run() {
        logger.debug("SKServerNode thread started");
        try {
            while (true) {

                synchronized (aliveLock) {
                    if (!statusIsAlive)
                        throw new NodeClosedException();
                }

                listenForIncomingMessages();
            }
        } catch (IOException | ClassNotFoundException e) {

            destroy();

            logger.info("Critical ObjectInputStream error while reading: {}" +
                    " . Socket Closed", e.getMessage()); //TODO risolvere meglio gli errori
        } catch (NodeClosedException e) {
            return;
        }
    }

    private void listenForIncomingMessages() throws IOException, ClassNotFoundException, NodeClosedException {
        Object message; // Variable containing the message object received from the client

        try {
            message = inputObtStr.readObject(); // Listen for incoming messages; wait here until a message is received
            logger.debug("Object received from socket stream: {}", message.getClass().getName());
        } catch (SocketTimeoutException e) {
            logger.debug("Socket timeout exception");
            return;
        }

        synchronized (ctoSProcessingLock) {

            synchronized (aliveLock) {
                if (!statusIsAlive)
                    throw new NodeClosedException();
                resetTimeCounter(); // Reset the ping counter (message has been received from client)
            }

            // Check type of message received

            if (message instanceof PingMessage) {
                config.getExecutorService().submit(() -> {
                    try {
                        logger.info("PingMessage received");
                        uploadToClient(new PongMessage(null));
                    } catch (UploadFailureException e) {
                        logger.error("Failed to send PongMessage to client");
                    }
                }); // Create a new thread that sends a PongMessage back to the client
            }
            else if (message instanceof CtoSMessage) {
                if (gameController == null) { // It should never happen that the gameController hasn't yet been assigned when a CtoSMessage is received
                    try {
                        uploadToClient(new ErrorMessage("Error: StoCMessage was sent before StoCLobbyMessage", "PLAYER"));
                        logger.info("StoCMessage received before StoCLobbyMessage. Sending ErrorMessage to client");
                    } catch (UploadFailureException e) {
                        logger.error("StoCMessage received before StoCLobbyMessage. Failed to send ErrorMessage to client");
                    }
                    return; // Early return needed to get back to listening for incoming messages
                }

                // As expected, the gameController has been assigned
                try {
                    ((CtoSMessage) message).elaborateMessage(gameController); // Process the message
                    logger.info("Elaborated CtoSMessage received: {}", message.toString());
                } catch (Exception e) { // Catch any exception thrown by the message elaboration
                    logger.fatal("Error while elaborating CtoSMessage: ", e);
                    throw e;
                }
            }
            else if (message instanceof CtoSLobbyMessage) {
                if (gameController != null) { // It should never happen that the gameController has already been assigned when a CtoSLobbyMessage is received
                    try {
                        uploadToClient(new ErrorMessage("Error: StoCLobbyMessage was sent when the game has already been chosen", "PLAYER"));
                        logger.info("StoCLobbyMessage received when gameController already assigned. Sending ErrorMessage to client");
                    } catch (UploadFailureException e) {
                        logger.error("StoCLobbyMessage received when gameController already assigned. Failed to send ErrorMessage to client");
                    }
                    return; // Early return needed to get back to listening for incoming messages
                }

                // As expected, the gameController has not yet been assigned
                try {
                    gameController = ((CtoSLobbyMessage) message).elaborateMessage(this);

                    notLinkedPingTask.cancel();
                    config.purgeTimer();
                    gameController.getTimer().scheduleAtFixedRate(serverPingTask, 0, Configuration.getInstance().getPingTimeInterval());

                    logger.info("Elaborated CtoSLobbyMessage received: {}", message.toString());

                    // TODO forse è meglio mettere il messaggio di errore nell'exception

                    notLinkedPingTask.cancel();
                    config.purgeTimer();
                    gameController.getTimer().scheduleAtFixedRate(serverPingTask, 0, Configuration.getInstance().getPingTimeInterval());

                    logger.info("Elaborated CtoSLobbyMessage received: {}", message.toString());
                } catch (InvalidPlayerNumberException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: invalid player number", "PLAYER"));
                        logger.info("Invalid player number. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Invalid player number. Failed to send ErrorMessage to client");
                    }
                } catch (GameAlreadyStartedException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Game already started", "PLAYER"));
                        logger.info("Game already started. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Game already started. Failed to send ErrorMessage to client");
                    }
                } catch (FullLobbyException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Lobby already is full", "PLAYER"));
                        logger.info("Lobby is already full. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Lobby is already full. Failed to send ErrorMessage to client");
                    }
                } catch (DuplicateNicknameException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Player nickname already in use", "PLAYER"));
                        logger.info("Player nickname already in use. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Player nickname already in use. Failed to send ErrorMessage to client");
                    }
                } catch (GameNotFoundException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Game not found", "PLAYER"));
                        logger.info("Game not found. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Game not found. Failed to send ErrorMessage to client");
                    }
                } catch (GameAlreadyEndedException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Game already ended", "PLAYER"));
                        logger.info("Game already ended. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Game already ended. Failed to send ErrorMessage to client");
                    }
                } catch (GameNotYetStartedException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Game has not yet started, cannot reconnect now." +
                                " Try accessing the game instead", "PLAYER"));
                        logger.info("Game not yet started. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Game not yet started. Failed to send ErrorMessage to client");
                    }
                } catch (PlayerNotFoundException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Player not found", "PLAYER"));
                        logger.info("Player not found. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Player not found. Failed to send ErrorMessage to client");
                    }
                } catch (PlayerAlreadyConnectedException e) {
                    try {
                        uploadToClient(new ErrorMessage("Error: Player already connected", "PLAYER"));
                        logger.info("Player already connected. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Player already connected. Failed to send ErrorMessage to client");
                    }
                } catch (Exception e) {
                    logger.fatal("Error while elaborating CtoSLobbyMessage: ", e);
                    throw e;
                }
            }
            else { // Unknown message type received
                try {
                    uploadToClient(new ErrorMessage("Error: message type not recognized", "PLAYER"));
                    logger.info("message type not recognized. Sending ErrorMessage to client");
                } catch (UploadFailureException e) {
                    logger.error("message type not recognized. Failed to send ErrorMessage to client");
                }
            }
        }
    }

    public void uploadToClient(StoCMessage msg) throws UploadFailureException {

        synchronized (stoCProcessingLock) {

            synchronized (aliveLock) {
                if (!statusIsAlive)
                    throw new UploadFailureException();
            }

            try {
                outputObtStr.writeObject(msg);
                logger.info("StoCMessage sent to client: {}", msg.toString());

            } catch (IOException e) {
                logger.error("Failed to send StoCMessage to client: {}", e.getMessage());

                destroy();

                throw new UploadFailureException();
            }

            try {
                outputObtStr.flush();
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void pingTimeOverdue() {

        boolean tmpDestroy = false;

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            logger.info("Ping time overdue. Ping count: {}", pingCount);

            if(pingCount <= 0) {
                statusIsAlive = false;
                logger.debug("Ping count reached minimum, starting destruction process");
                tmpDestroy = true;
            }
        }

        if(tmpDestroy)
            destroy();

    }

    @Override
    public void resetTimeCounter() {

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
            logger.info("Ping count reset");
        }
    }

    public void destroy(){

        synchronized (aliveLock) {
            statusIsAlive = false;
            if(destroyCalled)
                return;
            destroyCalled = true;
            serverPingTask.cancel();
            notLinkedPingTask.cancel();
        }

        synchronized (ctoSProcessingLock) {
            synchronized (stoCProcessingLock) {

                try {
                    inputObtStr.close();
                } catch (IOException ignored) {}

                try {
                    outputObtStr.close();
                } catch (IOException ignored) {}

                try {
                    if(!socket.isClosed())
                        socket.close();
                } catch (IOException ignored) {}

                if(gameController != null) {
                    gameController.getTimer().purge();
                    gameController.disconnect(this);
                    logger.info("SKServerNode destroyed and disconnected from GameController");
                    return;
                }

                logger.info("SKServerNode destroyed");
                config.purgeTimer();
            }
        }
    }
}
