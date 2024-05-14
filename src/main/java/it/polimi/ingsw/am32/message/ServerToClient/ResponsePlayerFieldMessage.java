package it.polimi.ingsw.am32.message.ServerToClient;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;
import java.util.Arrays;

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
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String toString(){
        String myString = "";
        myString += "recipientNickname: " + recipientNickname + "\n";
        myString += "playerNickname: " + playerNickname + "\n";
        myString += "playerField: " + playerField.toString() + "\n";
        myString += "playerResources: " + Arrays.toString(playerResources) + "\n";
        return myString;
    }
}
