package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to notify the server of his selection of a secret objective card.
 */
public class SelectedSecretObjectiveCardMessage implements CtoSMessage {
    /**
     * The nickname of the player who selected the secret objective card
     */
    private final String senderNickname;
    /**
     * The id of the secret objective card selected by the player
     */
    private final int cardId;

    public SelectedSecretObjectiveCardMessage(String senderNickname, int cardId) {
        this.senderNickname = senderNickname;
        this.cardId = cardId;
    }

    /**
     * This method is called when a player selects a secret objective card.
     * @param gameController the game controller of the game the player is playing
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.chooseSecretObjectiveCard(senderNickname, cardId);
    }

    @Override
    public String toString() {
        return "SelectedSecretObjectiveCardMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", cardId=" + cardId +
                '}' + "\n";
    }
}
