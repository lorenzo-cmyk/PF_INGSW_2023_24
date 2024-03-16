package it.polimi.ingsw.am32.model;

import java.util.ArrayList;

public interface ModelInterface {
    boolean shuffleDeck(int deckNum);
    int drawFromDeck(int deckNum);
    boolean selectInitialCardSide(String, boolean); // TODO
    boolean assignColourToPlayers(); // TODO
    ArrayList<Integer> getStartingHand(String nickname); // TODO
    ArrayList<Integer> getCommonObjectives(); // TODO
    ArrayList<Integer> getSecretObjectives(); // TODO
    boolean selectSecretObjective(String, int); // TODO

    boolean startPlay(); // TODO
    boolean placeCard(int, int, int, boolean); // TODO
    MatchStatus nextPlayer(); // TODO
    boolean computerFinalPoints(); // TODO
    String getWinner(); // TODO
    boolean endMatch(); // TODO

    boolean addPlayer(String nickname);
    boolean deletePlayer(String nickname);
    boolean startPreparationPhase(); // TODO
    boolean cancelMatch(); // TODO
}
