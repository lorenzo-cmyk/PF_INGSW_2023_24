package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;

/**
 * This class represents a player in the game. It contains information about the player's
 * connection status, nickname, and the virtual view associated with the player.
 */
public class PlayerQuadruple {
    private NodeInterface node;
    private final String nickname;
    private boolean connected;
    private final VirtualView virtualView;

    /**
     * Constructs a new PlayerQuadruple object.
     *
     * @param node        the node associated with the player
     * @param nickname    the player's nickname
     * @param connected   the player's connection status
     * @param virtualView the virtual view associated with the player
     * @throws CriticalFailureException if any of the parameters are null or if the nickname is empty
     */
    public PlayerQuadruple(NodeInterface node, String nickname, boolean connected, VirtualView virtualView) {
        this.node = node;
        this.nickname = nickname;
        this.connected = connected;
        this.virtualView = virtualView;
        if (node == null || nickname == null || virtualView == null || nickname.isEmpty()) {
            throw new CriticalFailureException("PlayerQuadruple cannot be created with null or empty values");
        }
    }

    /**
     * Sets the node associated with the player.
     *
     * @param node the new node
     * @throws CriticalFailureException if the node is null
     */
    public void setNode(NodeInterface node) {
        if (node == null) {
            throw new CriticalFailureException("Node cannot be null");
        }
        this.node = node;
    }

    /**
     * Returns the node associated with the player.
     *
     * @return the node
     */
    public NodeInterface getNode() {
        return node;
    }

    /**
     * Returns the player's nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the player's connection status.
     *
     * @param connected the new connection status
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Returns the player's connection status.
     *
     * @return true if the player is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Returns the virtual view associated with the player.
     *
     * @return the virtual view
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }
}
