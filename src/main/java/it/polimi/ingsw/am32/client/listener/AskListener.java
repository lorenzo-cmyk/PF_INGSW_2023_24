package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.network.ClientNodeInterface;

import java.util.ArrayList;

/**
 * This class is used
 *
 */
public class AskListener implements AskListenerInterface, Runnable {
    private ClientNodeInterface clientNode;
    private final ArrayList<CtoSLobbyMessage> lobbyMessagesBox;
    private final ArrayList<CtoSMessage> messagesBox;

    public AskListener(ClientNodeInterface clientNode) {
        this.clientNode = clientNode;
        this.lobbyMessagesBox = new ArrayList<>();
        this.messagesBox = new ArrayList<>();
    }

    @Override
    public void addLobbyMessage(CtoSLobbyMessage message) {
        synchronized (lobbyMessagesBox) {
            lobbyMessagesBox.add(message);
            notifyAll();
        }
    }

    @Override
    public void addMessage(CtoSMessage message) {
        synchronized (messagesBox) {
            messagesBox.add(message);
            notifyAll();
        }
    }

    @Override
    public void processLobbyMessages() {
        synchronized (lobbyMessagesBox) {
            if (lobbyMessagesBox.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            CtoSLobbyMessage message = lobbyMessagesBox.get(0);
            lobbyMessagesBox.remove(message);
            clientNode.uploadToServer(message);
        }
    }

    @Override
    public void processMessages() {
        synchronized (messagesBox) {
            if (messagesBox.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //TODO
                }
            }
            CtoSMessage message = messagesBox.get(0);
            messagesBox.remove(message);
            clientNode.uploadToServer(message);
        }
    }

    @Override
    public void run() {
        //TODO
    }
}
