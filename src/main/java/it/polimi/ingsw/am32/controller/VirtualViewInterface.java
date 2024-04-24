package it.polimi.ingsw.am32.controller;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;

public interface VirtualViewInterface {
    void addMessage(StoCMessage msg);
    void processMessage();
}
