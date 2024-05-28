package it.polimi.ingsw.am32.client.view.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class ChatArea {
    /**
     * An object representing the chat area in the GUI
     */
    private final VBox chatArea;
    /**
     * An object representing the area where messages are displayed.
     * Contained inside the scroll pane
     */
    private final VBox messageDisplayArea;
    /**
     * An object representing the scroll pane that contains the message display area
     */
    private final ScrollPane messageScrollPane;
    /**
     * An object representing the area where the user can input messages.
     * Contains the text field and the submit button
     */
    private final HBox submissionArea;
    /**
     * An object representing the text field where the user can input messages
     */
    private final TextField inputMessageField;
    /**
     * An object representing the button that submits the message
     */
    private final Button submitButton;

    private final ComboBox<String> playerList;

    /**
     * Constructor for the ChatArea class
     *
     * @param X X coordinate of the chat area
     * @param Y Y coordinate of the chat area
     * @param width Width of the chat area
     * @param height Height of the chat area
     */
    public ChatArea(int X, int Y, int width, int height, ArrayList<String> players) {
        // Initialize empty components

        this.chatArea = new VBox();

        this.messageDisplayArea = new VBox();
        this.messageScrollPane = new ScrollPane(messageDisplayArea);

        this.playerList = new ComboBox<String>();

        this.submissionArea = new HBox();
        this.inputMessageField = new TextField();
        this.submitButton = new Button("Send");

        // Configure components and arrange them

        initializeChatArea(X, Y, width, height, players);
    }

    /**
     * Initializes the chat area with the given dimensions.
     * Generates all components, and configures them.
     *
     * @param X X coordinate of the chat area
     * @param Y Y coordinate of the chat area
     * @param width Width of the chat area
     * @param height Height of the chat area
     */
    private void initializeChatArea(int X, int Y, int width, int height, ArrayList<String> players) {
        // Set the effective size of the chat area
        messageScrollPane.setMinSize(width+80, height);
        messageScrollPane.setMaxSize(width+80, height);

        // Enable only vertical scrolling
        messageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        for (String player : players) {
            playerList.getItems().add(player);
        }
        playerList.getItems().add("All");
        playerList.setStyle("-fx-background-color: #E6DEB3;-fx-text-fill: #3A2111;"+
                "-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';-fx-border-color: #3A2111; -fx-border-width: 1px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        // Set style of the chat area
        messageScrollPane.setStyle("-fx-background-color: #E6DEB3;-fx-border-color: #3A2111; -fx-border-width: 1px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        submitButton.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , " +
                "rgba(58,33,17,100,0.2),10,0,0,10);");
        playerList.setMaxSize(80, 30);

        inputMessageField.setPromptText("Type your message......");
        inputMessageField.setStyle("-fx-background-color: #E6DEB359;-fx-text-fill: #3A2111;"+
                "-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';-fx-border-color: #3A2111; -fx-border-width: 1px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        inputMessageField.setPrefSize(250, 30);
        chatArea.setPrefSize(width+100, height + inputMessageField.getHeight());

        // Link the button to a handler method
        submitButton.setOnAction(e -> submitChatMessage());
        submitButton.setMaxSize(100, 30);

        inputMessageField.setOnMouseClicked(e -> {
            messageScrollPane.setVisible(true);
        });
        messageScrollPane.setOnMouseClicked(e -> {
            messageScrollPane.setVisible(false);
        });

        // Generate VBox container
        submissionArea.getChildren().addAll(playerList,inputMessageField, submitButton); // Add the text field and submit button to the chat input area
        chatArea.getChildren().addAll(messageScrollPane,submissionArea); // Add the scroll pane and chat input area to the chat area
        // Set the position of the chat area
        chatArea.setTranslateX(X);
        chatArea.setTranslateY(Y);
    }

    /**
     * Adds an incoming message to the chat area.
     * The message is appended to the end of the message display area.
     * Called by outside classes to add messages to the chat area.
     *
     * @param message The message to be added
     */
    public void addIncomingMessageToChat(String message) {
        Label newMessage = new Label(message);
        newMessage.setStyle("-fx-text-fill: #E6DEB3;-fx-alignment: center;" +
                "-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
        messageDisplayArea.getChildren().add(newMessage);
    }

    /**
     * Submits a message to the chat area.
     * The message is taken from the input field, and added to the message display area.
     * Called when the user clicks the submit button.
     */
    private void submitChatMessage() {
        if (inputMessageField.getText().isEmpty()) return; // Do not send empty messages (or messages with only whitespace characters)

        Label newMessage = new Label(">> " + inputMessageField.getText());
        newMessage.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 20px;-fx-font-family: 'JejuHallasan';");
        messageDisplayArea.getChildren().add(newMessage);
        inputMessageField.clear(); // Clear the input field after the message is sent
    }

    /**
     * Returns the chat area
     *
     * @return The chat area
     */
    public VBox getChatArea() {
        return chatArea;
    }
}
