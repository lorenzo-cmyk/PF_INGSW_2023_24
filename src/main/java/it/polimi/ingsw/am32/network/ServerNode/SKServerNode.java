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
import it.polimi.ingsw.am32.network.exceptions.UninitializedException;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SKServerNode implements Runnable, NodeInterface {

    private final Logger logger;
    private final Configuration config;
    private GameController gameController;
    private ObjectInputStream inputObtStr;
    private ObjectOutputStream outputObtStr;
    private final Socket socket;
    private int pingCount;
    private PingTask pingTask;
    private boolean pingTaskAvailable;

    public SKServerNode(Socket socket) throws UninitializedException {
        this.gameController = null;
        pingTaskAvailable = false;
        this.socket = socket;
        config = Configuration.getInstance();
        pingCount = config.getMaxPingCount();

        this.logger = LogManager.getLogger("SkServerNode");

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

        pingTask = new PingTask(this);
    }

    public void run() {
        try {
            while (true) {

                listenForIncomingMessages();
            }
        } catch (IOException | ClassNotFoundException e) {

            try {
                if(!socket.isClosed())
                    socket.close();
            } catch (IOException ignored) {}

            //destroy();

            logger.error("Critical ObjectInputStream error while receiving: {}" +
                    "\nSocket Closed", e.getMessage()); //TODO risolvere meglio gli errori
        }
    }

    private void listenForIncomingMessages() throws IOException, ClassNotFoundException {

        Object message = inputObtStr.readObject();

        resetTimeCounter();

        if(message instanceof CtoSMessage) {

            if (gameController == null) {
                try {
                    uploadToClient(new ErrorMessage("Error: StoCMessage was sent before StoCLobbyMessage"));
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
                    uploadToClient(new ErrorMessage("Error: StoCLobbyMessage was sent when the game has already been chosen"));
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
                    uploadToClient(new ErrorMessage("Error: invalid player number"));
                    logger.info("Invalid player number. Sending ErrorMessage to client");

                } catch (UploadFailureException ex) {
                    logger.info("Invalid player number. Failed to send ErrorMessage to client");
                }

            } catch (GameAlreadyStartedException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Game already started"));
                    logger.info("Game already started. Sending ErrorMessage to client");

                } catch (UploadFailureException ex) {
                    logger.info("Game already started. Failed to send ErrorMessage to client");
                }

            } catch (FullLobbyException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Lobby already is full"));
                    logger.info("Lobby is already full. Sending ErrorMessage to client");

                } catch (UploadFailureException ex) {
                    logger.info("Lobby is already full. Failed to send ErrorMessage to client");
                }

            } catch (DuplicateNicknameException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Player nickname already in use"));
                    logger.info("Player nickname already in use. Sending ErrorMessage to client");

                } catch (UploadFailureException ex) {
                    logger.info("Player nickname already in use. Failed to send ErrorMessage to client");
                }

            } catch (GameNotFoundException e) {
                try {
                    uploadToClient(new ErrorMessage("Error: Game not found"));
                    logger.info("Game not found. Sending ErrorMessage to client");

                } catch (UploadFailureException ex) {
                    logger.info("Game not found. Failed to send ErrorMessage to client");
                }
            }

            pingTaskAvailable = true;
            // TODO aggiungi metodo per aggiungere pingtask al timer di controller

            logger.info("CtoSLobbyMessage received");
            return;
        }

        //TODO valutare se rimandare in dietro un messaggio di errore
        try {
            uploadToClient(new ErrorMessage("Error: message type not recognized"));
            logger.info("message type not recognized. Sending ErrorMessage to client");

        } catch (UploadFailureException e) {
            logger.info("message type not recognized. Failed to send ErrorMessage to client");
        }
    }

    public synchronized void uploadToClient(StoCMessage msg) throws UploadFailureException {

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

    @Override
    public void pingTimeOverdue() {
        synchronized (pingTask) {
            if(!pingTaskAvailable)
                return;

            pingCount--;

            if(pingCount == 0){
                //TODO il metodo seguente è ancora questo??

                destroy();
            }
        }
    }

    @Override
    public void resetTimeCounter() {
        synchronized (pingTask){
            if(!pingTaskAvailable)
                return;

            pingCount = config.getMaxPingCount();
        }
    }

    public void destroy(){

        synchronized (pingTask){
            if(!pingTaskAvailable)
                return;

            pingTask.cancel();
            pingTaskAvailable = false;
        }

        // TODO eliminare pingTask da timer in controller

        // TODO distruggere socket e objectStreams

        if(gameController != null)
            gameController.disconnect(this);


    }
}
