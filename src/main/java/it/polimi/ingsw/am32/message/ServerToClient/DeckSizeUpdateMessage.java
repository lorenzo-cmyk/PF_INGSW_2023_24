package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.Arrays;

/**
 * This class is used to manage the message sent by the server to the client to update the deck after one player
 * draws a card.
 */
public class DeckSizeUpdateMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the message to update the deck size after the draw card action.
     */
    private final String recipientNickname;
    /**
     * The size of the resource card deck after the draw card action.
     */
    private final int resourceCardDeckSize;
    /**
     * The resource card deck facing kingdom after the draw card action.
     */
    private final int resourceCardDeckFacingKingdom;
    /**
     * The size of the gold card deck after the draw card action.
     */
    private final int goldCardDeckSize;
    /**
     * The gold card deck facing kingdom after the draw card action.
     */
    private final int goldCardDeckFacingKingdom;
    /**
     * The current resource cards in the deck after the draw card action.
     */
    private final int[] currentResourceCards;
    /**
     * The current gold cards in the deck after the draw card action.
     */
    private final int[] currentGoldCards;

    /**
     * The constructor of the class: it creates a new DeckSizeUpdateMessage with the nickname of the recipient who will
     * receive the message to update the deck after the draw card action.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param resourceCardDeckSize the size of the resource card deck after the draw card action.
     * @param goldCardDeckSize the size of the gold card deck after the draw card action.
     * @param currentResourceCards the current resource card visible after the draw card action.
     * @param currentGoldCards the current gold card visible after the draw card action.
     * @param resourceCardDeckFacingKingdom the resource card deck facing kingdom after the draw card action.
     * @param goldCardDeckFacingKingdom the gold card deck facing kingdom after the draw card action.
     */
    public DeckSizeUpdateMessage(String recipientNickname, int resourceCardDeckSize, int goldCardDeckSize,
                                 int[] currentResourceCards, int[] currentGoldCards,
                                 int resourceCardDeckFacingKingdom, int goldCardDeckFacingKingdom) {
        this.recipientNickname = recipientNickname;
        this.resourceCardDeckSize = resourceCardDeckSize;
        this.goldCardDeckSize = goldCardDeckSize;
        this.currentResourceCards = currentResourceCards;
        this.currentGoldCards = currentGoldCards;
        this.resourceCardDeckFacingKingdom = resourceCardDeckFacingKingdom;
        this.goldCardDeckFacingKingdom = goldCardDeckFacingKingdom;
    }

    /**
     * This method is used to process the message to the client when the deck is updated after the draw card action.
     * It updates the view with the new deck size and the current cards in the deck.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateDeck(resourceCardDeckSize, goldCardDeckSize, currentResourceCards, currentGoldCards,resourceCardDeckFacingKingdom ,goldCardDeckFacingKingdom );
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the message to update the deck.
     * @return the nickname of the player who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the DeckSizeUpdateMessage object.
     */
    @Override
    public String toString() {
        return "DeckSizeUpdateMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", resourceCardDeckSize=" + resourceCardDeckSize +
                ", resourceCardDeckFacingKingdom=" + resourceCardDeckFacingKingdom +
                ", goldCardDeckSize=" + goldCardDeckSize +
                ", goldCardDeckFacingKingdom=" + goldCardDeckFacingKingdom +
                ", currentResourceCards=" + Arrays.toString(currentResourceCards) +
                ", currentGoldCards=" + Arrays.toString(currentGoldCards) +
                '}';
    }
}
