// TODO: Implement better tests for GamesManager after having tested GameController.
package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.NodeInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerTest {
    // Stub class for NodeInterface. This class is used to test the GamesManager class.
    private static class NodeInterfaceStub implements NodeInterface {
        public void uploadToClient(StoCMessage message) {}
        public void pingTimeOverdue() {}
        public void resetTimeCounter() {}
        public void setGameController(GameController gameController) {}
        public NodeInterfaceStub() {}
    }

    // NodeInterface instance
    private NodeInterface node;
    // GamesManager instance
    private GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        gamesManager = GamesManager.getInstance();
        node = new NodeInterfaceStub();
    }

    @AfterEach
    void clearSingleton(){
        gamesManager.clearInstance();
    }

    @DisplayName("Create a game with valid parameters - no exceptions expected")
    @Test
    void createGameWithValidParameters() {
        assertDoesNotThrow(() -> gamesManager.createGame("creator", 3, node));
        assertNotNull(gamesManager.getGames());
        assertEquals(1, gamesManager.getGames().size()); // Only one game has been created
    }

    @DisplayName("Create a game with null creator name - CriticalFailureException expected")
    @Test
    void createGameWithNullCreatorName() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.createGame(null, 3, node));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Create a game with blank creator name - CriticalFailureException expected")
    @Test
    void createGameWithBlankCreatorName() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.createGame("", 3, node));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Create a game with invalid player count - CriticalFailureException expected")
    @Test
    void createGameWithInvalidPlayerCount() {
        assertThrows(InvalidPlayerNumberException.class, () -> gamesManager.createGame("creator", 5, node));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Create a game with null node - CriticalFailureException expected")
    @Test
    void createGameWithNullNode() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.createGame("creator", 3, null));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Access a game with valid parameters - no exceptions expected")
    @Test
    void accessGameWithValidParameters() {
        try {
            GameController gameController = gamesManager.createGame("creator", 3, node);
            assertEquals(1, gamesManager.getGames().size());
            gamesManager.accessGame("player", gameController.getId(), node);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @DisplayName("Access a game with null nickname - CriticalFailureException expected")
    @Test
    void accessGameWithNullNickname() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.accessGame(null, 1, node));
    }

    @DisplayName("Access a game with blank nickname - CriticalFailureException expected")
    @Test
    void accessGameWithBlankNickname() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.accessGame("", 1, node));
    }

    @DisplayName("Access a game with invalid game code - GameNotFoundException expected")
    @Test
    void accessGameWithInvalidGameCode() {
        assertThrows(GameNotFoundException.class, () -> gamesManager.accessGame("player", 9999, node));
    }

    @DisplayName("Access a game with null node - CriticalFailureException expected")
    @Test
    void accessGameWithNullNode() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.accessGame("player", 1, null));
    }
}
