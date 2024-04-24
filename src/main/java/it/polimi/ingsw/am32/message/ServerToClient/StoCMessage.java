package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.controller.VirtualView;

/**
 * This interface represents a message from the server to the client.
 * It contains a single method to deliver the message to a virtual view.
 */
public interface StoCMessage {

    /**
     * Delivers the message to the specified virtual view.
     *
     * @param virtualView The virtual view to which the message should be delivered
     */
    void processMessage(VirtualView virtualView);
}
