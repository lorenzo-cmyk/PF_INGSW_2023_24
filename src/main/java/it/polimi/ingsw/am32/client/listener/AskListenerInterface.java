package it.polimi.ingsw.am32.client.listener;

import it.polimi.ingsw.am32.message.ClientToServer.CtoSLobbyMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage;

/**
 * Interface for the AskListener class: used to manage the messages that clients want to send to the server.
 * Listens for messages to be added to the queues (according to their type).
 * There are two queues: one for CtoSMessage and one for CtoSLobbyMessage.
 */
public interface AskListenerInterface {
    /**
     * Adds a CtoSMessage to the queue to be sent to the server.
     * @param message The message to be added to the queue and which will be sent to the server.
     */
    void addMessage(CtoSMessage message);

    /**
     * Adds a CtoSLobbyMessage to the queue of messages to be sent to the server.
     * @param message The message to be added to the queue and which will be sent to the server.
     */
    void addMessage(CtoSLobbyMessage message);

    /**
     * Flushes both the message and lobbyMessage queues.
     */
    void flushMessages();
}
