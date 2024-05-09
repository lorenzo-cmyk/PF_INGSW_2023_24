package it.polimi.ingsw.am32.network.ServerNode;

import java.util.TimerTask;

public class PingTask extends TimerTask {
    private final NodeInterface node;
    public PingTask(NodeInterface node) {
        this.node = node;
    }
    public void run() {
        node.pingTimeOverdue();
    }

}
