package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;

public interface AskListenerInterface {
    void addMessage(CtoSMessage message);
    void addMessage(CtoSLobbyMessage message);
    void flushMessages();
}
