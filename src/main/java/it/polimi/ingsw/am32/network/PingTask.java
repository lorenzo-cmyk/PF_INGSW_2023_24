package it.polimi.ingsw.am32.network;

public class PingTask {
    private final NodeInterface node;
    public PingTask(NodeInterface node) {
        this.node = node;
    }
    public void run() {
        node.pingTimeOverdue();
    }

}
