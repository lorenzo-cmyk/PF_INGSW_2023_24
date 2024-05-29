package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class NegativeResponsePlayerFieldMessage implements StoCMessage{
    private final String recipientNickname;
    private final String playerNickname;

    public NegativeResponsePlayerFieldMessage(String recipientNickname, String playerNickname) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
    }

    @Override
    public void processMessage(View view) {
        // This method is never called by Client, the NegativeResponsePlayerFieldMessage is kept for easier debugging and testing
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "NegativeResponsePlayerFieldMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                '}';
    }
}
