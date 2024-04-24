package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Timer;

import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
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
     * creatorName: The name of the player that created the game
     */
    private final String creatorName;
    /**
     * id: The id of the game
     */
    private final int id;

    public GameController(String creatorName, int id) {
        this.listeners = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.timer = null;
        this.creatorName = creatorName;
        this.id = id;
        //TODO timer
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

    public void removeListener(VirtualView listener) {
        //TODO
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

    public int getId() {
        return id;
    }
}

