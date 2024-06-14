package it.polimi.ingsw.am32.network.ServerNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class ServerPingTask extends TimerTask {
    private static final Logger logger = LogManager.getLogger(ServerPingTask.class);
    private NodeInterface node;
    public ServerPingTask(NodeInterface node) {
        this.node = node;
    }
    public void run() {
        // logger.debug("ServerPingTask started. The node {} will be checked for ping time.", node);  // Logging disabled to avoid spamming
        node.pingTimeOverdue();
    }

    @Override
    public boolean cancel() {
        boolean cancelled = super.cancel();
        node = null;
        return cancelled;
    }

}
