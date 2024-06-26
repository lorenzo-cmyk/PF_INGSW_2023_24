package it.polimi.ingsw.am32.network.ClientNode;

import java.util.TimerTask;

public class ClientPingTask extends TimerTask {

    private ClientNodeInterface node;

    public ClientPingTask(ClientNodeInterface node) {this.node = node;}

    public void run() {
        node.pongTimeOverdue();
    }

    @Override
    public boolean cancel() {
        boolean cancelled = super.cancel();
        node = null;
        return cancelled;
    }
}
