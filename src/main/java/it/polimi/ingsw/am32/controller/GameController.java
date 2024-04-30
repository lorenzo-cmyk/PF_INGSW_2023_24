package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Timer;

import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.controller.exceptions.ListenerNotFoundException;
import it.polimi.ingsw.am32.model.exceptions.*;
import it.polimi.ingsw.am32.model.match.Match;
import it.polimi.ingsw.am32.model.match.MatchStatus;
import it.polimi.ingsw.am32.network.NodeInterface;
import it.polimi.ingsw.am32.network.RMIServerNode;
import it.polimi.ingsw.am32.model.ModelInterface;

/**
 * Represents a controller for a single game.
 * Manages a single model instance, and a chat instance
 *
 * @author Anto
 */
public class GameController implements GameControllerInterface {
    /**
     * listeners: A list of all the VirtualViews that are currently connected to the game and are listening for outgoing messages
     */
    private final ArrayList<VirtualView> listeners;
    /**
     * nodeList: A list of all the nodes that are currently connected to the game (rmi or socket)
     */
    private final ArrayList<PlayerQuadruple> nodeList;
    /**
     * model: The model of the game
     */
    private final ModelInterface model;
    /**
     * chat: The chat of the game
     */
    private final Chat chat;
    /**
     * timer: The timer of the game
     */
    private final Timer timer;
    /**
     * id: The id of the game
     */
    private final int id;
    /**
     * gamePlayerCount: The number of players in the game
     */
    private final int gamePlayerCount;
    /**
     * placedCardFlag: Flag indicating whether a card has been placed by the current player
     * Used to prevent the same player from placing 2 cards in a row without drawing
     */
    private boolean placedCardFlag;

    public GameController(int id, int playerCount) {
        this.listeners = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.timer = null;
        this.id = id;
        this.gamePlayerCount = playerCount;
        this.placedCardFlag = false;

        // Enter lobby phase immediately
        model.enterLobbyPhase();
    }

    public void submitChatMessage(ChatMessage message){
        chat.addMessage(message);
    }

    protected void addNode(NodeInterface node, String nickname, boolean connected){
        // Used for testing
        // TODO: Should we check for duplicate nodes in nodeList?
    }

    public ArrayList<ChatMessage> getAllChatHistory(){
        return chat.getHistory();
    }

    protected void addListener(VirtualView listener) {
        // Used for testing
        // TODO: Check for duplicate listeners?
    }

    protected void removeListener(VirtualView listener) throws ListenerNotFoundException {
        // Used for testing
        // TODO
    }

    public void disconnect(NodeInterface node) {
        //TODO
    }

    public void reconnect(NodeInterface node){
        //TODO
    }

    public RMIServerNode reconFromDeath(String nickname){
        //TODO
        return null;
    }

    /**
     * Adds a player to the game.
     * Method used when a player joins the game.
     *
     * @param nickname The nickname of the player to add
     * @param node The node of the player to add
     */
    public void addPlayer(String nickname, NodeInterface node) {
        try {
            model.addPlayer(nickname);

            VirtualView virtualView = new VirtualView(node);
            PlayerQuadruple playerQuadruple = new PlayerQuadruple(node, nickname, true, virtualView);
            nodeList.add(playerQuadruple);
        } catch (DuplicateNicknameException e){
           throw new CriticalFailureException("Player " + nickname + " already in game");
        }
    }

    /**
     * Deletes a player from the game.
     * Methods used when a player leaves the game.
     *
     * @param nickname The nickname of the player to delete
     */
    public void deletePlayer(String nickname) {
        try {
            model.deletePlayer(nickname);
        } catch (PlayerNotFoundException e) {
            // TODO
        }
    }

    /**
     * Starts the game.
     * Method used when the game is ready to start.
     */
    public void startGame() {
        model.enterPreparationPhase();
        model.assignRandomColoursToPlayers();
        model.assignRandomStartingInitialCardsToPlayers();

        // TODO Notify all listeners
    }

