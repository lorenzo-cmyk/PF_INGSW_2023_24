package it.polimi.ingsw.am32.message.ClientToServer;

import it.polimi.ingsw.am32.controller.GamesManager;

/**
 * This interface represents a message from the client to the server in the lobby phase.
 * It contains a single method to elaborate the message with a game manager.
 */
public interface CtoSLobbyMessage {
    /**
     * Elaborates the message with the specified game manager.
     *
     * @param gamesManager The game manager with which the message should be elaborated
     */
    void elaborateMessage(GamesManager gamesManager);
}
