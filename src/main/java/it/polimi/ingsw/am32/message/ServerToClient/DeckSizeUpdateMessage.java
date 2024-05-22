package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.Arrays;

public class DeckSizeUpdateMessage implements StoCMessage {
    private final String recipientNickname;
    private final int resourceCardDeckSize;
    private final int resourceCardDeckFacingKingdom;
    private final int goldCardDeckSize;
    private final int goldCardDeckFacingKingdom;
    private final int[] currentResourceCards;
    private final int[] currentGoldCards;

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

    @Override
    public void processMessage(View view) {
        view.updateDeck(resourceCardDeckSize, goldCardDeckSize, currentResourceCards, currentGoldCards,resourceCardDeckFacingKingdom ,goldCardDeckFacingKingdom );
    }

    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "DeckSizeUpdateMessage{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", resourceCardDeckSize=" + resourceCardDeckSize +
                ", resourceCardDeckFacingKingdom=" + resourceCardDeckFacingKingdom +
                ", goldCardDeckSize=" + goldCardDeckSize +
                ", goldCardDeckFacingKingdom=" + goldCardDeckFacingKingdom +
                ", currentResourceCards=" + Arrays.toString(currentResourceCards) +
                ", currentGoldCards=" + Arrays.toString(currentGoldCards) +
                '}' + "\n";
    }
}
