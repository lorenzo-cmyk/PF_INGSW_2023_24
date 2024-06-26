package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.Arrays;

/**
 * This class is used to manage the message sent to all the players, notifying them that a player has placed a card and disconnected.
 */
public class PlaceCardRollbackMessage implements StoCMessage{
    /**
     * The nickname of the recipient who will receive the message.
     */
    private final String recipientNickname;
    /**
     * The nickname of the player who disconnected after placing
     */
    private final String playerNickname;
    /**
     * The card that has been placed by the player and should be removed.
     */
    private final int removedCard;
    /**
     * The old points of the player before the card placement who disconnected.
     */
    private final int playerPoints;
    /**
     * The old resources of the player before the card placement who disconnected.
     */
    private final int[] playerResources;

    /**
     * The constructor of the class: it creates a new PlaceCardRollbackMessage with the nickname of the recipient who will receive the message,
     * the nickname of the player who disconnected after placing a card, the card that has been placed by the player and should be removed,
     * the old points of the player before the card placement who disconnected and the old resources of the player before the card placement who disconnected.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param playerNickname the nickname of the player who disconnected after placing a card.
     * @param removedCard the card that has been placed by the player and should be removed.
     * @param playerPoints the old points of the player before the card placement who disconnected.
     * @param playerResources the old resources of the player before the card placement who disconnected.
     */
    public PlaceCardRollbackMessage(String recipientNickname, String playerNickname, int removedCard, int playerPoints, int[] playerResources) {
        this.recipientNickname = recipientNickname;
        this.playerNickname = playerNickname;
        this.removedCard = removedCard;
        this.playerPoints = playerPoints;
        this.playerResources = playerResources;
    }

    /**
     * This method is used to process the message to the client when a player has placed a card and disconnected,
     * updating the view of the player who will receive the message.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateRollback(playerNickname, removedCard, playerPoints, playerResources);
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the message.
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
     * @return A string representation of the PlaceCardRollbackMessage object.
     */
    @Override
    public String toString() {
        return "PlaceCardRollbackMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerNickname='" + playerNickname + '\'' +
                ", removedCard=" + removedCard +
                ", playerPoints=" + playerPoints +
                ", playerResources=" + Arrays.toString(playerResources) +
                '}';
    }
}
