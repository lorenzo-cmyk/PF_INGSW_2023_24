package it.polimi.ingsw.am32.client;

public interface EventHandler {
    void handleEvent(Event event);
    void handleChoiceEvent(Event event, int Choice);

}
