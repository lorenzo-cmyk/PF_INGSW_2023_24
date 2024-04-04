package it.polimi.ingsw.am32.model.match;

import it.polimi.ingsw.am32.model.ModelInterface;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.deck.CardDeck;
import it.polimi.ingsw.am32.model.deck.CardDeckBuilder;
import it.polimi.ingsw.am32.model.deck.NonObjectiveCardDeck;
import it.polimi.ingsw.am32.model.deck.NonObjectiveCardDeckBuilder;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import it.polimi.ingsw.am32.model.player.Colour;
import it.polimi.ingsw.am32.model.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

// FIXME For all methods: Why do methods need to return boolean?
public class Match implements ModelInterface {
    private final NonObjectiveCardDeck starterCardsDeck;
    private final CardDeck objectiveCardsDeck;
    private final NonObjectiveCardDeck resourceCardsDeck;
    private final NonObjectiveCardDeck goldCardsDeck;
    private final ArrayList<NonObjectiveCard> currentResourceCards;
    private final ArrayList<NonObjectiveCard> currentGoldCards;
    private final ArrayList<Card> commonObjectives;
    private final ArrayList<Player> players;
    private MatchStatus matchStatus;
    private String currentPlayerID;
    private int currentTurnNumber;

    public Match() {
        NonObjectiveCardDeckBuilder nonObjectiveCardDeckBuilder = new NonObjectiveCardDeckBuilder();
        CardDeckBuilder cardDeckBuilder = new CardDeckBuilder();

        this.objectiveCardsDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        this.starterCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        this.resourceCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        this.goldCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);

        currentResourceCards = new ArrayList<NonObjectiveCard>();
        currentGoldCards = new ArrayList<NonObjectiveCard>();
        commonObjectives = new ArrayList<Card>();

