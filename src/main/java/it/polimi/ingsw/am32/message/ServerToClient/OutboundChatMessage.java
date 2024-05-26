package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

public class OutboundChatMessage implements StoCMessage {
    private final String recipientString;
    private final String senderNickname;
    private final String content;

    public OutboundChatMessage(String recipientString, String senderNickname, String content) {
        this.recipientString = recipientString;
        this.senderNickname = senderNickname;
        this.content = content;
    }

    @Override
    public void processMessage(View view) {
        // TODO
    }

    @Override
    public String getRecipientNickname() {
        return recipientString;
    }

    @Override
    public String toString() {
        return "OutboundChatMessage:{" +
                "recipientString='" + recipientString + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
