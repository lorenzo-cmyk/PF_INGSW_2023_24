package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message sent by the server to the client to confirm the selected secret objective
 * card. The player will receive the confirmation of the selected secret objective card.
 */
public class ConfirmSelectedSecretObjectiveCardMessage implements StoCMessage {
    /**
     * The nickname of the recipient who will receive the confirmation of the selected secret objective card.
     */
    private final String recipientNickname;
    /**
     * The id of the secret objective card chosen by the player.
     */
    private final int chosenSecretObjectiveCard;

    /**
     * The constructor of the class: it creates a new ConfirmSelectedSecretObjectiveCardMessage with the nickname
     * of the recipient who will receive the confirmation of the selected secret objective card and the id of the
     * secret objective card chosen by the player.
     * @param recipientNickname the nickname of the player who will receive the confirmation message.
     * @param chosenSecretObjectiveCard the id of the secret objective card chosen by the player.
     */
    public ConfirmSelectedSecretObjectiveCardMessage(String recipientNickname, int chosenSecretObjectiveCard) {
        this.recipientNickname = recipientNickname;
        this.chosenSecretObjectiveCard = chosenSecretObjectiveCard;
    }

    /**
     * This method is used to get the nickname of the recipient who will receive the confirmation of the selected secret
     * objective card.
     * @return the nickname of the player who will receive the confirmation message.
     */
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method is used to process the message to the client when the selected secret objective card is confirmed,
     * updating the view with the selected secret objective card chosen by the player.
     * @param view the view of the player who will receive the message and should be updated.
     */
    @Override
    public void processMessage(View view) {
        view.updateConfirmSelectedSecretCard(chosenSecretObjectiveCard);
    }

    @Override
    public String toString() {
        return "ConfirmSelectedSecretObjectiveCardMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", chosenSecretObjectiveCard=" + chosenSecretObjectiveCard +
                '}';
    }
}
