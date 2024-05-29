package it.polimi.ingsw.am32.network;

import it.polimi.ingsw.am32.network.ServerNode.RMIServerNodeInt;

public class GameTuple {

    private final RMIServerNodeInt node;
    private final int id;

    public GameTuple(RMIServerNodeInt node, int id) {
        this.node = node;
        this.id = id;
    }

    public RMIServerNodeInt getNode(){
        return node;
    }

    public int getId() {
        return id;
    }
}
