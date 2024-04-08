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
import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.player.Colour;
import it.polimi.ingsw.am32.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Primary class used to represent a single instance of a game.
 *
 * @author jie
 * @author anto
 */
public class Match implements ModelInterface {
    /**
     * Deck containing all the starter cards of the game.
     */
    private final NonObjectiveCardDeck starterCardsDeck;
    /**
     * Deck containing all the objective cards of the game.
     */
    private final CardDeck objectiveCardsDeck;
    /**
     * Deck containing all the resource cards of the game.
     */
    private final NonObjectiveCardDeck resourceCardsDeck;
    /**
     * Deck containing all the gold cards of the game.
     */
    private final NonObjectiveCardDeck goldCardsDeck;
    /**
     * Contains the resource cards being displayed on the field which may be drawn by the player.
     */
    private final ArrayList<NonObjectiveCard> currentResourceCards;
    /**
     * Contains the gold cards being displayed on the field which may be drawn by the player.
     */
    private final ArrayList<NonObjectiveCard> currentGoldCards;
    /**
     * Contains the 2 objective cards common to all players.
     */
    private final Card[] commonObjectives;
    /**
     * Contains the list of all players participating in the game.
     */
    private final ArrayList<Player> players;
    /**
     * Flag indicating the current status of the match.
     */
    private MatchStatus matchStatus;
    /**
     * Nickname that identifies the current player.
     */
    private String currentPlayerID;
    /**
     * The number of the turn.
     */
    private int currentTurnNumber;

