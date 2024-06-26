package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.ServerNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VirtualViewMockitoTest {

    private ServerNodeInterface nodeInterface;
    private VirtualView virtualView;
    private StoCMessage message;

    @BeforeEach
    void setUp() {
        nodeInterface = mock(ServerNodeInterface.class);
        virtualView = new VirtualView(nodeInterface);
        message = mock(StoCMessage.class);
    }

    @DisplayName("Test the behaviour of the VirtualView thread when the queue is empty and then some messages are added.")
    @Test
    void processMessageWhenQueueIsEmptyAndThenAddSome() throws InterruptedException {
        // Start the VirtualView thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(virtualView);
        // Wait for the thread to process the message
        Thread.sleep(200); // wait for the thread to process the message
        assertEquals(0, virtualView.getMessageQueue().size());
        // Now add a message to the queue
        virtualView.addMessage(message);
        virtualView.addMessage(message);
        // Wait for the thread to process the message
        Thread.sleep(200); // wait for the thread to process the message
        assertEquals(0, virtualView.getMessageQueue().size());
        // Shutdown the thread forcefully
        virtualView.setTerminating();
        // Wait for the thread to destroy itself
        Thread.sleep(200);
        executor.shutdownNow();
    }

    @DisplayName("Test the behaviour of the VirtualView thread when an UploadFailureException is thrown.")
    @Test
    void processMessageWhenUploadFails() throws InterruptedException, UploadFailureException {
        // mock the uploadToClient method to throw an exception
        doThrow(UploadFailureException.class).when(nodeInterface).uploadToClient(any());
        // Start the VirtualView thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(virtualView);
        // Add a message to the queue
        virtualView.addMessage(message); // This will throw an exception in the thread

        // Wait for the thread to process the message
        Thread.sleep(200);
        // Shutdown the thread forcefully
        virtualView.setTerminating();
        // Wait for the thread to destroy itself
        Thread.sleep(200);
        executor.shutdownNow();
        // Check that the message is still in the queue
        assertEquals(1, virtualView.getMessageQueue().size());
    }
}
