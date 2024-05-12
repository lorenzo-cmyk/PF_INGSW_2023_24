package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.NodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import it.polimi.ingsw.am32.client.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {

    // Stub class for NodeInterface and StoCMessage. Used to test VirtualView.
    private static class NodeInterfaceStub implements NodeInterface {
        public void uploadToClient(StoCMessage message) throws UploadFailureException {}
        public void pingTimeOverdue() {}
        public void resetTimeCounter() {}
        public void setGameController(GameController gameController) {}
        public NodeInterfaceStub() {}
    }

    private static class StoCMessageStub implements StoCMessage {
        public StoCMessageStub() {}
        public void processMessage(View view) {}
        public String getRecipientNickname() {
            return "";
        }
    }

    @DisplayName("Should throw exception when VirtualView is created with a null connection node")
    @Test
    void shouldThrowExceptionWhenConnectionNodeIsNull() {
        assertThrows(CriticalFailureException.class, () -> new VirtualView(null));
    }

    @DisplayName("Should change connection node when changeNode is called")
    @Test
    void shouldChangeConnectionNodeWhenChangeNodeIsCalled() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);

        NodeInterface newNode = new NodeInterfaceStub();
        virtualView.changeNode(newNode);
        assertEquals(newNode, virtualView.getConnectionNode());
    }

    @DisplayName("Should throw exception when addMessage is called with null")
    @Test
    void shouldThrowExceptionWhenAddMessageIsCalledWithNull() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);

        assertThrows(CriticalFailureException.class, () -> virtualView.addMessage(null));
    }

    @DisplayName("Should add message to queue when addMessage is called")
    @Test
    void shouldAddMessageToQueueWhenAddMessageIsCalled() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        assertTrue(virtualView.getMessageQueue().contains(message));
    }

    @DisplayName("Should remove message from queue when processMessage is called")
    @Test
    void shouldRemoveMessageFromQueueWhenProcessMessageIsCalled() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        virtualView.processMessage();
        assertFalse(virtualView.getMessageQueue().contains(message));
    }

    @DisplayName("Should clear message queue when flushMessages is called")
    @Test
    void shouldClearMessageQueueWhenFlushMessagesIsCalled() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        virtualView.flushMessages();
        assertTrue(virtualView.getMessageQueue().isEmpty());
    }

    @DisplayName("Should handle multiple messages being added concurrently")
    @Test
    void shouldHandleMultipleMessagesBeingAddedConcurrently() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message1 = new StoCMessageStub();
        StoCMessage message2 = new StoCMessageStub();
        // Create two threads that add messages to the queue
        Thread thread1 = new Thread(() -> virtualView.addMessage(message1));
        Thread thread2 = new Thread(() -> virtualView.addMessage(message2));
        // Start the threads
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail();
        }
        // Check that all messages have been added
        assertTrue(virtualView.getMessageQueue().contains(message1));
        assertTrue(virtualView.getMessageQueue().contains(message2));
    }

    @DisplayName("Should handle multiple threads calling processMessage concurrently")
    @Test
    void shouldHandleMultipleThreadsCallingProcessMessageConcurrently() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message1 = new StoCMessageStub();
        StoCMessage message2 = new StoCMessageStub();
        // Add messages to the queue
        virtualView.addMessage(message1);
        virtualView.addMessage(message2);
        // Create two threads that process messages
        Thread thread1 = new Thread(virtualView::processMessage);
        Thread thread2 = new Thread(virtualView::processMessage);
        // Start the threads
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail();
        }
        // Check that all messages have been processed
        assertTrue(virtualView.getMessageQueue().isEmpty());
    }

    @DisplayName("Should handle multiple threads calling flushMessages concurrently")
    @Test
    void shouldHandleMultipleThreadsCallingFlushMessagesConcurrently() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message1 = new StoCMessageStub();
        StoCMessage message2 = new StoCMessageStub();
        // Add messages to the queue
        virtualView.addMessage(message1);
        virtualView.addMessage(message2);
        // Create two threads that flush the message queue
        Thread thread1 = new Thread(virtualView::flushMessages);
        Thread thread2 = new Thread(virtualView::flushMessages);
        // Start the threads
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(virtualView.getMessageQueue().isEmpty());
    }

    @DisplayName("Should handle multiple threads calling changeNode concurrently")
    @Test
    void shouldHandleMultipleThreadsCallingChangeNodeConcurrently() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        NodeInterface newNode1 = new NodeInterfaceStub();
        NodeInterface newNode2 = new NodeInterfaceStub();
        // Create two threads that change the connection node
        Thread thread1 = new Thread(() -> virtualView.changeNode(newNode1));
        Thread thread2 = new Thread(() -> virtualView.changeNode(newNode2));
        // Start the threads
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail();
        }
        NodeInterface finalNode = virtualView.getConnectionNode();
        assertTrue(finalNode == newNode1 || finalNode == newNode2);
    }

    @DisplayName("Should handle a combination of adding messages and processing messages concurrently")
    @Test
    void shouldHandleCombinationOfAddingAndProcessingMessagesConcurrently() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message1 = new StoCMessageStub();
        StoCMessage message2 = new StoCMessageStub();
        // Create threads that add and process messages
        Thread thread1 = new Thread(() -> virtualView.addMessage(message1));
        Thread thread2 = new Thread(virtualView::processMessage);
        Thread thread3 = new Thread(() -> virtualView.addMessage(message2));
        Thread thread4 = new Thread(virtualView::processMessage);
        // Start the threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        // Wait for the threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            fail();
        }
        // Check that all messages have been added and processed correctly
        assertTrue(virtualView.getMessageQueue().isEmpty());
    }

    @DisplayName("Should process message when submitted to ThreadPoolExecutor")
    @Test
    void shouldProcessMessageWhenSubmittedToThreadPoolExecutor() {
        NodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();
        // Create a ThreadPoolExecutor with a single thread: the VirtualView
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(virtualView);
        // Add a message to the VirtualView
        virtualView.addMessage(message);
        // Give some time for the VirtualView to process the message
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
        }
        // Check that the message has been processed
        assertTrue(virtualView.getMessageQueue().isEmpty());
        // Shutdown the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            fail();
        }
    }

    @DisplayName("Should wait when UploadFailureException is thrown")
    @Test
    void shouldWaitWhenUploadFailureExceptionIsThrown() {
        // Create a VirtualView with a NodeInterface that throws UploadFailureException
        NodeInterface node = new NodeInterfaceStub() {
            @Override
            public void uploadToClient(StoCMessage message) throws UploadFailureException {
                throw new UploadFailureException();
            }
        };
        VirtualView virtualView = new VirtualView(node);

        // Create a ThreadPoolExecutor with a single thread: the VirtualView
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(virtualView);

        // Add a message to the VirtualView
        StoCMessage message = new StoCMessageStub();
        virtualView.addMessage(message);

        // Give some time for the VirtualView to process the message
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that the message has not been processed and is still in the queue
        assertFalse(virtualView.getMessageQueue().isEmpty());
    }

}