    /**
     * Method called when a message of type start card side selection is received.
     * The method initializes the player's field and checks if all players have chosen their starting card's side.
     *
     * @param nickname The nickname of the player that sent the message
     * @param isUp The side of the starting card that the player has chosen
     */
    public void chooseStarterCardSide(String nickname, boolean isUp) {
        try {
            model.createFieldPlayer(nickname, isUp); // Initialize the player's field
            // TODO Notify the player that his field has been initialized?

            boolean playersReady = true; // Assume all players are ready
            for (String playerNickname : model.getPlayersNicknames()) { // Scan all players in the current game
                if (model.getPlayerField(playerNickname) == null) { // If a player doesn't have an assigned secret objective, he has not yet chosen his secret objective
                    playersReady = false; // We have to wait for all other players to make their choice
                    break;
                }
            }

            if (playersReady) { // All players have selected their starting card's side
                // TODO Notify all listeners

                 model.assignRandomStartingResourceCardsToPlayers();
                 model.assignRandomStartingGoldCardsToPlayers();
                 model.pickRandomCommonObjectives();
                 model.assignRandomStartingSecretObjectivesToPlayers();

                 // TODO Notify all listeners
            }
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        }
    }

    /**
     * Method called when a message of type secret objective card choice is received.
     * The method assigns the player's secret objective and checks if all players have chosen their secret objective.
     *
     * @param nickname The nickname of the player that sent the message
     * @param id The id of the secret objective card that the player has chosen
     */
    public void chooseSecretObjectiveCard(String nickname, int id) {
        try {
            model.receiveSecretObjectiveChoiceFromPlayer(nickname, id); // Set the player's secret objective

            boolean playersReady = true; // Assume all players are ready
            for (String playerNickname : model.getPlayersNicknames()) { // Scan all players in the current game
                if (model.getPlayerSecretObjective(nickname) == -1) { // If a player doesn't have an assigned secret objective, he has not yet chosen his secret objective
                    playersReady = false; // We have to wait for all other players to make their choice
                    break;
                }
            }

            if (playersReady) { // All players have selected a secret objective card
                // TODO Notify all listeners

                model.randomizePlayersOrder();
                model.enterPlayingPhase();
                model.startTurns();
            }
        } catch (InvalidSelectionException e) {
            // TODO
        } catch (PlayerNotFoundException e) {
            // TODO
        }
    }

    public void placeCard(String nickname, int id, int x, int y, boolean side) {
        if (!nickname.equals(model.getCurrentPlayerNickname())) {
            // TODO Notify the player that it isn't his turn to play
            return;
        }
        // Player has the playing rights
        try {
            model.placeCard(id, x, y, side); // Try to place card
            placedCardFlag = true; // Card has been placed successfully
            // TODO Notify player of valid placement
        } catch (InvalidSelectionException e) {
            // TODO
        } catch (MissingRequirementsException e) {
            // TODO
        } catch (InvalidPositionException e) {
            // TODO
        } catch (PlayerNotFoundException e) {
            // TODO
        }
    }

    public void drawCard(String nickname, int deckType, int id) {
        if (!nickname.equals(model.getCurrentPlayerNickname())) {
            // TODO Notify the player that it isn't his turn to play
            return;
        }
        // Player has the playing rights
        if (!placedCardFlag) { // The current player hasn't yet placed a card
           // TODO Notify the player that he hasn't yet placed a card
            return;
        }
        if (model.getMatchStatus()!= MatchStatus.LAST_TURN.getValue()) {
            // TODO Notify the player that he cannot draw on the last turn
        }

        try {
            model.drawCard(deckType, id);
            placedCardFlag = false; // Card has been drawn successfully
            // TODO Notify the player of the drawn card
            model.nextTurn(); // Update turn number and current player
            // TODO Notify the players of the new current player

            // After updating the current player, we need to update the game state
            if (model.isFirstPlayer()) { // The first player is now playing
               if (model.areWeTerminating()) { // We are in the terminating phase
                   model.setLastTurn();
                   // TODO Notify the players that we have entered the last turn phase
               } else if (model.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) {
                   model.enterTerminatedPhase();
                   // TODO Notify the players that the game has ended
                   // TODO Destroy game?
               }
            }
        } catch (PlayerNotFoundException e) {
            // TODO
        } catch (DrawException e) {
            // TODO
        }
    }

    public int getId() {
        return id;
    }

    public int getGamePlayerCount() {
        return gamePlayerCount;
    }

    public int getLobbyPlayerCount() {
        return model.getPlayersNicknames().size();
    }
}

