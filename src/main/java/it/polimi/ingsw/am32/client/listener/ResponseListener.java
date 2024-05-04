package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ResponseListener implements ResponseListenerInterface, Runnable{
    View view;
    private final BlockingQueue<StoCMessage> messagesSenderBox;
    public ResponseListener(View view){
        this.view = view;
        this.messagesSenderBox = new LinkedBlockingQueue<>();
    }
    @Override
    public void run(){}; //TODO
    @Override
    public void addMessage(StoCMessage message) {
        messagesSenderBox.add(message);
    }
    @Override
    public void processMessages(StoCMessage message) {
        view.updateView(message);
    }

}
