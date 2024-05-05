package it.polimi.ingsw.am32.controller;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import it.polimi.ingsw.am32.chat.Chat;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.model.match.Match;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import it.polimi.ingsw.am32.network.ServerNode.RMIServerNode;
import it.polimi.ingsw.am32.model.ModelInterface;

public class GameController implements GameControllerInterface{
    private final ArrayList<VirtualView> listeners;
    private final ArrayList<NodeTriple> nodeList;
    private final ModelInterface model;
    private final Chat chat;
    private final Timer timer;
    private final String creatorName;
    private final int id;

    public GameController(String creatorName){
        this.listeners = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.model = new Match();
        this.chat = new Chat();
        this.id= new Random().nextInt(1000);
        this.creatorName = creatorName;
        this.timer = null;
        //TODO timer
    }
    public void submitChatMessage(ChatMessage message){
        //TODO
    }
    public void addNode(NodeInterface node, String nickname, boolean connected){
        //TODO
    }
    public ArrayList<ChatMessage>getAllChatHistory(){
        //TODO
        return null;
    }
    public void addListener(VirtualView listener) {
        //TODO
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

