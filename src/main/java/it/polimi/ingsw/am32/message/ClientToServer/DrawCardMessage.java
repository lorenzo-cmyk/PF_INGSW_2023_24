package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to draw a card.
 */
public class DrawCardMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to draw a card
     */
    private final String senderNickname;
    /**
     * An identifier of the place from which the player wants to draw the card
     */
    private final int deckType;
    /**
     * The id of the face up card the player wants to draw; can be any value if player is not drawing from the face up cards
     */
    private final int cardId;

    public DrawCardMessage(String senderNickname, int deckType, int cardId) {
        this.senderNickname = senderNickname;
        this.deckType = deckType;
        this.cardId = cardId;
    }

    /**
     * This method is called when a player wants to draw a card.
     * Draws a card from the deck the player wants to draw from.
     * @param gameController the game controller of the game the player is playing in
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.drawCard(senderNickname, deckType, cardId);
    }

    @Override
    public String toString() {
        return "DrawCardMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", deckType=" + deckType +
                ", cardId=" + cardId +
                '}' + "\n";
    }
}
