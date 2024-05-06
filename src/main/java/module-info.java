module it.polimi.ingsw.am32 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires net.bytebuddy;
    requires org.apache.logging.log4j;

    opens it.polimi.ingsw.am32 to javafx.fxml;
    exports it.polimi.ingsw.am32;
}