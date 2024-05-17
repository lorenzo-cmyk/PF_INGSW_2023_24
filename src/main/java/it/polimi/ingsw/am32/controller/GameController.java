package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Collectors;

import it.polimi.ingsw.am32.Utilities.Configuration;
import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import it.polimi.ingsw.am32.model.exceptions.*;
import it.polimi.ingsw.am32.model.match.Match;
import it.polimi.ingsw.am32.model.match.MatchStatus;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import it.polimi.ingsw.am32.model.ModelInterface;

/**
 * Represents a controller for a single game.
 * Manages a single model instance, and a chat instance
 *
 * @author Anto
 */
public class GameController {
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
    /**
     * lastOnlinePlayer: The nickname of the last player that was online
     */
    private String lastOnlinePlayer;

    /**
     * Constructor for the GameController class. Initializes the game controller with the given id and game size.
     * The game controller is initialized in the lobby phase. There is one GameController instance for each game.
     *
     * @param id The id of the game
     * @param gameSize The number of players in the game at fully capacity
     */
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

    /**
     * Submits a chat message to the chat history.
     * If the message is a broadcast message, it is sent to all players in the game.
     * If the message is a direct message, it is sent only to the recipient.
     *
     * @param message The message to be submitted
     */
    public synchronized void submitChatMessage(ChatMessage message) {
        // Exception should be thrown if message sender does not exist
        if (nodeList.stream().noneMatch(n -> n.getNickname().equals(message.getSenderNickname()))) {
            throw new CriticalFailureException("Sender " + message.getSenderNickname() + " not found");
        }

        // Message sender does exist
        if (message.isMulticastFlag()) { // Broadcast message
            for (PlayerQuadruple playerQuadruple : nodeList) { // Notify all players
                try {
                    submitVirtualViewMessage(new OutboundChatMessage(playerQuadruple.getNickname(), message.getSenderNickname(), message.getMessageContent()));
                } catch (VirtualViewNotFoundException e) { // The recipient's VirtualView could not be found when attempting to notify players in the game
                    throw new CriticalFailureException("VirtualViewNotFoundException when broadcasting chat message");
                }
            }
        } else { // Direct message
            boolean found = false; // Flag indicating whether the recipient of the message appears in the list of players
            for (PlayerQuadruple playerQuadruple : nodeList) { // Scan list of all players
                if (playerQuadruple.getNickname().equals(message.getRecipientNickname())) { // Found recipient of message
                    try {
                        submitVirtualViewMessage(new OutboundChatMessage(message.getRecipientNickname(), message.getSenderNickname(), message.getMessageContent()));
                        found = true;
                        break;
                    } catch (VirtualViewNotFoundException e) { // The recipient's VirtualView could not be found when attempting to notify a single recipient; message was malformed
                        throw new CriticalFailureException("VirtualViewNotFoundException when sending direct chat message");
                    }
                }
            }
            if (!found) { // The recipient could not be found among the players in the game
                try {
                    submitVirtualViewMessage(new InvalidInboundChatMessage(message.getSenderNickname(), "Recipient " + message.getRecipientNickname() + " not found"));
                    return;
                } catch (VirtualViewNotFoundException e) {
                    throw new CriticalFailureException("VirtualViewNotFoundException when alerting the sender that the recipient of a chat message could not be found");
                }
            }
        }
        // Add the message to the chat history, regardless of whether it was broadcast or direct
        chat.addMessage(message); // Adds the message to the chat history
    }

    /*
    Disconnection types:
    - Player disconnects during the lobby phase
    - Player disconnects before game has started
    - Player disconnects after game has started but is not current player
    - Player disconnects after game has started and is current player (has placed but not yet drawn)
    - Player disconnects after game has started and is current player (has not yet placed)
    - Player disconnects after game has ended
     */

