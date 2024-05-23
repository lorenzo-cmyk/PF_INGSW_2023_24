package it.polimi.ingsw.am32.message.ServerToClient;

import it.polimi.ingsw.am32.client.View;

import java.util.ArrayList;

public class MatchWinnersMessage implements StoCMessage {
    private final String recipientNickname;
    private final ArrayList<String> players;
    private final ArrayList<Integer> points;
    private final ArrayList<Integer> secrets;
    private final ArrayList<Integer> pointsGainedFromSecrets;
    private final ArrayList<String> winners;

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

    @Override
    public void processMessage(View view) {
        view.showMatchWinners(players, points, secrets, pointsGainedFromSecrets, winners);
    }

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
                '}' + "\n";
    }
}
