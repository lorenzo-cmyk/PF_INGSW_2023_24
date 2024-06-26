package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

/**
 * This class is used to manage the message send by the server to the players notifying them the winners of the match.
 */
public class MatchWinnersMessage implements StoCMessage {
    /**
     * The nickname of the player who will receive the message.
     */
    private final String recipientNickname;
    /**
     * The list of the players who played the match.
     */
    private final ArrayList<String> players;
    /**
     * The list of the points gained by the players.
     */
    private final ArrayList<Integer> points;
    /**
     * The list of the secrets gained by the players.
     */
    private final ArrayList<Integer> secrets;
    /**
     * The list of the points gained by the players from the secrets.
     */
    private final ArrayList<Integer> pointsGainedFromSecrets;
    /**
     * The list of the winners of the match.
     */
    private final ArrayList<String> winners;

    /**
     * The constructor of the class.
     * @param recipientNickname the nickname of the player who will receive the message.
     * @param players the list of the players who played the match.
     * @param points the list of the points gained by the players.
     * @param secrets the list of the secrets gained by the players.
     * @param pointsGainedFromSecrets the list of the points gained by the players from the secrets.
     * @param winners the list of the winners of the match.
     */
    public MatchWinnersMessage(String recipientNickname, ArrayList<String> players,
                               ArrayList<Integer> points, ArrayList<Integer> secrets,
                               ArrayList<Integer> pointsGainedFromSecrets, ArrayList<String> winners) {
        this.recipientNickname = recipientNickname;
        this.players = players;
        this.points = points;
        this.secrets = secrets;
        this.pointsGainedFromSecrets = pointsGainedFromSecrets;
        this.winners = winners;
    }

    /**
     * This method is used to show the message to the player.
     * @param view the view of the player.
     */
    @Override
    public void processMessage(View view) {
        view.showMatchWinners(players, points, secrets, pointsGainedFromSecrets, winners);
    }

    /**
     * This method is used to get the nickname of the player who will receive the message.
     * @return the nickname of the player who will receive the message.
     */
    @Override
    public String getRecipientNickname() {
        return recipientNickname;
    }

    @Override
    public String toString() {
        return "MatchWinnersMessage:{" +
                "recipientNickname='" + recipientNickname + '\'' +
                ", players=" + players.toString() +
                ", points=" + points.toString() +
                ", secrets=" + secrets.toString() +
                ", pointsGainedFromSecrets=" + pointsGainedFromSecrets.toString() +
                ", winners=" + winners.toString() +
                '}';
    }
}
