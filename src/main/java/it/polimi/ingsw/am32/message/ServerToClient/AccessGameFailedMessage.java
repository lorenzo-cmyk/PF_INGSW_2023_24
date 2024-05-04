package it.polimi.ingsw.am32.message.ServerToClient;


public class AccessGameFailedMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public AccessGameFailedMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    @Override
    public void processMessage() {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }
}