    /**
     * Method called when a player disconnects from the game.
     * Handles the disconnection based on the current status of the game.
     *
     * @param node The node of the player that has disconnected
     */
    public synchronized void disconnect(NodeInterface node) {
        PlayerQuadruple playerQuadruple = nodeList.stream().filter(pq -> pq.getNode().equals(node)).findFirst().orElse(null); // Get the player quadruple associated with the disconnected player

        if (playerQuadruple == null) { // The player quadruple could not be found
            throw new CriticalFailureException("Player quadruple not found when disconnecting player");
        }

        // The player quadruple was found
        if (status == GameControllerStatus.LOBBY) { // We are in the lobby phase
            disconnectDuringLobby(playerQuadruple);
        }
        else if (status == GameControllerStatus.WAITING_STARTER_CARD_CHOICE || status == GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE) { // We are in the preparation phase
            disconnectBeforeGameStart(playerQuadruple);
        }
        // We are in the playing phase
        else if (!model.getCurrentPlayerNickname().equals(playerQuadruple.getNickname())) { // We are not the current player
            disconnectNotCurrentPlayer(playerQuadruple);
        }
        // We are the current player
        else if (status == GameControllerStatus.WAITING_CARD_PLACEMENT) { // We have not yet placed a card
            disconnectCurrentPlayerBeforePlacing(playerQuadruple);
        }
        else if (status == GameControllerStatus.WAITING_CARD_DRAW) { // We have placed a card but not yet drawn one
            disconnectCurrentPlayerAfterPlacing(playerQuadruple);
        }
    }

