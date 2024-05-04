package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

public interface ResponseListenerInterface {
    void addMessage(StoCMessage message);

    void processMessages(StoCMessage message);
}
