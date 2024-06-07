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
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }
    public void updateScene(Parent parent,int x,int y) {
        primaryStage.setScene(new Scene(parent,x,y));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}