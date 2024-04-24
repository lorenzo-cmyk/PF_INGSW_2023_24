package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.controller.GameController;
import it.polimi.ingsw.am32.message.toClient.StoCMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SKServerNode implements Runnable {
    private Socket socket;
    private final GameController gameController;
    private ObjectInputStream inputObtStr;
    private ObjectOutputStream outputObtStr;
    private int pingCount;

    public SKServerNode(Socket socket){
        this.socket = socket;
        this.gameController = null;
    }

    public void run() {
        try {
            inputObtStr = new ObjectInputStream(socket.getInputStream());
            outputObtStr = new ObjectOutputStream(socket.getOutputStream());

            while (true) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void uploadToClient(StoCMessage msg) {

    }
}
