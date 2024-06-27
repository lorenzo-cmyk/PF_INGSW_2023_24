package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.controller.exceptions.abstraction.LobbyMessageException;
import it.polimi.ingsw.am32.network.exceptions.ErrorMessageCode;
import it.polimi.ingsw.am32.utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.ErrorMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
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
import java.util.Arrays;

/**
 * Each instance of class {@code SKServerNode} handles a socket connection with a client. <br>
 * If, at some point, the connection were to go down, this instance will begin automatically a termination process. <br>
 * The class implements the {@code Runnable} interface to allow the instance to be run in a separate thread. <br>
 *
 * @author Matteo
 */
public class SKServerNode implements Runnable, ServerNodeInterface {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    /**
     * Variables used for service purposes
     */
    private final Logger logger;
    private final Configuration config;

    /**
     * Variables used to communicate with the {@code GameController}
     */
    private GameController gameController;

    /**
     * Variables used to manage the connection with the server
     */
    private int pingCount;
    private final String nickname;

    /**
     * Variables used to communicate with the client
     */
    private final Socket socket;
    private final ObjectInputStream inputObtStr;
    private final ObjectOutputStream outputObtStr;

    /**
     * Variables used to verify and maintain active the connection with the client
     */
    private ServerPingTask notLinkedPingTask;
    private ServerPingTask serverPingTask;

    /**
     * Variables used to manage the state of the connection and the instance
     */
    private boolean statusIsAlive;
    private boolean destroyCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class. <br>
     * It creates a new instance of {@code SKServerNode} and initializes the input and output streams. <br>
     * It also sets the socket timeout. <br>
     * A temporary ping task is created to check if the client is still alive while the gameController is not yet assigned
     * Throwing an exception implies that this class is not correctly initialized and should be destroyed. This also
     * implies that the socket and its streams are closed.
     *
     * @param socket
     * @throws UninitializedException thrown if, during the instantiation, there were some problems
     */
    public SKServerNode(Socket socket) throws UninitializedException {
        this.gameController = null;
        this.socket = socket;
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        ctoSProcessingLock = new Object();
        stoCProcessingLock = new Object();
        nickname = "Unknown";

        this.logger = LogManager.getLogger(SKServerNode.class);

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
            logger.debug("Socket timeout successfully set");

        } catch (SocketException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("InputTimeout Error: {} . Socket Closed", e.getMessage());

            throw new UninitializedException();
        }

        logger.debug("SKServerNode ready");

        statusIsAlive = true;
        destroyCalled = false;
        serverPingTask = new ServerPingTask(this);
        notLinkedPingTask = new ServerPingTask(this);
        config.addTimerTask(notLinkedPingTask);
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Method that starts the reading of the incoming messages from the input stream of the client. <br>
     * This method will process the incoming messages and will call the appropriate method of the {@code GameController}
     * to handle the message. <br>
     * If at some point, the instance of {@code SKServerNode} is not alive, the thread will terminate. <br>
     * Any problem with the input stream will cause the destruction of the {@code SKServerNode}. <br>
     */
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

            logger.error("Critical ObjectInputStream error while reading.\n" +
                    "Exception message: {}\n" +
                    "Exception local message: {}\n" +
                    "Stack Trace: {}\n",
                    e.getMessage(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));

