package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

public class InboundChatMessage implements CtoSMessage {
    private final String senderNickname;
    private final String recipientNickname;
    private final boolean multicastFlag;
    private final String content;

    public InboundChatMessage(String senderNickname, String recipientNickname, boolean multicastFlag, String content) {
        this.senderNickname = senderNickname;
        this.recipientNickname = recipientNickname;
        this.multicastFlag = multicastFlag;
        this.content = content;
    }

    @Override
    public void elaborateMessage(GameController gameController) {
        // TODO
    }
}