    /**
     * Method called when a player disconnects during the lobby phase.
     * Removes the player from the game, notifies all players that the player has left the lobby, and shuts down the player's VirtualView.
     *
     * @param playerQuadruple The PlayerQuadruple of the player that has disconnected
     */
    private void disconnectDuringLobby(PlayerQuadruple playerQuadruple) {
        // Delete player from model
        try {
            model.deletePlayer(playerQuadruple.getNickname());
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player not found when deleting player from model");
        }

        // Shutdown virtual view
        playerQuadruple.getVirtualView().setTerminating();

        // Remove player quadruple
        nodeList.remove(playerQuadruple);

        // Notify all players that a player has left the lobby
        ArrayList<String> allPlayerNicknames = getNodeList().stream()
                .map(PlayerQuadruple::getNickname)
                .collect(Collectors.toCollection(ArrayList::new));
        for (PlayerQuadruple playerQuadruple1 : nodeList) {
            try {
                submitVirtualViewMessage(new LobbyPlayerListMessage(playerQuadruple1.getNickname(), allPlayerNicknames));
                submitVirtualViewMessage(new PlayerDisconnectMessage(playerQuadruple1.getNickname(), playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that a player has left the lobby");
            }
        }
    }

    /**
     * Method called when a player disconnects before the game has started, in the preparation phase such as
     * when players are choosing the side of their starter card.
     * Sets the player status to "Disconnected" hoping that it will reconnect eventually.
     *
     * @param playerQuadruple The PlayerQuadruple of the player that has disconnected
     */
    private void disconnectBeforeGameStart(PlayerQuadruple playerQuadruple) {
        // Set player state to disconnected
        playerQuadruple.setConnected(false);

        // Notify all players that a player has left the game
        for (PlayerQuadruple playerQuadruple1 : nodeList) {
            try {
                submitVirtualViewMessage(new PlayerDisconnectMessage(playerQuadruple1.getNickname(), playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that a player has left the game");
            }
        }
    }

    /**
     * Method called when a player disconnects after the game has started but is not the current player.
     * Sets the player status to "Disconnected" hoping that it will reconnect eventually.
     * Notifies all players that a player has left the game.
     * If only one player remain connected, starts a timer for winner declaration.
     *
     * @param playerQuadruple The PlayerQuadruple of the player that has disconnected
     */
    private void disconnectNotCurrentPlayer(PlayerQuadruple playerQuadruple) {
        // Set player state to disconnected
        playerQuadruple.setConnected(false);

        // Notify all players that a player has left the game
        for (PlayerQuadruple playerQuadruple1 : nodeList) {
            try {
                submitVirtualViewMessage(new PlayerDisconnectMessage(playerQuadruple1.getNickname(), playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that a player has left the game");
            }
        }

        if(nodeList.stream().filter(PlayerQuadruple::isConnected).count() == 1) {
            lastOnlinePlayer = Objects.requireNonNull(nodeList.stream().filter(PlayerQuadruple::isConnected).findFirst().orElse(null)).getNickname();
            timer.schedule(new EndMatchDueToDisconnectionTimerTask(this), 1000 * 60 * 2);
            // FIXME: The timer is set to 2 minutes for testing purposes. The actual time should be read from the Configuration class.
        }
    }


    /**
     * Method called when a player disconnects after the game has started and is the current player, and has already placed a card (but not yet drawn).
     * Sets the player status to "Disconnected" hoping that it will reconnect eventually.
     * Rolls back the player's placement and informs all players of the rollback.
     * Notifies all players that a player has left the game and updates the current player.
     * If only one player remain connected, starts a timer for winner declaration.
     *
     * @param playerQuadruple The PlayerQuadruple of the player that has disconnected
     */
    private void disconnectCurrentPlayerAfterPlacing(PlayerQuadruple playerQuadruple) {
        // Set player state to disconnected
        playerQuadruple.setConnected(false);

        // Undo the player's placement
        try {
            model.rollbackPlacement();
        } catch (RollbackException e) {
            throw new CriticalFailureException("RollbackException when rolling back placement");
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + playerQuadruple.getNickname() + " not found when rolling back placement");
        }

        // Notify all players that the current player has rolled back his placement
        for (PlayerQuadruple pq : nodeList) {
            try {
                submitVirtualViewMessage(new PlaceCardRollbackMessage(
                        pq.getNickname(),
                        playerQuadruple.getNickname(),
                        model.getPlayerHand(playerQuadruple.getNickname()).getFirst(),
                        model.getPlayerPoints(playerQuadruple.getNickname()),
                        model.getPlayerResources(playerQuadruple.getNickname())
                ));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that the current player has rolled back his placement");
            } catch (PlayerNotFoundException e) {
                throw new CriticalFailureException("Player " + playerQuadruple.getNickname() + " not found when notifying players that the current player has rolled back his placement");
            }
        }

        // Notify all players that a player has left the game
        for (PlayerQuadruple playerQuadruple1 : nodeList) {
            try {
                submitVirtualViewMessage(new PlayerDisconnectMessage(playerQuadruple1.getNickname(), playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that a player has left the game");
            }
        }

        // Update the new current player. The notification is handled internally
        setNextPlayer();

        if(nodeList.stream().filter(PlayerQuadruple::isConnected).count() == 1) {
            lastOnlinePlayer = Objects.requireNonNull(nodeList.stream().filter(PlayerQuadruple::isConnected).findFirst().orElse(null)).getNickname();
            timer.schedule(new EndMatchDueToDisconnectionTimerTask(this), 1000 * 60 * 2);
            // FIXME: The timer is set to 2 minutes for testing purposes. The actual time should be read from the Configuration class.
        }
    }

    /**
     * Method called when a player disconnects after the game has started and is the current player, but has not yet placed a card.
     * Sets the player status to "Disconnected" hoping that it will reconnect eventually.
     * Notifies all players that a player has left the game, updates the current player, and starts a timer for winner declaration if only one player remains connected.
     *
     * @param playerQuadruple The PlayerQuadruple of the player that has disconnected
     */
    private void disconnectCurrentPlayerBeforePlacing(PlayerQuadruple playerQuadruple) {
        // Set player state to disconnected
        playerQuadruple.setConnected(false);

        // Notify all players that a player has left the game
        for (PlayerQuadruple playerQuadruple1 : nodeList) {
            try {
                submitVirtualViewMessage(new PlayerDisconnectMessage(playerQuadruple1.getNickname(), playerQuadruple.getNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that a player has left the game");
            }
        }

        // Update the new current player
        setNextPlayer();

        if(nodeList.stream().filter(PlayerQuadruple::isConnected).count() == 1) {
            lastOnlinePlayer = Objects.requireNonNull(nodeList.stream().filter(PlayerQuadruple::isConnected).findFirst().orElse(null)).getNickname();
            timer.schedule(new EndMatchDueToDisconnectionTimerTask(this), 1000 * 60 * 2);
            // FIXME: The timer is set to 2 minutes for testing purposes. The actual time should be read from the Configuration class.
        }
    }

    protected void endMatchDueToDisconnection() {
        // Set the Game Controller status to GAME_ENDED
        status = GameControllerStatus.GAME_ENDED;

        // Notify all players that the game was won by the remaining player
        ArrayList<String> players = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>();
        ArrayList<Integer> secrets = new ArrayList<>();
        ArrayList<Integer> pointsGainedFromSecrets = new ArrayList<>();
        ArrayList<String> winners = new ArrayList<>();

        // Iterate on each player in the nodeList using their nickname as key
        for(PlayerQuadruple playerQuadruple : nodeList){
            try {
                players.add(playerQuadruple.getNickname());
                points.add(model.getPlayerPoints(playerQuadruple.getNickname()));
                secrets.add(model.getPlayerSecretObjective(playerQuadruple.getNickname()));
                pointsGainedFromSecrets.add(0);
            } catch (PlayerNotFoundException e) {
                throw new CriticalFailureException("Player " + playerQuadruple.getNickname() + " not found");
            }
        }

        // Add the remaining player to the list of winners
        winners.add(lastOnlinePlayer);

        // Notify all players of the new match status and of the winners
        for (PlayerQuadruple playerQuadruple : nodeList) {
            try {
                // Notify the player of the status of the match
                submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                // Notify the player of the winners
                submitVirtualViewMessage(new MatchWinnersMessage(
                        playerQuadruple.getNickname(),
                        players,
                        points,
                        secrets,
                        pointsGainedFromSecrets,
                        winners
                ));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualViewNotFoundException when notifying players that the game has ended");
            }
        }
    }

    public void reconnect(String nickname, NodeInterface node) throws PlayerNotFoundException {
        // Throw exception if nickname is not present in the list of players
        if (nodeList.stream().noneMatch(pq -> pq.getNickname().equals(nickname))) {
            throw new PlayerNotFoundException("Player " + nickname + " not found when reconnecting");
        }
        // TODO
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
    protected void enterEndPhase() {
        status = GameControllerStatus.GAME_ENDED;
        model.enterTerminatedPhase();

        try {
            model.addObjectivePoints();

            ArrayList<String> players = new ArrayList<>();
            ArrayList<Integer> points = new ArrayList<>();
            ArrayList<Integer> secrets = new ArrayList<>();
            ArrayList<Integer> pointsGainedFromSecrets = new ArrayList<>();
            ArrayList<String> winners = model.getWinners();

            // Iterate on each player in the nodeList using their nickname as key
            for(PlayerQuadruple playerQuadruple : nodeList){
                try {
                    players.add(playerQuadruple.getNickname());
                    points.add(model.getPlayerPoints(playerQuadruple.getNickname()));
                    secrets.add(model.getPlayerSecretObjective(playerQuadruple.getNickname()));
                    pointsGainedFromSecrets.add(model.getPointsGainedFromObjectives(playerQuadruple.getNickname()));

                } catch (PlayerNotFoundException e) {
                    throw new CriticalFailureException("Player " + playerQuadruple.getNickname() + " not found");
                }
            }

            for (PlayerQuadruple playerQuadruple : nodeList) {
                // Notify the player of the status of the match
                submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                // Notify the player of the winners
                submitVirtualViewMessage(new MatchWinnersMessage(
                        playerQuadruple.getNickname(),
                        players,
                        points,
                        secrets,
                        pointsGainedFromSecrets,
                        winners
                ));
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
    public synchronized void chooseStarterCardSide(String nickname, boolean isUp) {
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
            submitVirtualViewMessage(new ConfirmStarterCardSideSelectionMessage(
                    nickname,
                    model.getInitialCardPlayer(nickname),
                    isUp,
                    model.getAvailableSpacesPlayer(nickname),
                    model.getPlayerResources(nickname),
                    model.getPlayerColour(nickname)
            ));

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
                     submitVirtualViewMessage(new AssignedSecretObjectiveCardMessage(
                                playerQuadruple.getNickname(),
                                model.getSecretObjectiveCardsPlayer(playerQuadruple.getNickname()),
                                model.getCommonObjectives(),
                                model.getPlayerHand(playerQuadruple.getNickname())
                     ));
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
    public synchronized void chooseSecretObjectiveCard(String nickname, int id) {
        if (status != GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE) { // Received a secret objective card choice message, but the controller is not waiting for it
            try {
                submitVirtualViewMessage(new InvalidSelectedSecretObjectiveCardMessage(nickname, "You cannot choose a secret objective card at this time"));
                return;
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
            submitVirtualViewMessage(new ConfirmSelectedSecretObjectiveCardMessage(nickname, id));

            boolean playersReady = true; // Assume all players are ready
            for (String playerNickname : model.getPlayersNicknames()) { // Scan all players in the current game
                if (model.getPlayerSecretObjective(playerNickname) == -1) { // If a player doesn't have an assigned secret objective, he has not yet chosen his secret objective
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
                    // Keep this message in order to keep coherency with the client build-in controller
                    // Notify the players of the current player
                    submitVirtualViewMessage(new PlayerTurnMessage(playerQuadruple.getNickname(), model.getCurrentPlayerNickname()));
                    // PlayerTurnMessage is still needed in order to keep the event order in the client even if the current player is already known thanks to the previous message
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

    /**
     * Method called when a message of type place card is received.
     * Attempts to place the selected card on the player's field at the specified coordinates.
     * If the card placement is successful, the player is notified of the successful placement, and the game status is updated.
     * If the card placement fails, the player is notified of the failure.
     *
     * @param nickname The nickname of the player that sent the message
     * @param id The id of the card to place
     * @param x X coordinate of the card
     * @param y Y coordinate of the card
     * @param side Side of the card to place
     */
    public synchronized void placeCard(String nickname, int id, int x, int y, boolean side) {
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
                return;
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
            }
        }
        // Player has the playing rights
        try {
            model.placeCard(id, x, y, side); // Try to place card

            // Notify all the players that the current contender has successfully placed the card
            for (PlayerQuadruple playerQuadruple : nodeList) {
                submitVirtualViewMessage(new PlaceCardConfirmationMessage(
                        playerQuadruple.getNickname(),
                        model.getCurrentPlayerNickname(),
                        id,
                        new int[]{x, y},
                        side,
                        model.getPlayerPoints(playerQuadruple.getNickname()),
                        model.getPlayerResources(playerQuadruple.getNickname()),
                        model.getAvailableSpacesPlayer(playerQuadruple.getNickname())
                ));
            }

            if (model.getMatchStatus() != MatchStatus.LAST_TURN.getValue()) { // We are not in the last turn; the player should draw a card
                status = GameControllerStatus.WAITING_CARD_DRAW; // Update game status
                return;
            }

            setNextPlayer();
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

    /**
     * Method called when a message of type draw card is received.
     * Attempts to draw a card from the specified deck.
     * If the draw is successful, the player is notified of the successful draw, and the game status is updated.
     * If the draw fails, the player is notified of the failure.
     *
     * @param nickname The nickname of the player that sent the message
     * @param deckType The type of deck to draw from
     * @param id The id of the card to draw
     */
    public synchronized void drawCard(String nickname, int deckType, int id) {
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
            submitVirtualViewMessage(new DrawCardConfirmationMessage(nickname, model.getPlayerHand(nickname)));

            // Notify to all the players that the deck size has changed
            for (PlayerQuadruple playerQuadruple : nodeList) {
                submitVirtualViewMessage(new DeckSizeUpdateMessage(
                        playerQuadruple.getNickname(),
                        model.getResourceCardDeckSize(),
                        model.getGoldCardDeckSize(),
                        model.getCurrentResourcesCards().stream().mapToInt(Integer::intValue).toArray(),
                        model.getCurrentGoldCards().stream().mapToInt(Integer::intValue).toArray()
                ));
            }

            setNextPlayer();

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
     * Sets the current player, skipping over any disconnected players.
     * Updates the current game status.
     * Notifies all players of any changes in the model status, notifies all players of the newly elected current player.
     */
    private void setNextPlayer() {
        // If all players are disconnected, we don't want to get stuck in an infinite loop
        if (nodeList.stream().noneMatch(PlayerQuadruple::isConnected)) {
            return;
        }

        do {
            model.nextTurn(); // Update turn number and current player

            // After updating the current player, we need to update the game state
            if (model.isFirstPlayer()) { // The first player is now playing
                if (model.areWeTerminating()) { // We are in the terminating phase
                    model.setLastTurn();

                    // Notify all players of the new match status
                    for (PlayerQuadruple playerQuadruple : nodeList) {
                        try {
                            submitVirtualViewMessage(new MatchStatusMessage(playerQuadruple.getNickname(), model.getMatchStatus()));
                        } catch (VirtualViewNotFoundException e) {
                            throw new CriticalFailureException("VirtualView for player " + playerQuadruple.getNickname() + " not found");
                        }
                    }
                }
                else if (model.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) {
                    enterEndPhase();
                    return;
                }
            }
        } while (!isCurrentPlayerConnected());

        // Found the next player that is currently connected
        // Notify the players of the current player
        for (PlayerQuadruple playerQuadruple : nodeList) {
            try {
                submitVirtualViewMessage(new PlayerTurnMessage(playerQuadruple.getNickname(), model.getCurrentPlayerNickname()));
            } catch (VirtualViewNotFoundException e) {
                throw new CriticalFailureException("VirtualView for player " + playerQuadruple.getNickname() + " not found");
            }
        }
    }

    /**
     * Checks to see if the current player is connected.
     *
     * @return True if the current player is connected, false otherwise
     */
    private boolean isCurrentPlayerConnected() {
        return nodeList.stream()
                .filter(pq -> pq.getNickname().equals(model.getCurrentPlayerNickname()))
                .map(PlayerQuadruple::isConnected)
                .findFirst()
                .orElse(false);
    }

    /**
     * Method called when a message of type request game status is received.
     * The method sends a response game status message to the requester, updating them on the current state of the model.
     *
     * @param requesterNickname The nickname of the player that sent the message
     */
    // We need to keep this method since the client knows that it is reconnecting (and not just accessing a new game) and therefore it will ask for the updated game information
    public synchronized void sendGameStatus(String requesterNickname) {
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
    // This method is technically not needed, since the client can just request the game status and get the field of the player whose field is requested
    // However, we keep this method in order to allow easier debugging and testing
    public synchronized void sendPlayerField(String requesterNickname, String playerNickname) {
        try {
            submitVirtualViewMessage(new ResponsePlayerFieldMessage(requesterNickname, playerNickname, model.getPlayerField(playerNickname), model.getPlayerResources(playerNickname)));
        } catch (PlayerNotFoundException e) { // The player whose field is requested could not be found
            try {
                submitVirtualViewMessage(new NegativeResponsePlayerFieldMessage(requesterNickname, playerNickname));
            } catch (VirtualViewNotFoundException ex) {
                throw new CriticalFailureException("VirtualView for player " + requesterNickname + " not found");
            }
        } catch (VirtualViewNotFoundException e) { // The requester's VirtualView could not be found
            throw new CriticalFailureException("VirtualView for player " + requesterNickname + " not found");
        }
    }

    /**
     * Generates a response game status message for a given player.
     *
     * @param nickname The nickname of the player to generate the message for
     * @return The generated response game status message
     */
    protected PlayerGameStatusMessage generateResponseGameStatusMessage(String nickname) {
        try {
            ArrayList<String> playerNicknames = model.getPlayersNicknames();
            ArrayList<Boolean> playerConnected = nodeList.stream().map(PlayerQuadruple::isConnected).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> playerColours = model.getPlayersNicknames().stream().map(playerNickname -> {
                try {
                    return model.getPlayerColour(playerNickname);
                } catch (PlayerNotFoundException e) {
                    throw new CriticalFailureException("Player " + playerNickname + " not found when generating game status message");
                } catch (NullColourException e) {
                    throw new CriticalFailureException("Player " + playerNickname + " has a null colour");
                }
            }).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> playerHand = model.getPlayerHand(nickname);
            int playerSecretObjective = model.getPlayerSecretObjective(nickname);
            int[] playerPoints = model.getPlayersNicknames().stream().map(n -> {
                try {
                    return model.getPlayerPoints(n);
                } catch (PlayerNotFoundException e) {
                    throw new CriticalFailureException("Player " + n + " not found when generating game status message");
                }
            }).mapToInt(Integer::intValue).toArray();
            ArrayList<ArrayList<int[]>> playerFields = model.getPlayersNicknames().stream().map(n -> {
                try {
                    return model.getPlayerField(n);
                } catch (PlayerNotFoundException e) {
                    throw new CriticalFailureException("Player " + n + " not found when generating game status message");
                }
            }).collect(Collectors.toCollection(ArrayList::new));
            int[] playerResources = model.getPlayerResources(nickname);
            ArrayList<Integer> gameCommonObjectives = model.getCommonObjectives();
            ArrayList<Integer> gameCurrentResourceCards = model.getCurrentResourcesCards();
            ArrayList<Integer> gameCurrentGoldCards = model.getCurrentGoldCards();
            int gameResourcesDeckSize = model.getResourceCardDeckSize();
            int gameGoldDeckSize = model.getGoldCardDeckSize();
            int matchStatus = model.getMatchStatus();
            ArrayList<ChatMessage> playerChatHistory = chat.getPlayerChatHistory(nickname);
            String currentPlayer = model.getCurrentPlayerNickname();
            ArrayList<int[]> newAvailableFieldSpaces = model.getAvailableSpacesPlayer(nickname);

            return new PlayerGameStatusMessage(nickname, playerNicknames, playerConnected, playerColours, playerHand, playerSecretObjective, playerPoints, playerFields, playerResources, gameCommonObjectives, gameCurrentResourceCards, gameCurrentGoldCards, gameResourcesDeckSize, gameGoldDeckSize, matchStatus, playerChatHistory, currentPlayer, newAvailableFieldSpaces);
        } catch (PlayerNotFoundException e) {
            throw new CriticalFailureException("Player " + nickname + " not found");
        }
    }

    /**
     * Used to reply to a PingMessage. The method sends a PongMessage to the requester.
     *
     * @param nickname The nickname of the player that sent the ping message
     */
    public synchronized void pongPlayer(String nickname) {
        try {
            submitVirtualViewMessage(new PongMessage(nickname));
        } catch (VirtualViewNotFoundException e) {
            throw new CriticalFailureException("VirtualView for player " + nickname + " not found");
        }
    }

    /**
     * Getter for the ID of the game controller.
     *
     * @return The ID of the game controller
     */
    public int getId() {
        return id;
    }

    protected ArrayList<PlayerQuadruple> getNodeList() {
        return nodeList;
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

    /**
     * Getter for the timer of the game controller.
     *
     * @return The timer of the game controller
     */
    public Timer getTimer() {
        return timer;
    }

    protected ModelInterface getModel(){
        return model;
    }

    protected Chat getChat(){
        return chat;
    }
}
