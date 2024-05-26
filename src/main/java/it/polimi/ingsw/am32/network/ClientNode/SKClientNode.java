package it.polimi.ingsw.am32.network.ClientNode;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ClientToServer.PingMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.exceptions.NodeClosedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;

public class SKClientNode implements ClientNodeInterface, Runnable {

    private final Logger logger;
    private View view;
    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream outputObtStr;
    private ObjectInputStream inputObtStr;
    private String nickname;
    private ClientPingTask clientPingTask;
    private Timer timer;
    private int pongCount;
    private boolean statusIsAlive;
    private boolean reconnectCalled;
    private final Object aliveLock;
    private final Object ctoSProcessingLock;
    private final Object stoCProcessingLock;

    public SKClientNode(View view, String ip, int port) {
        this.view = view;
        this.ip = ip;
        this.port = port;
        clientPingTask = new ClientPingTask(this);
        timer = new Timer();
        aliveLock = new Object();
        ctoSProcessingLock = new Object();
        stoCProcessingLock = new Object();
        statusIsAlive = false;
        pongCount = 3; // todo fare un config??
        logger = LogManager.getLogger("SKClientNode");
        reconnectCalled = false;
    }

    public void run() {
        // Listen for incoming messages
        try{
            while(true) {
                listenForIncomingMessages();
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                if (!socket.isClosed()){
                    socketIn.close();
                    socketOut.close();
                    socket.close();
                }
            } catch (IOException ignored) {}
        }
    }

    public void listenForIncomingMessages() throws IOException, ClassNotFoundException {
        StoCMessage message = (StoCMessage) socketIn.readObject();
        System.out.println("Received"+message.getClass().getName()+" from server");
        System.out.println(message.toString());
        message.processMessage(view);
    }

    @Override
    public void uploadToServer(CtoSLobbyMessage message) throws UploadFailureException {
        if (socket.isClosed()) {
            throw new UploadFailureException();
        }

        try {
            outputObtStr.writeObject(message);
        } catch (IOException e) {
            throw new UploadFailureException();
        }
    }

    @Override
    public void uploadToServer(CtoSMessage message) throws UploadFailureException {
        if (socket.isClosed()) {
            throw new UploadFailureException();
        }

        try {
            outputObtStr.writeObject(message);
        } catch (IOException e) {
            throw new UploadFailureException();
        }
    }

    private void reconnect() {

        boolean reconnectionProcess = true;

        while (reconnectionProcess) {

            if (socket != null && !socket.isClosed()) {

                try {
                    inputObtStr.close();
                } catch (IOException ignore) {}

                try {
                    outputObtStr.close();
                } catch (IOException ignore) {}

                try {
                    socket.close();
                } catch (IOException ignore) {}
            }

            try {
                socket = new Socket(ip, port);
                inputObtStr = new ObjectInputStream(socket.getInputStream());
                outputObtStr = new ObjectOutputStream(socket.getOutputStream());

            } catch (IOException ignore) {

                try {
                    wait(100); // TODO parametrizzazione con config?
                } catch (InterruptedException ignore2) {}

                continue;
            }

            reconnectionProcess = false;

        }

        synchronized (aliveLock) {
            statusIsAlive = true;
            reconnectCalled = false;
            aliveLock.notifyAll();
        }

    }

    public void startConnection(){

        // TODO da fare/controllare
        new Thread(this).start();
    }

    @Override
    public void pingTimeOverdue() {
        try {
            uploadToServer(new PingMessage(nickname));

            // diminuzione pongCount
        } catch (UploadFailureException ignored) {}
    }

    public void resetTimeCounter() {

        synchronized (aliveLock) {

            if (!statusIsAlive)
                return;

            pongCount = 3; // TODO modificare se si aggiunge config
        }
    }
}