package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.ServerNodeInterface;
import it.polimi.ingsw.am32.network.exceptions.UploadFailureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {

    // Stub class for ServerNodeInterface and StoCMessage. Used to test VirtualView.
    private static class NodeInterfaceStub implements ServerNodeInterface {
        private int messageCount;
        public void uploadToClient(StoCMessage message) throws UploadFailureException { messageCount++; }
        public void pingTimeOverdue() {}
        public void resetTimeCounter() {}
        public int getMessageCount() { return messageCount; }
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
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);

        ServerNodeInterface newNode = new NodeInterfaceStub();
        virtualView.changeNode(newNode);
        assertEquals(newNode, virtualView.getConnectionNode());
    }

    @DisplayName("Should throw exception when addMessage is called with null")
    @Test
    void shouldThrowExceptionWhenAddMessageIsCalledWithNull() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);

        assertThrows(CriticalFailureException.class, () -> virtualView.addMessage(null));
    }

    @DisplayName("Should add message to queue when addMessage is called")
    @Test
    void shouldAddMessageToQueueWhenAddMessageIsCalled() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        assertTrue(virtualView.getMessageQueue().contains(message));
    }

    @DisplayName("Should remove message from queue when processMessage is called")
    @Test
    void shouldRemoveMessageFromQueueWhenProcessMessageIsCalled() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        virtualView.processMessage();
        assertFalse(virtualView.getMessageQueue().contains(message));
    }

    @DisplayName("Should clear message queue when flushMessages is called")
    @Test
    void shouldClearMessageQueueWhenFlushMessagesIsCalled() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        StoCMessage message = new StoCMessageStub();

        virtualView.addMessage(message);
        virtualView.flushMessages();
        assertTrue(virtualView.getMessageQueue().isEmpty());
    }

    @DisplayName("Should handle multiple messages being added concurrently")
    @Test
    void shouldHandleMultipleMessagesBeingAddedConcurrently() {
        ServerNodeInterface node = new NodeInterfaceStub();
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

    @DisplayName("Should handle multiple threads calling flushMessages concurrently")
    @Test
    void shouldHandleMultipleThreadsCallingFlushMessagesConcurrently() {
        ServerNodeInterface node = new NodeInterfaceStub();
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
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);
        ServerNodeInterface newNode1 = new NodeInterfaceStub();
        ServerNodeInterface newNode2 = new NodeInterfaceStub();
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
        ServerNodeInterface finalNode = virtualView.getConnectionNode();
        assertTrue(finalNode == newNode1 || finalNode == newNode2);
    }

    @DisplayName("Should process message when submitted to ThreadPoolExecutor")
    @Test
    void shouldProcessMessageWhenSubmittedToThreadPoolExecutor() {
        ServerNodeInterface node = new NodeInterfaceStub();
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
        // Create a VirtualView with a ServerNodeInterface that throws UploadFailureException
        ServerNodeInterface node = new NodeInterfaceStub() {
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

    @DisplayName("Should be able to handle multiple messages being added concurrently")
    @Test
    void shouldBeAbleToHandleMessagesBurst(){
        // Create a VirtualView with a ServerNodeInterface that throws UploadFailureException
        NodeInterfaceStub node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualView(node);

        // Create a ThreadPoolExecutor with a single thread: the VirtualView
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(virtualView);

        // Add 1000 messages to the VirtualView
        for(int i = 0; i < 1000; i++){
            StoCMessage message = new StoCMessageStub();
            virtualView.addMessage(message);
        }

        // Give some time for the VirtualView to process the messages
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that the messages have been processed
        assertEquals(1000, node.getMessageCount());

        // Terminate the VirtualView
        virtualView.setTerminating();

        // Give some time for the VirtualView to terminate
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
        }

        // Check that the VirtualView has terminated
        assertTrue(future.isDone());
    }

}