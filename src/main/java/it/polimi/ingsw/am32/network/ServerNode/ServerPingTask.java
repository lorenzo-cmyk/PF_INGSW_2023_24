package it.polimi.ingsw.am32.network.ServerNode;

import java.util.TimerTask;

public class ServerPingTask extends TimerTask {
    private final NodeInterface node;
    public ServerPingTask(NodeInterface node) {
        this.node = node;
    }
    public void run() {
        node.pingTimeOverdue();
    }

}
