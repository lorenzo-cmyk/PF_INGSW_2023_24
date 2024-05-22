package it.polimi.ingsw.am32.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class EndMatchDueToDisconnectionTimerTaskMockitoTest {

    @Mock
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = mock(GameController.class);
    }

    @DisplayName("EndMatchDueToDisconnectionTimerTask should execute endMatchDueToDisconnection method on GameController when run is called")
    @Test
    public void testRun() {
        // Mock the method endMatchDueToDisconnection in GameController
        doNothing().when(gameController).endMatchDueToDisconnection();

        // Create an instance of EndMatchDueToDisconnectionTimerTask
        EndMatchDueToDisconnectionTimerTask endMatchDueToDisconnectionTimerTask = new EndMatchDueToDisconnectionTimerTask(gameController);
        endMatchDueToDisconnectionTimerTask.run();

        // Verify that the method endMatchDueToDisconnection in GameController was called
        verify(gameController, times(1)).endMatchDueToDisconnection();
    }
}
