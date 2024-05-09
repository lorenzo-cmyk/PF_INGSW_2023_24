package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @DisplayName("Create a game and check if game ID is within range and unique")
    @Test
    void createGameAndCheckGameId() {
        try {
            GameController gameController1 = gamesManager.createGame("creator1", 3, node);
            GameController gameController2 = gamesManager.createGame("creator2", 3, node);
            assertTrue(gameController1.getId() >= 0 && gameController1.getId() <= 2048);
            assertTrue(gameController2.getId() >= 0 && gameController2.getId() <= 2048);
            assertNotEquals(gameController1.getId(), gameController2.getId());
        } catch (Exception e) {
            fail();
        }
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
        assertThrows(InvalidPlayerNumberException.class, () -> gamesManager.createGame("creator", 1, node));
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
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Access a game with blank nickname - CriticalFailureException expected")
    @Test
    void accessGameWithBlankNickname() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.accessGame("", 1, node));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Access a game with invalid game code - GameNotFoundException expected")
    @Test
    void accessGameWithInvalidGameCode() {
        assertThrows(GameNotFoundException.class, () -> gamesManager.accessGame("player", 9999, node));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Access a game with null node - CriticalFailureException expected")
    @Test
    void accessGameWithNullNode() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.accessGame("player", 1, null));
        assertEquals(0, gamesManager.getGames().size());
    }

    @DisplayName("Access an already started game - GameAlreadyStartedException expected")
    @Test
    void accessGameAlreadyStarted() {
        try {
            GameController gameController = gamesManager.createGame("creator", 3, node);
            assertDoesNotThrow(() -> gamesManager.accessGame("player1", gameController.getId(), node));
            assertDoesNotThrow(() -> gamesManager.accessGame("player2", gameController.getId(), node));
            assertThrows(GameAlreadyStartedException.class, () -> gamesManager.accessGame("player3", gameController.getId(), node));
        } catch (Exception e) {
            fail();
        }
    }

    @DisplayName("Test concurrency when creating and accessing games")
    @Test
    void testConcurrencyOnCreateAndAccessGame() {
        // Create 500 games and access them with 500 different players
        ExecutorService service = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 500; i++) {
            service.submit(() -> {
                try {
                    GameController gameController = gamesManager.createGame("creator" + Thread.currentThread().getId(), 3, node);
                    gamesManager.accessGame("player" + Thread.currentThread().getId(), gameController.getId(), node);
                } catch (Exception e) {
                    fail("Unexpected exception: " + e.getMessage());
                }
            });
        }
        // Shutdown the service
        service.shutdown();
        try {
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
        // Check if all games have been created and accessed
        assertEquals(500, gamesManager.getGames().size());
        assertEquals(500, gamesManager.getGames().stream().map(GameController::getId).distinct().count());
    }

}
