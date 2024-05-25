module it.polimi.ingsw.am32 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires java.rmi;
    requires org.jetbrains.annotations;

    opens it.polimi.ingsw.am32 to javafx.fxml;
    exports it.polimi.ingsw.am32;
    exports it.polimi.ingsw.am32.client.view.gui to javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.am32.client.view.tui to javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.am32.client to javafx.fxml, javafx.graphics;
}