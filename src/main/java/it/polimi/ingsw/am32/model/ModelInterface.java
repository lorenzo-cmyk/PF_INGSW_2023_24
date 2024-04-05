package it.polimi.ingsw.am32.model;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.match.MatchStatus;

import java.util.ArrayList;

public interface ModelInterface {
    void enterLobbyPhase();
    boolean addPlayer(String nickname);
    boolean deletePlayer(String nickname);
    void enterPreparationPhase();
    void assignRandomColoursToPlayers();
    void assignRandomStartingInitialCardsToPlayers();
    int getInitialCardPlayer(String nickname);
    boolean createFieldPlayer(String nickname, boolean side);
    void assignRandomStartingResourceCardsToPlayers();
    void assignRandomStartingGoldCardsToPlayers();
    void pickRandomCommonObjectives();
    void assignRandomStartingSecretObjectivesToPlayers();
    ArrayList<Integer> getSecretObjectiveCardsPlayer(String nickname);
    boolean receiveSecretObjectiveChoiceFromPlayer(String nickname, int id);
    void randomizePlayersOrder();
    void enterPlayingPhase();
    void startTurns();
    boolean placeCard(int id, int x, int y, boolean side);
    boolean drawCard(int deckType, int id);
    boolean nextTurn();
    void setTerminating();
    boolean areWeTerminating();
    boolean isFirstPlayer();
    void setLastTurn();
    void enterTerminatedPhase();
    boolean addObjectivePoints();
    ArrayList<String> getWinners();
    ArrayList<String> getPlayersNicknames();
    ArrayList<Integer> getCurrentResourcesCards();
    ArrayList<Integer> getCurrentGoldCards();
    ArrayList<Integer> getCommonObjectives();
    int[] getPlayerResources(String nickname);
    int getPlayerSecretObjective(String nickname);
    ArrayList<Integer> getPlayerHand(String nickname);
    ArrayList<CardPlaced> getPlayerField(String nickname);
    MatchStatus getMatchStatus();
    int getCurrentTurnNumber();
}
