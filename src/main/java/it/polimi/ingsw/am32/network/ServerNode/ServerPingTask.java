package it.polimi.ingsw.am32.network.ServerNode;

import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

/**
 * The {@code ServerPingTask} class contains the pointer to a ServerNode, given when the object is instantiated. <br>
 * This class is used by a {@link java.util.Timer} to invoke the method {@link ServerNodeInterface#pingTimeOverdue()}
 * on the given node.
 *
 * @author Matteo
 */
public class ServerPingTask extends TimerTask {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    // private static final Logger logger = LogManager.getLogger(ServerPingTask.class);
    private ServerNodeInterface node;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class.
     *
     * @param node the ServerNode on which will be invoked the {@link ServerNodeInterface#pingTimeOverdue()}
     */
    public ServerPingTask(ServerNodeInterface node) {
        this.node = node;
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Invoke the method {@link ServerNodeInterface#pingTimeOverdue()} on the ServerNode stored in the object.
     */
    public void run() {
        // logger.debug("ServerPingTask started. The node {} will be checked for ping time.", node);  // Logging disabled to avoid spamming
        node.pingTimeOverdue();
    }

    /**
     * invoke the method {@link TimerTask#cancel()} and delete the reference to the ServerNode to facilitate deletion
     * of the object by the garbage collector.
     *
     * @return the return value of the method {@code cancel()} of the super class
     */
    @Override
    public boolean cancel() {
        boolean cancelled = super.cancel();
        node = null;
        return cancelled;
    }
}
