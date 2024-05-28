package it.polimi.ingsw.am32.message.ServerToClient;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ResponsePlayerFieldMessage implements StoCMessage {
    private final String recipientNickname;
    private final String playerNickname;
    private final ArrayList<int[]> playerField;
    private final int[] playerResources;

    public ResponsePlayerFieldMessage(String recipientNickname, String playerNickname, ArrayList<int[]> playerField,
                                      int[] playerResources) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.playerField = playerField;
        this.playerResources = playerResources;
    }

    @Override
    public void processMessage(View view) {
        // This method is never called by Client, the ResponsePlayerFieldMessage is kept for easier debugging and testing
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

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
