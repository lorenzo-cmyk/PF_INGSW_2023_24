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
        view.updateChat(recipientString,senderNickname,content);
    }

    @Override
    public String getRecipientNickname() {
        return recipientString;
    }

    public String toString(){
        String myString = "";
        myString += "recipientString: " + recipientString + "\n";
        myString += "senderNickname: " + senderNickname + "\n";
        myString += "content: " + content + "\n";
        return myString;
    }
}
