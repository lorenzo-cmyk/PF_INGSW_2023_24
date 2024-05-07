package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Timer;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.*;
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
     * nodeList: A list of all the nodes that are currently connected to the game (RMI or Socket)
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
     * status: The status of the game controller
     */
    private GameControllerStatus status;

    public GameController(int id, int gameSize) {
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.timer = new Timer();
        this.id = id;
        this.gameSize = gameSize;

        // Enter lobby phase immediately
        model.enterLobbyPhase();
        status = GameControllerStatus.LOBBY;
    }

    /**
     * Assigns a new message to be delivered to the VirtualView of a given client.
     * This method is the primary way through which clients are notified of events. In exceptional cases, such as when joining a non-existent game,
     * this method is not used.
     *
     * @param message The message object to be delivered
     * @throws VirtualViewNotFoundException If the recipient's VirtualView could not be found among the listeners
     */
    protected void submitVirtualViewMessage(StoCMessage message) throws VirtualViewNotFoundException {
        for (PlayerQuadruple playerQuadruple : nodeList) { // Look through list of all connected players
            if (playerQuadruple.getNickname().equals((message.getRecipientNickname()))) { // If the correct recipient is found
                playerQuadruple.getVirtualView().addMessage(message); // Add the message to the recipient's VirtualView
                return;
            }
        }
        throw new VirtualViewNotFoundException("VirtualView for player " + message.getRecipientNickname() + " not found");
    }

    public void submitChatMessage(ChatMessage message){
        chat.addMessage(message);
    }

    public void disconnect(NodeInterface node) {
        //TODO
    }

    public void reconnect(NodeInterface node){
        //TODO
    }

    /**
     * Adds a player to the game. Gets called both when a player joins a game, and when a player creates a new game.
     * Adds the player to the list of players in the model, and creates a new VirtualView for the player
     *
     * @param nickname The nickname of the player to add
     * @param node The node of the player to add
     * @throws FullLobbyException If the lobby is already full
     */
    protected void addPlayer(String nickname, NodeInterface node) throws FullLobbyException, DuplicateNicknameException {
        if (model.getPlayersNicknames().size() == gameSize) throw new FullLobbyException("Lobby is full"); // Lobby is full

        model.addPlayer(nickname); // Add the player to the actual match instance

        VirtualView virtualView = new VirtualView(node); // Create new virtual view and link it to the client server node
        PlayerQuadruple newPlayerQuadruple = new PlayerQuadruple(node, nickname, true, virtualView);
        nodeList.add(newPlayerQuadruple);
        Configuration.getInstance().getExecutorService().submit(virtualView); // Start virtualView thread so that it can start listening for messages to send to the client
    }

    /**
     * Deletes a player from the game.
     * Methods used when a player leaves the game.
     *
     * @param nickname The nickname of the player to delete
     */
    public void deletePlayer(String nickname) {
        // TODO
    }

    /**
     * Method called when the lobby is full.
     * Enters the preparation phase of the game, assigns colours and starting cards to players, and notifies all players of the game start.
     */
    protected void enterPreparationPhase() {
        for (PlayerQuadruple playerQuadruple : nodeList) { // Notify all players that the game has started
            try {
                submitVirtualViewMessage(new GameStartedMessage(playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that the game has started");
            }
        }

        model.enterPreparationPhase();
        model.assignRandomColoursToPlayers();
        model.assignRandomStartingInitialCardsToPlayers();

        for (PlayerQuadruple playerQuadruple : nodeList) { // Notify all players of the new match status and of their assigned starting card
            try {
                // Notify the player of the status of the match
                submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                // Notify the player of the assigned starting card
                submitVirtualViewMessage(new AssignedStarterCardMessage(playerQuadruple.getNickname(), model.getPlayerHand(playerQuadruple.getNickname()).getFirst()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players of the new match status and of their assigned starting card");
            } catch (PlayerNotFoundException e) {
                throw new CriticalFailureException("PlayerNotFoundException when notifying players of the new match status and of their assigned starting card");
            }
        }

        status = GameControllerStatus.WAITING_STARTER_CARD_CHOICE;
    }

    /**
     * Sets the model to the terminated phase, and notifies all players that the game has ended.
     */
    private void enterEndPhase() {
        status = GameControllerStatus.GAME_ENDED;
        model.enterTerminatedPhase();

        try {
            model.addObjectivePoints();
            ArrayList<String> winners = model.getWinners();

            for (PlayerQuadruple playerQuadruple : nodeList) {
                // Notify the player of the status of the match
                submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                // Notify the player of the winners
                submitVirtualViewMessage(new MatchWinnersMessage(playerQuadruple.getNickname(), winners));
            }
        } catch (AlreadyComputedPointsException e) {
            throw new CriticalFailureException("Points have already been computed");
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that the game has ended");
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
        if (status != GameControllerStatus.WAITING_STARTER_CARD_CHOICE) { // Received a starter card side choice message, but the controller is not waiting for it
            try {
                submitVirtualViewMessage(new InvalidStarterCardSideSelectionMessage(nickname, "You cannot choose a starter card side at this time"));
                return;
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }
        // The controller is waiting for a starter card side choice
        try {
            if (model.getPlayerField(nickname) != null) { // The player has already chosen his starting card's side
                try {
                    submitVirtualViewMessage(new InvalidStarterCardSideSelectionMessage(nickname, "You have already chosen your starting card's side"));
                    return;
                } catch (VirtualViewNotFoundException e) {
                    throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
                }
            }

            model.createFieldPlayer(nickname, isUp); // Initialize the player's field
            // Notify the player that he has successfully chosen his starting card's side
            submitVirtualViewMessage(new ConfirmStarterCardSideSelectionMessage(nickname));

            boolean playersReady = true; // Assume all players are ready
            for (String playerNickname : model.getPlayersNicknames()) { // Scan all players in the current game
                if (model.getPlayerField(playerNickname) == null) { // If a player doesn't have an assigned secret objective, he has not yet chosen his secret objective
                    playersReady = false; // We have to wait for all other players to make their choice
                    break;
                }
            }

            if (playersReady) { // All players have selected their starting card's side
                 model.assignRandomStartingResourceCardsToPlayers();
                 model.assignRandomStartingGoldCardsToPlayers();
                 model.pickRandomCommonObjectives();
                 model.assignRandomStartingSecretObjectivesToPlayers();

                 status = GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE;

                 for (PlayerQuadruple playerQuadruple : nodeList) {
                     submitVirtualViewMessage(new AssignedSecretObjectiveCardMessage(playerQuadruple.getNickname(), model.getSecretObjectiveCardsPlayer(playerQuadruple.getNickname())));
                 }
            }
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
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
        if (status != GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE) { // Received a secret objective card choice message, but the controller is not waiting for it
            try {
                submitVirtualViewMessage(new InvalidSelectedSecretObjectiveCardMessage(nickname, "You cannot choose a secret objective card at this time"));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }
        // The controller is waiting for a secret objective card choice
        try {
            if (model.getPlayerSecretObjective(nickname) != -1) { // The player has already chosen his secret objective
                try {
                    submitVirtualViewMessage(new InvalidSelectedSecretObjectiveCardMessage(nickname, "You have already chosen your secret objective card"));
                    return;
                } catch (VirtualViewNotFoundException e) {
                    throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
                }
            }

            model.receiveSecretObjectiveChoiceFromPlayer(nickname, id); // Set the player's secret objective
            // Notify the player that he has successfully chosen his secret objective
            submitVirtualViewMessage(new ConfirmSelectedSecretObjectiveCardMessage(nickname));

            boolean playersReady = true; // Assume all players are ready
            for (String playerNickname : model.getPlayersNicknames()) { // Scan all players in the current game
                if (model.getPlayerSecretObjective(nickname) == -1) { // If a player doesn't have an assigned secret objective, he has not yet chosen his secret objective
                    playersReady = false; // We have to wait for all other players to make their choice
                    break;
                }
            }

            if (playersReady) { // All players have selected a secret objective card
                model.randomizePlayersOrder();
                model.enterPlayingPhase();
                model.startTurns();

                status = GameControllerStatus.WAITING_CARD_PLACEMENT;

                for (PlayerQuadruple playerQuadruple : nodeList) {
                    // Notify the player of the status of the match
                    submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                    // Notify the player of his current game status
                    submitVirtualViewMessage(generateResponseGameStatusMessage(playerQuadruple.getNickname()));
                    // Notify the players of the current player
                    submitVirtualViewMessage(new PlayerTurnMessage(playerQuadruple.getNickname(), model.getCurrentPlayerNickname()));
                }
            }
        } catch (InvalidSelectionException e) { // The player has chosen an invalid secret objective card
            try {
                submitVirtualViewMessage(new InvalidSelectedSecretObjectiveCardMessage(nickname, "Invalid secret objective card selection"));
            } catch (VirtualViewNotFoundException ex) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
        }
    }

    public void placeCard(String nickname, int id, int x, int y, boolean side) {
        if (status != GameControllerStatus.WAITING_CARD_PLACEMENT) { // The controller is not waiting for a card placement
            try {
                submitVirtualViewMessage(new PlaceCardFailedMessage(nickname, "You cannot place a card at this time"));
                return;
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }
        if (!nickname.equals(model.getCurrentPlayerNickname())) { // The player doesn't have the playing rights
            try {
                submitVirtualViewMessage(new PlaceCardFailedMessage(nickname, "It's not your turn to play. The current player is: " + model.getCurrentPlayerNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
            return;
        }
        // Player has the playing rights
        try {
            model.placeCard(id, x, y, side); // Try to place card

            // Notify the player that he has successfully placed the card
            submitVirtualViewMessage(new PlaceCardConfirmationMessage(nickname, model.getPlayerResources(nickname), model.getPlayerPoints(nickname)));

            if (model.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) {
                model.nextTurn();
                if (model.isFirstPlayer()) {
                    enterEndPhase();
                }
            }
            else {
                status = GameControllerStatus.WAITING_CARD_DRAW; // Update game status
            }
        } catch (InvalidSelectionException | InvalidPositionException | MissingRequirementsException e) {
            try {
                submitVirtualViewMessage(new PlaceCardFailedMessage(nickname, e.getMessage()));
            } catch (VirtualViewNotFoundException ex) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        } catch (PlayerNotFoundException | VirtualViewNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        }
    }

    public void drawCard(String nickname, int deckType, int id) {
        // FIXME If someone tries to draw a card when getCurrentPlayerNickname is null, server will crash
        if (!nickname.equals(model.getCurrentPlayerNickname())) { // The player doesn't have the playing rights
            try {
                submitVirtualViewMessage(new DrawCardFailedMessage(nickname, "It's not your turn to play. The current player is: " + model.getCurrentPlayerNickname()));
                return;
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }
        // Player has the playing rights
        if (status != GameControllerStatus.WAITING_CARD_DRAW) { // The current player hasn't yet placed a card
            try {
                submitVirtualViewMessage(new DrawCardFailedMessage(nickname, "You have not placed a card yet."));
                return;
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }

        try {
            model.drawCard(deckType, id);
            status = GameControllerStatus.WAITING_CARD_PLACEMENT; // Update game status

            // Notify the player that he has successfully drawn the card
            submitVirtualViewMessage(new DrawCardConfirmationMessage(nickname, id));

            model.nextTurn(); // Update turn number and current player

            // Notify the players of the current player
            submitVirtualViewMessage(new PlayerTurnMessage(nickname, model.getCurrentPlayerNickname()));

            // After updating the current player, we need to update the game state
            if (model.isFirstPlayer()) { // The first player is now playing
               if (model.areWeTerminating()) { // We are in the terminating phase
                   model.setLastTurn();

                   // Notify all players of the new match status
                   for (PlayerQuadruple playerQuadruple : nodeList) {
                       submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                   }
               }
            }
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        } catch (DrawException e) { // Player tried to draw a card, but the draw failed
            try {
                submitVirtualViewMessage(new DrawCardFailedMessage(nickname, e.getMessage()));
            } catch (VirtualViewNotFoundException ex) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found after failed draw");
            }
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
        }
    }

    /**
     * Method called when a message of type request game status is received.
     * The method sends a response game status message to the requester, updating them on the current state of the model.
     *
     * @param requesterNickname The nickname of the player that sent the message
     */
    public void sendGameStatus(String requesterNickname) {
        try {
            submitVirtualViewMessage(generateResponseGameStatusMessage(requesterNickname));
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualView for player " + requesterNickname + " not found");
        }
    }

    /**
     * Method called when a message of type request player field is received.
     * The method fetches the field of the player whose field is requested, and sends a response player field message to the requester.
     *
     * @param requesterNickname The nickname of the player that sent the request message
     * @param playerNickname The nickname of the player whose field is requested
     */
    public void sendPlayerField(String requesterNickname, String playerNickname) throws PlayerNotFoundException {
        try {
            submitVirtualViewMessage(new ResponsePlayerFieldMessage(requesterNickname, playerNickname, model.getPlayerField(playerNickname), model.getPlayerResources(playerNickname)));
        } catch (VirtualViewNotFoundException e) { // The requester's VirtualView could not be found
            throw new CriticalFailureException("VirtualView for player " + requesterNickname + " not found");
        }
    }

    /**
     * Generates a response game status message for a given player.
     * @param nickname The nickname of the player to generate the message for
     * @return The generated response game status message
     */
    protected PlayerGameStatusMessage generateResponseGameStatusMessage(String nickname) {
        try {
            ArrayList<String> playerNicknames = model.getPlayersNicknames();
            ArrayList<Integer> playerColours = (ArrayList<Integer>)model.getPlayersNicknames().stream().map(playerNickname -> {
                try {
                    return model.getPlayerColour(playerNickname);
                } catch (PlayerNotFoundException | NullColourException e) {
                    throw new CriticalFailureException("Could not generate game status for Player " + playerNickname);
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

            return new PlayerGameStatusMessage(nickname, playerNicknames, playerColours, playerHand, playerSecretObjective, playerPoints, playerColour, playerField, playerResources, gameCommonObjectives, gameCurrentResourceCards, gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus);
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        }
    }

    public ArrayList<ChatMessage> getAllChatHistory(){
        return chat.getHistory();
    }

    protected ArrayList<PlayerQuadruple> getNodeList() {
        return nodeList;
    }

    protected int getId() {
        return id;
    }

    protected int getGameSize() {
        return gameSize;
    }

    protected int getLobbyPlayerCount() {
        return model.getPlayersNicknames().size();
    }

    protected GameControllerStatus getStatus() {
        return status;
    }

    public Timer getTimer() {
        return timer;
    }
}

