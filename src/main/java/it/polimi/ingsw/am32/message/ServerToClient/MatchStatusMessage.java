package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

/**
 * This class is used to manage the message send to the players notifying them of the game status.
 */
public class MatchStatusMessage implements StoCMessage {
    /**
     * The nickname of the player that will receive the message.
     */
    private final String recipientNickname;
    /**
     * The status of the match.
     */
    private final int matchStatus;

    /**
     * The constructor of the class.
     * @param recipientNickname the nickname of the player that will receive the message.
     * @param matchStatus the status of the match.
     */
    public MatchStatusMessage(String recipientNickname, int matchStatus) {
        this.recipientNickname = recipientNickname;
        this.matchStatus = matchStatus;
    }

    /**
     * This method is used to update the view of the player that will receive the message.
     * @param view the view of the player that will receive the message.
     */
    @Override
    public void processMessage(View view) {
        view.updateMatchStatus(matchStatus);
    }

    /**
     * This method is used to get the nickname of the player that will receive the message.
     * @return the nickname of the player that will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the MatchStatusMessage object.
     */
    @Override
    public String toString() {
        return "MatchStatusMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", matchStatus=" + matchStatus +
                '}';
    }
}
