package it.polimi.ingsw.am32.client.view.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUITemporary extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setMaxHeight(750);
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(975);
        primaryStage.setMaxWidth(975);
        StackPane preparationPhase = new StackPane();
        preparationPhase.setBackground(new Background(new BackgroundImage(new Image("/PreparationDisplay.png"), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(975, 750, false, false, false, false))));
        primaryStage.setScene(new Scene(preparationPhase, 975, 750));
        primaryStage.show();
    }
}
