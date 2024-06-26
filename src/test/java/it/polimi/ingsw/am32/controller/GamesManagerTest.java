package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import it.polimi.ingsw.am32.network.ServerNode.ServerNodeInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GamesManagerTest {
    // Stub class for ServerNodeInterface. This class is used to test the GameController class.
    private static class NodeInterfaceStub implements ServerNodeInterface {
        private final ArrayList<StoCMessage> internalMessages;
        public NodeInterfaceStub() {
            internalMessages = new ArrayList<>();
        }
        public synchronized void uploadToClient(StoCMessage message) {
            internalMessages.add(message);
        }
        public void pingTimeOverdue() {
            // STUB
        }
        public void resetTimeCounter() {
            // STUB
        }
        public synchronized ArrayList<StoCMessage> getInternalMessages() {
            return internalMessages;
        }
        public synchronized void clearInternalMessages() {
            internalMessages.clear();
        }
    }

    // ServerNodeInterface instance
    private ServerNodeInterface node;
    // GamesManager instance
    private GamesManager gamesManager;

    @BeforeEach
    void setUp() {
        GamesManager.getInstance().clearInstance();
        gamesManager = GamesManager.getInstance();
        node = new NodeInterfaceStub();
    }

    @AfterEach
    void clearSingleton(){
        gamesManager.clearInstance();
    }

    @DisplayName("Create a game with valid parameters - no exceptions expected, creator should receive a NewGameConfirmationMessage")
    @Test
    void createGameWithValidParameters() {
        assertDoesNotThrow(() -> gamesManager.createGame("creator", 3, node));
        assertNotNull(gamesManager.getGames());
        assertEquals(1, gamesManager.getGames().size()); // Only one game has been created

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Get the node of the creator
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)gamesManager.getGames().getFirst().getNodeList().getFirst().getNode();
        // Check that creator has received a single NewGameConfirmationMessage
        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(NewGameConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
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

    @DisplayName("Access a game with valid parameters - no exceptions expected, players should receive correct messages")
    @Test
    void accessGameWithValidParameters() {
        try {
            GameController gameController = gamesManager.createGame("creator", 3, new NodeInterfaceStub());
            assertEquals(1, gamesManager.getGames().size());
            gamesManager.accessGame("player", gameController.getId(), new NodeInterfaceStub());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        for (PlayerQuadruple playerQuadruple : gamesManager.getGames().getFirst().getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("creator")) {
                // Creator should have received a NewGameConfirmationMessage, a PlayerConnectedMessage, and a LobbyPlayerListMessage
                assertEquals(3, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(NewGameConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(PlayerConnectedMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
            }
            else if (playerQuadruple.getNickname().equals("player")) {
                // Player should have received an AccessGameConfirmMessage, and a LobbyPlayerListMessage
                assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(AccessGameConfirmMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
            }
        }
    }

    @DisplayName("Access a game with valid parameters - no exceptions expected, players should receive correct messages")
    @Test
    void accessGameWithValidParametersThreePlayer() {
        try {
            GameController gameController = gamesManager.createGame("creator", 4, new NodeInterfaceStub());
            assertEquals(1, gamesManager.getGames().size());
            gamesManager.accessGame("player", gameController.getId(), new NodeInterfaceStub());
            gamesManager.accessGame("player2", gameController.getId(), new NodeInterfaceStub());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        for (PlayerQuadruple playerQuadruple : gamesManager.getGames().getFirst().getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("creator")) {
                // Creator should have received:
                // - a NewGameConfirmationMessage
                // - a LobbyPlayerListMessage
                // - a PlayerConnectedMessage
                // - a LobbyPlayerListMessage
                // - a PlayerConnectedMessage
                assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(NewGameConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(PlayerConnectedMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
                assertInstanceOf(PlayerConnectedMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
            }
            else if (playerQuadruple.getNickname().equals("player")) {
                // Player should have received:
                // - an AccessGameConfirmMessage
                // - a LobbyPlayerListMessage
                // - a LobbyPlayerListMessage
                // - a PlayerConnectedMessage
                assertEquals(4, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(AccessGameConfirmMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
                assertInstanceOf(PlayerConnectedMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
            }
            else if (playerQuadruple.getNickname().equals("player2")) {
                // Player2 should have received:
                // - an AccessGameConfirmMessage
                // - a LobbyPlayerListMessage
                assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(AccessGameConfirmMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
            }
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
                    GameController gameController = gamesManager.createGame("creator " + Thread.currentThread().getId(), 3, node);
                    gamesManager.accessGame("player " + Thread.currentThread().getId(), gameController.getId(), node);
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
