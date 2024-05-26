package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.VirtualViewNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    // GameController objects used for testing
    private GameController gameController;

    // Stub class for NodeInterface. This class is used to test the GameController class.
    private static class NodeInterfaceStub implements NodeInterface {
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

    @BeforeEach
    void setUp() {
        gameController = new GameController(1, 3);
        assertEquals(GameControllerStatus.LOBBY, gameController.getStatus());
        assertEquals(3, gameController.getGameSize());
        assertEquals(1, gameController.getId());
    }

    @DisplayName("Adding a player to the game successfully")
    @Test
    void addPlayerValid()  {
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        assertEquals(1, gameController.getLobbyPlayerCount());
        // Get the list of PlayerQuadruple to find the node associated with the player
        boolean found = false;
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()){
            if (Objects.equals(playerQuadruple.getNickname(), "player1")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @DisplayName("Adding a player to a full game throws FullLobbyException")
    @Test
    void addPlayerToFullGame() {
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        assertThrows(FullLobbyException.class, () -> gameController.addPlayer("player4", new NodeInterfaceStub()));
    }

    @DisplayName("submitVirtualViewMessage should deliver a message to the correct player and throw an exception if the player has not a node associated")
    @Test
    void submitVirtualViewMessageTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // Submit a message to the first player
        try {
            gameController.submitVirtualViewMessage(new GameStartedMessage("player1"));
        } catch (VirtualViewNotFoundException e) {
            fail();
        }
        // Check that the message has been delivered to the correct player
        // ----
        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }
        // ----
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()){
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) playerQuadruple.getNode();
            if (Objects.equals(playerQuadruple.getNickname(), "player1")) {
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
            } else {
                assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
            }
        }
        // Submit a message to a player that has not a node associated
        assertThrows(VirtualViewNotFoundException.class, () -> gameController.submitVirtualViewMessage(new GameStartedMessage("player3")));
    }

    @DisplayName("enterPreparationPhase should prepare the game making it playable")
    @Test
    void enterPreparationPhaseTest() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Check that inside each nodeStub for each node in playerQuadruple there are the correct messages

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
           fail();
        }

        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()){
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) playerQuadruple.getNode();
            assertEquals(3, nodeInterfaceStub.getInternalMessages().size());
            // Check that the messages are correct and in the correct order
            assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
            assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
            assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        }
        assertEquals(GameControllerStatus.WAITING_STARTER_CARD_CHOICE, gameController.getStatus());
    }

    @DisplayName("chooseStarterCardSide should place the starting card on the correct side")
    @Test
    void chooseStarterCardSideTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check the messages delivered to the player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(4, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
    }

    @DisplayName("chooseStarterCardSide should inform the player if run out of scope")
    @Test
    void chooseStarterCardSideOutOfScope() {
        // Add 1 player to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        gameController.chooseStarterCardSide("player1", true);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(InvalidStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("chooseStarterCardSide should inform the player if the card has already been chosen")
    @Test
    void chooseStarterCardSideAlreadyChosen() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player1", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(InvalidStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
    }

    @DisplayName("chooseStarterCardSide should ask the players to choose their secret objective card if all of them have chosen the side of the starting card")
    @Test
    void chooseStarterCardSideAllChosen() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        // Check that the game status is now WAITING_SECRET_OBJECTIVE_CARD_CHOICE
        assertEquals(GameControllerStatus.WAITING_SECRET_OBJECTIVE_CARD_CHOICE, gameController.getStatus());
    }

    @DisplayName("chooseSecretObjectiveCard should assign the secret objective card to the player")
    @Test
    void chooseSecretObjectiveCardTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));

        // I need to obtain the secret objective cards ids to test the method. I will do it by checking the message delivered to the player.
        // Since the message is a private field, I will use reflection to access it.
        AssignedSecretObjectiveCardMessage assignedSecretObjectiveCardMessage = (AssignedSecretObjectiveCardMessage) nodeInterfaceStub.getInternalMessages().get(4);
        int secretObjectiveCardId = -1;
        try {
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedSecretObjectiveCards");
            assignedCards.setAccessible(true);
            ArrayList<Integer> secretObjectiveCardIds = (ArrayList<Integer>) assignedCards.get(assignedSecretObjectiveCardMessage);
            secretObjectiveCardId = secretObjectiveCardIds.getFirst();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", secretObjectiveCardId);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(6, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(ConfirmSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));
    }

    @DisplayName("chooseSecretObjectiveCard should inform the player if the card has already been chosen")
    @Test
    void chooseSecretObjectiveCardAlreadySelected() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));

        // I need to obtain the secret objective cards ids to test the method. I will do it by checking the message delivered to the player.
        // Since the message is a private field, I will use reflection to access it.
        AssignedSecretObjectiveCardMessage assignedSecretObjectiveCardMessage = (AssignedSecretObjectiveCardMessage) nodeInterfaceStub.getInternalMessages().get(4);
        int secretObjectiveCardId = -1;
        try {
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedSecretObjectiveCards");
            assignedCards.setAccessible(true);
            ArrayList<Integer> secretObjectiveCardIds = (ArrayList<Integer>) assignedCards.get(assignedSecretObjectiveCardMessage);
            secretObjectiveCardId = secretObjectiveCardIds.getFirst();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", secretObjectiveCardId);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(6, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(ConfirmSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));

        // Choose the secret objective card again
        gameController.chooseSecretObjectiveCard("player1", secretObjectiveCardId);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(7, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(ConfirmSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));
        assertInstanceOf(InvalidSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(6));
    }

    @DisplayName("chooseSecretObjectiveCard should inform the player if run out of scope")
    @Test
    public void chooseSecretObjectiveCardOutOfScope() {
        // Add 1 player to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        gameController.chooseSecretObjectiveCard("player1", 0);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(InvalidSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("chooseSecretObjectiveCard should inform the player if the card chosen is not valid")
    @Test
    void chooseSecretObjectiveCardInvalidCard() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", 0);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(6, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(InvalidSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));
    }

    @DisplayName("chooseSecretObjectiveCard should move the game to the next phase if all the players have chosen their secret objective card")
    @Test
    void chooseSecretObjectiveCardMoveTheGameOn() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        NodeInterfaceStub nodeInterfaceStub2 = (NodeInterfaceStub) gameController.getNodeList().getLast().getNode();
        assertEquals(5, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));

        // I need to obtain the secret objective cards ids to test the method. I will do it by checking the message delivered to the player.
        // Since the message is a private field, I will use reflection to access it.
        AssignedSecretObjectiveCardMessage assignedSecretObjectiveCardMessage = (AssignedSecretObjectiveCardMessage) nodeInterfaceStub.getInternalMessages().get(4);
        int secretObjectiveCardId = -1;
        try {
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedSecretObjectiveCards");
            assignedCards.setAccessible(true);
            ArrayList<Integer> secretObjectiveCardIds = (ArrayList<Integer>) assignedCards.get(assignedSecretObjectiveCardMessage);
            secretObjectiveCardId = secretObjectiveCardIds.getFirst();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        AssignedSecretObjectiveCardMessage assignedSecretObjectiveCardMessage2 = (AssignedSecretObjectiveCardMessage) nodeInterfaceStub2.getInternalMessages().get(4);
        int secretObjectiveCardId2 = -1;
        try {
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedSecretObjectiveCards");
            assignedCards.setAccessible(true);
            ArrayList<Integer> secretObjectiveCardIds2 = (ArrayList<Integer>) assignedCards.get(assignedSecretObjectiveCardMessage2);
            secretObjectiveCardId2 = secretObjectiveCardIds2.getFirst();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }


        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", secretObjectiveCardId);
        gameController.chooseSecretObjectiveCard("player2", secretObjectiveCardId2);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(9, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(ConfirmSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(6));
        assertInstanceOf(PlayerGameStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(7));
        assertInstanceOf(PlayerTurnMessage.class, nodeInterfaceStub.getInternalMessages().get(8));
    }

    @DisplayName("placeCard should inform the player if run out of scope")
    @Test
    void placeCardOutOfScope() {
        // Add 1 player to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        gameController.placeCard("player1", 0, 0, 0, true);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(PlaceCardFailedMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("placeCard should inform the player if run out of its turn")
    @Test
    void placeCardOutOfTurn() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // I'm deliberately trying to place a card out of my turn.
        gameController.placeCard(
                // Get the nickname of a player that is not playing
                gameController.getNodeList().stream()
                        .map(PlayerQuadruple::getNickname)
                        .filter(nickname -> !nickname.equals(gameController.getModel().getCurrentPlayerNickname()))
                        .findAny()
                        .orElse(null),
                0, 0, 0, true
        );

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        assertEquals(10, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(GameStartedMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(AssignedStarterCardMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
        assertInstanceOf(ConfirmStarterCardSideSelectionMessage.class, nodeInterfaceStub.getInternalMessages().get(3));
        assertInstanceOf(AssignedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(4));
        assertInstanceOf(ConfirmSelectedSecretObjectiveCardMessage.class, nodeInterfaceStub.getInternalMessages().get(5));
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(6));
        assertInstanceOf(PlayerGameStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(7));
        assertInstanceOf(PlayerTurnMessage.class, nodeInterfaceStub.getInternalMessages().get(8));
        assertInstanceOf(PlaceCardFailedMessage.class, nodeInterfaceStub.getInternalMessages().get(9));
    }

    @DisplayName("enterEndGamePhase should move the game to GAME_ENDED status")
    @Test
    void enterEndGamePhaseTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        nodeInterfaceStub.clearInternalMessages();

        gameController.enterEndPhase();

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(GameControllerStatus.GAME_ENDED, gameController.getStatus());
        assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(MatchStatusMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(MatchWinnersMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
    }

    @DisplayName("sendGameStatus should deliver the game status to the player")
    @Test
    void sendGameStatusTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        nodeInterfaceStub.clearInternalMessages();

        gameController.sendGameStatus("player1");

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(PlayerGameStatusMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("sendPlayerField should deliver the field of a player to the requesting entity")
    @Test
    void sendPlayerFieldTest(){
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        nodeInterfaceStub.clearInternalMessages();

        gameController.sendPlayerField("player1", "player2");

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(ResponsePlayerFieldMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("sendPlayerField should inform the player if the requested field's player does not exist")
    @Test
    void sendPlayerFieldPlayerDoesNotExist(){
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().getFirst().getNode();
        nodeInterfaceStub.clearInternalMessages();

        gameController.sendPlayerField("player1", "player3");

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(NegativeResponsePlayerFieldMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("placeCard should place the card on the field of the player if the placement is valid and inform the player or just inform the player if the placement is invalid")
    @Test
    void placeCardTest() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", false);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Get the current player nickname
        String currentPlayer = gameController.getModel().getCurrentPlayerNickname();
        // Get the node associate with the player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals(currentPlayer))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        // Get the card int the player's hand
        ArrayList<Integer> handCards = null;
        try {
            handCards = gameController.getModel().getPlayerHand(currentPlayer);
        } catch (PlayerNotFoundException e) {
            fail();
        }
        // Clear the internal messages
        assertNotNull(nodeInterfaceStub);
        nodeInterfaceStub.clearInternalMessages();

        // Try to place a card
        for(int card : handCards){
            // The only position valid are 1,1; 1,-1; -1,1; -1,-1 we will try to place the card in all of them up until we find a valid one
            int[][] positions = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            // Start the insanity
            for(int[] position : positions){
                // Place the card
                gameController.placeCard(currentPlayer, card, position[0], position[1], true);
                // Get the outcome
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    fail();
                }
                // Check if the card was placed
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
                if(nodeInterfaceStub.getInternalMessages().getFirst() instanceof PlaceCardFailedMessage){
                    // If the card was not placed, we will try again
                    nodeInterfaceStub.clearInternalMessages();
                } else {
                    assertInstanceOf(PlaceCardConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
                    assertEquals(GameControllerStatus.WAITING_CARD_DRAW, gameController.getStatus());
                    return;
                }
            }
        }
    }

    @DisplayName("submitChatMessage should crash when player tries to send a chat message to non-existing player")
    @Test
    void submitChatMessageShouldCrashWhenRecipientDoesNotExist() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send message to non-existing player
        gameController.submitChatMessage(new ChatMessage("player1", "player3", false, "Hello, player3!"));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("player1")) {
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
            }
            else {
                assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
            }
        }

        // Check the messages received by the first player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)gameController.getNodeList().getFirst().getNode();
        assertInstanceOf(InvalidInboundChatMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());

        // There should be no messages in the chat history
        assertEquals(0, gameController.getChat().getHistory().size());
    }

    @DisplayName("submitChatMessage should crash when a direct chat message is received from a non-existing player")
    @Test
    void submitDirectChatMessageShouldCrashWhenSenderDoesNotExist() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send a message from a non-existing player
        assertThrows(CriticalFailureException.class, () -> gameController.submitChatMessage(new ChatMessage("player3", "player1", false, "Hello, player1!")));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) playerQuadruple.getNode();
            assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
        }

        // There should be no messages in the chat history
        assertEquals(0, gameController.getChat().getHistory().size());
    }

    @DisplayName("submitChatMessage should crash when a broadcast chat message is received from a non-existing player")
    @Test
    void submitBroadCastChatMessageShouldCrashWhenSenderDoesNotExist() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send a message from a non-existing player
        assertThrows(CriticalFailureException.class, () -> gameController.submitChatMessage(new ChatMessage("player3", "player1", true, "Hello, player1!")));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) playerQuadruple.getNode();
            assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
        }

        // There should be no messages in the chat history
        assertEquals(0, gameController.getChat().getHistory().size());
    }

    @DisplayName("submitChatMessage in direct mode should properly deliver message to recipient")
    @Test
    void submitDirectChatMessageShouldWork() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send message
        ChatMessage chatMessage = new ChatMessage("player1", "player3", false, "Hello, player3!");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("player3")) {
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
            }
            else {
                assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
            }
        }

        // Check the messages received by the recipient player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)gameController.getNodeList().get(2).getNode();
        assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());

        // There should be one message in the chat history
        assertEquals(1, gameController.getChat().getHistory().size());
        // Message in the chat history should be exactly the same as the one sent
        assertEquals(chatMessage, gameController.getChat().getHistory().getFirst());
    }

    @DisplayName("submitChatMessage in broadcast mode should properly deliver message to everyone")
    @Test
    void submitBroadcastChatMessageShouldWork() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send message
        ChatMessage chatMessage = new ChatMessage("player1", null, true, "Hello");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
            assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
        }

        // There should be one message in the chat history
        assertEquals(1, gameController.getChat().getHistory().size());
        // Message in the chat history should be exactly the same as the one sent
        assertEquals(chatMessage, gameController.getChat().getHistory().getFirst());
    }

    @DisplayName("sequential submitChatMessage in direct mode to same recipient should properly deliver message to recipient")
    @Test
    void submitSequentialDirectChatMessageShouldWork() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send message
        ChatMessage chatMessage = new ChatMessage("player1", "player3", false, "Hello, player3!");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage));
        // Send message again
        ChatMessage chatMessage2 = new ChatMessage("player1", "player3", false, "Hello, again player3!");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage2));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("player3")) {
                assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
            }
            else {
                assertEquals(0, nodeInterfaceStub.getInternalMessages().size());
            }
        }

        // Check the messages received by the recipient player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)gameController.getNodeList().get(2).getNode();
        assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
        assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().get(1));

        // There should be two messages in the chat history
        assertEquals(2, gameController.getChat().getHistory().size());
        // Messages in the chat history should be exactly the same as the ones sent
        assertEquals(chatMessage, gameController.getChat().getHistory().get(0));
        assertEquals(chatMessage2, gameController.getChat().getHistory().get(1));
    }

    @DisplayName("sequential submitChatMessage in broadcast mode should properly deliver messages to everyone")
    @Test
    void submitSequentialBroadcastChatMessageShouldWork() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Try to send message
        ChatMessage chatMessage = new ChatMessage("player1", null, true, "Hello");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage));
        // Send another message
        ChatMessage chatMessage2 = new ChatMessage("player1", null, true, "Hello again");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage2));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
            assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
        }

        // There should be two messages in the chat history
        assertEquals(2, gameController.getChat().getHistory().size());
        // Messages in the chat history should be exactly the same as the ones sent
        assertEquals(chatMessage, gameController.getChat().getHistory().get(0));
        assertEquals(chatMessage2, gameController.getChat().getHistory().get(1));
    }

    @DisplayName("submitChatMessage should properly deliver different kinds of messages to proper recipients")
    @Test
    void submitMixChatMessageShouldWork() {
        // Add 3 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
            gameController.addPlayer("player3", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Send a direct message
        ChatMessage chatMessage = new ChatMessage("player1", "player2", false, "Hello, player2!");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage));
        // Send a broadcast message
        ChatMessage chatMessage2 = new ChatMessage("player3", null, true, "Hello");
        assertDoesNotThrow(() -> gameController.submitChatMessage(chatMessage2));

        // Wait for threads to deliver all messages to players
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that players have received the right amount of messages
        for (PlayerQuadruple playerQuadruple : gameController.getNodeList()) {
            NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub)playerQuadruple.getNode();
            if (playerQuadruple.getNickname().equals("player2")) {
                assertEquals(2, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
                assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
            }
            else {
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
                assertInstanceOf(OutboundChatMessage.class, nodeInterfaceStub.getInternalMessages().get(0));
            }
        }

        // There should be two messages in the chat history
        assertEquals(2, gameController.getChat().getHistory().size());
        // Messages in the chat history should be exactly the same as the ones sent
        assertEquals(chatMessage, gameController.getChat().getHistory().get(0));
        assertEquals(chatMessage2, gameController.getChat().getHistory().get(1));
    }

    @DisplayName("drawCard should inform the player if it's trying to draw out of its turn")
    @Test
    void drawCardOutOfTurn() {
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // I'm deliberately trying to draw a card out of my turn.
        gameController.drawCard(
                // Get the nickname of a player that is not playing
                Objects.requireNonNull(gameController.getNodeList().stream()
                        .map(PlayerQuadruple::getNickname)
                        .filter(nickname -> !nickname.equals(gameController.getModel().getCurrentPlayerNickname()))
                        .findAny()
                        .orElse(null)),
                0, 0
        );

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that the player has received the right amount of messages. Retrieve the NodeStub of the player that tried to draw the card.
        NodeInterfaceStub nodeStub = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> !playerQuadruple.getNickname().equals(gameController.getModel().getCurrentPlayerNickname()))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        assertInstanceOf(DrawCardFailedMessage.class, Objects.requireNonNull(nodeStub).getInternalMessages().getLast());
    }

    @DisplayName("drawCard should inform the player if the game is not expecting a card draw")
    @Test
    public void drawCardOutOfScope(){
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", true);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // I'm deliberately trying to draw a card out of my turn.
        gameController.drawCard(
                // Get the nickname of a player that is not playing
                gameController.getModel().getCurrentPlayerNickname(),
                0, 0
        );

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        NodeInterfaceStub nodeStub = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals(gameController.getModel().getCurrentPlayerNickname()))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        assertInstanceOf(DrawCardFailedMessage.class, Objects.requireNonNull(nodeStub).getInternalMessages().getLast());
    }

    @DisplayName("drawCard should draw a card from the deck and inform the player")
    @Test
    public void drawCardTest(){
        // Add 2 players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }
        // We are now ready to prepare the game
        gameController.enterPreparationPhase();
        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", false);
        gameController.chooseStarterCardSide("player2", false);

        // Choose the secret objective card
        gameController.chooseSecretObjectiveCard("player1", gameController.getModel().getSecretObjectiveCardsPlayer("player1").getFirst());
        gameController.chooseSecretObjectiveCard("player2", gameController.getModel().getSecretObjectiveCardsPlayer("player2").getFirst());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Get the current player nickname
        String currentPlayer = gameController.getModel().getCurrentPlayerNickname();
        // Get the node associate with the player
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals(currentPlayer))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        // Get the card int the player's hand
        ArrayList<Integer> handCards = null;
        try {
            handCards = gameController.getModel().getPlayerHand(currentPlayer);
        } catch (PlayerNotFoundException e) {
            fail();
        }
        // Clear the internal messages
        assertNotNull(nodeInterfaceStub);
        nodeInterfaceStub.clearInternalMessages();

        // Try to place a card
        outerloop:
        for(int card : handCards){
            // The only position valid are 1,1; 1,-1; -1,1; -1,-1 we will try to place the card in all of them up until we find a valid one
            int[][] positions = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            // Start the insanity
            for(int[] position : positions){
                // Place the card
                gameController.placeCard(currentPlayer, card, position[0], position[1], true);
                // Get the outcome
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    fail();
                }
                // Check if the card was placed
                assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
                if(nodeInterfaceStub.getInternalMessages().getFirst() instanceof PlaceCardFailedMessage){
                    // If the card was not placed, we will try again
                    nodeInterfaceStub.clearInternalMessages();
                } else {
                    assertInstanceOf(PlaceCardConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
                    assertEquals(GameControllerStatus.WAITING_CARD_DRAW, gameController.getStatus());
                    nodeInterfaceStub.clearInternalMessages();
                    break outerloop;
                }
            }
        }

        // We now can draw a card
        gameController.drawCard(currentPlayer, 0, 0);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(GameControllerStatus.WAITING_CARD_PLACEMENT, gameController.getStatus());
        assertNotEquals(gameController.getModel().getCurrentPlayerNickname(), currentPlayer);
        assertEquals(3, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(DrawCardConfirmationMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
        assertInstanceOf(DeckSizeUpdateMessage.class, nodeInterfaceStub.getInternalMessages().get(1));
        assertInstanceOf(PlayerTurnMessage.class, nodeInterfaceStub.getInternalMessages().get(2));
    }

    @DisplayName("pongPlayer should send a PongMessage to the player requesting it")
    @Test
    void pongPlayerTest() {
        // Add a player to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Clear the internal messages that were sent during the player addition
        NodeInterfaceStub nodeInterfaceStub = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player1"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        assertNotNull(nodeInterfaceStub);
        nodeInterfaceStub.clearInternalMessages();

        // Send a PongMessage to the player
        gameController.pongPlayer("player1");

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        assertEquals(1, nodeInterfaceStub.getInternalMessages().size());
        assertInstanceOf(PongMessage.class, nodeInterfaceStub.getInternalMessages().getFirst());
    }

    @DisplayName("getTimer should return the timer")
    @Test
    void getTimerTest() {
        // Check that the timer is not null
        assertNotNull(gameController.getTimer());
        // Check that the timer is an instance of Timer
        assertInstanceOf(Timer.class, gameController.getTimer());
    }

    @DisplayName("Test disconnect() method with GameController in LOBBY status")
    @Test
    void disconnectLobbyTest() {
        // Add two players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // Get the nodes associate with each one of the players
        NodeInterfaceStub nodeInterfaceStub1 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player1"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        NodeInterfaceStub nodeInterfaceStub2 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player2"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);

        // Check that the player has been removed from the model
        assertEquals(2, gameController.getModel().getPlayersNicknames().size());

        // Check that the player has been removed from the game
        assertEquals(2, gameController.getNodeList().size());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Clear the internal messages
        nodeInterfaceStub1.clearInternalMessages();
        nodeInterfaceStub2.clearInternalMessages();

        // Disconnect player1
        gameController.disconnect(nodeInterfaceStub1);

        // Check that the player has been removed from the model
        assertEquals(1, gameController.getModel().getPlayersNicknames().size());

        // Check that the player has been removed from the game
        assertEquals(1, gameController.getNodeList().size());

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // player2 should receive a message that player1 has disconnected and a new player list
        assertEquals(2, nodeInterfaceStub2.getInternalMessages().size());
        assertInstanceOf(LobbyPlayerListMessage.class, nodeInterfaceStub2.getInternalMessages().getFirst());
        assertInstanceOf(PlayerDisconnectMessage.class, nodeInterfaceStub2.getInternalMessages().getLast());
    }

    @DisplayName("Test disconnect() method with GameController in WAITING_STARTER_CARD_CHOICE status")
    @Test
    void disconnectWaitingStarterCardChoiceTest() {
        // Add two players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // We are now ready to prepare the game
        gameController.enterPreparationPhase();

        // Get the nodes associate with each one of the players
        NodeInterfaceStub nodeInterfaceStub1 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player1"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        NodeInterfaceStub nodeInterfaceStub2 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player2"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Clear the internal messages
        nodeInterfaceStub1.clearInternalMessages();
        nodeInterfaceStub2.clearInternalMessages();

        // Disconnect player1
        gameController.disconnect(nodeInterfaceStub1);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // player2 should receive a message that player1 has disconnected
        assertEquals(1, nodeInterfaceStub2.getInternalMessages().size());
        assertInstanceOf(PlayerDisconnectMessage.class, nodeInterfaceStub2.getInternalMessages().getFirst());

        // player1 should be marked as disconnected in the nodeList
        // Retrieve the PlayerQuadruple associated with player1
        PlayerQuadruple playerQuadruple = gameController.getNodeList().stream()
                .filter(p -> p.getNickname().equals("player1"))
                .findFirst()
                .orElse(null);
        assertNotNull(playerQuadruple);
        assertFalse(playerQuadruple.isConnected());
    }

    @DisplayName("Test disconnect() method with GameController in WAITING_SECRET_OBJECTIVE_CARD_CHOICE status")
    @Test
    void disconnectWaitingSecretObjectiveCardChoiceTest() {
        // Add two players to the game
        try {
            gameController.addPlayer("player1", new NodeInterfaceStub());
            gameController.addPlayer("player2", new NodeInterfaceStub());
        } catch (FullLobbyException | DuplicateNicknameException e) {
            fail();
        }

        // We are now ready to prepare the game
        gameController.enterPreparationPhase();

        // Choose the side of the starting card
        gameController.chooseStarterCardSide("player1", false);
        gameController.chooseStarterCardSide("player2", false);

        // Get the nodes associate with each one of the players
        NodeInterfaceStub nodeInterfaceStub1 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player1"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);
        NodeInterfaceStub nodeInterfaceStub2 = (NodeInterfaceStub) gameController.getNodeList().stream()
                .filter(playerQuadruple -> playerQuadruple.getNickname().equals("player2"))
                .findFirst()
                .map(PlayerQuadruple::getNode)
                .orElse(null);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // Clear the internal messages
        nodeInterfaceStub1.clearInternalMessages();
        nodeInterfaceStub2.clearInternalMessages();

        // Disconnect player1
        gameController.disconnect(nodeInterfaceStub1);

        // Wait until all the VirtualView are executed by the OS. I know this is not the best way to test this.
        // Otherwise, I will need to mock the VirtualView and check if the methods are called correctly.
        // Mockito is broken on IntelliJ IDEA.
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail();
        }

        // player2 should receive a message that player1 has disconnected
        assertEquals(1, nodeInterfaceStub2.getInternalMessages().size());
        assertInstanceOf(PlayerDisconnectMessage.class, nodeInterfaceStub2.getInternalMessages().getFirst());

        // player1 should be marked as disconnected in the nodeList
        // Retrieve the PlayerQuadruple associated with player1
        PlayerQuadruple playerQuadruple = gameController.getNodeList().stream()
                .filter(p -> p.getNickname().equals("player1"))
                .findFirst()
                .orElse(null);
        assertNotNull(playerQuadruple);
        assertFalse(playerQuadruple.isConnected());
    }

}
