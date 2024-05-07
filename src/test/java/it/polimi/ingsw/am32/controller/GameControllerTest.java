package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.FullLobbyException;
import it.polimi.ingsw.am32.controller.exceptions.VirtualViewNotFoundException;
import it.polimi.ingsw.am32.message.ServerToClient.*;
import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.network.NodeInterface;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

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
        public void uploadToClient(StoCMessage message) {
            internalMessages.add(message);
        }
        public void pingTimeOverdue() {
            // STUB
        }
        public void resetTimeCounter() {
            // STUB
        }
        public void setGameController(GameController gameController) {
            // STUB
        }
        public ArrayList<StoCMessage> getInternalMessages() {
            return internalMessages;
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedCards");
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedCards");
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedCards");
            assignedCards.setAccessible(true);
            ArrayList<Integer> secretObjectiveCardIds = (ArrayList<Integer>) assignedCards.get(assignedSecretObjectiveCardMessage);
            secretObjectiveCardId = secretObjectiveCardIds.getFirst();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }

        AssignedSecretObjectiveCardMessage assignedSecretObjectiveCardMessage2 = (AssignedSecretObjectiveCardMessage) nodeInterfaceStub2.getInternalMessages().get(4);
        int secretObjectiveCardId2 = -1;
        try {
            Field assignedCards = AssignedSecretObjectiveCardMessage.class.getDeclaredField("assignedCards");
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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
            Thread.sleep(1000);
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

}
