module it.polimi.ingsw.am32 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw.am32 to javafx.fxml;
    exports it.polimi.ingsw.am32;
}