        this.players = new ArrayList<Player>();
    }

    public boolean enterLobbyPhase() {
        matchStatus = MatchStatus.LOBBY;
        return true;
    }

    public boolean addPlayer(String nickname) {
        for (int i=0; i<players.size(); i++) { // Player with similar nickname already present in list of players
            if (players.get(i).getNickname().equals(nickname)) return false;
        }
        // Nickname not in use
        Player newplayer = new Player(nickname);
        players.add(newplayer);
        return true;
    }

    public boolean deletePlayer(String nickname) {
        for (int i=0; i<players.size(); i++) {
            if (players.get(i).getNickname().equals(nickname)) {
                players.remove(i);
                return true;
            }
        }
        // Player with given nickname not found
        return false;
    }

    public boolean enterPreparationPhase() {
        matchStatus = MatchStatus.PREPARATION;
        return true;
    }

    public boolean assignRandomColoursToPlayers() {
        ArrayList<Colour> colour_array = new ArrayList<Colour>(Arrays.asList(Colour.values())); // Create ArrayList of colours
        colour_array.remove(Colour.BLACK); // Remove black from ArrayList

        Collections.shuffle(colour_array);

        for (int i=0; i<players.size(); i++) { // Assign colour to each player
            players.get(i).setColour(colour_array.get(i));
        }

        return true;
    }

    public boolean assignRandomStartingInitialCardsToPlayers() {
        for (int i=0; i<players.size(); i++) { // For each player
            NonObjectiveCard drawnCard = starterCardsDeck.draw(); // Draw a card from the starter cards deck
            players.get(i).assignStartingCard(drawnCard); // Place drawn card in player hand
        }
        return true;
    }

    public int getInitialCardPlayer(String nickname) {
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) { // Found player with correct nickname
                return players.get(i).getInitialCard().getId(); // Get initial card from player and return id
            }
        }
        // FIXME What if nickname is null?
        return -1;
    }

    public boolean createFieldPlayer(String nickname, boolean side) {
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) {
                players.get(i).initializeGameField(true);
            }
            return true;
        }
        return false;
        // TODO
    }

    public boolean assignRandomStartingResourceCardsToPlayers() {
        for (int i=0; i<players.size(); i++) { // For all players
            for (int j=0; j<2; j++) { // Puts 2 card in each player's hand
                NonObjectiveCard c = resourceCardsDeck.draw();
                players.get(i).putCardInHand(c);
            }
        }
        return true;
    }

    public boolean assignRandomStartingGoldCardsToPlayers() {
        for (int i=0; i<players.size(); i++) { // For all players
            NonObjectiveCard c = goldCardsDeck.draw(); // Puts 1 card in each player's hand
            players.get(i).putCardInHand(c);
        }
        return true;
    }

    public boolean pickRandomCommonObjectives() {
        for (int i=0; i<2; i++) {
            Card c = objectiveCardsDeck.draw();
            commonObjectives.add(c);
        }
        return true;
        // FIXME I don't know how useful this method is
    }

    public boolean assignRandomStartingSecretObjectivesToPlayers() {
        for (int i=0; i<players.size(); i++) {
            Card c1 = objectiveCardsDeck.draw();
            Card c2 = objectiveCardsDeck.draw();

            players.get(i).receiveSecretObjective(c1, c2);
        }
        return true;
        // TODO Need to complete method
    }

    public ArrayList<Integer> getSecretsObjectivesCardsPlayer(String nickname) {
        return null;
        // TODO
    }

    public boolean receiveSecretObjectiveChoiceFromPlayer(String nickname, int id) {
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) { // Found player with correct nickname
                players.get(i).secretObjectiveSelection(id);
            }
        }
        return true;
    }

    public boolean randomizePlayersOrder() {
        Collections.shuffle(players);
        // FIXME There is no first player attribute
        return true;
    }

    public boolean enterPlayingPhase() {
        matchStatus = MatchStatus.PLAYING;
        return true;
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
        matchStatus = MatchStatus.TERMINATING;
        return true;
        // TODO Need to double check
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
        matchStatus = MatchStatus.TERMINATED;
        return true;
        // TODO Need to double check
    }

    public boolean addObjectivePoints() {
        return false;
    }

    public ArrayList<String> getWinners() {
        return null;
    }

    public ArrayList<String> getPlayersNicknames() {
        return (ArrayList<String>)players.stream().map(p -> p.getNickname()).collect(Collectors.toList());
    }

    public ArrayList<Integer> getCurrentResourcesCards() {
        return (ArrayList<Integer>)currentResourceCards.stream().map(c -> c.getId()).collect(Collectors.toList());
    }

    public ArrayList<Integer> getCurrentGoldCards() {
        return (ArrayList<Integer>)currentGoldCards.stream().map(c -> c.getId()).collect(Collectors.toList());
    }

    public ArrayList<Integer> getCommonObjectives() {
        return (ArrayList<Integer>)commonObjectives.stream().map(c -> c.getId()).collect(Collectors.toList());
    }

    public int[] getPlayerResources(String nickname) {
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) { // Found player with correct nickname
                int[] playerRes = new int[7];
                for(ObjectType ob : ObjectType.values()) {
                    playerRes[ob.getValue()] = players.get(i).getField().getActiveRes(ob);
                }
                return playerRes;
            }
        }
        return null;
    }

    public int getPlayerSecretObjective(String nickname) {
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) { // Found player with correct nickname
                return players.get(i).getSecretObjective().getId();
            }
        }
        return -1; // FIXME Intellij marks error if this return is not present
    }

    public ArrayList<Integer> getPlayerHand(String nickname) {
        ArrayList<Integer> retArr = new ArrayList<Integer>();
        for (int i=0; i<players.size(); i++) { // Scan all players
            if (players.get(i).getNickname().equals(nickname)) { // Found player with correct nickname
                ArrayList<NonObjectiveCard> playerHand = players.get(i).getHand(); // Get player hand
                return (ArrayList<Integer>)playerHand.stream().map(c -> c.getId()).collect(Collectors.toList()); // Extract card ids
                // TODO Need to check if stream conversion works properly
            }
        }
        return null; // FIXME What if nickname = null
    }

    public ArrayList<Object> getPlayerField(String nickname) {
        return null;
        // TODO
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }
}
