package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.am32.controller.exceptions.GameNotFoundException;
import it.polimi.ingsw.am32.controller.exceptions.InvalidPlayerNumberException;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.ErrorMessage;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
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
    private ObjectInputStream inputObtStr;
    private ObjectOutputStream outputObtStr;
    private final Socket socket;
    private int pingCount;
    private final PingTask pingTask;
    private boolean statusIsAlive;
    private final Object aliveLock;
    private final Object processingLock;

    public SKServerNode(Socket socket) throws UninitializedException {
        this.gameController = null;
        this.socket = socket;
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();
        aliveLock = new Object();
        processingLock = new Object();

        this.logger = LogManager.getLogger("SkServerNode");

        try {
            socket.setSoTimeout(5000); // TODO da mettere in config

        } catch (SocketException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("InputTimeout Error: {}\nSocket Closed", e.getMessage());

            throw new UninitializedException();
        }

        try {
            inputObtStr = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("Could not open input stream: {}\nSocket Closed", e.getMessage());

            throw new UninitializedException();
        }

        try {
            outputObtStr = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {

            inputObtStr = null;

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            logger.error("Could not open output stream: {}\nSocket Closed", e.getMessage());

            throw new UninitializedException();
        }

        statusIsAlive = true;
        pingTask = new PingTask(this);
    }

    public void run() {
        try {
            while (true) {

                synchronized (aliveLock) {
                    if (!statusIsAlive)
                        throw new NodeClosedException();
                }

                listenForIncomingMessages();
            }
        } catch (IOException | ClassNotFoundException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            destroy();

            logger.error("Critical ObjectInputStream error while receiving: {}" +
                    "\nSocket Closed", e.getMessage()); //TODO risolvere meglio gli errori
        } catch (NodeClosedException e) {
            return;
        }
    }

    private void listenForIncomingMessages() throws IOException, ClassNotFoundException, NodeClosedException {

        Object message;

        try {
            message = inputObtStr.readObject();
        } catch (SocketTimeoutException e) {
            return;
        }


        synchronized (aliveLock) {
            if (!statusIsAlive)
                throw new NodeClosedException();
            resetTimeCounter();
        }

        synchronized (processingLock) {

            if (message instanceof CtoSMessage) {

                if (gameController == null) {
                    try {
                        uploadToClient(new ErrorMessage("Error: StoCMessage was sent before StoCLobbyMessage", "PLAYER"));
                        logger.info("StoCMessage received before StoCLobbyMessage. Sending ErrorMessage to client");
                    } catch (UploadFailureException e) {
                        logger.info("StoCMessage received before StoCLobbyMessage. Failed to send ErrorMessage to client");
                    }
                    return;
                }

                ((CtoSMessage) message).elaborateMessage(gameController);

                logger.info("CtoSMessage received");
                return;
            }

            if (message instanceof CtoSLobbyMessage) {

                if (gameController != null) {

                    try {
                        uploadToClient(new ErrorMessage("Error: StoCLobbyMessage was sent when the game has already been chosen", "PLAYER"));
                        logger.info("StoCLobbyMessage received when gameController already assigned. Sending ErrorMessage to client");

                    } catch (UploadFailureException e) {
                        logger.info("StoCLobbyMessage received when gameController already assigned. Failed to send ErrorMessage to client");
                    }

                    return;
                }

                try {
                    gameController = ((CtoSLobbyMessage) message).elaborateMessage(this);

                // TODO forse è meglio mettere il messaggio di errore nell'exception
            } catch (InvalidPlayerNumberException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: invalid player number", "PLAYER"));
                    logger.info("Invalid player number. Sending ErrorMessage to client");

                    } catch (UploadFailureException ex) {
                        logger.info("Invalid player number. Failed to send ErrorMessage to client");
                    }

            } catch (GameAlreadyStartedException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Game already started", "PLAYER"));
                    logger.info("Game already started. Sending ErrorMessage to client");

                    } catch (UploadFailureException ex) {
                        logger.info("Game already started. Failed to send ErrorMessage to client");
                    }

            } catch (FullLobbyException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Lobby already is full", "PLAYER"));
                    logger.info("Lobby is already full. Sending ErrorMessage to client");

                    } catch (UploadFailureException ex) {
                        logger.info("Lobby is already full. Failed to send ErrorMessage to client");
                    }

            } catch (DuplicateNicknameException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Player nickname already in use", "PLAYER"));
                    logger.info("Player nickname already in use. Sending ErrorMessage to client");

                    } catch (UploadFailureException ex) {
                        logger.info("Player nickname already in use. Failed to send ErrorMessage to client");
                    }

            } catch (GameNotFoundException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Game not found", "PLAYER"));
                    logger.info("Game not found. Sending ErrorMessage to client");

                    } catch (UploadFailureException ex) {
                        logger.info("Game not found. Failed to send ErrorMessage to client");
                    }
                }

                // TODO aggiungi metodo per aggiungere pingtask al timer di controller

                logger.info("CtoSLobbyMessage received");
                return;
            }

        }

        //TODO valutare se rimandare in dietro un messaggio di errore
        try {
            uploadToClient(new ErrorMessage("Error: message type not recognized", "PLAYER"));
            logger.info("message type not recognized. Sending ErrorMessage to client");

        } catch (UploadFailureException e) {
            logger.info("message type not recognized. Failed to send ErrorMessage to client");
        }
    }

    public void uploadToClient(StoCMessage msg) throws UploadFailureException {

        // TODO manca sincronizzazione qui
        if(socket.isClosed()) {
            logger.error("Failed to send StoCMessage to client: Socket is closed");
            throw new UploadFailureException();
        }

        try {
            outputObtStr.writeObject(msg);
            logger.info("StoCMessage sent to client");

        } catch (IOException e) {
            logger.error("Failed to send StoCMessage to client: {}",  e.getMessage());
            //TODO risolvere meglio gli errori (distruzione??)
            throw new UploadFailureException();
        }

        try {
            outputObtStr.flush();
        } catch (IOException ignored) {}
    }

    // TODO posso mettere protected
    @Override
    public void pingTimeOverdue() {

        boolean tmpDestroy = false;

        synchronized (aliveLock) {

            if(!statusIsAlive)
                return;

            pingCount--;

            if(pingCount == 0)
                tmpDestroy = true;
        }

        if(tmpDestroy)
            destroy();

        return;
    }

    // TODO posso mettere private
    @Override
    public void resetTimeCounter() {
        synchronized (aliveLock){

            if(!statusIsAlive)
                return;

            pingCount = config.getMaxPingCount();
        }
    }

    public void destroy(){

        synchronized (aliveLock) {
            statusIsAlive = false;
        }

        pingTask.cancel();

        synchronized (processingLock) {

            // TODO distruggere socket e objectStreams

            if(gameController != null) {
                gameController.disconnect(this);
                // TODO eliminare pingTask da timer in controller
            }
        }
    }
}
