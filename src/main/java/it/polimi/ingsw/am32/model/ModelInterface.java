package it.polimi.ingsw.am32.model;

import java.util.ArrayList;
import java.util.Objects;

public interface ModelInterface {
    boolean enterLobbyPhase(); // TODO
    boolean addPlayer(String nickname); // TODO
    boolean deletePlayer(String nickname); // TODO
    boolean enterPreparationPhase(); // TODO
    boolean assignRandomColoursToPlayers(); // TODO
    boolean assignRandomStartingInitialCardToPlayer(); // TODO
    int getInitialCardPlayer(String nickname); // TODO
    boolean createFieldPlayer(String nickname, boolean side); // TODO
    boolean assignRandomStartingResourceCardsToPlayers(); // TODO
    boolean assignRandomStartingGoldCardsToPlayers(); // TODO
    boolean pickRandomCommonObjectives(); // TODO
    boolean assignRandomStartingSecretObjectivesToPlayers(); // TODO
    ArrayList<Integer> getSecretsObjectivesCardsPlayer(String nickname); // TODO
    boolean receiveSecretObjectiveChoiceFromPlayer(String nickname, int id); // TODO
    boolean randomizePlayersOrder(); // TODO
    boolean enterPlayingPhase(); // TODO
    boolean startTurns(); // TODO
    boolean placeCard(int id, int x, int y, boolean side); // TODO
    boolean drawCard(int id, int deckType); // TODO
    boolean nextTurn(); // TODO
    boolean setTerminating(); // TODO
    boolean areWeTerminating(); // TODO
    boolean isFirstPlayer(); // TODO
    boolean setLastTurn(); // TODO
    boolean enterTerminatedPhase(); // TODO
    boolean addObjectivePoints(); // TODO
    ArrayList<String> getWinners(); // TODO
    ArrayList<String> getPlayersNicknames(); // TODO
    ArrayList<Integer> getCurrentResourcesCards(); // TODO
    ArrayList<Integer> getCurrentGoldCards(); // TODO
    ArrayList<Integer> getCommonObjectives(); // TODO
    ArrayList<Integer> getPlayerResources(String nickname); // TODO
    int getPlayerSecretObjective(String nickname); // TODO
    ArrayList<Integer> getPlayerHand(String nickname); // TODO
    ArrayList<Object> getPlayerField(String nickname); // TODO
    String getMatchStatus(); // TODO
    int getCurrentTurnNumber(); // TODO
}
