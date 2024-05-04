package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

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
    void processMessage(); // TODO this method should not take VirtualView as parameter
    String getRecipientNickname();
}
