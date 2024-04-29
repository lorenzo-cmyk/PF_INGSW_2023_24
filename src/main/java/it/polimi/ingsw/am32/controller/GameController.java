package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Timer;

import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.controller.exceptions.ListenerNotFoundException;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.model.match.Match;
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
    private final ArrayList<NodeTriple> nodeList;
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

    public GameController(String creatorName, int id, int playerCount) {
        this.listeners = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.timer = null;
        this.id = id;
        this.gamePlayerCount = playerCount;
        //TODO timer

        // Enter lobby phase immediately
        model.enterLobbyPhase();
        addPlayer(creatorName);
    }

    public void submitChatMessage(ChatMessage message){
        chat.addMessage(message);
    }

    public void addNode(NodeInterface node, String nickname, boolean connected){
        nodeList.add(new NodeTriple(node, nickname, connected));
        // TODO: Should we check for duplicate nodes in nodeList?
    }

    public ArrayList<ChatMessage> getAllChatHistory(){
        return chat.getHistory();
    }

    public void addListener(VirtualView listener) {
        listeners.add(listener);
        // TODO: Check for duplicate listeners?
    }

    public void removeListener(VirtualView listener) throws ListenerNotFoundException {
        boolean present = listeners.remove(listener); // Flag indicating if listener was present in the list of listeners
        if (!present) {
            throw new ListenerNotFoundException("Listener does not exist");
        }
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
     * Methods used when a new player joins, or when a new game is created.
     *
     * @param nickname The nickname of the player to add
     */
    public void addPlayer(String nickname) {
        try {
            model.addPlayer(nickname);
        } catch (DuplicateNicknameException e){
           throw new CriticalFailureException("Player " + nickname + " already in game");
        }
        // TODO
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

