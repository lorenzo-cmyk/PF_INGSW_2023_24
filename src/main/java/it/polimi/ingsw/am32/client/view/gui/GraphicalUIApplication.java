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
    String[] ImagesNames={}; //TODO
    StackPane root= new StackPane();
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, 1200, 785); // Create a scene with the layout pane as the root
        primaryStage.setScene(scene);

        Button button = new Button("RuleBook");
        VBox buttonBox = new VBox();
        buttonBox.getChildren().add(button);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        root.getChildren().add(buttonBox);
        Image backgroundWelcomePage = new Image("WelcomeDisplay.png");
        BackgroundImage backgroundImg = new BackgroundImage(backgroundWelcomePage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1200, 1025, false, false, false, false));
        Background background = new Background(backgroundImg);

        root.setBackground(background);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}