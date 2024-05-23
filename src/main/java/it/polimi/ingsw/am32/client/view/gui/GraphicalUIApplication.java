package it.polimi.ingsw.am32.client.view.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class GraphicalUIApplication extends Application {
    private GraphicalUI graphicalUI;
    String[] ImagesNames={}; //TODO
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setMaxHeight(750);
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(975);
        primaryStage.setMaxWidth(975);
        graphicalUI = new GraphicalUI();
        primaryStage.setScene(new Scene(graphicalUI.getWelcomeRoot(), 975, 750));
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            primaryStage.setScene(new Scene(graphicalUI.getSelectionRoot(), 975, 750));
        }); //FIXME WHY AFTER 3 SECONDS THE NEW BACKGROUND IS NOT CENTERED ANYMORE?
        pauseTransition.play();



        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}