    public Match() {
        // Initialize the deck-builders
        NonObjectiveCardDeckBuilder nonObjectiveCardDeckBuilder = new NonObjectiveCardDeckBuilder();
        CardDeckBuilder cardDeckBuilder = new CardDeckBuilder();
        // Build the decks
        this.objectiveCardsDeck = cardDeckBuilder.buildCardDeck(DeckType.OBJECTIVE);
        this.starterCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.STARTING);
        this.resourceCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.RESOURCE);
        this.goldCardsDeck = nonObjectiveCardDeckBuilder.buildNonObjectiveCardDeck(DeckType.GOLD);
        // Initialize the lists to store the deck cards visible on the field
        currentResourceCards = new ArrayList<>();
        currentGoldCards = new ArrayList<>();
        // Fill each list with 2 cards
        for (int i=0; i<2; i++) {
            currentResourceCards.add(resourceCardsDeck.draw());
            currentGoldCards.add(goldCardsDeck.draw());
        }
        // Initialize the common objectives array. The cards will be picked later as stated in the rules.
        commonObjectives = new Card[2];
        // Initialize the list of players
        this.players = new ArrayList<>();
    }

    /**
     * Sets the match status flag to LOBBY.
     */
    public void enterLobbyPhase() {
        matchStatus = MatchStatus.LOBBY;
    }

    /**
     * Adds a new player to the game.
     *
     * @param nickname The nickname of the player to add to the game
     * @return true if nickname was not already in use and new player instance could be created, false otherwise
     */
    public boolean addPlayer(String nickname) {
        for (Player player : players) {
            if (player.getNickname().equals(nickname))
                // Player with similar nickname already present in list of players
                return false;
        }
        // Nickname not in use
        Player newplayer = new Player(nickname);
        players.add(newplayer);
        return true;
    }

    /**
     * Removes an existing player instance from the game.
     *
     * @param nickname The nickname of the player to delete from the game
     * @return true if nickname was valid and player instance could be deleted, false otherwise
     */
    public boolean deletePlayer(String nickname) {
        return players.removeIf(player -> player.getNickname().equals(nickname));
    }

    /**
     * Sets the match status flag to PREPARATION.
     */
    public void enterPreparationPhase() {
        matchStatus = MatchStatus.PREPARATION;
    }

    /**
     * Assigns a random colour to each player in the game.
     */
    public void assignRandomColoursToPlayers() {
        ArrayList<Colour> colour_array = new ArrayList< >(Arrays.asList(Colour.values())); // Create ArrayList of colours
        colour_array.remove(Colour.BLACK); // Remove black from ArrayList

        Collections.shuffle(colour_array);

        for (int i=0; i<players.size(); i++) { // Assign colour to each player
            players.get(i).setColour(colour_array.get(i));
        }
    }

    /**
     * Assigns a random starting initial card to each player in the game
     */
    public void assignRandomStartingInitialCardsToPlayers() {
        for (Player player : players) { // For each player
            NonObjectiveCard drawnCard = starterCardsDeck.draw(); // Draw a card from the starter cards deck
            player.assignStartingCard(drawnCard); // Place drawn card in player hand
        }
    }

    /**
     * Returns the id of the starting card of a specific player.
     *
     * @param nickname The nickname of the player whose starting card we want to get
     * @return id of the player's starting card if the player with the given nickname was found in the list of players, -1 otherwise
     */
    public int getInitialCardPlayer(String nickname) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                return player.getInitialCard().getId(); // Get initial card from player and return id
            }
        }
        return -1;
    }

    /**
     * Initializes a particular player's field.
     *
     * @param nickname The nickname of the player whose field we want to initialize
     * @param side The side the starting card will be placed on the player's field
     * @return true if player's field could be initialized and the nickname was found in the list of players, false otherwise
     */
    public boolean createFieldPlayer(String nickname, boolean side) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) {
                player.initializeGameField(side);
                return true;
            }
        }
        return false;
    }

    /**
     * Assigns the two initial resource cards to each player at the beginning of the game
     */
    public void assignRandomStartingResourceCardsToPlayers() {
        for (Player player : players) { // For all players
            for (int j = 0; j < 2; j++) { // Puts 2 card in each player's hand
                NonObjectiveCard c = resourceCardsDeck.draw();
                player.putCardInHand(c);
            }
        }
    }

    /**
     * Assigns the two initial gold cards to each player at the beginning of the game
     */
    public void assignRandomStartingGoldCardsToPlayers() {
        for (Player player : players) { // For all players
            NonObjectiveCard c = goldCardsDeck.draw(); // Puts 1 card in each player's hand
            player.putCardInHand(c);
        }
    }

    /**
     * Selects the two initial common objective cards at the beginning of the game
     */
    public void pickRandomCommonObjectives() {
        for (int i=0; i<2; i++) {
            Card c = objectiveCardsDeck.draw();
            commonObjectives[i] = c;
        }
    }

    /**
     * Assigns to each player a secret objective card
     */
    public void assignRandomStartingSecretObjectivesToPlayers() {
        for (Player player : players) {
            Card c1 = objectiveCardsDeck.draw();
            Card c2 = objectiveCardsDeck.draw();
            player.receiveSecretObjective(c1, c2);
        }
    }

    /**
     * Returns the ids of the secret objective cards possessed by the player with the given nickname.
     *
     * @param nickname Nickname of the player whose secret objective card ids we want
     * @return An ArrayList containing the ids of the player's secret objective cards if the nickname was found in the list of players,
     * or an empty ArrayList otherwise
     */
    public ArrayList<Integer> getSecretObjectiveCardsPlayer(String nickname) {
        ArrayList<Integer> idCards = new ArrayList<>();
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                idCards.add(player.getTmpSecretObj()[0].getId());
                idCards.add(player.getTmpSecretObj()[1].getId());
            }
        }
        return idCards;
    }

    /**
     *  Receives the secret objective card selected by the player and saves it in the attribute
     *  secretObjective.
     * @param nickname Nickname of player who select the secret objective card.
     * @param id ID of the objective card selected.
     * @return True, if the player with that nickname exists and the selection process is successful, otherwise false.
     */
    public boolean receiveSecretObjectiveChoiceFromPlayer(String nickname, int id) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                return player.secretObjectiveSelection(id);
            }
        }
        return false;
    }

    /**
     * Shuffles the players ArrayList
     */
    public void randomizePlayersOrder() {
        Collections.shuffle(players);
    }

    /**
     * Sets the match status flag to PLAYING.
     */
    public void enterPlayingPhase() {
        matchStatus = MatchStatus.PLAYING;
    }

    /**
     * Resets current turn number to 1 and sets current player to the first player.
     */
    public void startTurns() {
        currentPlayerID = players.getFirst().getNickname();
        currentTurnNumber = 1;
    }

    public boolean placeCard(int id, int x, int y, boolean side) {
        for (int i=0; i<=players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayerID)) { // Found current player
                boolean success = players.get(i).performMove(id, x, y, side); // Place card

                if (!success) return false; // There was an error in the placement of the card

                if (players.get(i).getPoints() >= 20) {
                    setTerminating();
                }
                return true;
            }
        }
        return false;
    }

    public boolean drawCard(int deckType, int id) {
        // Retrieve the player who is playing using the currentPlayerID
        for (Player player : players){
            if(player.getNickname().equals(currentPlayerID)){
                // Retrieve the card from the corresponding deck based on the deckType.
                Optional<NonObjectiveCard> card;
                switch (deckType){
                    case 0:
                        card = Optional.ofNullable(resourceCardsDeck.draw());
                        break;
                    case 1:
                        card = Optional.ofNullable(goldCardsDeck.draw());
                        break;
                    case 2:
                        // Retrieve the card from the currentResourceCards list using the card ID. If the card is not found, return false.
                        card = currentResourceCards.stream().filter(c -> c.getId() == id).findFirst();
                        break;
                    case 3:
                        // Retrieve the card from the currentGoldCards list using the card ID. If the card is not found, return false.
                        card = currentGoldCards.stream().filter(c -> c.getId() == id).findFirst();
                        break;
                    default:
                        return false;
                }
                // If the card is found, and it's dawn from currentResourceCards or currentGoldCards, remove it from the
                // list and replenish it if it is possible.
                if(card.isPresent() && (deckType == 2 || deckType == 3)){
                    if(deckType == 2){
                        currentResourceCards.remove(card.get());
                        if(!resourceCardsDeck.getCards().isEmpty()){
                            currentResourceCards.add(resourceCardsDeck.draw());
                        }
                    } else {
                        currentGoldCards.remove(card.get());
                        if(!goldCardsDeck.getCards().isEmpty()){
                            currentGoldCards.add(goldCardsDeck.draw());
                        }
                    }
                }
                // If the card is found, and it's dawn from resourceCardsDeck or goldCardsDeck, and the card is not
                // null, check if them are now both empty to set the Match in TERMINATING state.
                if((deckType == 0 || deckType == 1) && card.isPresent()){
                    if(resourceCardsDeck.getCards().isEmpty() && goldCardsDeck.getCards().isEmpty()){
                        setTerminating();
                    }
                }
                // If the card is found, add it to the player's hand and return true. Otherwise, return false.
                return card.filter(player::putCardInHand).isPresent();
            }
        }
        return false;
    }

    public boolean nextTurn() {
        for (int i=0; i<players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayerID)) {
                currentPlayerID = (i == players.size() - 1) ? players.getFirst().getNickname() : players.get(i+1).getNickname();
                currentTurnNumber=currentTurnNumber+1;
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the match status flag to TERMINATING.
     */
    public void setTerminating() {
        matchStatus = MatchStatus.TERMINATING;
    }

    /**
     * Checks if game is in terminating phase
     *
     * @return true if and only if match status flag is set to TERMINATING
     */
    public boolean areWeTerminating() {
        return matchStatus == MatchStatus.TERMINATING;
    }

    /**
     * Checks if the current player is the first player in the list of players.
     *
     * @return true if and only if the first player in the list of players is playing
     */
    public boolean isFirstPlayer() {
        return currentPlayerID.equals(players.getFirst().getNickname());
    }

    /**
     * Sets the match status flag to LAST_TURN.
     */
    public void setLastTurn() {
        matchStatus = MatchStatus.LAST_TURN;
    }

    /**
     * Sets the match status flag to TERMINATED.
     */
    public void enterTerminatedPhase() {
        matchStatus = MatchStatus.TERMINATED;
    }

    /**
     * Control if the process of adding objective cards points to the total scores is successful
     * @return true, if everything is ok, false otherwise.
     */
    public boolean addObjectivePoints() {
        boolean check;
        for (Player player : players) {
            check = player.updatePointsForObjectives(commonObjectives) && player.updatePointsForSecretObjective();
            if(!check){
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the players that have accumulated the most points. If two players have gained the same amount of points,
     * the winner is the one with the highest number of points obtained by completing the objectives (secret or common).
     *
     * @return ArrayList containing the nicknames of all winners
     */
    public ArrayList<String> getWinners() {
        ArrayList<String> winners = new ArrayList<>();
        int tmpScoreObj = 0;
        int maxPoints = 0;
        for (Player value : players) {
            if (value.getPoints() > maxPoints)
                maxPoints = value.getPoints();
        } // search maxPoints.
        for (Player player : players) {
            if (player.getPoints() == maxPoints) { // Found the player who has maxPoints
                if (player.getPointsGainedFromObjectives() > tmpScoreObj) { // Compare the points gained from the Objective cards
                    tmpScoreObj = player.getPointsGainedFromObjectives();
                    if (!winners.isEmpty()) {
                        winners.clear();
                    }
                    winners.add(player.getNickname());
                }
                else if (player.getPointsGainedFromObjectives() == tmpScoreObj) {
                    winners.add(player.getNickname());
                }
            }
        }
        return winners;
    }

    /**
     * Returns all players' nicknames
     * @return ArrayList containing all the players' nicknames
     */
    public ArrayList<String> getPlayersNicknames() {
        return (ArrayList<String>)players.stream().map(Player::getNickname).collect(Collectors.toList());
    }

    /**
     * Returns the ids of all the drawable face up resource cards.
     *
     * @return ArrayList containing the ids of all the drawable face up resource cards
     */
    public ArrayList<Integer> getCurrentResourcesCards() {
        return (ArrayList<Integer>)currentResourceCards.stream().map(Card::getId).collect(Collectors.toList());
    }

    /**
     * Returns the ids of all the drawable face up gold cards.
     *
     * @return ArrayList containing the ids of all the drawable face up gold cards
     */
    public ArrayList<Integer> getCurrentGoldCards() {
        return (ArrayList<Integer>)currentGoldCards.stream().map(Card::getId).collect(Collectors.toList());
    }

    /**
     * Returns the ids of all the common objective cards.
     *
     * @return ArrayList containing the ids of all the common objective cards
     */
    public ArrayList<Integer> getCommonObjectives() {
        ArrayList<Integer> idCommonObj = new ArrayList<>();
        for (Card commonObjective : commonObjectives) {
            // We should not return the Card ID if its null!
            if(!isNull(commonObjective)){
                int id = commonObjective.getId();
                idCommonObj.add(id);
            }
        }
        return idCommonObj;
    }

    /**
     * Returns a given player's field's active resources.
     *
     * @param nickname Nickname of player whose active resources we want to obtain
     * @return An array of integers containing the count of active resources in the player's field
     */
    public int[] getPlayerResources(String nickname) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                if(!isNull(player.getField())){ // Check if the player has a field (it should always have one, but just in case
                    int[] playerRes = new int[7];
                    for (ObjectType ob : ObjectType.values()) {
                        playerRes[ob.getValue()] = player.getField().getActiveRes(ob);
                    }
                    return playerRes;
                }
            }
        }
        return null;
    }

    /**
     * Get id of the specified player's secret objective card.
     * @param nickname Nickname of player whose secret objective card should be analyzed.
     * @return If exists the nickname in the arraylist players and
     */
    public int getPlayerSecretObjective(String nickname) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                // Return the ID of the secret objective card of the player. If the player has no secret objective card, return -1.
                return isNull(player.getSecretObjective()) ? -1 : player.getSecretObjective().getId();
            }
        }
        return -1;
    }

    /**
     * Get the id of all cards in the Play's hand.
     *
     * @param nickname Nickname of player whose hand should be analyzed.
     * @return Array list of integer that contains the ID of all cards belong to the specific player's hand.
     */
    public ArrayList<Integer> getPlayerHand(String nickname) {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                ArrayList<NonObjectiveCard> playerHand = player.getHand(); // Get player hand
                return (ArrayList<Integer>) playerHand.stream().map(Card::getId).collect(Collectors.toList()); // Extract card ids
            }
        }
        return null; // No player with given nickname found
    }

    /**
     * Get all cards placed in the field of the specific player
     *
     * @param nickname Nickname of player whose field should be returned
     * @return The ArrayList of the cards placed in the Field of given player, through which we could get ID, position
     * and side of every card placed in the field.
     */
    public ArrayList<CardPlaced> getPlayerField(String nickname) {
        ArrayList<CardPlaced> playerField = null;
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                playerField = player.getField().getFieldCards();
            }
        }
        return playerField;
    }

    /**
     * Getter
     *
     * @return Status of match
     */
    public int getMatchStatus() {
        // Return the integer value associated with the current match status. If the matchStatus is null, return -1.
        return isNull(matchStatus) ? -1 : matchStatus.getValue();
    }

    /**
     * Getter
     *
     * @return Number of the current turn
     */
    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    /**
     * Getter
     *
     * @param nickname The nickname of the player whose colour we want to get.
     * @return The colour of a player.
     */
    public int getPlayerColour(String nickname) {
        return players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst()
                .map(player -> player.getColour() != null ? player.getColour().getValue() : -1)
                .orElse(-1);
    }

    /**
     * Getter
     *
     * @return The nickname of the current Player.
     */
    public String getCurrentPlayerID() {
        return currentPlayerID;
    }

}
