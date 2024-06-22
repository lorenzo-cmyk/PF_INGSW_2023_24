package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

/**
 * This class is used to manage the message sent by the server to the client to assign the secret objective cards which
 * should be chosen by the player, the common objective cards and the cards to the player's hand.
 */
public class AssignedSecretObjectiveCardMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the assignment of the cards.
     */
    private final String recipientNickname;
    /**
     * The list of the secret objective cards assigned to the player so that he can choose one of them.
     */
    private final ArrayList<Integer> assignedSecretObjectiveCards;
    /**
     * The list of the common objective cards assigned to the player for this game.
     */
    private final ArrayList<Integer> chosenCommonObjectiveCards;
    /**
     * The list of the cards assigned to the player's hand at the beginning of the game: three cards (two resource cards
     * and one god card).
     */
    private final ArrayList<Integer> playerHand;

    /**
     * The constructor of the class: it creates a new AssignedSecretObjectiveCardMessage with the nickname of the
     * recipient who will receive the assignment of the cards.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param assignedSecretObjectiveCards the list of the secret objective cards assigned to the player so that he can
     *                                     choose one of them.
     * @param chosenCommonObjectiveCards the list of the common objective cards assigned to the player for this game.
     * @param playerHand the list of the cards assigned to the player's hand at the beginning of the game.
     */
    public AssignedSecretObjectiveCardMessage(String recipientNickname, ArrayList<Integer> assignedSecretObjectiveCards,
                                              ArrayList<Integer> chosenCommonObjectiveCards, ArrayList<Integer> playerHand) {
        this.recipientNickname = recipientNickname;
        this.assignedSecretObjectiveCards = assignedSecretObjectiveCards;
        this.chosenCommonObjectiveCards = chosenCommonObjectiveCards;
        this.playerHand = playerHand;
    }

    /**
     * This method is used to process the message to the client when is a turn to choose the secret objective card,
     * setting the cards received by the server and updating the current event of the game.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.setCardsReceived(assignedSecretObjectiveCards, chosenCommonObjectiveCards, playerHand);
        view.updateCurrentEvent(Event.SELECT_SECRET_OBJ_CARD);
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the assignment of the cards.
     * @return the nickname of the player who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "AssignedSecretObjectiveCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", assignedSecretObjectiveCards=" + assignedSecretObjectiveCards.toString() +
                ", chosenCommonObjectiveCards=" + chosenCommonObjectiveCards.toString() +
                ", playerHand=" + playerHand.toString() +
                '}';
    }
}
