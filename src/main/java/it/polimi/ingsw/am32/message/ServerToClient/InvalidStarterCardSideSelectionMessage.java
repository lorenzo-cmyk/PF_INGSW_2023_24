package it.polimi.ingsw.am32.message.ServerToClient;


import it.polimi.ingsw.am32.controller.VirtualView;

public class InvalidStarterCardSideSelectionMessage implements StoCMessage {
    private final String recipientNickname;
    private final String reason;

    public InvalidStarterCardSideSelectionMessage(String recipientNickname, String reason) {
        this.recipientNickname = recipientNickname;
        this.reason = reason;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void processMessage(VirtualView virtualView) {

    }

}
