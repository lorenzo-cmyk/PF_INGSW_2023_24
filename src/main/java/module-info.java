module it.polimi.ingsw.am32 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires java.rmi;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j.core;

    opens it.polimi.ingsw.am32 to javafx.fxml;
    exports it.polimi.ingsw.am32;
    exports it.polimi.ingsw.am32.network.ClientAcceptor to java.rmi;
    exports it.polimi.ingsw.am32.network to java.rmi;
    exports it.polimi.ingsw.am32.model.exceptions to java.rmi;
    exports it.polimi.ingsw.am32.controller.exceptions.abstraction to java.rmi;
    exports it.polimi.ingsw.am32.controller to java.rmi;
    exports it.polimi.ingsw.am32.controller.exceptions to java.rmi;
    exports it.polimi.ingsw.am32.network.exceptions to java.rmi;
    exports it.polimi.ingsw.am32.network.ClientNode to java.rmi;
    exports it.polimi.ingsw.am32.network.ServerNode to java.rmi;
    exports it.polimi.ingsw.am32.message.ClientToServer to java.rmi;
    exports it.polimi.ingsw.am32.message.ServerToClient to java.rmi;
    exports it.polimi.ingsw.am32.client.view.gui to javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.am32.client.view.tui to javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.am32.client to javafx.fxml, javafx.graphics;
}