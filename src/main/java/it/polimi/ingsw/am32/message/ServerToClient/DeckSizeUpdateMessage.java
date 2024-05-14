package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.Arrays;

public class DeckSizeUpdateMessage implements StoCMessage {
    private final String recipientNickname;
    private final int resourceCardDeckSize;
    private final int goldCardDeckSize;
    private final int[] currentResourceCards;
    private final int[] currentGoldCards;

    public DeckSizeUpdateMessage(String recipientNickname, int resourceCardDeckSize, int goldCardDeckSize,
                                 int[] currentResourceCards, int[] currentGoldCards) {
        this.recipientNickname = recipientNickname;
        this.resourceCardDeckSize = resourceCardDeckSize;
        this.goldCardDeckSize = goldCardDeckSize;
        this.currentResourceCards = currentResourceCards;
        this.currentGoldCards = currentGoldCards;
    }

    @Override
    public void processMessage(View view) {
        view.updateDeck(resourceCardDeckSize, goldCardDeckSize, currentResourceCards, currentGoldCards);
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
                ", goldCardDeckSize=" + goldCardDeckSize +
                ", currentResourceCards=" + Arrays.toString(currentResourceCards) +
                ", currentGoldCards=" + Arrays.toString(currentGoldCards) +
                '}' + "\n";
    }
}
