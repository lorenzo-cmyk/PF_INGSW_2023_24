package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Timer;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.VirtualViewNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import it.polimi.ingsw.am32.model.exceptions.*;
import it.polimi.ingsw.am32.model.match.Match;
import it.polimi.ingsw.am32.model.match.MatchStatus;
import it.polimi.ingsw.am32.network.NodeInterface;
import it.polimi.ingsw.am32.model.ModelInterface;

/**
 * Represents a controller for a single game.
 * Manages a single model instance, and a chat instance
 *
 * @author Anto
 */
public class GameController implements GameControllerInterface {
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
     * gameSize: The number of players in the game at fully capacity
     */
    private final int gameSize;
    /**
     * placedCardFlag: Flag indicating whether a card has been placed by the current player
     * Used to prevent the same player from placing 2 cards in a row without drawing
     */
    private boolean placedCardFlag;

    public GameController(int id, int gameSize) {
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.timer = null;
        this.id = id;
        this.gameSize = gameSize;
        this.placedCardFlag = false;

        // Enter lobby phase immediately
        model.enterLobbyPhase();
    }

    /**
     * Assigns a new message to be delivered to the VirtualView of a given client.
     * This method is the primary way through which clients are notified of events. In exceptional cases, such as when joining a non-existent game,
     * this method is not used.
     *
     * @param message The message object to be delivered
     * @throws VirtualViewNotFoundException If the recipient's VirtualView could not be found among the listeners
     */
    public void submitVirtualViewMessage(StoCMessage message) throws VirtualViewNotFoundException {
        for (PlayerQuadruple playerQuadruple : nodeList) { // Look through list of all connected players
            if (playerQuadruple.getNickname().equals((message.getRecipientNickname()))) { // If the correct recipient is found
                playerQuadruple.getVirtualView().addMessage(message); // Add the message to the recipient's VirtualView
            }
        }
        throw new VirtualViewNotFoundException("VirtualView for player " + message.getRecipientNickname() + " not found");
    }

    public void submitChatMessage(ChatMessage message){
        chat.addMessage(message);
    }

    public ArrayList<ChatMessage> getAllChatHistory(){
        return chat.getHistory();
    }

    public void disconnect(NodeInterface node) {
        //TODO
    }

    public void reconnect(NodeInterface node){
        //TODO
    }

    /**
     * Adds a player to the game.
     * Adds the player to the list of players in the model, and creates a new VirtualView for the player
     *
     * @param nickname The nickname of the player to add
     * @param node The node of the player to add
     * @throws FullLobbyException If the lobby is already full
     */
    public void addPlayer(String nickname, NodeInterface node) throws FullLobbyException {
        if (model.getPlayersNicknames().size() == gameSize) throw new FullLobbyException("Lobby is full"); // Lobby is full

        try {
            model.addPlayer(nickname); // Add the player to the actual match instance

            VirtualView virtualView = new VirtualView(node); // Create new virtual view and link it to the client server node
            PlayerQuadruple newPlayerQuadruple = new PlayerQuadruple(node, nickname, true, virtualView);
            nodeList.add(newPlayerQuadruple);
            Configuration.getInstance().getExecutorService().submit(virtualView); // Start virtualView thread so that it can start listening for messages to send to the client
        } catch (DuplicateNicknameException e){
            // TODO
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
     * Starts the game. Gets called when the lobby is full
     */
    public void startGame() {
        for (PlayerQuadruple playerQuadruple : nodeList) { // Notify all players that the game has started
            try {
                submitVirtualViewMessage(new GameStartedMessage(playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                // TODO
            }
        }

        model.enterPreparationPhase();
        model.assignRandomColoursToPlayers();
        model.assignRandomStartingInitialCardsToPlayers();

        for (PlayerQuadruple playerQuadruple : nodeList) { // Notify all players of their assigned colour
            try {
                // Notify the player of the status of the match
                submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                // Notify the player of the assigned starting card
                submitVirtualViewMessage(new AssignedStarterCardMessage(playerQuadruple.getNickname(), model.getPlayerHand(playerQuadruple.getNickname()).getFirst()));
            } catch (VirtualViewNotFoundException e) {
                // TODO
            } catch (PlayerNotFoundException e) {
                // TODO
            }
        }
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

    /**
     * Generates a response game status message for a given player.
     * @param nickname The nickname of the player to generate the message for
     * @return The generated response game status message
     */
    protected ResponseGameStatusMessage generateResponseGameStatusMessage(String nickname) {
        try {
            String recipientNickname = nickname;
            ArrayList<String> playerNicknames = model.getPlayersNicknames();
            ArrayList<Integer> playerColours = (ArrayList<Integer>)model.getPlayersNicknames().stream().map(playerNickname -> {
                try {
                    return model.getPlayerColour(playerNickname);
                } catch (PlayerNotFoundException | NullColourException e) {
                    // TODO
                    return -1;
                }
            }).toList();
            ArrayList<Integer> playerHand = model.getPlayerHand(nickname);
            int playerSecretObjective = model.getPlayerSecretObjective(nickname);
            int playerPoints = model.getPlayerPoints(nickname);
            int playerColour = model.getPlayerColour(nickname);
            ArrayList<int[]> playerField = model.getPlayerField(nickname);
            int[] playerResources = model.getPlayerResources(nickname);
            ArrayList<Integer> gameCommonObjectives = model.getCommonObjectives();
            ArrayList<Integer> gameCurrentResourceCards = model.getCurrentResourcesCards();
            ArrayList<Integer> gameCurrentGoldCards = model.getCurrentGoldCards();
            int gameResourcesDeckSize = model.getCurrentResourcesCards().size();
            int gameGoldDeckSize = model.getCurrentGoldCards().size();
            int matchStatus = model.getMatchStatus();

            return new ResponseGameStatusMessage(recipientNickname, playerNicknames, playerColours, playerHand, playerSecretObjective, playerPoints, playerColour, playerField, playerResources, gameCommonObjectives, gameCurrentResourceCards, gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus);
        } catch (PlayerNotFoundException e) {
            // TODO
            return null;
        }
    }

    public ArrayList<PlayerQuadruple> getNodeList() {
        return nodeList;
    }

    public int getId() {
        return id;
    }

    public int getGameSize() {
        return gameSize;
    }

    public int getLobbyPlayerCount() {
        return model.getPlayersNicknames().size();
    }
}

