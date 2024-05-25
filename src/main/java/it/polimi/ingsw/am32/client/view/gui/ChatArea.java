package it.polimi.ingsw.am32.client.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Constructor for the ChatArea class
     *
     * @param X X coordinate of the chat area
     * @param Y Y coordinate of the chat area
     * @param width Width of the chat area
     * @param height Height of the chat area
     */
    public ChatArea(int X, int Y, int width, int height) {
        // Initialize empty components

        this.chatArea = new VBox();

        this.messageDisplayArea = new VBox();
        this.messageScrollPane = new ScrollPane(messageDisplayArea);

        this.submissionArea = new HBox();
        this.inputMessageField = new TextField();
        this.submitButton = new Button("Send");

        // Configure components and arrange them

        initializeChatArea(X, Y, width, height);
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
    private void initializeChatArea(int X, int Y, int width, int height) {
        // Set the effective size of the chat area
        messageScrollPane.setMinSize(width, height);
        messageScrollPane.setMaxSize(width, height);

        // Set the position of the chat area
        chatArea.setTranslateX(X);
        chatArea.setTranslateY(Y);

        // Enable only vertical scrolling
        messageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Link the button to a handler method
        submitButton.setOnAction(e -> submitChatMessage());

        // Generate VBox container
        submissionArea.getChildren().addAll(inputMessageField, submitButton); // Add the text field and submit button to the chat input area
        chatArea.getChildren().addAll(messageScrollPane, submissionArea); // Add the scroll pane and chat input area to the chat area
    }

    /**
     * Adds an incoming message to the chat area.
     * The message is appended to the end of the message display area.
     * Called by outside classes to add messages to the chat area.
     *
     * @param message The message to be added
     */
    public void addIncomingMessageToChat(@NotNull String message) {
        Label newMessage = new Label(message);
        messageDisplayArea.getChildren().add(newMessage);
    }

    /**
     * Submits a message to the chat area.
     * The message is taken from the input field, and added to the message display area.
     * Called when the user clicks the submit button.
     */
    private void submitChatMessage() {
        Label newMessage = new Label(">> " + inputMessageField.getText());
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
