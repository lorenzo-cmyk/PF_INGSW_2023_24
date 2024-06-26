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
     * @param view the view that should be updated with the data contained in the message.
     */
    void processMessage(View view);

    /**
     * Gets the recipient's nickname of the message.
     * Method needed for submitVirtualViewMessage method in Game controller.
     * GameController needs to know who to send a message to.
     * @return the recipient's nickname of the message.
     */
    String getRecipientNickname();
    /**
     * This method provides a string representation of a message object, which can be useful for debugging purposes.
     * It will be overridden by the classes that implement the StoCMessage interface.
     *
     * @return A string representation of the StoCMessage object.
     */
    String toString();
}
