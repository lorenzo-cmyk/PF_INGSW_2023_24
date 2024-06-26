package it.polimi.ingsw.am32.message.ServerToClient;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class is used to manage the message sent to the player who requested the field of another player (used for testing only).
 */
public class ResponsePlayerFieldMessage implements StoCMessage {
    /**
     * The nickname of the recipient.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player whose field has been requested.
     */
    private final String playerNickname;
    /**
     * The field of the player whose field has been requested.
     */
    private final ArrayList<int[]> playerField;
    /**
     * The resources of the player whose field has been requested.
     */
    private final int[] playerResources;

    /**
     * The constructor of the class.
     * @param recipientNickname The nickname of the recipient.
     * @param playerNickname The nickname of the player whose field has been requested.
     * @param playerField The field of the player whose field has been requested.
     * @param playerResources The resources of the player whose field has been requested.
     */
    public ResponsePlayerFieldMessage(String recipientNickname, String playerNickname, ArrayList<int[]> playerField,
                                      int[] playerResources) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.playerField = playerField;
        this.playerResources = playerResources;
    }

    /**
     * This method is never called by Client, the ResponsePlayerFieldMessage is kept for easier debugging and testing
     * @param view The view of the player who receives the message.
     */
    @Override
    public void processMessage(View view) {
        // This method is never called by Client, the ResponsePlayerFieldMessage is kept for easier debugging and testing
    }

    /**
     * This method is never called by Client, the ResponsePlayerFieldMessage is kept for easier debugging and testing
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
     * @return A string representation of the ResponsePlayerFieldMessage object.
     */
    @Override
    public String toString() {
        return "ResponsePlayerFieldMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                ", playerField=" + playerField.stream().map(Arrays::toString).collect(Collectors.joining(", ")) +
                ", playerResources=" + Arrays.toString(playerResources) +
                '}';
    }
}
