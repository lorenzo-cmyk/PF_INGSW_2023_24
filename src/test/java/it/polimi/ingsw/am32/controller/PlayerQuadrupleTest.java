package it.polimi.ingsw.am32.controller;

import it.polimi.ingsw.am32.controller.exceptions.CriticalFailureException;
import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.network.ServerNode.ServerNodeInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerQuadrupleTest {

    // ServerNodeInterface and VirtualView stubs. They are not relevant for the test, so they can be mocked.
    private static class NodeInterfaceStub implements ServerNodeInterface {
        // Fake Methods
        public void uploadToClient(StoCMessage message) {}
        public void pingTimeOverdue() {}
        public void resetTimeCounter() {}

        // Fake Constructor
        public NodeInterfaceStub() {}
    }

    private static class VirtualViewStub extends VirtualView {
        // Fake Constructor
        public VirtualViewStub(ServerNodeInterface node) {
            super(node);
        }
    }

    @DisplayName("Should create a player quadruple when parameters are valid")
    @Test
    void shouldCreatePlayerQuadrupleWhenParametersAreValid() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        PlayerQuadruple playerQuadruple = new PlayerQuadruple(node, "nickname", true, virtualView);

        assertEquals(node, playerQuadruple.getNode());
        assertEquals("nickname", playerQuadruple.getNickname());
        assertTrue(playerQuadruple.isConnected());
        assertEquals(virtualView, playerQuadruple.getVirtualView());
    }

    @DisplayName("Should throw exception when node is null")
    @Test
    void shouldThrowExceptionWhenNodeIsNull() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        assertThrows(CriticalFailureException.class, () -> new PlayerQuadruple(null, "nickname", true, virtualView));
    }

    @DisplayName("Should throw exception when nickname is null")
    @Test
    void shouldThrowExceptionWhenNicknameIsNull() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        assertThrows(CriticalFailureException.class, () -> new PlayerQuadruple(node, null, true, virtualView));
    }

    @DisplayName("Should throw exception when nickname is empty")
    @Test
    void shouldThrowExceptionWhenNicknameIsEmpty() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        assertThrows(CriticalFailureException.class, () -> new PlayerQuadruple(node, "", true, virtualView));
    }

    @DisplayName("Should throw exception when virtual view is null")
    @Test
    void shouldThrowExceptionWhenVirtualViewIsNull() {
        ServerNodeInterface node = new NodeInterfaceStub();
        assertThrows(CriticalFailureException.class, () -> new PlayerQuadruple(node, "nickname", true, null));
    }

    @DisplayName("Should change connection status when setConnected is called")
    @Test
    void shouldChangeConnectionStatusWhenSetConnectedIsCalled() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        PlayerQuadruple playerQuadruple = new PlayerQuadruple(node, "nickname", true, virtualView);

        playerQuadruple.setConnected(false);
        assertFalse(playerQuadruple.isConnected());
    }

    @DisplayName("Should change node when setNode is called")
    @Test
    void shouldChangeNodeWhenSetNodeIsCalled() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        PlayerQuadruple playerQuadruple = new PlayerQuadruple(node, "nickname", true, virtualView);

        ServerNodeInterface newNode = new NodeInterfaceStub();
        playerQuadruple.setNode(newNode);
        assertEquals(newNode, playerQuadruple.getNode());
    }

    @DisplayName("Should throw exception when setNode is called with null")
    @Test
    void shouldThrowExceptionWhenSetNodeIsCalledWithNull() {
        ServerNodeInterface node = new NodeInterfaceStub();
        VirtualView virtualView = new VirtualViewStub(node);
        PlayerQuadruple playerQuadruple = new PlayerQuadruple(node, "nickname", true, virtualView);

        assertThrows(CriticalFailureException.class, () -> playerQuadruple.setNode(null));
    }
}
