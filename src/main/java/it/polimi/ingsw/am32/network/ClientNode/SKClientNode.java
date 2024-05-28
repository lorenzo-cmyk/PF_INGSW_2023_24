package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SKClientNode implements ClientNodeInterface, Runnable {

    private final Logger logger;
    private final ExecutorService executorService;
    private final View view;
    private Socket socket;
    private final String ip;
    private final int port;
    private ObjectOutputStream outputObtStr;
    private ObjectInputStream inputObtStr;
    private String nickname;
    private ClientPingTask clientPingTask;
    private final Timer timer;
    private int pongCount;
    private boolean statusIsAlive;
    private boolean reconnectCalled;
    private final Object aliveLock;
    private final Object cToSProcessingLock;
    private final Object sToCProcessingLock;

    public SKClientNode(View view, String ip, int port) {
        this.view = view;
        executorService = Executors.newCachedThreadPool();
        this.ip = ip;
        this.port = port;
        clientPingTask = new ClientPingTask(this);
        timer = new Timer();
        aliveLock = new Object();
        cToSProcessingLock = new Object();
        sToCProcessingLock = new Object();
        statusIsAlive = false;
        pongCount = 3; // todo fare un config??
        logger = LogManager.getLogger("SKClientNode");
        reconnectCalled = false;
    }

    public void run() {

        // Listen for incoming messages
        while(true) {
            try {

                checkConnection();

                listenForIncomingMessages();

            } catch (IOException | ClassNotFoundException | NodeClosedException e) {
                logger.debug("inputObtStr exception: {}. {}. {}",e.getClass(), e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()));
                resetConnection();
            }
        }
    }

    public void listenForIncomingMessages() throws IOException, ClassNotFoundException, NodeClosedException {

        Object message;

        try {
            synchronized (sToCProcessingLock) {
                message = inputObtStr.readObject();
            }
        } catch (SocketTimeoutException e) {
            //logger.debug("Socket timeout exception");
            return;
        }

        // TODO server sync??
        resetTimeCounter();

        if(message instanceof StoCMessage) {

            ((StoCMessage) message).processMessage(view);
            logger.info("Message received. Type: StoCMessage. Processing");

        } else {

            logger.info("Message received. Message type not recognized");
        }
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) {

        while (true) {
            try {
                synchronized (cToSProcessingLock) {
                    outputObtStr.writeObject(message);

                    try {
                        outputObtStr.flush();
                    } catch (IOException ignore) {}
                }
                logger.info("Message sent. Type: CtoSLobbyMessage");
                break;
            } catch (IOException | NullPointerException e) {
                resetConnection();
            }
        }
    }

    @Override
    public void uploadToServer(CtoSMessage message) {

        while (true) {
            try {
                synchronized (cToSProcessingLock) {
                    outputObtStr.writeObject(message);

                    try {
                        outputObtStr.flush();
                    } catch (IOException ignore) {}
                }
                logger.info("Message sent. Type: CtoSMessage");
                break;
            } catch (IOException | NullPointerException e) {
                resetConnection();
            }
        }
    }

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

    private void resetConnection() {

        boolean tmpReconnect;

        synchronized (aliveLock) {

            statusIsAlive = false;

            tmpReconnect =  manageReconnectionRequests();
        }

        if(tmpReconnect) {
            logger.info("Connection status: Down. Reconnecting...");
            connect();
        }
    }

    private boolean manageReconnectionRequests() {

        if(reconnectCalled){

            try {
                aliveLock.wait();
            } catch (InterruptedException ignore) {}

            return false;

        } else {
            reconnectCalled = true;
            clientPingTask.cancel();
            timer.purge();
            clientPingTask = new ClientPingTask(this);
            return true;
        }
    }

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
                        socket.setSoTimeout(100);
                        logger.info("Connection established");

                    } catch (IOException ignore) {

                        logger.info("Failed to connect to {}:{}", ip, port);
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
            pongCount = 3;
            aliveLock.notifyAll();
            timer.scheduleAtFixedRate(clientPingTask, 0, 5000);
        }
    }

    public void startConnection(){

        executorService.submit(this);
        logger.info("SKClientNode started");
    }

    @Override
    public void pongTimeOverdue() {

        synchronized (aliveLock){
            if(!statusIsAlive)
                return;
        }

        pongCount--;

        logger.info("Pong time overdue. Pong count: {}", pongCount);

        if(pongCount <= 0) {
            logger.info("Pong count reached minimum. Trying to check connection");
            resetConnection();
            return;
        }

        executorService.submit(() -> {synchronized (cToSProcessingLock) {
            try {
                outputObtStr.writeObject(new PingMessage(nickname));
            } catch (IOException | NullPointerException ignore) {}
        }});
    }

    public void resetTimeCounter() {

        synchronized (aliveLock) {

            if (!statusIsAlive)
                return;

            pongCount = 3; // TODO modificare se si aggiunge config
        }

        logger.info("Pong count reset");
    }
}