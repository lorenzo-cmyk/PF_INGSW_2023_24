package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.io.Serializable;

/**
 * This interface represents a message from the server to the client.
 * It contains a single method to deliver the message to a virtual view.
 */
public interface StoCMessage extends Serializable {
    /**
     * Delivers the message to the specified virtual view.
     *
     *
     */
    void processMessage(View view);
    String getRecipientNickname(); // Method needed for submitVirtualViewMessage method in Gamecontroller; GameController needs to know who to send message to
    String toString(); // Used for debugging purposes
}
