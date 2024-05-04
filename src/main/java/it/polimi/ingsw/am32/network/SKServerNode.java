package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SKServerNode implements Runnable, NodeInterface {
    private Socket socket;
    private final GameController gameController;
    private ObjectInputStream inputObtStr;
    private ObjectOutputStream outputObtStr;
    private int pingCount;

    public SKServerNode(Socket socket) {
        this.socket = socket;
        this.gameController = null;
    }

    public void run() {
        try {
            inputObtStr = new ObjectInputStream(socket.getInputStream());
            outputObtStr = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void uploadToClient(StoCMessage msg) {
    }

    @Override
    public void pingTimeOverdue() {

    }

    @Override
    public void resetTimeCounter() {

    }

    @Override
    public void setGameController(GameController gameController) {

    }
}