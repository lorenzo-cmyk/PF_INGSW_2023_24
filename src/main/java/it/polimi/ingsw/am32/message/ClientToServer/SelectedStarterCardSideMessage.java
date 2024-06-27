package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GameController;

/**
 * This class is used to manage the message sent by the client when he wants to notify the server of his selection of the side of the starter card.
 */
public class SelectedStarterCardSideMessage implements CtoSMessage {
    /**
     * The nickname of the player who wants to select the side of the starter card
     */
    private final String senderNickname;
    /**
     * The side of the starter card selected by the player
     */
    private final boolean isUp;

    /**
     * Constructor: a message containing the nickname of the player who selected the side of the starter card and
     * the side selected.
     * @param senderNickname the nickname of the player who selected the side of the starter card
     * @param isUp the side of the starter card selected by the player
     */
    public SelectedStarterCardSideMessage(String senderNickname, boolean isUp) {
        this.senderNickname = senderNickname;
        this.isUp = isUp;
    }

    /**
     * This method is called when a player selects the side of the starter card.
     * @param gameController the game controller of the game the player is playing
     */
    @Override
    public void elaborateMessage(GameController gameController) {
        gameController.chooseStarterCardSide(senderNickname, isUp);
    }

    /**
     * This method overrides the default toString method.
     * It provides a string representation of a message object, which can be useful for debugging purposes.
     *
     * @return A string representation of the SelectedStarterCardSideMessage object.
     * The string includes the message type, the senderNickname and the isUp properties of the object.
     */
    @Override
    public String toString() {
        return "SelectedStarterCardSideMessage:{" +
                "senderNickname='" + senderNickname + '\'' +
                ", isUp=" + isUp +
                '}';
    }
}
