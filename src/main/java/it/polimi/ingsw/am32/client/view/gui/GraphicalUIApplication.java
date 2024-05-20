package it.polimi.ingsw.am32.client.view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;

import java.io.IOException;


public class GraphicalUIApplication extends Application {
    private GraphicalUI graphicalUI;
    String[] ImagesNames={}; //TODO
    StackPane root= new StackPane();
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Codex Naturalis");
        graphicalUI = new GraphicalUI();
        Scene scene = new Scene(graphicalUI.getWelcomeRoot(), 975, 750); // Create a scene with the layout pane as the root
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(750);
        primaryStage.setMaxWidth(975);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}