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
import it.polimi.ingsw.am32.model.exceptions.*;
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
 * @author Jie
 * @author Antony
 * @author Lorenzo
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
     * Backup of matchStatus used for the rollback functionality.
     */
    private MatchStatus backupMatchStatus;
    /**
     * Nickname that identifies the current player.
     */
    private String currentPlayerNickname;
    /**
     * The number of the turn.
     */
    private int currentTurnNumber;

    /**
     * Constructor: Initialize a new Match instance. It builds the decks and place the needed cards on the field.
     */
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
     * @throws DuplicateNicknameException Nickname taken
     */
    public void addPlayer(String nickname) throws DuplicateNicknameException {
        for (Player player : players) {
            if (player.getNickname().equals(nickname))
                // Player with similar nickname already present in list of players
                throw new DuplicateNicknameException("Nickname cannot be used as it is already taken.");
        }
        // Nickname not in use
        Player newplayer = new Player(nickname);
        players.add(newplayer);
    }

    /**
     * Removes an existing player instance from the game.
     *
     * @param nickname The nickname of the player to delete from the game
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players
     */
    public void deletePlayer(String nickname) throws PlayerNotFoundException {
        if(!players.removeIf(player -> player.getNickname().equals(nickname))){
            throw new PlayerNotFoundException("Player not found in the list of players");
        }
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
        ArrayList<Colour> colour_array = new ArrayList<>(Arrays.asList(Colour.values())); // Create ArrayList of colours
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
     * @return id of the player's starting card if the player with the given nickname was found in the list of players
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players
     */
    public int getInitialCardPlayer(String nickname) throws PlayerNotFoundException{
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                return player.getInitialCard().getId(); // Get initial card from player and return id
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Initializes a particular player's field.
     *
     * @param nickname The nickname of the player whose field we want to initialize
     * @param side The side the starting card will be placed on the player's field
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players
     */
    public void createFieldPlayer(String nickname, boolean side) throws PlayerNotFoundException {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) {
                player.initializeGameField(side);
                return;
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
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
     * Receives the secret objective card selected by the player and saves it in the attribute
     * secretObjective.
     *
     * @param nickname Nickname of player who select the secret objective card.
     * @param id ID of the objective card selected.
     * @throws InvalidSelectionException if the card selected is not valid.
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     */
    public void receiveSecretObjectiveChoiceFromPlayer(String nickname, int id) throws InvalidSelectionException, PlayerNotFoundException {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                player.secretObjectiveSelection(id);
                return;
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Shuffles the players ArrayList
     */
    public void randomizePlayersOrder() {
        ArrayList<Player> originalOrder = new ArrayList<>(players);
        do {
            Collections.shuffle(players);
        } while (players.equals(originalOrder));
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
        currentPlayerNickname = players.getFirst().getNickname();
        currentTurnNumber = 1;
    }

    /**
     * Places a card on the field of the player with the given nickname.
     * @param id The id of the card to place
     * @param x The x-coordinate of the card
     * @param y The y-coordinate of the card
     * @param side The side of the card
     * @throws InvalidSelectionException if the card selected is not valid.
     * @throws MissingRequirementsException if the requirements of given card are not met.
     * @throws InvalidPositionException if the position selected is not valid position.
     * @throws PlayerNotFoundException if currentPlayerNickname was not found in the list of players.
     */
    public void placeCard(int id, int x, int y, boolean side) throws InvalidSelectionException,
            MissingRequirementsException, InvalidPositionException, PlayerNotFoundException {
        backupMatchStatus = matchStatus;
        for (int i=0; i<=players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayerNickname)) { // Found current player
                players.get(i).performMove(id, x, y, side); // Place card
                if (getMatchStatus()!=MatchStatus.LAST_TURN.getValue() && players.get(i).getPoints() >= 20) {
                    setTerminating();
                }
                return;
            }
        }
        throw new PlayerNotFoundException("currentPlayerNickname not found in the list of players");
    }

    /**
     * Rollback the last placement of the current player.
     * @throws RollbackException if the rollback is not possible.
     * @throws PlayerNotFoundException if currentPlayerNickname was not found in the list of players.
     */
    public void rollbackPlacement() throws RollbackException, PlayerNotFoundException {
        for (Player player : players) {
            if (player.getNickname().equals(currentPlayerNickname)) {
                player.rollbackMove();
                matchStatus = backupMatchStatus;
                return;
            }
        }
        throw new PlayerNotFoundException("currentPlayerNickname not found in the list of players");
    }

/**
 * Used to make the current player draw a card from the chosen deck.
 * @param deckType The type of deck from which the card is drawn. 0 for resourceCardsDeck, 1 for goldCardsDeck, 2 for currentResourceCards, 3 for currentGoldCards.
 * @param id If the deckType is 2 or 3, the id parameter is used to identify the card to be drawn.
 * @throws DrawException if the deckType selected for draw is not valid or the id of the card is not found. Gets also thrown if the deck is empty
 * @throws PlayerNotFoundException if the current player could not be found
 */
    public void drawCard(int deckType, int id) throws DrawException, PlayerNotFoundException {
        // Retrieve the player who is playing using the currentPlayerNickname
        for (Player player : players){
            if(player.getNickname().equals(currentPlayerNickname)){
                // Retrieve the card from the corresponding deck based on the deckType.
                Optional<NonObjectiveCard> card = switch (deckType) {
                    case 0 -> Optional.ofNullable(resourceCardsDeck.draw());
                    case 1 -> Optional.ofNullable(goldCardsDeck.draw());
                    case 2 ->
                        // Retrieve the card from the currentResourceCards list using the card ID. If the card is not found, return false.
                            currentResourceCards.stream().filter(c -> c.getId() == id).findFirst();
                    case 3 ->
                        // Retrieve the card from the currentGoldCards list using the card ID. If the card is not found, return false.
                            currentGoldCards.stream().filter(c -> c.getId() == id).findFirst();
                    default -> throw new DrawException("Invalid deck type.");
                };
                // If the card is found, and it's dawn from currentResourceCards or currentGoldCards, remove it from the
                // list and replenish it if it is possible.
                if(card.isPresent() && (deckType == 2 || deckType == 3)){
                    if(deckType == 2){
                        currentResourceCards.remove(card.get());
                        if(!resourceCardsDeck.getCards().isEmpty()){
                            currentResourceCards.add(resourceCardsDeck.draw());
                        }
                        else if(!goldCardsDeck.getCards().isEmpty()) { // If resourceCardsDeck is empty, draw from goldCardsDeck
                            currentResourceCards.add(goldCardsDeck.draw());
                        }
                    } else {
                        currentGoldCards.remove(card.get());
                        if(!goldCardsDeck.getCards().isEmpty()){
                            currentGoldCards.add(goldCardsDeck.draw());
                        }
                        else if(!resourceCardsDeck.getCards().isEmpty()) { // If the goldCardsDeck is empty, draw from the resourceCardsDeck
                            currentGoldCards.add(resourceCardsDeck.draw());
                        }
                    }
                    // After drawing a card, check if both decks are empty to set the Match in TERMINATING state.
                    if(resourceCardsDeck.getCards().isEmpty() && goldCardsDeck.getCards().isEmpty()) {
                        setTerminating();
                    }
                }
                // If the card is found, and it's dawn from resourceCardsDeck or goldCardsDeck, and the card is not
                // null, check if them are now both empty to set the Match in TERMINATING state.
                if((deckType == 0 || deckType == 1) && card.isPresent()){
                    if(resourceCardsDeck.getCards().isEmpty() && goldCardsDeck.getCards().isEmpty()){
                        setTerminating();
                    }
               }
                // If the card is found, add it to the player's hand.
                if(card.isPresent()){
                    player.putCardInHand(card.get());
                    return;
                } else {
                    throw new DrawException("Card not found.");
                }
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    public void nextTurn() {
        for (int i=0; i<players.size(); i++) {
            if (players.get(i).getNickname().equals(currentPlayerNickname)) {
                currentPlayerNickname = (i == players.size() - 1) ? players.getFirst().getNickname() : players.get(i+1).getNickname();
                currentTurnNumber = currentTurnNumber+1;
                return;
            }
        }
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
        return currentPlayerNickname.equals(players.getFirst().getNickname());
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
     * Control if the process of adding objective cards points to the total scores is successful.
     * @throws AlreadyComputedPointsException Tried to calculate points when they were already calculated.
     */
    public void addObjectivePoints() throws AlreadyComputedPointsException {
        for (Player player : players) {
            player.updatePointsForObjectives(commonObjectives);
            player.updatePointsForSecretObjective();
        }
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
     * Returns the points obtained from the objective cards of the player with the given nickname.
     *
     * @return The points obtained from the objective cards of the player with the given nickname.
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     */
    public int getPointsGainedFromObjectives(String nickname) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player.getPointsGainedFromObjectives();
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
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
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     * @exception NullFieldException if the player's field is null.
     */
    public int[] getPlayerResources(String nickname) throws PlayerNotFoundException {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                if(!isNull(player.getField())){ // Check if the player has a field (it should always have one, but just in case
                    int[] playerRes = new int[7];
                    for (ObjectType ob : ObjectType.values()) {
                        playerRes[ob.getValue()] = player.getField().getActiveRes(ob);
                    }
                    return playerRes;
                } else {
                    throw new NullFieldException("Player's field is null.");
                }
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Get id of the specified player's secret objective card.
     * @param nickname Nickname of player whose secret objective card should be analyzed.
     * @return If exists the nickname in the arraylist players and
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     */
    public int getPlayerSecretObjective(String nickname) throws PlayerNotFoundException {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                // Return the ID of the secret objective card of the player. If the player has no secret objective card, return -1.
                return isNull(player.getSecretObjective()) ? -1 : player.getSecretObjective().getId();
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Get the id of all cards in the Play's hand.
     *
     * @param nickname Nickname of player whose hand should be analyzed.
     * @return Array list of integer that contains the ID of all cards belong to the specific player's hand.
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     */
    public ArrayList<Integer> getPlayerHand(String nickname) throws PlayerNotFoundException {
        for (Player player : players) { // Scan all players
            if (player.getNickname().equals(nickname)) { // Found player with correct nickname
                ArrayList<NonObjectiveCard> playerHand = player.getHand(); // Get player hand
                return (ArrayList<Integer>) playerHand.stream().map(Card::getId).collect(Collectors.toList()); // Extract card ids
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * This method retrieves the field of a specific player in the game.
     * The field is represented as an ArrayList of integer arrays, where each array represents a card placed on
     * the field. The elements of the array are the x-coordinate, y-coordinate, id of the card, and a boolean value
     * (1 for true, 0 for false) indicating whether the card is face up.
     *
     * @param nickname The nickname of the player whose field we want to retrieve.
     * @return An ArrayList of integer arrays representing the player's field. Each array represents a card placed on
     * the field. The elements of the array are the x-coordinate, y-coordinate, id of the card, and a boolean
     * value (1 for true, 0 for false) indicating whether the card is face up. If no player with the provided
     * nickname is found, an empty ArrayList is returned. Returns null if the player's field has not yet been initialized.
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     */
    public ArrayList<int[]> getPlayerField(String nickname) throws PlayerNotFoundException {
        Optional<Player> playerOptional = players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst();

        if (playerOptional.isEmpty()) {
            throw new PlayerNotFoundException("Player not found in the list of players");
        }
        if (playerOptional.get().getField() == null) { // Player field has not yet been initialized
            return null;
        }

        return playerOptional.get().getField().getFieldCards().stream()
                .map(card -> new int[]{card.getX(), card.getY(), card.getNonObjectiveCard().getId(),
                        card.getIsUp() ? 1 : 0})
                .collect(Collectors.toCollection(ArrayList::new));
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
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     * @throws NullColourException if the player's colour is not set.
     */
    public int getPlayerColour(String nickname) throws PlayerNotFoundException, NullColourException {
        Optional<Player> playerOptional = players.stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst();

        if (playerOptional.isEmpty()) {
            throw new PlayerNotFoundException("Player not found in the list of players");
        }

        Colour playerColour = playerOptional.get().getColour();
        if (playerColour == null) {
            throw new NullColourException("Player's colour is not set");
        }

        return playerColour.getValue();
    }

    /**
     * Getter
     *
     * @return The nickname of the current Player.
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    /**
     * Getter. Used only for testing purposes.
     *
     * @return The arraylist which contains all players of this match.
     */
    protected ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Getter. Used only for testing purposes.
     *
     * @return The arraylist  of cards that make up the resource cards deck.
     */
    protected ArrayList<NonObjectiveCard> getResourceCardsDeck() {
        return resourceCardsDeck.getCards();
    }

    /**
     * Getter. Used only for testing purposes.
     *
     * @return The arraylist  of cards that make up the gold cards deck.
     */
    protected ArrayList<NonObjectiveCard> getGoldCardsDeck() {
        return goldCardsDeck.getCards();
    }

    /**
     * Getter. Get the points of a Player.
     *
     * @param nickname The nickname of the player whose points we want to get.
     * @throws PlayerNotFoundException if the player with the given nickname was not found in the list of players.
     * @return The points of the player with the given nickname.
     */
    public int getPlayerPoints(String nickname) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player.getPoints();
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Getter. Get the remaining cards in the resource card deck.
     *
     * @return The number of cards in the resource card deck.
     */
    public int getResourceCardDeckSize() {
        return resourceCardsDeck.getCards().size();
    }

    /**
     * Getter. Get the remaining cards in the gold card deck.
     *
     * @return The number of cards in the gold card deck.
     */
    public int getGoldCardDeckSize() {
        return goldCardsDeck.getCards().size();
    }

    /**
     * Getter. Get the list of all available spaces in the player's field upon which a card can be freely played.
     *
     * @param nickname The nickname of the player whose available spaces we want to get.
     * @return ArrayList of integer arrays containing the x and y coordinates of all available spaces in the player's field.
     */
    public ArrayList<int[]> getAvailableSpacesPlayer(String nickname) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player.availableSpacesPlayer();
            }
        }
        throw new PlayerNotFoundException("Player not found in the list of players");
    }

    /**
     * Getter. Get the Kingdom of the card ready to be drawn from the resource card deck (if not empty).
     *
     * @return The Kingdom of the card ready to be drawn from the resource card deck. If the deck is empty, return an empty optional.
     */
    public Optional<Integer> getNextResourceCardKingdom() {
        if (resourceCardsDeck.getCards().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resourceCardsDeck.getCards().getLast().getKingdom().getValue());
    }

    /**
     * Getter. Get the Kingdom of the card ready to be drawn from the gold card deck (if not empty).
     *
     * @return The Kingdom of the card ready to be drawn from the gold card deck. If the deck is empty, return an empty optional.
     */
    public Optional<Integer> getNextGoldCardKingdom() {
        if (goldCardsDeck.getCards().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(goldCardsDeck.getCards().getLast().getKingdom().getValue());
    }

}
