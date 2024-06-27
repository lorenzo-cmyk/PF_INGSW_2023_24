package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.PongMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.ConnectionSetupFailedException;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SKClientNode is the class that manage the connection with the server. <br>
 * It implements the {@link ClientNodeInterface} interface and the method {@link #run()} of {@link Runnable} interface
 * to handle the messages incoming from the server. <br>
 * Is necessary and enough to instantiate one instance of this class to connect to the server. <br>
 * If the connection were to go down, the instance will automatically try to reset and reconnect. <br>
 *
 * @author Matteo
 */
public class SKClientNode implements ClientNodeInterface, Runnable {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    /**
     * Constants used in the class
     */
    private static final int PONGMAXCOUNT = 3;
    private static final int SOCKETTIMEOUT = 100;
    private static final int PINGINTERVAL = 5000;

    /**
     * Variables used for service purposes
     */
    private final Logger logger;
    private final ExecutorService executorService;

    /**
     * Variables used to communicate with the view
     */
    private final View view;

    /**
     * Variables used to manage the connection with the server
     */
    private final String ip;
    private final int port;
    private final String nickname;
    private int pongCount;

    /**
     * Variables used to communicate with the server
     */
    private Socket socket;
    private ObjectOutputStream outputObtStr;
    private ObjectInputStream inputObtStr;

    /**
     * Variables used to verify and maintain active the connection with the server
     */
    private ClientPingTask clientPingTask;
    private final Timer timer;

    /**
     * Variables used to manage the state of the connection and the instance
     */
    private boolean statusIsAlive;
    private boolean reconnectCalled;
    private final Object aliveLock;
    private final Object cToSProcessingLock;
    private final Object sToCProcessingLock;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class. <br>
     * Create the {@link Socket} connection with the server and the input and output streams. <br>
     * At initialization a ping task is scheduled to run to check and maintain the connection alive. <br>
     * A {@link ConnectionSetupFailedException} is thrown if the connection couldn't be established or during the
     * initialization of the input and output streams there were problems. <br>
     *
     * @param view the view that will be used to process incoming messages
     * @param ip the ip of the server
     * @param port the port of the server
     * @throws ConnectionSetupFailedException thrown if the connection couldn't be established
     */
    public SKClientNode(View view, String ip, int port) throws ConnectionSetupFailedException {
        this.view = view;
        this.ip = ip;
        this.port = port;
        statusIsAlive = true;
        reconnectCalled = false;
        pongCount = PONGMAXCOUNT; // todo fare un config??
        nickname = "Unknown";

        logger = LogManager.getLogger(SKClientNode.class);

        try {

            logger.info("Attempting to connect to the server at {}:{}", ip, port);

            socket = new Socket(ip, port);
            //socket.setSoTimeout(SOCKETTIMEOUT*100); // Time out sulle read sulla input stream in caso qualcosa vada storto
            outputObtStr = new ObjectOutputStream(socket.getOutputStream());
            outputObtStr.flush();
            inputObtStr = new ObjectInputStream(socket.getInputStream());
            socket.setSoTimeout(SOCKETTIMEOUT);

            logger.info("Connection established. Personal connection data: {}", socket.getLocalSocketAddress());

        } catch (IOException e) {

            if (inputObtStr != null) {
                try {
                    inputObtStr.close();
                } catch (IOException ignore) {}
            }

            if (outputObtStr != null) {
                try {
                    outputObtStr.close();
                } catch (IOException ignore) {}
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignore) {}
            }

            //System.out.println("Connection failed do to wrong parameters or inaccessible server");
            logger.info("Connection failed do to wrong parameters or inaccessible server");

            throw new ConnectionSetupFailedException();
        }

        executorService = Executors.newCachedThreadPool();
        clientPingTask = new ClientPingTask(this);
        timer = new Timer();
        aliveLock = new Object();
        cToSProcessingLock = new Object();
        sToCProcessingLock = new Object();
        timer.scheduleAtFixedRate(clientPingTask, PINGINTERVAL, PINGINTERVAL);
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Standard run method of the class. <br>
     * The thread that runs this method will be used to listen for incoming messages. <br>
     * This will be done by remaining in a loop that will check if the connection is still alive and then listen for
     * incoming messages. <br>
     * Listening for incoming messages and processing them will be done by invoking the method
     * {@link #listenForIncomingMessages()}. <br>
     * If the method {@link #listenForIncomingMessages()} throws an exception, the connection will be reset and the
     * reconnection process will start. <br>
     */
    public void run() {

        while(true) {
            try {

                checkConnection();

                listenForIncomingMessages();

            } catch (IOException | ClassNotFoundException | NodeClosedException e) {
                logger.error("inputObtStr exception: {}. {}. {}",e.getClass(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
                resetConnection();
            }
        }
    }

    /**
     * Listen for incoming messages using a {@link ObjectInputStream}. <br>
     * Upon receiving a message, based on the type of the message, there can be two different outcomes: <br>
     * If the message is a {@link PongMessage}, the method will return immediately. <br>
     * If the message is a {@link StoCMessage}, the method will process the message by invoking the method
     * {@link StoCMessage#processMessage(View)}. <br>
     * If the {@link IOException} or {@link ClassNotFoundException} is thrown, the input stream will be corrupted and
     * the connection has to be reset and re-established. <br>
     *
     *
     * @throws IOException thrown if there were problems with the input stream
     * @throws ClassNotFoundException thrown if the class of the object received by the input stream is not recognized
     * @throws NodeClosedException thrown if the SKClientNode is not alive
     */
    public void listenForIncomingMessages() throws IOException, ClassNotFoundException, NodeClosedException {

        Object message;

        try {
            synchronized (sToCProcessingLock) {
                message = inputObtStr.readObject();
            }
        } catch (SocketTimeoutException e) {
            // logger.debug("Socket timeout exception"); Disabled because it's too verbose
            return;
        }

        // TODO server sync??

        resetTimeCounter();


        if(message instanceof PongMessage) {

            logger.debug("PongMessage received");
            return;
        }

        if(message instanceof StoCMessage) {

            try {
                logger.info("Message received. Type: StoCMessage. Processing: {}", message);
                ((StoCMessage) message).processMessage(view);
            } catch (Exception e) {
                logger.fatal("Critical Runtime Exception:\nException Type: {}\nLocal Message: {}\nStackTrace: {}",
                        e.getClass(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
            }

        } else {

            logger.error("Message received. Message type not recognized");
        }
    }

    /**
     * Send a {@link CtoSLobbyMessage} to the server. <br>
     * If a {@link IOException} or {@link NullPointerException} is thrown, the reset and reconnection process will be
     * assigned to a new thread while the caller will receive a {@link UploadFailureException}. <br>
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message couldn't be sent to the server
     */
    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {

        try {
            synchronized (cToSProcessingLock) {
                outputObtStr.writeObject(message);

                try {
                    outputObtStr.flush();
                } catch (IOException ignore) {}
            }

            logger.info("Message sent. Type: CtoSLobbyMessage. Content: {}", message);

        } catch (IOException | NullPointerException e) {

            logger.info("Failed to send CtoSLobbyMessage to server. Exception: {}", e.getMessage());

            executorService.submit(this::resetConnection);

            throw new UploadFailureException();
        }
    }

    /**
     * Send a {@link CtoSMessage} to the server. <br>
     * If a {@link IOException} or {@link NullPointerException} is thrown, the reset and reconnection process will be
     * assigned to a new thread while the caller will receive a {@link UploadFailureException}. <br>
     *
     * @param message is the message that the client wants to send
     * @throws UploadFailureException if the message couldn't be sent to the server
     */
    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {

        try {
            synchronized (cToSProcessingLock) {
                outputObtStr.writeObject(message);

                try {
                    outputObtStr.flush();
                } catch (IOException ignore) {}
            }

            logger.info("Message sent. Type: CtoSMessage: {}", message);

        } catch (IOException | NullPointerException e) {

            logger.info("Failed to send CtoSMessage to server. Exception: {}", e.getMessage());

            executorService.submit(this::resetConnection);

            throw new UploadFailureException();
        }
    }

    /**
     * Check if the connection is still alive and if it is, return immediately. <br>
     * Alternatively, the method {@link #manageReconnectionRequests()} will be invoked. A positive result of this
     * invocation will trigger the reset and reconnection process. <br>
     */
    private void checkConnection() {

        boolean tmpReconnect;

        synchronized (aliveLock) {

            if (statusIsAlive) {
                return;
            }

            tmpReconnect = manageReconnectionRequests();
        }

        if(tmpReconnect) {
            logger.info("Connection status: Down. Reconnecting...");
            connect();
        }
    }

    /**
     * Invoking this method will forcibly set the {@code SKClientNode} status to not alive. <br>
     * The method {@link #manageReconnectionRequests()} is then invoked. A positive result of this invocation will
     * trigger the reset and reconnection process. <br>
     */
    private void resetConnection() {

        boolean tmpReconnect;

        synchronized (aliveLock) {

            statusIsAlive = false;

            tmpReconnect = manageReconnectionRequests();
        }

        if(tmpReconnect) {
            logger.info("Connection status: Down. Reconnecting...");
            connect();
        }
    }

    /**
     * Check if the reset and reconnection process has already been requested. <br>
     * If so, the caller will wait until the process is completed. <br>
     * On the other hand, flag the reset and reconnection process as requested. <br>
     * This will result in cancelling the ping tasks of the {@code SKClientNode} and purging the timer. <br>
     * Additionally the view will be notified that the node has been disconnected. <br>
     *
     * @return true if the caller has to start the reset and reconnection process, false otherwise
     */
    private boolean manageReconnectionRequests() {

        if(reconnectCalled){

            try {
                aliveLock.wait();
            } catch (InterruptedException | IllegalMonitorStateException ignore) {
            }

            return false;

        } else {
            reconnectCalled = true;
            clientPingTask.cancel();
            timer.purge();
            clientPingTask = new ClientPingTask(this);
            view.nodeDisconnected();
            return true;
        }
    }

    /**
     * Reset the current connection by closing the input and output streams and the socket. <br>
     * Try until successful to establish a new connection to server. <br>
     * If the connection is established: <br>
     * - New input and output streams will be created. <br>
     * - The pong count will be reset to the maximum value. <br>
     * - The status of the {@code SKClientNode} will be set to alive and the view will be notified that the node has
     * reconnected. <br>
     * - Finally all waiting threads for the reconnection process will be notified and a new {@link ClientPingTask}
     * will be scheduled. <br>
     */
    private void connect() {

        synchronized (sToCProcessingLock) {
            synchronized (cToSProcessingLock) {

                boolean reconnectionProcess = true;

                while (reconnectionProcess) {

                    if (inputObtStr != null) {
                        try {
                            inputObtStr.close();
                        } catch (IOException ignore) {}
                    }

                    if (outputObtStr != null) {
                        try {
                            outputObtStr.close();
                        } catch (IOException ignore) {}
                    }

                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ignore) {}
                    }

                    try {
                        socket = new Socket(ip, port);
                        outputObtStr = new ObjectOutputStream(socket.getOutputStream());
                        outputObtStr.flush();
                        inputObtStr = new ObjectInputStream(socket.getInputStream());
                        socket.setSoTimeout(SOCKETTIMEOUT);
                        logger.info("Connection established. Personal connection data: {}", socket.getLocalSocketAddress());

                    } catch (IOException ignore) {

                        logger.debug("Failed to connect to {}:{}", ip, port);
                        try {
                            Thread.sleep(100); // TODO parametrizzazione con config?
                        } catch (InterruptedException ignore2) {}

                        continue;
                    }

                    reconnectionProcess = false;

                }
            }
        }

        synchronized (aliveLock) {
            statusIsAlive = true;
            reconnectCalled = false;
            pongCount = PONGMAXCOUNT;
            aliveLock.notifyAll();
            timer.scheduleAtFixedRate(clientPingTask, PINGINTERVAL, PINGINTERVAL);
            view.nodeReconnected();
        }
    }

    /**
     * Start the connection. <br>
     * The method will submit the {@code SKClientNode} to the {@link ExecutorService} to start the connection. <br>
     */
    public void startConnection(){

        executorService.submit(this);
        logger.debug("SKClientNode started");
    }

    /**
     * If the {@code SKClientNode} is not alive, the method will return immediately. <br>
     * If the {@code SKClientNode} is alive, the pong count will be decremented. <br>
     * If the pong count reaches 0, the {@code SKClientNode} will start the reset and reconnection process. <br>
     * On the other hand, if the pong count is still more than 0 after decrementing, the client will send a
     * {@link PingMessage} to the server.
     */
    @Override
    public void pongTimeOverdue() {

        boolean toReset = false;

        synchronized (aliveLock){
            if(!statusIsAlive)
                return;

            pongCount--;

            logger.debug("Pong time overdue. Pong count: {}", pongCount);

            if(pongCount <= 0) {
                logger.info("Pong count reached minimum. Trying to check connection");
                toReset = true;
            }
        }

        if(toReset){
            resetConnection();
            return;
        }

        executorService.submit(() -> {synchronized (cToSProcessingLock) {
            try {
                outputObtStr.writeObject(new PingMessage(nickname));
            } catch (IOException | NullPointerException ignore) {}
        }});
    }

    /**
     * Reset the pong count to the maximum value if the {@code SKClientNode} is alive. <br>
     */
    public void resetTimeCounter() {

        synchronized (aliveLock) {

            if (!statusIsAlive)
                return;

            pongCount = PONGMAXCOUNT; // TODO modificare se si aggiunge config
        }

        logger.debug("Pong count reset");
    }
}