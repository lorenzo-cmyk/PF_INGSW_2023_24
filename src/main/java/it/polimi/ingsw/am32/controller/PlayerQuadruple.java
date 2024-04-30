package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.network.NodeInterface;

public class PlayerQuadruple {
    private final NodeInterface node;
    private final String nickname;
    private boolean connected;
    private final VirtualView virtualView;

    public PlayerQuadruple(NodeInterface node, String nickname, boolean connected, VirtualView virtualView) {
        this.node = node;
        this.nickname = nickname;
        this.connected = connected;
        this.virtualView = virtualView;
    }
}