            //TODO risolvere meglio gli errori
            destroy();

        } catch (NodeClosedException e) {
            return;
        }
    }

    /**
     * Method that listens for incoming messages from the client. <br>
     * The method will wait until a message is received from the client. <br>
     * When a message is received, the method will check the type of the message: <br>
     * - If the message is a {@link PingMessage}, the method will return immediately. <br>
     * - If the message is a {@link CtoSMessage} and a {@link GameController} is already set, the method will call the
     * {@code elaborateMessage} method on the message. <br>
     * - If the message is a {@link CtoSLobbyMessage} and a {@link GameController} is not yet set, the method will call
     * the {@code elaborateMessage} method on the message and set the {@code GameController} to the one returned by the
     * method. <br>
     * - If the message is not recognized, the method will send an {@link ErrorMessage} to the client. <br>
     * If the method encounters any problem while processing the message, it will throw an exception. <br>
     *
     * @throws IOException exception thrown if there are problems with the input stream
     * @throws ClassNotFoundException exception thrown if the class of the object received from the input stream is not found
     * @throws NodeClosedException exception thrown if the instance of {@code SKServerNode} is not alive
     */
    private void listenForIncomingMessages() throws IOException, ClassNotFoundException, NodeClosedException {
        Object message; // Variable containing the message object received from the client

        try {
            message = inputObtStr.readObject(); // Listen for incoming messages; wait here until a message is received
            logger.debug("Object received from socket stream: {}", message.getClass().getName());
        } catch (SocketTimeoutException e) {
            // logger.debug("Socket timeout exception"); Removed because it's too verbose
            return;
        }

        synchronized (ctoSProcessingLock) {

            synchronized (aliveLock) {
                if (!statusIsAlive)
                    throw new NodeClosedException();
                resetTimeCounter(); // Reset the ping counter (message has been received from client)
            }

            // Check type of message received

            if (message instanceof PingMessage) {return;}
            else if (message instanceof CtoSMessage) {
                if (gameController == null) { // It should never happen that the gameController hasn't yet been assigned when a CtoSMessage is received
                    try {
                        uploadToClient(new ErrorMessage(
                                "StoCMessage was sent before StoCLobbyMessage",
                                "PLAYER",
                                ErrorMessageCode.STOCMESSAGE_SENT_BEFORE_STOCLOBBYMESSAGE.getCode()
                        ));
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
                        uploadToClient(new ErrorMessage(
                                "StoCLobbyMessage was sent when the game has already been chosen",
                                "PLAYER",
                                ErrorMessageCode.STOCLOBBYMESSAGE_SENT_BUT_GAMECONTROLLER_ALREADY_PRESENT.getCode()
                        ));
                        logger.info("StoCLobbyMessage received when gameController already assigned. Sending ErrorMessage to client");
                    } catch (UploadFailureException e) {
                        logger.error("StoCLobbyMessage received when gameController already assigned. Failed to send ErrorMessage to client");
                    }
                    return; // Early return needed to get back to listening for incoming messages
                }

                // As expected, the gameController has not yet been assigned
                try {
                    gameController = ((CtoSLobbyMessage) message).elaborateMessage(this);
                    // TODO forse è meglio mettere il messaggio di errore nell'exception
                    // TODO sincronizzare ??
                    notLinkedPingTask.cancel();
                    config.purgeTimer();
                    gameController.getTimer().scheduleAtFixedRate(serverPingTask,
                            Configuration.getInstance().getPingTimeInterval(), Configuration.getInstance().getPingTimeInterval());

                    logger.info("Elaborated CtoSLobbyMessage received: {}", message.toString());
                } catch (LobbyMessageException e) {
                    try {
                        uploadToClient(new ErrorMessage(
                                e.getMessage(),
                                "PLAYER",
                                e.getExceptionType().getValue()
                        ));
                        logger.info("Invalid player number. Sending ErrorMessage to client");
                    } catch (UploadFailureException ex) {
                        logger.error("Invalid player number. Failed to send ErrorMessage to client");
                    }
                } catch (Exception e) {
                    logger.fatal("Error while elaborating CtoSLobbyMessage: ", e);
                    throw e;
                }
            }
            else { // Unknown message type received
                try {
                    uploadToClient(new ErrorMessage(
                            "Message type not recognized",
                            "PLAYER",
                            ErrorMessageCode.MESSAGE_TYPE_NOT_RECOGNIZED.getCode()
                    ));
                    logger.info("Message type not recognized. Sending ErrorMessage to client");
                } catch (UploadFailureException e) {
                    logger.error("Message type not recognized. Failed to send ErrorMessage to client");
                }
            }
        }
    }

    /**
     * Method that sends a {@link StoCMessage} to the client. <br>ù
     * If the client is not alive or the connection had issues and the message couldn't reach the client, a
     * {@link UploadFailureException} will be thrown. <br>
     * If the client wasn't reachable, the method will start the destruction process. <br>
     *
     * @param msg is the message that the server wants to send
     * @throws UploadFailureException if the message couldn't be sent to the client or the ServerNode is not alive
     */
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

    /**
     * Method that should be called at regular intervals. <br>
     * If the {@code SKServerNode} is not alive, the method will return immediately. <br>
     * If the {@code SKServerNode} is alive, the ping count will be decremented. <br>
     * If the ping count reaches 0, the {@code SKServerNode} will start the destruction process. <br>
     * On the other hand, if the ping count is still more than 0 after decrementing, the server will send a
     * {@link PongMessage} to the client. <br>
     */
    @Override
    public void pingTimeOverdue() {

        boolean tmpDestroy = false;

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            logger.debug("Ping time overdue. Ping count: {}", pingCount);

            if(pingCount <= 0) {
                statusIsAlive = false;
                logger.debug("Ping count reached minimum, starting destruction process");
                tmpDestroy = true;
            }
        }

        if(tmpDestroy)
            destroy();
        else
            config.getExecutorService().submit(() -> {
                try {
                    uploadToClient(new PongMessage(nickname));
                } catch (UploadFailureException e) {
                    logger.error("Failed to send PongMessage to client");
                }
            }); // Create a new thread that sends a PongMessage back to the client
    }

    /**
     * Method that resets the ping counter to its maximum value if the {@code SKServerNode} is alive. <br>
     */
    @Override
    public void resetTimeCounter() {

        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
            logger.debug("Ping count reset");
        }
    }

    /**
     * Method that destroys the instance of {@code SKServerNode}. <br>
     * It closes the input and output streams and the socket. <br>
     * If the {@code SKServerNode} is linked to a {@code GameController}, it will disconnect from it. <br>
     * It also cancels all ping tasks and then purges the timer of the {@code Configuration} class and
     * {@code GameController}. <br>
     * The method will also set the status of the {@code SKServerNode} to not alive. <br>
     *
     */
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

                serverPingTask = null;
                notLinkedPingTask = null;

                logger.info("SKServerNode destroyed: Stack Trace" + Arrays.toString(Thread.currentThread().getStackTrace()));
                config.purgeTimer();
            }
        }
    }
}
