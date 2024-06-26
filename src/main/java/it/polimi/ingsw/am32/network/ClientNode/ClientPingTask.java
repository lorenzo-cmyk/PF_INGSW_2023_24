package it.polimi.ingsw.am32.network.ClientNode;

import java.util.TimerTask;

/**
 * The {@code ClientPingTask} class contains the pointer to a ClientNode, given when the object is instantiated. <br>
 * This class is used by a {@link java.util.Timer} to invoke the method {@link ClientNodeInterface#pongTimeOverdue()}
 * on the given node.
 *
 * @author Matteo
 */
public class ClientPingTask extends TimerTask {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private ClientNodeInterface node;


    //---------------------------------------------------------------------------------------------
    // Constructor

    /**
     * Standard constructor of the class.
     *
     * @param node the ClientNode on which will be invoked the {@link ClientNodeInterface#pongTimeOverdue()}
     */
    public ClientPingTask(ClientNodeInterface node) {this.node = node;}


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Invoke the method {@link ClientNodeInterface#pongTimeOverdue()} on the ClientNode stored in the object.
     */
    public void run() {
        node.pongTimeOverdue();
    }

    /**
     * invoke the method {@link TimerTask#cancel()} and delete the reference to the ClientNode to facilitate deletion
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
