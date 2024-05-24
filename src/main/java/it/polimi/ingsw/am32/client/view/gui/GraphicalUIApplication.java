package it.polimi.ingsw.am32.client.view.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
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
        changeStageDimensions(primaryStage, 750, 750, 975, 975);
        primaryStage.setScene(new Scene(graphicalUI.getWelcomeRoot(), 975, 750));
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            primaryStage.setScene(new Scene(graphicalUI.getSelectionRoot(), 975, 750));
        }); //FIXME WHY AFTER 3 SECONDS THE NEW BACKGROUND IS NOT CENTERED ANYMORE?
        pauseTransition.play();



        primaryStage.show();
    }
    public void changeStageDimensions(Stage stage,int MAXHigh, int MINHigh, int MAXWidth, int MINWidth) {
        stage.setMaxHeight(MAXHigh);
        stage.setMinHeight(MINHigh);
        stage.setMinWidth(MINWidth);
        stage.setMaxWidth(MAXWidth);
    }
    public void updateScene(Parent parent, int x, int y) {
        primaryStage.setScene(new Scene(parent, x, y));
    }
}