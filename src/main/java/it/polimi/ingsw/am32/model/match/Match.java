package it.polimi.ingsw.am32.model.match;

import it.polimi.ingsw.am32.model.ModelInterface;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.deck.CardDeck;
import it.polimi.ingsw.am32.model.deck.NonObjectiveCardDeck;
import it.polimi.ingsw.am32.model.player.Player;
import java.util.ArrayList;

public class Match implements ModelInterface {
    private NonObjectiveCardDeck starterCardsDeck;
    private CardDeck objectiveCardsDeck;
    private CardDeck secretObjectiveCardsDeck;
    private NonObjectiveCardDeck resourceCardsDeck;
    private NonObjectiveCardDeck goldCardsDeck;
    private NonObjectiveCard[] currentResourceCards;
    private NonObjectiveCard[] currentGoldCards;
    private Card[] commonObjectives;
    private ArrayList<Player> players;
    private MatchStatus matchStatus;
    private String currentPlayerID;
    private int currentTurnNumber;

    public Match() {
        // TODO
    }

    public boolean enterLobbyPhase() {
        return false;
    }
    public boolean addPlayer(String nickname) {
        return false;
    }
    public boolean deletePlayer(String nickname) {
        return false;
    }
    public boolean enterPreparationPhase() {
        return false;
    }
    public boolean assignRandomColoursToPlayers() {
        return false;
    }
    public boolean assignRandomStartingInitialCardToPlayer() {
        return false;
    }
    public int getInitialCardPlayer(String nickname) {
        return 0;
    }
    public boolean createFieldPlayer(String nickname, boolean side) {
        return false;
    }
    public boolean assignRandomStartingResourceCardsToPlayers() {
        return false;
    }
    public boolean assignRandomStartingGoldCardsToPlayers() {
        return false;
    }
    public boolean pickRandomCommonObjectives() {
        return false;
    }
    public boolean assignRandomStartingSecretObjectivesToPlayers() {
        return false;
    }
    public ArrayList<Integer> getSecretsObjectivesCardsPlayer(String nickname) {
        return null;
    }
    public boolean receiveSecretObjectiveChoiceFromPlayer(String nickname, int id) {
        return false;
    }
    public boolean randomizePlayersOrder() {
        return false;
    }
    public boolean enterPlayingPhase() {
        return false;
    }
    public boolean startTurns() {
        return false;
    }
    public boolean placeCard(int id, int x, int y, boolean side) {
        return false;
    }
    public boolean drawCard(int id, int deckType) {
        return false;
    }
    public boolean nextTurn() {
        return false;
    }
    public boolean setTerminating() {
        return false;
    }
    public boolean areWeTerminating() {
        return false;
    }
    public boolean isFirstPlayer() {
        return false;
    }
    public boolean setLastTurn() {
        return false;
    }
    public boolean enterTerminatedPhase() {
        return false;
    }
    public boolean addObjectivePoints() {
        return false;
    }
    public ArrayList<String> getWinners() {
        return null;
    }
    public ArrayList<String> getPlayersNicknames() {
        return null;
    }
    public ArrayList<Integer> getCurrentResourcesCards() {
        return null;
    }
    public ArrayList<Integer> getCurrentGoldCards() {
        return null;
    }
    public ArrayList<Integer> getCommonObjectives() {
        return null;
    }
    public ArrayList<Integer> getPlayerResources(String nickname) {
        return null;
    }
    public int getPlayerSecretObjective(String nickname) {
        return 0;
    }
    public ArrayList<Integer> getPlayerHand(String nickname) {
        return null;
    }
    public ArrayList<Object> getPlayerField(String nickname) {
        return null;
    }
    public String getMatchStatus() {
        return null;
    }
    public int getCurrentTurnNumber() {
        return 0;
    }
}
