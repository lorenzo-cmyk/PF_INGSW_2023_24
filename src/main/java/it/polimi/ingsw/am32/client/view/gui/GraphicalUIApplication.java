package it.polimi.ingsw.am32.client.view.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class GraphicalUIApplication extends Application {
    private final GraphicalUI graphicalUI;
    private Stage primaryStage;
    public GraphicalUIApplication(){
        this.graphicalUI = new GraphicalUI();
        this.graphicalUI.setApp(this);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(975);
        primaryStage.setScene(new Scene(graphicalUI.getWelcomeRoot(), 975, 750));
        primaryStage.setScene(new Scene(graphicalUI.getSelectionRoot(), 975, 750));

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }
    public void changeStageDimensions(Stage stage,int MAXHigh, int MINHigh, int MAXWidth, int MINWidth) {
        stage.setMaxHeight(MAXHigh);
        stage.setMinHeight(MINHigh);
        stage.setMinWidth(MINWidth);
        stage.setMaxWidth(MAXWidth);
    }
    public void updateScene(Parent parent) {
        primaryStage.setScene(new Scene(parent));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}