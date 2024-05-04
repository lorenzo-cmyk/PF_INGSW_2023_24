package it.polimi.ingsw.am32.client;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

public interface View {
    void updateView(StoCMessage message);
    void notifyAskListener(CtoSMessage message);
    void notifyAskListenerLobby(CtoSLobbyMessage message);
    void launch();
    //TODO
}
