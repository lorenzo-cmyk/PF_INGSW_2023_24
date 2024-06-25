package it.polimi.ingsw.am32.client.view.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * The GraphicalUIApplication class extends the Application class from JavaFX. It interacts with the GraphicalUI
 * class to display the graphical user interface to the user.
 */
public class GraphicalUIApplication extends Application {
    /**
     * The reference to the graphical user interface
     */
    private final GraphicalUI graphicalUI;
    /**
     * The primary stage of the application
     */
    private Stage primaryStage;
    /**
     * The constructor of the class
     */
    public GraphicalUIApplication(){
        this.graphicalUI = new GraphicalUI();
        this.graphicalUI.setApp(this);
    }

    /**
     * The start method override as required by the Application class:
     * it sets the primary stage of the application, the title, the minimum height and width, the scene and
     * the close request event when the user closes the application.
     * @param primaryStage the primary stage of the application
     * @throws IOException as required by the superclass
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        // set the title of the application
        primaryStage.setTitle("Codex Naturalis");
        // set the icon of the application
        String iconPath;
        try {
            iconPath = Objects.requireNonNull(GraphicalUI.class.getResource("codexNaturalis.png")).toURI().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Image icon = new Image(iconPath);
        primaryStage.getIcons().add(icon);

        // set the minimum height and width and the beginning scene of the application
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(975);
        primaryStage.setScene(new Scene(graphicalUI.getWelcomeRoot(), 975, 750));
        // set the close request event
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        // show the primary stage
        primaryStage.show();
    }

    /**
     * The method that updates the scene of the primary stage by setting a new scene.
     * @param parent the parent of the new scene which is the base of all nodes
     * @param x the width of the new scene
     * @param y the height of the new scene
     */
    public void updateScene(Parent parent,int x,int y) {
        primaryStage.setScene(new Scene(parent,x,y));
    }

    /**
     * The getter of the primary stage of the application
     * @return the primary stage of the application
     */
    protected Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Launches the application with the given arguments
     * @param args the arguments to be passed to the application
     **/
    public static void main(String[] args) {
        launch(args);
    }
}