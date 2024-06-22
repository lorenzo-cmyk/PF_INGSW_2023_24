package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

/**
 * This class is used to manage the message send to the players to notify them of the updated list of players in the lobby.
 */
public class LobbyPlayerListMessage implements StoCMessage {
    /**
     * The nickname of the recipient.
     */
    private final String recipientNickname;
    /**
     * The list of players in the lobby.
     */
    private final ArrayList<String> playerList;

    /**
     * The constructor of the class.
     * @param recipientNickname the nickname of the recipient.
     * @param playerList the list of players in the lobby.
     */
    public LobbyPlayerListMessage(String recipientNickname, ArrayList<String> playerList) {
        this.recipientNickname = recipientNickname;
        this.playerList = playerList;
    }

    /**
     * This method is used to update the player list in the view.
     * @param view the view of the player.
     */
    @Override
    public void processMessage(View view) {
        view.updatePlayerList(playerList); // update the player list in the view
    }

    /**
     * This method is used to get the list of players in the lobby.
     * @return the list of players in the lobby.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "LobbyPlayerListMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", playerList=" + playerList.toString() +
                '}';
    }
}
