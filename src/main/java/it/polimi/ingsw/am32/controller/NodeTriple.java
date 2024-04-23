package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.network.NodeInterface;

public class NodeTriple {
    private NodeInterface node;
    private String nickname;
    private boolean connected;

    public NodeTriple(NodeInterface node, String nickname, boolean connected) {
        this.node = node;
        this.nickname = nickname;
        this.connected = connected;
    }

    public NodeInterface getNode() {
        return node;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isConnected() {
        return connected;
    }
}
