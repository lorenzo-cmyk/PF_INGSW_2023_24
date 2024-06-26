package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent to the player who requested the field of another player to notify him that the field could not be fetched (used for testing only).
 */
public class NegativeResponsePlayerFieldMessage implements StoCMessage{
    /**
     * The nickname of the recipient.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player whose field has been requested.
     */
    private final String playerNickname;

    /**
     * The constructor of the class.
     * @param recipientNickname The nickname of the recipient.
     * @param playerNickname The nickname of the player whose field has been requested.
     */
    public NegativeResponsePlayerFieldMessage(String recipientNickname, String playerNickname) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
    }

    /**
     * This method is never called by Client, the NegativeResponsePlayerFieldMessage is kept for easier debugging and testing
     * @param view The view of the player who receives the message.
     */
    @Override
    public void processMessage(View view) {
        // This method is never called by Client, the NegativeResponsePlayerFieldMessage is kept for easier debugging and testing
    }

    /**
     * This method is never called by Client, the NegativeResponsePlayerFieldMessage is kept for easier debugging and testing
     * @return The nickname of the recipient.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the NegativeResponsePlayerFieldMessage object.
     */
    @Override
    public String toString() {
        return "NegativeResponsePlayerFieldMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                '}';
    }
}
