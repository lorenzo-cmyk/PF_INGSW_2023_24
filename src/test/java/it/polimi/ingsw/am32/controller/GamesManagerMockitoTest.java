package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.*;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GamesManagerMockitoTest {

    private GamesManager gamesManager;

    @Mock
    private GameController gameController;

    @Mock
    private NodeInterface node;

    @Mock
    private VirtualView virtualView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Clear the instance of GamesManager
        GamesManager.getInstance().clearInstance();
        gamesManager = GamesManager.getInstance();
        // Add the mocked GameController to the list of games
        gamesManager.getGames().add(gameController);
    }

    @DisplayName("A player cannot reconnect to a game if the nickname is null")
    @Test
    void reconnectToGameTestNicknameIsNull() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.reconnectToGame(null, 1, node));
    }

    @DisplayName("A player cannot reconnect to a game if the nickname is empty")
    @Test
    void reconnectToGameTestNicknameIsEmpty() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.reconnectToGame("", 1, node));
    }

    @DisplayName("A player cannot reconnect to a game if the node is null")
    @Test
    void reconnectToGameTestNodeIsNull() {
        assertThrows(CriticalFailureException.class, () -> gamesManager.reconnectToGame("testPlayer", 1, null));
    }

    @DisplayName("A player cannot reconnect to a game if the game does not exist")
    @Test
    void reconnectToGameTestGameNotFound() {
        when(gameController.getId()).thenReturn(2);
        assertThrows(GameNotFoundException.class, () -> gamesManager.reconnectToGame("testPlayer", 1, node));
    }

    @DisplayName("A player cannot reconnect to a game if the game has ended")
    @Test
    void reconnectToGameTestGameEnded() {
        when(gameController.getId()).thenReturn(1);
        when(gameController.getStatus()).thenReturn(GameControllerStatus.GAME_ENDED);
        assertThrows(GameAlreadyEndedException.class, () -> gamesManager.reconnectToGame("testPlayer", 1, node));
    }

    @DisplayName("A player cannot reconnect to a game if the game has not yet started")
    @Test
    void reconnectToGameTestGameNotStarted() {
        when(gameController.getId()).thenReturn(1);
        when(gameController.getStatus()).thenReturn(GameControllerStatus.LOBBY);
        assertThrows(GameNotYetStartedException.class, () -> gamesManager.reconnectToGame("testPlayer", 1, node));
    }

    @DisplayName("GamesManager should throw a CriticalFailureException if the GameController throws a VirtualViewNotFoundException when reconnecting to a game")
    @Test
    void reconnectToGameTestVirtualViewNotFound() throws Exception {
        when(gameController.getId()).thenReturn(1);
        when(gameController.getStatus()).thenReturn(GameControllerStatus.WAITING_STARTER_CARD_CHOICE);
        doNothing().when(gameController).reconnect(any(), any());
        doThrow(VirtualViewNotFoundException.class).when(gameController).submitVirtualViewMessage(any());
        assertThrows(CriticalFailureException.class, () -> gamesManager.reconnectToGame("testPlayer", 1, node));
    }

    @DisplayName("reconnectToGame should call GameController.reconnect() when reconnecting to a game")
    @Test
    void reconnectToGameTestReconnect() throws Exception {
        // THIS TEST RELIES ON THE FACT THAT GAMECONTROLLER WILL BE TESTED IN A SEPARATE TEST CLASS

        // Spoof the behavior of the GameController
        when(gameController.getId()).thenReturn(1);
        when(gameController.getStatus()).thenReturn(GameControllerStatus.WAITING_STARTER_CARD_CHOICE);
        doNothing().when(gameController).reconnect(any(), any());
        doNothing().when(gameController).submitVirtualViewMessage(any());
        // Spoof the behavior of the GameController.getNodeList() method
        ArrayList<PlayerQuadruple> fakePlayers = new ArrayList<>();
        fakePlayers.add(new PlayerQuadruple(node, "connectedPlayer", true, virtualView));
        fakePlayers.add(new PlayerQuadruple(node, "reconnectingPlayer", true, virtualView));
            // GameController will set the connected boolean to true
        when(gameController.getNodeList()).thenReturn(fakePlayers);

        gamesManager.reconnectToGame("reconnectingPlayer", 1, node);
        verify(gameController, times(1)).reconnect(any(), any());
        verify(gameController, times(1)).submitVirtualViewMessage(any());
    }

}
