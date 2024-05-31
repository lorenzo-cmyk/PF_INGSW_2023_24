package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.*;
import it.polimi.ingsw.am32.message.ClientToServer.*;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

public class GraphicalUI extends View {
    private GraphicalUIApplication app;
    private Pane masterPane;
    private StackPane welcomeRoot;
    private StackPane selectionPane;
    private StackPane connectionRoot;
    private StackPane waitingRoot;
    private Pane endGamePane;
    private TextField playerListView;
    private Label matchStatus;
    private HBox playerOrder;
    private Label goldSize;
    private Label resourceSize;
    private final Glow glow = new Glow(0.3);
    private final DropShadow dropShadow = new DropShadow(15, Color.rgb(255, 196, 83));

    private final HashMap<String, PlayerPubView> playerViews = new HashMap<>();
    private final HashMap<String, Image> imagesMap = new HashMap<>();
    private final HashMap<String, StackPane> playerField = new HashMap<>();
    private ChatArea chatArea;
    /**
     * An array of ImageView objects containing the images of the cards in the player's hand.
     */
    private ImageView [] handView;
    /**
     * An array of booleans indicating whether the card in the player's hand is being viewed front side or back side.
     */
    private boolean [] handViewCardSide;
    private ImageView[] resourceDeckView;
    private ImageView[] goldDeckView;
    private ImageView secretObjCardView;
    private ImageView[] commonObjCardView;
    private VBox notice;
    private Label eventLabel;
    private Group noticeEventPanel;
    private ScrollPane board;
    private Button returnToMyField;
    private VBox deckArea;
    private final Font jejuHallasanFont = Font.loadFont(getClass().getResourceAsStream("/JejuHallasan.ttf"), 20);
    private final String[] ruleBookImages = {"/codex_rulebook_it_01.png", "/codex_rulebook_it_02.png", "/codex_rulebook_it_03.png",
            "/codex_rulebook_it_04.png", "/codex_rulebook_it_05.png", "/codex_rulebook_it_06.png", "/codex_rulebook_it_07.png",
            "/codex_rulebook_it_08.png", "/codex_rulebook_it_09.png", "/codex_rulebook_it_10.png", "/codex_rulebook_it_11.png",
            "/codex_rulebook_it_12.png"};
    /**
     * ID of card selected for placement on the field by player.
     * Set to 0 when no card is selected.
     */
    private int selectedCardId;

    /**
     * Used to launch the GUIApplication class.
     */
    @Override
    public void launch() {
        Application.launch(GraphicalUIApplication.class);
    }

    /**
     * Constructor of the class.
     */
    public GraphicalUI() {
        super();
    }

    /**
     * Set the connection between the GUI and the GUIApplication. In this way the GUI can update the scene of the
     * GUIApplication using the reference of the GUIApplication and the methods of the GUIApplication.
     *
     * @param app The GUIApplication with which the GUI will be connected.
     */
    protected void setApp(GraphicalUIApplication app) {
        this.app = app;
    }

    /**
     * Get the root of the welcome page, which is the first page shown to the player.
     */
    protected StackPane getWelcomeRoot() {
        showWelcome();
        return welcomeRoot;
    }

    /**
     * Get the root of the selection page, which is the page where the player can choose the connection type, choose
     * the game mode and insert the data needed to create, join or reconnect to a game.
     */
    public StackPane getSelectionRoot() {
        chooseConnection();
        return selectionPane;
    }

    /**
     * Set up the welcome page of the GUI, which contains the welcome image and the button to show the rule book.
     */
    @Override
    public void showWelcome() {
        welcomeRoot = new StackPane();
        Button ruleButton = createTransButton("[Rule Book]", 30, "#E6DEB3", 0, 300);
        welcomeRoot.getChildren().add(ruleButton);
        ruleButton.setTranslateX(350);
        ruleButton.setTranslateY(300);
        ruleButton.setOnAction(e -> showRuleBook());
        Image backgroundWelcomePage = new Image("/WelcomeDisplay.png");
        BackgroundImage backgroundImg = new BackgroundImage(backgroundWelcomePage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(975, 925, false, false, false, false));
        Background background = new Background(backgroundImg);
        welcomeRoot.setBackground(background);
    }

    /**
     * Set up the rule book of the game. The player can navigate through the pages of the rule book using the buttons
     * “<” to go to the previous page and “>” to go to the next page.
     */
    private void showRuleBook() {
        StackPane ruleBookRoot = new StackPane();
        Stage ruleBookStage = new Stage();
        Image[] ruleBookImage = new Image[ruleBookImages.length];
        for (int i = 0; i < ruleBookImages.length; i++) {
            ruleBookImage[i] = new Image(ruleBookImages[i], 750, 750, true, true);
        }
        ImageView ruleBook = new ImageView(ruleBookImage[0]);
        Button[] buttons = getBox(ruleBook);
        ruleBookRoot.getChildren().add(ruleBook);
        ruleBookRoot.getChildren().add(buttons[0]);
        ruleBookRoot.getChildren().add(buttons[1]);
        buttons[0].setTranslateX(-325);
        buttons[0].setTranslateY(0);
        buttons[1].setTranslateX(325);
        buttons[1].setTranslateY(0);

        Scene scene = new Scene(ruleBookRoot, 750, 750);
        ruleBookStage.setTitle("Rule Book");
        ruleBookStage.setScene(scene);
        ruleBookStage.setMaxHeight(750);
        ruleBookStage.setMaxWidth(750);
        ruleBookStage.show();

    }

    /**
     * Get the buttons to navigate through the pages of the rule book and set the action of the buttons.
     *
     * @param ruleBook The image view of the rule book.
     * @return The array of buttons to navigate through the pages of the rule book.
     */
    private Button[] getBox(ImageView ruleBook) {
        AtomicInteger index = new AtomicInteger();
        Button nextButton = new Button(">");
        Button previousButton = new Button("<");
        nextButton.setStyle("-fx-background-color: transparent;-fx-text-fill: #DBBE70;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.8) , 10,0,0,10 );");
        previousButton.setStyle("-fx-background-color: transparent;-fx-text-fill: #DBBE70;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.8) , 10,0,0,10 );");
        nextButton.setOnAction(e -> {
            index.set((index.get() + 1) % ruleBookImages.length);
            ruleBook.setImage(new Image(ruleBookImages[index.get()], 750, 750, true, true));
        });
        previousButton.setOnAction(e -> {
            index.set((index.get() - 1 + ruleBookImages.length) % ruleBookImages.length);
            ruleBook.setImage(new Image(ruleBookImages[index.get()], 750, 750, true, true));
        });
        return new Button[]{previousButton, nextButton};
    }

    /**
     * Set the connection page of the GUI. The player can choose the connection type between socket and RMI.
     */
    @Override
    public void chooseConnection() {
        currentEvent = Event.CHOOSE_CONNECTION;
        selectionPane = new StackPane();
        connectionRoot = new StackPane();

        // set the background image of the selection page
        Image backgroundSelectionPage = new Image("/SelectionDisplay.png");
        BackgroundImage backgroundImg = new BackgroundImage(backgroundSelectionPage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(975, 925, false, false, false, false));
        Background background = new Background(backgroundImg);
        selectionPane.setBackground(background);

        // create the connection root
        Label label = createLabel("Connection", -80, -70); // add the title of the connection root
        Button socketButton = createButton("[Socket]", 100, 0); // create the button to choose the socket connection
        Button rmiButton = createButton("[RMI]", -100, 0); // create the button to choose the RMI connection
        connectionRoot.getChildren().addAll(label, socketButton, rmiButton);

        // add the connection root to the selection pane in this way set the choose connection page for the player
        selectionPane.getChildren().add(connectionRoot);

        // create the socket connection root
        StackPane socketRoot = new StackPane();
        Label labelIP = createLabel("Socket", -80, -80); // add the title
        TextField ip = createTextField("Enter the IP", 35, 250, -50, -30); // create the text field asks the player to enter the IP address
        TextField port = createTextField("Enter the port", 35, 250, -50, 50); // create the text field asks the player to enter the port number
        Button OkButton = createButton("[OK]", 160, 0); // create the button Ok to confirm the input.
        socketRoot.getChildren().addAll(OkButton, labelIP, ip, port); // the view when the player choose the socket connection

        // create the RMI connection root
        StackPane RMIRoot = new StackPane();
        Label labelRMIIP = createLabel("RMI", -80, -80); // add the title
        TextField RMIIp = createTextField("Enter the IP", 35, 250, -50, -30); // create the text field asks the player to enter the IP address
        TextField RMIPort = createTextField("Enter the port", 35, 250, -50, 50); // create the text field asks the player to enter the port number
        Button OkRMIButton = createButton("[OK]", 160, 0); // create the button Ok to confirm the input.
        RMIRoot.getChildren().addAll(OkRMIButton, labelRMIIP, RMIIp, RMIPort); // the view when the player choose the socket connection

        // set the action of the buttons
        socketButton.setOnAction(e -> {
            selectionPane.getChildren().remove(connectionRoot); // exit from the choose connection page
            selectionPane.getChildren().add(socketRoot); // enter the socket connection page
        });

        OkButton.setOnAction(e -> {
            String ServerIP = ip.getText(); // Read the player's input and save it the server IP address
            String ServerPort = port.getText();
            try {
                int portNumber = Integer.parseInt(ServerPort);
                if (isValid.isIpValid(ServerIP) && isValid.isPortValid(portNumber)) { // Check if the IP address is valid
                    setSocketClient(ServerIP, portNumber);
                    selectionPane.getChildren().remove(socketRoot);
                    askSelectGameMode();
                } else {
                    createAlert("Invalid IP/port number");
                    ip.clear();
                    port.clear();
                }
            } catch (NumberFormatException ex) {
                createAlert("Invalid port number");
                port.clear();
            } catch (IOException ex) {
                createAlert("Connection failed");
                ip.clear();
                port.clear();
            }
        });
        // set the action of the buttons
        rmiButton.setOnAction(e -> {
            selectionPane.getChildren().remove(connectionRoot); // exit from the choose connection page
            selectionPane.getChildren().add(RMIRoot); // enter the socket connection page
        });

        OkRMIButton.setOnAction(e -> {
            String ServerIP = RMIIp.getText(); // Read the player's input and save it the server IP address
            String ServerPort = RMIPort.getText();
            try {
                int portNumber = Integer.parseInt(ServerPort);
                if (isValid.isIpValid(ServerIP) && isValid.isPortValid(portNumber)) { // Check if the IP address is valid
                    setRMIClient(ServerIP, portNumber);
                        selectionPane.getChildren().remove(RMIRoot);
                        askSelectGameMode();
                } else {
                    createAlert("Invalid IP/port number");
                    ip.clear();
                    port.clear();
                }
            } catch (NumberFormatException ex) {
                createAlert("Invalid port number");
                port.clear();
            } /*catch (IOException ex) { //TODO
                createAlert("Connection failed");
                ip.clear();
                port.clear();
            }*/
        });
    }
    /**
     * Set the socket connection between the client and the server.
     *
     * @param ServerIP the IP address of the server
     * @param portNumber the port number of the server
     * @throws IOException if the connection to the server fails
     */
    @Override
    public void setSocketClient(String ServerIP, int portNumber) throws IOException {
        super.setSocketClient(ServerIP, portNumber);
    }
    @Override
    public void setRMIClient(String serverURL, int port) {
        super.setRMIClient(serverURL, port); // see the method in the superclass
    }


    /**
     * Set the page where the player can select the game mode. The player can choose between creating a new game, joining
     * an existing game or reconnecting to a game. The player can choose the game mode using the buttons “New Game”, “Join
     * Game” and “Reconnect”.
     */
    @Override
    public void askSelectGameMode() {
        StackPane gameModeRoot = new StackPane();
        Label label = createLabel("Game \n Mode", 150, 10);
        VBox gameMode = new VBox();
        gameMode.setSpacing(10);
        Button newGameButton = createButton("[New Game]");
        Button joinGameButton = createButton("[Join Game]");
        Button reconnectGameButton = createButton("[Reconnect]");
        gameMode.getChildren().addAll(newGameButton, joinGameButton, reconnectGameButton);
        gameMode.setTranslateX(300);
        gameMode.setTranslateY(300);
        gameModeRoot.getChildren().addAll(label, gameMode);
        selectionPane.getChildren().add(gameModeRoot);
        newGameButton.setOnAction(e -> {
            selectionPane.getChildren().remove(gameModeRoot);
            askCreateGame();
        });
        joinGameButton.setOnAction(e -> {
            selectionPane.getChildren().remove(gameModeRoot);
            askJoinGame();
        });
        reconnectGameButton.setOnAction(e -> {
            selectionPane.getChildren().remove(gameModeRoot);
            askReconnectGame();
        });
    }

    /**
     * Set the page which asks the player to insert the nickname and the players number to create a new game.
     */
    @Override
    public void askCreateGame() {
        currentEvent = Event.CREATE_GAME;
        StackPane createGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname", -90, -80);
        Label labelPlayers = createLabel("Players number", -60, 30);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);

        Button twoButton = createTransButton("[2]", 30, "#3A2111", -150, 70);
        Button threeButton = createTransButton("[3]", 30, "#3A2111", -70, 70);
        Button fourButton = createTransButton("[4]", 30, "#3A2111", 10, 70);
        Button createButton = createButton("[Create]", 140, 65);
        Light.Distant light = new Light.Distant();
        light.setColor(Color.CHOCOLATE);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        createGameRoot.getChildren().addAll(labelNickname, labelPlayers, nickname, twoButton, threeButton, fourButton, createButton);
        twoButton.setOnAction(e -> {
            playerNum = 2;
            handleButtonClick(twoButton, threeButton, fourButton, lighting);
        });
        threeButton.setOnAction(e -> {
            playerNum = 3;
            handleButtonClick(threeButton, twoButton, fourButton,lighting);
        });
        fourButton.setOnAction(e -> {
            playerNum = 4;
            handleButtonClick(fourButton, threeButton, twoButton,lighting);
        });
        createButton.setOnAction(e -> {
            thisPlayerNickname = nickname.getText();
            if (thisPlayerNickname.isBlank()) {
                createAlert("Nickname cannot be left blank");
                nickname.clear();
            } else if (thisPlayerNickname.length() > 20) {
                createAlert("Nickname must be less than 20 characters");
                nickname.clear();
            } else {
                if (playerNum == 2 || playerNum == 3 || playerNum == 4) {
                    notifyAskListener(new NewGameMessage(thisPlayerNickname, playerNum));
                } else {
                    createAlert("Please select the number");
                }
            }
        });
        selectionPane.getChildren().add(createGameRoot);
    }

    /**
     * Set the page which asks the player to insert the nickname and the game ID to reconnect to a game.
     */
    @Override
    public void askJoinGame() {
        currentEvent = Event.JOIN_GAME;
        StackPane joinGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname&GameID", -80, -80);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);
        TextField accessID = createTextField("Game ID", 35, 220, -100, 35);

        Button joinButton = createButton("[Join]", 140, 65);

        joinGameRoot.getChildren().addAll(labelNickname, nickname, accessID, joinButton);

        joinButton.setOnAction(e -> {
            handelButtonJoinAndReconnectClick(nickname,accessID);
        });
        selectionPane.getChildren().add(joinGameRoot);
    }
    @Override
    public void askReconnectGame() {
        currentEvent = Event.RECONNECT_GAME;
        StackPane reconnectGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname&GameID", -80, -80);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);
        TextField accessID = createTextField("Game ID", 35, 220, -100, 35);

        Button joinButton = createButton("[Reconnect]", 140, 65);

        reconnectGameRoot.getChildren().addAll(labelNickname, nickname, accessID, joinButton);

        joinButton.setOnAction(e -> {
            handelButtonJoinAndReconnectClick(nickname,accessID);
        });
        selectionPane.getChildren().add(reconnectGameRoot);
    }
    public void handelButtonJoinAndReconnectClick(TextField nickname, TextField accessID){
            thisPlayerNickname = nickname.getText();
            String ID = accessID.getText();
            if (thisPlayerNickname.isBlank()) {
                createAlert("Nickname cannot be left blank");
                nickname.clear();
            } else if (thisPlayerNickname.length() > 20) {
                createAlert("Nickname must be less than 20 characters");
                nickname.clear();
            } else {
                try {
                    gameID = Integer.parseInt(ID);
                    if (currentEvent.equals(Event.RECONNECT_GAME)) {
                        notifyAskListener(new ReconnectGameMessage(thisPlayerNickname, gameID));
                    } else {
                        notifyAskListener(new AccessGameMessage(gameID, thisPlayerNickname));
                    }
                } catch (NumberFormatException ex) {
                    createAlert("Game ID must be a number");
                    accessID.clear();
                }
            }
    }
    /**
     * Once the player receives the NewGameConfirmationMessage from the server, the method is called by processMessage
     * to store the gameID, the nickname of the player who created the game, and add it in the list of players.
     * In addition, the method sets the currentEvent to WAITING_FOR_START and the Status to LOBBY, then it creates the
     * waiting page where the player can see the gameID and the list of players who joined the game.
     *
     * @param gameID the game ID returned by the server after the confirmation of the new game
     * @param recipientNickname the nickname of the player who asked to create the new game
     */

    @Override
    public void updateNewGameConfirm(int gameID, String recipientNickname) {
        // use Platform.runLater to run the code in the JavaFX Application Thread
        Platform.runLater(() -> {
            selectionPane.getChildren().removeLast(); // remove the last page
            waitingRoot = new StackPane();
            // set the gameID, the nickname of the player who created the game and insert it in the list of players
            this.gameID = gameID;
            this.thisPlayerNickname = recipientNickname;
            this.players.add(recipientNickname);
            currentEvent = Event.WAITING_FOR_START; // enter the waiting for start event
            Status = Event.LOBBY;

            // set the components of the waiting page: the gameID, the status waiting and the list of players
            Label id = createLabel("ID: " + gameID, -80, -80);
            Label waiting = createLabel("Waiting...", 130, 100);

            playerListView = createTextField(null, 135, 360, 0, 0);
            playerListView.setText("Players: \n" + String.join("\n", players));
            playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                    "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
            playerListView.setTranslateX(0);
            playerListView.setTranslateY(0);
            playerListView.setEditable(false);

            // add the components to the waiting page
            waitingRoot.getChildren().addAll(id, waiting, playerListView);
            // add the waiting page to the selection pane which contains the background image
            selectionPane.getChildren().add(waitingRoot);
        });
    }

    /**
     * Once the player receives the LobbyPlayerList message from the server, the method is called by
     * processMessage, to update the player's list in the Lobby phase and print the player's list updated.
     * @param players the list updated of players in the game.
     */
    @Override
    public void updatePlayerList(ArrayList<String> players) {
        this.players = players;
        // if the LobbyPlayerList message is received after the NewGameConfirmationMessage or the AccessGameConfirmationMessage.
        if (playerListView == null) {
            playerListView = createTextField(null, 135, 360, 0, 0);
            playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                    "-fx-font-size: 25px;-fx-font-family: 'JejuHallasan';");
            playerListView.setTranslateX(0);
            playerListView.setTranslateY(0);
            playerListView.setEditable(false);
        }
        // update the list of players in the waiting page with the new list of players.
        playerListView.setText("Players: \n" + String.join(",\n", players));
    }
    /**
     * After receiving the GameStarted message from the server, the method is called to set up the view of the player
     * and initialize the data and the boards of the players.
     */
    @Override
    public void setUpPlayersData() {
        // load the images which will be used in the master pane
        imagesMap.put("BLUE", new Image("/CODEX_pion_bleu.png", 15, 15, true, false));
        imagesMap.put("YELLOW", new Image("/CODEX_pion_jaune.png", 15, 15, true, false));
        imagesMap.put("BLACK", new Image("/CODEX_pion_noir.png", 15, 15, true, false));
        imagesMap.put("RED", new Image("/CODEX_pion_rouge.png", 15, 15, true, false));
        imagesMap.put("GREEN", new Image("CODEX_pion_vert.png", 15, 15, true, false));
        imagesMap.put("GREY", new Image("CODEX_pion_grey.png", 15, 15, true, false));
        imagesMap.put("PLANT", new Image("kingdom_plant.png", 15, 15, true, false));
        imagesMap.put("FUNGI", new Image("kingdom_fungi.png", 15, 15, true, false));
        imagesMap.put("ANIMAL", new Image("kingdom_animal.png", 15, 15, true, false));
        imagesMap.put("INSECT", new Image("kingdom_insect.png", 15, 15, true, false));
        imagesMap.put("QUILL", new Image("kingdom_quill.png", 15, 15, true, false));
        imagesMap.put("INKWELL", new Image("kingdom_inkwell.png", 15, 15, true, false));
        imagesMap.put("MANUSCRIPT", new Image("kingdom_manuscript.png", 15, 15, true, false));
        imagesMap.put("AVAILABLESPACE", new Image("availableSpace.png", 120, 80, true, true));
        imagesMap.put("PLACEHOLDER", new Image("placeholder.png", 120, 80, true, true));
        imagesMap.put("0", new Image("cards_back_011.png", 120, 80, true, false));
        imagesMap.put("1", new Image("cards_back_001.png", 120, 80, true, false));
        imagesMap.put("2", new Image("cards_back_021.png", 120, 80, true, false));
        imagesMap.put("3", new Image("cards_back_031.png", 120, 80, true, false));
        imagesMap.put("4", new Image("cards_back_051.png", 120, 80, true, false));
        imagesMap.put("5", new Image("cards_back_041.png", 120, 80, true, false));
        imagesMap.put("6", new Image("cards_back_061.png", 120, 80, true, false));
        imagesMap.put("7", new Image("cards_back_071.png", 120, 80, true, false));

        if (this.matchStatus == null) {
            this.matchStatus = createLabel(String.valueOf(Status), 15);
        }
        Platform.runLater(()->{
            for (String player : players) {
                        // set up the data of the players and initialize the board of the players
                        publicInfo.put(player, new PlayerPub(null, 0, new ArrayList<>(), new int[]{0, 0, 0, 0, 0, 0, 0},
                                true));
                        // create the view of the players
                        playerViews.put(player, new PlayerPubView(
                                createLabel(player,18),
                                new ImageView(imagesMap.get("BLACK")),
                                createLabel("0", 15),
                                new Label[]{
                                        createLabel("0", 15),
                                        createLabel("0", 15),
                                        createLabel("0", 15),
                                        createLabel("0", 15),
                                        createLabel("0", 15),
                                        createLabel("0", 15),
                                        createLabel("0", 15)
                                }));
                        playerField.put(player, new StackPane());
                    }
        setGameView();
    });// set the view of the game in the playing phase
    }
    private void setGameView() {
        // set the master pane
        Platform.runLater(()->{
            masterPane = new Pane();
        masterPane.setBackground(new Background(new BackgroundFill(Color.rgb(246, 243, 228), new CornerRadii(0), new Insets(0))));

        // set the players info panel
        VBox playerInfoPanel = new VBox();
        playerOrder = new HBox();
        Label OrderTitle = createLabel("Order:", 15);
        playerOrder.getChildren().add(OrderTitle);

        for (String player : players) {
            // set up the player view
            PlayerPubView playerPubView = playerViews.get(player);
            // set the player info line
            HBox playerInfo = new HBox();
            playerInfo.setSpacing(10);
            playerInfo.setMaxWidth(300);
            playerInfo.setMinWidth(300);
            Label scoreLabel = createLabel("Score: ", 15);
            HBox scoreBox = new HBox();
            scoreBox.getChildren().addAll(scoreLabel, playerPubView.getPoints());
            playerInfo.getChildren().addAll(playerPubView.getColour(), playerPubView.getNickname());
            HBox playerInfoBox = new HBox();
            playerInfoBox.setSpacing(10);
            playerInfoBox.getChildren().addAll(playerInfo, scoreBox);

            // set the player resource line
            HBox playerResource = new HBox();
            playerResource.setSpacing(10);
            playerResource.getChildren().addAll(
                    new Label("     "),
                    new ImageView(imagesMap.get("PLANT")), playerPubView.getResourceLabels()[0],
                    new ImageView(imagesMap.get("FUNGI")), playerPubView.getResourceLabels()[1],
                    new ImageView(imagesMap.get("ANIMAL")), playerPubView.getResourceLabels()[2],
                    new ImageView(imagesMap.get("INSECT")), playerPubView.getResourceLabels()[3],
                    new ImageView(imagesMap.get("QUILL")), playerPubView.getResourceLabels()[4],
                    new ImageView(imagesMap.get("INKWELL")), playerPubView.getResourceLabels()[5],
                    new ImageView(imagesMap.get("MANUSCRIPT")), playerPubView.getResourceLabels()[6]);

            // set the player info and resource area
            VBox playerInfoAndResource = new VBox();
            playerInfoAndResource.getChildren().addAll(playerInfoBox, playerResource);
            playerInfoPanel.getChildren().add(playerInfoAndResource);


            // set field view of the player
            StackPane boardReal = playerField.get(player);

            handleBoardAction(boardReal);
            handlePlayerNicknameClick(player);
        }

        // set the position of the player info panel in the master pane
        playerInfoPanel.translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(20)));
        playerInfoPanel.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(60)));

        // set the top line panel of the master pane which contains the game ID, the status and the player order
        Label labelPlayer= createLabel(thisPlayerNickname+"'s View", 20);
        Label labelID = createLabel("ID: " + gameID, 15);
        HBox StatusBox = new HBox();
        Label statusTitle = createLabel("Status: ", 15);
        StatusBox.getChildren().addAll(statusTitle, matchStatus);
        Button rulesButton = createTransButton("[Rules]", 15, "#3A2111", 0, 0);
        rulesButton.setOnAction(e -> showRuleBook());
        Button helpInfoButton = createTransButton("[Help]", 15, "#3A2111", 0, 0);
        helpInfoButton.setOnAction(e -> showHelpInfo());

        HBox topLine = new HBox();
        topLine.setSpacing(100);
        topLine.getChildren().addAll(labelPlayer,labelID, StatusBox, playerOrder, rulesButton, helpInfoButton);
        topLine.translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(20)));
        topLine.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(20)));

        masterPane.getChildren().addAll(topLine, playerInfoPanel);

        // create the board of this player
        StackPane boardMove = playerField.get(thisPlayerNickname);
        board = new ScrollPane(); // Fixed board where cards are displayed
        board.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179, 0.35), new CornerRadii(0), new Insets(0))));
        board.setPrefSize(770, 525);
        board.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(50))); // set position X of the board in the masterPane.
        board.translateXProperty().bind(masterPane.widthProperty().subtract(board.widthProperty().add(20))); // set position Y of the board in the masterPane.
        board.setContent(boardMove);

        // create the notification area
            notice = new VBox();
            notice.setStyle("-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';" + "-fx-text-fill: #3A2111;");
            ScrollPane noticeArea= new ScrollPane();
            noticeArea.setContent(notice);
            noticeArea.setBorder(new Border(new BorderStroke(Color.rgb(58,33,17), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            noticeArea.setPrefSize(380,115);
            noticeArea.translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(40)));
            noticeArea.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(notice.getHeight()+280)));

        // Initialize the chat area
        chatArea = new ChatArea(0, 0, 305, 75, players, this);
        chatArea.getChatArea().translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(40)));
        chatArea.getChatArea().translateYProperty().bind(masterPane.heightProperty().subtract(chatArea.getChatArea().heightProperty().add(20)));
        chatArea.setActive(false); // At the start, disable the chat area

        //Images to hold the spaces for the cards in hand, common objective cards and secret objective cards
        handView = new ImageView[]{
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false))
        };
        commonObjCardView = new ImageView[]{
                new ImageView(new Image("/cards_back_089.png", 120, 80, true, false)),
                new ImageView(new Image("/cards_back_089.png", 120, 80, true, false))
        };
        secretObjCardView = new ImageView(new Image("/cards_back_089.png", 120, 80, true, false));

        resourceDeckView = new ImageView[]{
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false))
        };
        goldDeckView = new ImageView[]{
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false)),
                new ImageView(new Image("/placeholder.png", 120, 80, true, false))
        };

        // Set up the click action of the cards in the hand
        handleHandClicks();

        deckArea = new VBox();
        deckArea.setSpacing(10);
        HBox resourceDeck = new HBox();
        resourceDeck.setSpacing(10);
        resourceDeck.getChildren().addAll(resourceDeckView[0], resourceDeckView[1], resourceDeckView[2]);
        HBox goldDeck = new HBox();
        goldDeck.setSpacing(10);
        goldDeck.getChildren().addAll(goldDeckView[0], goldDeckView[1], goldDeckView[2]);
        deckArea.getChildren().addAll(resourceDeck,goldDeck);


        deckArea.translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(40)));
        deckArea.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(deckArea.getHeight() + 400)));
        // set Deck size label
            HBox deckSize = new HBox();
            deckSize.setStyle("-fx-text-fill: #3A2111 ;-fx-alignment: center; -fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
            deckSize.setSpacing(10);
            Label goldDeckLabel = new Label("Gold Deck:");
            goldSize = new Label("--");
            Label resourceDeckLabel = new Label("Resource Deck:");
            resourceSize = new Label("--");
            deckSize.getChildren().addAll(resourceDeckLabel, resourceSize,goldDeckLabel, goldSize);
            deckSize.translateXProperty().bind(masterPane.widthProperty().subtract(masterPane.widthProperty().subtract(40)));
            deckSize.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(deckArea.getHeight()+570)));
            deckSize.setOnMouseClicked(e -> showDeck());
        HBox bottomLine = new HBox();
        bottomLine.setSpacing(10);
        bottomLine.getChildren().addAll(commonObjCardView[0], commonObjCardView[1], secretObjCardView, handView[0], handView[1], handView[2]);
        bottomLine.translateXProperty().bind(masterPane.widthProperty().subtract(bottomLine.widthProperty().add(20)));
        bottomLine.translateYProperty().bind(masterPane.heightProperty().subtract(bottomLine.heightProperty().add(20)));

        // create the label of the card area
        Label commonObjLabel = createLabel("Common Objective Cards", 15);
        Label secretObjLabel = createLabel("Secret",15);
        Label handLabel = createLabel("Hand Cards",15);
        handLabel.setOnMouseClicked(e -> showHand(hand));

        HBox cardLabels = new HBox();
        cardLabels.setSpacing(80);
        cardLabels.getChildren().addAll(commonObjLabel,secretObjLabel,handLabel);
        cardLabels.translateXProperty().bind(masterPane.widthProperty().subtract(bottomLine.widthProperty().add(10)));
        cardLabels.translateYProperty().bind(masterPane.heightProperty().subtract(bottomLine.heightProperty().add(50)));

        // set the important event notify panel
        noticeEventPanel = createNoticeEventPanel();
        noticeEventPanel.setVisible(false);

        // add the buttons to the return to the thisPlayer's field
        returnToMyField = createTransButton("[x] Return to my field", 15, "#3A2111", 0, 0);
        returnToMyField.setOnAction(e -> {
            board.setContent(this.playerField.get(thisPlayerNickname));
            returnToMyField.setVisible(false);
        });
        returnToMyField.translateXProperty().bind(masterPane.widthProperty().subtract(200));
        returnToMyField.translateYProperty().bind(masterPane.heightProperty().subtract(masterPane.heightProperty().subtract(80)));
        returnToMyField.setVisible(false);


        masterPane.getChildren().addAll(board, deckArea,deckSize, bottomLine, noticeArea,chatArea.getChatArea(),cardLabels,noticeEventPanel,returnToMyField);

            app.updateScene(masterPane);
            app.getPrimaryStage().setMinWidth(1250);
           // app.getPrimaryStage().setFullScreen(true);
        });
    }
    private Group createNoticeEventPanel() {
        Group root = new Group();
        root.setAutoSizeChildren(true);
        ImageView background = new ImageView(new Image("/popNotice.png"));
        background.setFitHeight(150);
        background.setFitWidth(150);
        eventLabel = createLabel ("It's your turn!",15);
        eventLabel.setTranslateX(25);
        eventLabel.setTranslateY(70);
        root.getChildren().addAll(background,eventLabel);
        root.setTranslateX(375);
        root.setTranslateY(265);
        root.setEffect(new Glow(0.3));
        root.setEffect(new DropShadow(15, Color.rgb(236, 197, 123)));
        root.setOnMouseClicked(e -> {
            handleNoticeClicks(root);
        });
        return root;
    }
    private void handleNoticeClicks(Group root) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), root);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAxis(Rotate.Y_AXIS);

        rotateTransition.play();

    }
    /**
     * Method set the click action of the player's field. The player can zoom in/out the field using the mouse wheel
     * and move the field using the drag action.
     * @param boardReal the board of the player
     */
    private void handleBoardAction(StackPane boardReal) {
        boardReal.setOnScroll(e -> {
            e.consume();
            if (e.getDeltaY() == 0) {
                return;
            }
            double scaleFactor = (e.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
            /*if (boardReal.getScaleX() * scaleFactor > 1 || boardReal.getScaleY() * scaleFactor > 1)
                return;*/
            boardReal.setScaleX(boardReal.getScaleX() * scaleFactor);
            boardReal.setScaleY(boardReal.getScaleY() * scaleFactor);
        }); // Enable zooming in/out of player field with mouse wheel

        double[] dragPos = new double[2];
        boardReal.setOnMousePressed(e -> {
            dragPos[0] = e.getSceneX();
            dragPos[1] = e.getSceneY();
        });
        boardReal.setOnMouseDragged(e -> {
            boardReal.setTranslateX(e.getSceneX() - dragPos[0]);
            boardReal.setTranslateY(e.getSceneY() - dragPos[1]);
        }); // Enable translating of player field when mouse button is held down
    }
    /**
     * Method set the click action of the player's nickname. The player can see the field of the player that he wants
     * to see by clicking on the nickname of the player.
     * @param player the nickname of the player that thisPlayer wants to see the field.
     */
    private void handlePlayerNicknameClick(String player) {
        playerViews.get(player).getNickname().setOnMouseClicked(e -> {
            if (player.equals(thisPlayerNickname)) {
                return;
            }
            showPlayersField(player);
        });
    }

    /**
     * Method set the click action of the cards in the deck. The player can draw a card from the deck by clicking on the
     * card in the deck.
     *
     * @param cardView the view of the card in the deck.
     * @param cardID the ID of the card.
     * @param cardType the type of the deck where the card is drawn.
     */
    private void handleDeckCardsClicks(ImageView cardView, int cardID, int cardType) {
        cardView.setOnMouseClicked(e -> {
            if (!currentEvent.equals(Event.DRAW_CARD)) { // The player doesn't have the rights to draw a card
                createAlert("In this phase you can't draw a card");
            } else { // The player has the rights to draw a card
                notifyAskListener(new DrawCardMessage(thisPlayerNickname, cardType, cardID)); // Notify the server of the selected card to draw

                // Disable the click action of all cards in the deck area
                for (int i = 0; i < 3; i++) {
                    resourceDeckView[i].setOnMouseClicked(null);
                    goldDeckView[i].setOnMouseClicked(null);
                }
            }
        });
    }

    /**
     * Method to set the click action of the cards in the drawing area.
     */
    private void handleDeckClicks() {
        handleDeckCardsClicks(resourceDeckView[0],-1, 0);
        handleDeckCardsClicks(goldDeckView[0],-1, 1);
        for (int i = 1; i < 3; i++) {
            handleDeckCardsClicks(resourceDeckView[i], currentResourceCards.get(i-1), 2);
            handleDeckCardsClicks(goldDeckView[i], currentGoldCards.get(i-1), 3);
        }
    }

    /**
     * Method to set the click action of the cards in the hand.
     */
    private void handleHandClicks() {
        for (int i = 0; i < 3; i++) { // For all cards in the player's hand
            int finalI = i;
            handView[finalI].setOnMouseClicked(e -> { // Assign all cards in hand a click action
                        if (e.getButton() == MouseButton.PRIMARY) { // User left clicks card; highlights the card and prepare it to be placed
                            if (!currentEvent.equals(Event.PLACE_CARD)) { // The player doesn't have the rights to place a card
                                createAlert("Cannot place card now");
                            } else { // The player has the rights to place a card
                                selectedCardId = hand.get(finalI); // Set selected card
                                indexCardPlaced = finalI; // Set index of selected card

                                // Clear highlight effect on all cards
                                for (int j=0; j<3; j++) {
                                    handView[j].setEffect(null);
                                }

                                // Set highlight effect on newly selected card
                                DropShadow dropShadow = new DropShadow();
                                dropShadow.setRadius(50.0);
                                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                                handView[finalI].setEffect(dropShadow);
                            }
                        }
                        else if (e.getButton() == MouseButton.SECONDARY) { // User right clicks card; flip the card
                            handViewCardSide[finalI] = !handViewCardSide[finalI]; // Toggle card side
                            String cardImagePath = convertToImagePath(hand.get(finalI), handViewCardSide[finalI]); // Load card image
                            handView[finalI].setImage(new Image(cardImagePath, 120, 80, true, false)); // Update card image
                        }
            });
        }
    }

    /**
     * Once the player receives the MatchStatus message from the server, the method is called by processMessage to
     * update the match status of the player, and update the player of the current match status.
     * If the match status is TERMINATING, the method is called to show the points of all players in the game with
     * a pop-up window.
     * @param matchStatus the current match status received from the server
     */
    @Override
    public void updateMatchStatus(int matchStatus) {
        this.Status = Event.getEvent(matchStatus); // update the match status of the player.
        Platform.runLater(() -> {
            this.matchStatus.setText(String.valueOf(Status));
        if (Status.equals(Event.TERMINATING)) { // if the match status is TERMINATING show the points of all players.
            for (String player : players) {
            showPointsAndResource(player);
            }
        }
        });
    }


    // Methods to create the components of the GUI

    /**
     * Create a stylized label with the specified text and size.
     * Used anywhere a stylized label is needed.
     *
     * @param text The text contained inside the label
     * @param size The size of the font
     * @return The label created
     */
    private Label createLabel(String text, int size) {
        Label label = new Label(text);

        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: " + size + "px;-fx-font-family: 'JejuHallasan';");

        return label;
    }

    /**
     * Create a stylized label with the specified text, X and Y coordinates.
     * This version of the method is used in starting page.
     *
     * @param text The text contained inside the label
     * @param X    The X coordinate of the label
     * @param Y    The Y coordinate of the label
     * @return The label created
     */
    private Label createLabel(String text, int X, int Y) {
        Label label = new Label(text);

        label.setStyle("-fx-text-fill: #3A2111; -fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
        label.setTranslateX(X);
        label.setTranslateY(Y);

        return label;
    }

    private Button createTransButton(String buttonName, int size, String color, int X, int Y) {
        Button button = new Button(buttonName);
        button.setStyle("-fx-background-color: transparent;-fx-text-fill:" + color + ";-fx-alignment: center;" +
                "-fx-font-size: " + size + "px;-fx-font-family: 'JejuHallasan';");
        button.setTranslateX(X);
        button.setTranslateY(Y);
        return button;
    }

    private Button createButton(String buttonName) {
        Button button = new Button(buttonName);
        button.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , " +
                "rgba(58,33,17,100,0.2),10,0,0,10);");
        return button;
    }

    private Button createButton(String buttonName, int X, int Y) {
        Button button = new Button(buttonName);
        button.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , " +
                "rgba(58,33,17,100,0.2),10,0,0,10);");
        button.setTranslateX(X);
        button.setTranslateY(Y);
        return button;
    }

    private TextField createTextField(String promptText, int MaxHeight, int MaxWidth, int X, int Y) {
        TextField textField = new TextField();
        textField.setMaxHeight(MaxHeight);
        textField.setMaxWidth(MaxWidth);
        textField.setPromptText(promptText);
        textField.setStyle("-fx-background-color: #E6DEB3;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , " +
                "rgba(58,33,17,100,0.2) , 10,0,0,10 );-fx-border-color: #3A2111; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        textField.setTranslateX(X);
        textField.setTranslateY(Y);
        return textField;
    }

    private void createAlert(String reason) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = new DialogPane();
            Label label1 = new Label(reason);
            label1.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
            dialogPane.setContent(label1);
            dialogPane.setStyle("-fx-pref-height: 180px;-fx-pref-width: 600px;-fx-background-image: " +
                    "url('/NoticeDisplay.png');-fx-background-position: center;-fx-background-size: 600px 180px;");
            dialogPane.setMinHeight(180);
            dialogPane.setMinWidth(600);
            dialogPane.setMaxHeight(600);
            dialogPane.setMaxWidth(180);
            alert.setDialogPane(dialogPane);
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        });
    }


    @Override
    public void showPlayersField(String playerNickname) {
        Platform.runLater(()-> {
            StackPane playerField = this.playerField.get(playerNickname);
            /*Label playerLabel = playerViews.get(playerNickname).getNickname();
            playerLabel.translateXProperty().bind(board.widthProperty().subtract(board.widthProperty()));
            playerLabel.translateYProperty().bind(board.heightProperty().subtract(board.heightProperty()));
            playerLabel.setTranslateY(20);*/
            board.setContent(playerField);
            returnToMyField.setVisible(true);
        });
    }

    @Override
    public void showPointsAndResource(String playerNickname) {
        playerViews.get(playerNickname).getPoints().setEffect(dropShadow);
        playerViews.get(playerNickname).getNickname().setEffect(dropShadow);
        for (Label label: playerViews.get(playerNickname).getResourceLabels()) {
            label.setEffect(dropShadow);
        }
    }

    /**
     * Once the player receives the PlayerTurnMessage from the server, the method is called by processMessage to update
     * the currentPlayer in the game and print the message to notify the player whose turn is now, also, the method
     * notifies players the order of the turn in the game.
     * @param playerNickname the nickname of the player who should be able to place the card and draw card in the field.
     */
    @Override
    public void updatePlayerTurn(String playerNickname) {
        playerViews.get(this.currentPlayer).getPoints().setEffect(null);
        playerViews.get(this.currentPlayer).getNickname().setEffect(null);
        for(Label label: playerViews.get(this.currentPlayer).getResourceLabels()) {
            label.setEffect(null);
        }
        this.currentPlayer = playerNickname;
        showPointsAndResource(playerNickname);
        if (this.currentPlayer.equals(thisPlayerNickname)) {
            // if the player's turn is now, request the player to place the card in the field.
            currentEvent = Event.PLACE_CARD;
            requestPlaceCard();
            noticeEventPanel.setVisible(true);
            eventLabel.setText("It's your turn!");
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), noticeEventPanel);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAxis(Rotate.Y_AXIS);
            rotateTransition.play();

        } else {
            noticeEventPanel.setVisible(false);
            currentEvent = Event.WAITING_FOR_TURN;
            Platform.runLater(()->notice.getChildren().add(new Label("> It is " + currentPlayer + "'s turn.")));
        }
    }

    @Override
    public void updatePlayerData(ArrayList<String> playerNicknames, ArrayList<Boolean> playerConnected,
                                 ArrayList<Integer> playerColours, ArrayList<Integer> playerHand,
                                 int playerSecretObjective, int[] playerPoints,
                                 ArrayList<ArrayList<int[]>> playerFields, int[] playerResources,
                                 ArrayList<Integer> gameCommonObjectives, ArrayList<Integer> gameCurrentResourceCards,
                                 ArrayList<Integer> gameCurrentGoldCards, int gameResourcesDeckSize,
                                 int gameGoldDeckSize, int matchStatus, ArrayList<ChatMessage> chatHistory,
                                 String currentPlayer, ArrayList<int[]> newAvailableFieldSpaces, int resourceCardDeckFacingKingdom, int goldCardDeckFacingKingdom, ArrayList<int[]> playersResourcesSummary) { // once the player reconnects to the game

        // Since this method is called both when the game enters the playing phase, and when the player reconnects to the game, we enable the chat here
        startChatting(); // Enable chat area

        // store all the data of the game received from the server
        if (currentEvent.equals(Event.RECONNECT_GAME)) {
            showChatHistory(chatHistory);
            //TODO
        } else {
            // once the game enters the playing phase, the method is called to update a part of the data of the player
            // that not yet updated in the previous phases.
            this.currentResourceCards = gameCurrentResourceCards;
            this.currentGoldCards = gameCurrentGoldCards;
            this.resourceDeckSize = gameResourcesDeckSize;
            this.goldDeckSize = gameGoldDeckSize;
            this.resourceCardDeckFacingKingdom = resourceCardDeckFacingKingdom;
            this.goldCardDeckFacingKingdom = goldCardDeckFacingKingdom;
            this.chatHistory = chatHistory;
            this.currentPlayer = currentPlayer;
            this.players = playerNicknames;
            int[] card;
            this.resourceDeckView[0].setImage(imagesMap.get(String.valueOf(resourceCardDeckFacingKingdom)));
            this.resourceDeckView[1].setImage(new Image(convertToImagePath(currentResourceCards.get(0),true), 120, 80, true, false));
            this.resourceDeckView[2].setImage(new Image(convertToImagePath(currentResourceCards.get(1),true), 120, 80, true, false));
            this.goldDeckView[0].setImage(imagesMap.get(String.valueOf(goldCardDeckFacingKingdom+4)));
            this.goldDeckView[1].setImage(new Image(convertToImagePath(currentGoldCards.get(0),true), 120, 80, true, false));
            this.goldDeckView[2].setImage(new Image(convertToImagePath(currentGoldCards.get(1),true), 120, 80, true, false));
            Platform.runLater(()->{
                 ImageView firstPlayer = new ImageView(imagesMap.get("BLACK"));
                 playerOrder.getChildren().addAll(firstPlayer,createLabel(String.valueOf(playerNicknames), 15));
                 goldSize.setText(String.valueOf(goldDeckSize));
                 resourceSize.setText(String.valueOf(resourceDeckSize));
            });
            PlayerPub playerSpecific;
            // update the data of the players except this player: set the colour, resources, points, online status,
            // and the field of the players after the placement of the starter card.
            for (int i = 0; i < players.size(); i++) {
                if(!playerNicknames.get(i).equals(thisPlayerNickname)) {
                    playerSpecific = publicInfo.get(players.get(i));
                    playerSpecific.updateColour(convertToColour(playerColours.get(i)));
                    playerSpecific.updateResources(playersResourcesSummary.get(i));
                    card = playerFields.get(i).get(0);
                    playerSpecific.addToField(new CardPlacedView(card[2], null,
                            card[0], card[1], card[3] == 1));
                    updateAfterPlacedCard(players.get(i), card[2], card[0], card[1],
                            card[3] == 1, newAvailableFieldSpaces, playersResourcesSummary.get(i), playerPoints[i]);
                    int finalI = i;
                    Platform.runLater(() -> playerViews.get(players.get(finalI)).setColour(imagesMap.get(convertToColour(playerColours.get(finalI)))));
                }
                }
        }
    }
    private String convertToImagePath(int cardID, boolean isUp) {
        String cardIdStr= String.format("%03d", cardID);
        return isUp ? "/cards_front_" + cardIdStr + ".png" : "/cards_back_" + cardIdStr + ".png";
    }

    @Override
    public void setCardsReceived(ArrayList<Integer> secrets, ArrayList<Integer> common, ArrayList<Integer> hand) {
        this.hand = hand;
        this.commonObjCards = common;
        this.secretObjCards = secrets;
        String cardIdStr1 = convertToImagePath(hand.get(0), true);
        String cardIdStr2 = convertToImagePath(hand.get(1), true);
        String cardIdStr3 = convertToImagePath(hand.get(2), true);
        handView[0].setImage(new Image(cardIdStr1, 120, 80, true, true));
        handView[1].setImage(new Image(cardIdStr2, 120, 80, true, true));
        handView[2].setImage(new Image(cardIdStr3, 120, 80, true, true));
        handViewCardSide = new boolean[]{true, true, true}; // All cards displayed side up in the beginning
        String commonCardIdStr1 = convertToImagePath(common.get(0), true);
        String commonCardIdStr2 = convertToImagePath(common.get(1), true);
        commonObjCardView[0].setImage(new Image(commonCardIdStr1,120, 80, true, true));
        commonObjCardView[1].setImage(new Image(commonCardIdStr2, 120, 80, true, true));
        Platform.runLater(()->{
        notice.getChildren().add(new Label("> Your hand cards and common objective cards are ready in your card area:)\n"));
        notice.getChildren().add(new Label("> Please select your secret objective cards\n"));
        });
        requestSelectSecretObjectiveCard();
    }

    @Override
    public void showHand(ArrayList<Integer> hand) {
        handView[0].setEffect(glow);
        handView[1].setEffect(glow);
        handView[2].setEffect(glow);
        handView[0].setEffect(dropShadow);
        handView[1].setEffect(dropShadow);
        handView[2].setEffect(dropShadow);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            handView[0].setEffect(null);
            handView[1].setEffect(null);
            handView[2].setEffect(null);
        });
        pause.play();
    }

    @Override
    public void showCard(int ID, boolean isUp) {
            Platform.runLater(() -> {
            ImageView cardView = new ImageView(new Image(convertToImagePath(ID, isUp), 120, 80, true, false));
            VBox cardArea = new VBox(); // Entire selection area
            Label promptLabel = createLabel("Card "+ID+": ", 15);
            cardArea.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179, 0.35), new CornerRadii(0), new Insets(0))));
            cardArea.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179, 0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(30))));
            cardArea.getChildren().addAll(promptLabel, cardView);
            Pane pane;
            pane = Status.equals(Event.TERMINATED) ? endGamePane : masterPane;
            cardArea.translateXProperty().bind(pane.widthProperty().subtract(cardArea.widthProperty()).divide(2)); // Set position of selectionArea
            cardArea.translateYProperty().bind(pane.heightProperty().subtract(cardArea.heightProperty()).divide(2));
            pane.getChildren().add(cardArea);
            PauseTransition pause = new PauseTransition(Duration.seconds(10)); // Set a timer for the visibility of the card
            pause.setOnFinished(e -> {
                pane.getChildren().remove(cardArea);
                cardArea.getChildren().removeAll(promptLabel, cardView);
            });
            pause.play();
        });

    }

    /**
     * Method called by the processMessage to update the player's field after receiving confirmation from the server that the card placement was successful.
     *
     * @param playerNickname the nickname of the player
     * @param placedCard the ID of the card placed in the field
     * @param placedCardCoordinates the coordinates of the card placed in the field
     * @param placedSide the side of the card placed in the field
     * @param playerPoints the updated points of the player
     * @param playerResources the updated resources count of the player
     * @param newAvailableFieldSpaces the updated available spaces in the field after the placement of the card
     */
    @Override
    public void updatePlacedCardConfirm(String playerNickname, int placedCard, int[] placedCardCoordinates, boolean placedSide, int playerPoints, int[] playerResources, ArrayList<int[]> newAvailableFieldSpaces) {
            if (playerNickname.equals(thisPlayerNickname)) { // This player has just placed a card
                Platform.runLater(()->notice.getChildren().add(new Label("> Your card has been placed successfully in the field.\n")));

                selectedCardId = 0; // Reset selected card
                for (int i=0; i<3; i++) {
                    handView[i].setEffect(null); // Clear highlight effect on all cards
                    handView[i].setOnMouseClicked(null); // Remove click action from all cards
                }

                int cardIdx = hand.indexOf(placedCard); // Get index of card in the player's hand
                hand.remove(cardIdx); // Remove placed card from hand
                handView[cardIdx].setImage(new Image("/placeholder.png", 120, 80, true, false)); // Replace card with placeholder


                StackPane playerBoard = playerField.get(thisPlayerNickname);
                // Remove all the available spaces in the field
                if(playerBoard.getChildren().size() > 1) {
                    int sizeAvailable = availableSpaces.size();
                    for (int i = 0; i < sizeAvailable; i++) {
                        Platform.runLater(() -> {
                            playerBoard.getChildren().removeLast();
                        });
                    }
                }
                updateAfterPlacedCard(playerNickname, placedCard, placedCardCoordinates[0], placedCardCoordinates[1], placedSide, newAvailableFieldSpaces, playerResources, playerPoints); // Update player's info
                currentEvent = Event.DRAW_CARD;

                requestDrawCard(); // Request the player to draw a card
            } else { // Another player has placed a card
                Platform.runLater(()->notice.getChildren().add(new Label("> " + playerNickname + "'s card has been placed successfully in the field.\n")));
                updateAfterPlacedCard(playerNickname, placedCard, placedCardCoordinates[0], placedCardCoordinates[1], placedSide, newAvailableFieldSpaces, playerResources, playerPoints); // Update player's info
            }
    }

    @Override
    public void showMatchWinners(ArrayList<String> players, ArrayList<Integer> points, ArrayList<Integer> secrets,
                                 ArrayList<Integer> pointsGainedFromObj, ArrayList<String> winners) {
        Platform.runLater(()-> {
            endGamePane = new Pane();
            endGamePane.setBackground(new Background(new BackgroundImage(new Image("/endGameDisplay.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(975, 925, false, false, false, false))));
            Label winnersLabel = createLabel("Winners: " + winners + "\n", 25);
            // print the final points of the players, the secret objective card of the players, and the points gained from the
            // objective card of the players.
            VBox PointsArea = new VBox();
            PointsArea.getChildren().add(winnersLabel);
            PointsArea.setSpacing(10);
            for (int i = 0; i < players.size(); i++) {
                Label pointsLabel= createLabel(players.get(i) + ": " + points.get(i) + " points with " + pointsGainedFromObj.get(i) + " points from the objective card.",15);
                Label cardLabel = createLabel("Secret Objective Card: " + secrets.get(i)+ "[CLICK ON ID to see the card].", 15);
                int finalI = i;
                cardLabel.setOnMouseClicked(e -> {
                    showCard(secrets.get(finalI), true);
                });
                PointsArea.getChildren().addAll(pointsLabel, cardLabel);
            }
            PointsArea.translateXProperty().bind(endGamePane.widthProperty().subtract(PointsArea.widthProperty()).divide(2));
            PointsArea.translateYProperty().bind(endGamePane.heightProperty().subtract(PointsArea.heightProperty()).divide(2));
            PointsArea.setStyle("-fx-background-color:transparent;");
            endGamePane.getChildren().addAll(PointsArea);
            app.updateScene(endGamePane);
            app.getPrimaryStage().setMinWidth(975);
            app.getPrimaryStage().setMaxWidth(975);
            app.getPrimaryStage().show();
        });
    }

    @Override
    public void updateRollback(String playerNickname, int removedCard, int playerPoints, int[] playerResources) {
        //TODO
    }

    @Override
    public void showChatHistory(List<ChatMessage> chatHistory) {
        for (ChatMessage message : chatHistory) {
            chatArea.addIncomingMessageToChat(message.getMessageContent(), message.getSenderNickname());
        }
    }

    /**
     * Called when a chat message is received from the server.
     * @param recipientString the nickname of the recipient of the message
     * @param senderNickname the nickname of the sender of the message
     * @param content the content of the message
     */
    @Override
    public void updateChat(String recipientString, String senderNickname, String content) {
        Platform.runLater(() -> {
            if (recipientString.equals(thisPlayerNickname)) { // We have received a message we shouldn't have received
                // TODO Throw exception
            }
            chatArea.addIncomingMessageToChat(content, senderNickname); // Add message to chat area
        });
    }

    @Override
    public void setStarterCard(int cardId) {
        startCard = cardId;
        requestSelectStarterCardSide(cardId);
    }



    @Override
    public void requestSelectStarterCardSide(int ID) {
        VBox starterCardSideSelection = setupInitialCardSideSelectionArea(ID);
        Platform.runLater(() -> masterPane.getChildren().add(starterCardSideSelection));
    }

    /**
     * Method called by the processMessage after receiving confirmation from the server that the starter card side was selected.
     *
     * @param colour the colour assigned to the player
     * @param cardID the ID of the starter card
     * @param isUp the side of the starter card
     * @param availablePos the available spaces in the field after the placement of the starter card
     * @param resources the initial resources count of the player
     */
    @Override
    public void updateConfirmStarterCard(int colour, int cardID, boolean isUp, ArrayList<int[]> availablePos, int[] resources) {
        Platform.runLater(() -> {
            masterPane.getChildren().removeLast();
            String colourReceived = convertToColour(colour);
            publicInfo.get(thisPlayerNickname).updateColour(colourReceived);
            playerViews.get(thisPlayerNickname).setColour(imagesMap.get(colourReceived));
            // notify the player the colour received from the server
            notice.getChildren().add(new Label("> Your colour of this game is " + colourReceived+".\n"));
            // update the available spaces in the field after the placement of the starter card
            availableSpaces = availablePos;
            // use the updateAfterPlacedCard method to add the starter card in the field of the player, update the resources count
            updateAfterPlacedCard(thisPlayerNickname, cardID, 0, 0, isUp, availablePos,
                    resources, 0);
        });
        // print the board of the player after the placement of the starter card with the current resources count
    }

    /**
     * Method called when place card confirmation is received from server.
     * Enable drawing from deck
     */
    @Override
    public void requestDrawCard() {
        Platform.runLater(()->notice.getChildren().add(new Label("> You can draw a card from the deck now.\n")));
        handleDeckClicks();
    }

    /**
     * Method called by the processMessage after receiving confirmation from the server that the card was drawn successfully.
     *
     * @param hand the updated hand of the player after the draw
     */
    @Override
    public void updateAfterDrawCard(ArrayList<Integer> hand) {
        Platform.runLater(() -> {
            notice.getChildren().add(new Label("> You have drawn a card from the deck.\n"));

            this.hand.add(indexCardPlaced, hand.getLast()); // Add drawn card to my hand
            handView[indexCardPlaced].setImage(new Image("/cards_front_" + String.format("%03d", hand.getLast()) + ".png", 120, 80, true, true)); // Update hand view
            handViewCardSide[indexCardPlaced] = true; // Set card side to face up

            // Enable click action on all cards in hand
            handleHandClicks();
        });
    }

    @Override
    public void updateDeck(int resourceDeckSize, int goldDeckSize, int[] currentResourceCards, int[] currentGoldCards, int resourceDeckFace, int goldDeckFace) {
        Platform.runLater(()-> {
            this.resourceDeckSize = resourceDeckSize;
            this.goldDeckSize = goldDeckSize;
            goldSize.setText(String.valueOf(goldDeckSize));
            resourceSize.setText(String.valueOf(resourceDeckSize));
            // update the current visible resource cards and gold cards in the game that the player can draw.
            // if the player drew the card from the resource deck
            if (!this.currentResourceCards.contains(currentResourceCards[1])) {
                // player drew the left card from the resource deck
                if (this.currentResourceCards.get(1) == currentResourceCards[0]) {
                    this.currentResourceCards.remove(0);
                    this.currentResourceCards.addFirst(currentResourceCards[1]);
                    this.resourceDeckView[1].setImage(new Image(convertToImagePath(currentResourceCards[1], true), 120, 80, true, false));
                }
                // player drew the right card from the resource deck
                else {
                    this.currentResourceCards.remove(1);
                    this.currentResourceCards.addLast(currentResourceCards[1]);
                    this.resourceDeckView[2].setImage(new Image(convertToImagePath(currentResourceCards[1], true), 120, 80, true, false));
                }
            }
            // if the player drew the card from the resource deck
            if (!this.currentGoldCards.contains(currentGoldCards[1])) {
                // player drew the left card from the gold deck
                if (this.currentGoldCards.get(1) == currentGoldCards[0]) {
                    this.currentGoldCards.remove(0);
                    this.currentGoldCards.addFirst(currentGoldCards[1]);
                    this.goldDeckView[1].setImage(new Image(convertToImagePath(currentGoldCards[1], true), 120, 80, true, false));
                }
                // player drew the right card from the gold deck
                else {
                    this.currentGoldCards.remove(1);
                    this.currentGoldCards.addLast(currentGoldCards[1]);
                    this.goldDeckView[2].setImage(new Image(convertToImagePath(currentGoldCards[1], true), 120, 80, true, false));
                }
            }
            this.resourceCardDeckFacingKingdom = resourceDeckFace;
            this.goldCardDeckFacingKingdom = goldDeckFace;
            if(goldDeckSize == 0) {
                this.goldDeckView[0].setImage(imagesMap.get("PLACEHOLDER"));
            }else {
                this.goldDeckView[0].setImage(imagesMap.get(String.valueOf(goldDeckFace + 4)));
            }
            if(resourceDeckSize == 0) {
                this.resourceDeckView[0].setImage(imagesMap.get("PLACEHOLDER"));
            }else {
                this.resourceDeckView[0].setImage(imagesMap.get(String.valueOf(resourceDeckFace)));
            }
            notice.getChildren().add(new Label("The deck has been updated.\n"));
        });
    }

    @Override
    public void handleFailureCase(Event event, String reason) {
        createAlert(reason);
        // in case of create game failure, join game failure, reconnect game failure just notify the player with the reason of the failure.
        switch (event){
            case PLACE_CARD_FAILURE -> { // Place card failure
                currentEvent = Event.PLACE_CARD;
            }
            case DRAW_CARD_FAILURE -> { // Draw card failure
                currentEvent = Event.DRAW_CARD;
                requestDrawCard();
            }
            case SELECT_SECRET_OBJ_CARD_FAILURE -> { // Select secret objective card failure
                currentEvent = Event.SELECTED_SECRET_OBJ_CARD;
                requestSelectSecretObjectiveCard();
            }
            case SELECT_STARTER_CARD_SIDE_FAILURE -> { // Select starter card side failure
                currentEvent = Event.SELECT_STARTER_CARD_SIDE;
                requestSelectStarterCardSide(startCard);
            }
            case CHAT_ERROR -> { // Chat error
                chatArea.addIncomingMessageToChat(reason, "NOTICE");
            }
        }

    }

    @Override
    public void startChatting() {
        chatArea.setActive(true);
    }

    @Override
    public void showDeck() {
        deckArea.setEffect(glow);
        deckArea.setEffect(dropShadow);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            deckArea.setEffect(null);
        });
        pause.play();
    }

    @Override
    public void showHelpInfo() {
        VBox helpArea = new VBox(); // Entire selection area

        Label promptLabel = createLabel("HELP", 20); // Text label prompting user to pick a card
        TextArea helpInfo = new TextArea();
        helpInfo.setEditable(false);
        helpInfo.setStyle("-fx-control-inner-background: #E6DEB3;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
        helpInfo.setWrapText(true);
        helpInfo.setText(
                "1. When it is your turn, you can place a card in the field, click on the card that you want to " +
                "choose and click on one available position.\n" +
                "2. You can draw a card from the deck when you have placed a card in the field.\n" +
                "3. The chat area is enabled when the game starts.\n" +
                "4. You can draw a card from the deck when you have placed the starter card.\n" +
                "5. To see the field of the other players, click on the player's name.\n" +
                "6. The player with the most points wins the game.\n");

        Button closeHelp = createButton("Close", 0, 0);
        helpArea.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179, 0.35), new CornerRadii(0), new Insets(0))));
        helpArea.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179, 0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(30))));
        helpArea.setSpacing(10);
        helpArea.getChildren().addAll(promptLabel, helpInfo, closeHelp);
        helpArea.translateXProperty().bind(masterPane.widthProperty().subtract(helpArea.widthProperty()).divide(2)); // Set position of selectionArea
        helpArea.translateYProperty().bind(masterPane.heightProperty().subtract(helpArea.heightProperty()).divide(2));
        masterPane.getChildren().add(helpArea);
        closeHelp.setOnAction(e -> masterPane.getChildren().remove(helpArea));
    }

    @Override
    public void requestSelectSecretObjectiveCard() {
        VBox secretObjectiveCardSelection = setupSecretObjectiveCardSelectionArea(secretObjCards.get(0), secretObjCards.get(1));
        Platform.runLater(() -> masterPane.getChildren().add(secretObjectiveCardSelection));
    }

    @Override
    public void updateConfirmSelectedSecretCard(int chosenSecretObjectiveCard) {
        secretObjCardSelected = chosenSecretObjectiveCard;
        Platform.runLater(() -> {
            masterPane.getChildren().removeLast();
            String cardIdStr = chosenSecretObjectiveCard > 99 ? String.valueOf(chosenSecretObjectiveCard) : "0" + chosenSecretObjectiveCard;
            secretObjCardView.setImage(new Image("/cards_front_" + cardIdStr + ".png", 120, 80, true, true));
        });
    }


    @Override
    public void requestPlaceCard() {
        int posX;
        int posY;
        StackPane playerBoard = playerField.get(thisPlayerNickname);
        for (int[] pos : availableSpaces) {
            posX = 300+pos[0] * 95;
            posY = 250+pos[1] * (-50);
            int finalPosX = posX;
            int finalPosY = posY;
            Platform.runLater(() ->{
                ImageView availableSpace = new ImageView(imagesMap.get("AVAILABLESPACE"));
                availableSpace.setOpacity(0.5);
            handleAvailableSpaceClick(availableSpace,pos[0], pos[1]);
            availableSpace.setTranslateX(finalPosX);
            availableSpace.setTranslateY(finalPosY);
            availableSpace.setEffect(new DropShadow(20, Color.BLACK));
            playerBoard.getChildren().add(availableSpace);
        });
        }
        Platform.runLater(()->notice.getChildren().add(new Label("> Click on the card then position available.\n")));
    }

    /**
     * Method set to handle the click action of the available space in the field.
     * When a player clicks on the available space, the method notifies the listener to place the card in the field.
     *
     * @param availableSpace the image view of the available space the player has clicked on
     * @param x the x coordinate of the available space (field coordinates)
     * @param y the y coordinate of the available space (field coordinates)
     */
    private void handleAvailableSpaceClick(ImageView availableSpace, int x, int y){
        availableSpace.setOnMouseClicked(e->{
            notifyAskListener(new PlaceCardMessage(thisPlayerNickname, selectedCardId, x, y, handViewCardSide[indexCardPlaced])); // Notify server of card selection
            Platform.runLater(()->notice.getChildren().add(new Label("> You selected the position (" + x + ", " + y + ") to place the card.\n")));

        });
    }

    @Override
    public void updateAfterPlacedCard(String playerNickname, int cardID, int x, int y, boolean isUp, ArrayList<int[]> availablePos, int[] resources, int points) {
        // update the field of the player
        publicInfo.get(playerNickname).addToField(new CardPlacedView(cardID, null, x, y, isUp));
        publicInfo.get(playerNickname).updateResources(resources); // update the resources
        publicInfo.get(playerNickname).updatePoints(points); // update the points
        // update the view
        Platform.runLater(()->{
            playerViews.get(playerNickname).setPoints(points);
        playerViews.get(playerNickname).setResourceLabels(resources);
        // represents the sequence of the card placed in the field.
        int posX = 300+x * 95;
        int posY = 250+y * (-50);
        StackPane playerBoard = playerField.get(playerNickname);
        String cardSide = isUp ? "/cards_front_" : "/cards_back_";
        if(x==0 && y==0){
            cardSide = isUp ? "/cards_back_" : "/cards_front_";
        }
        String cardIdStr = cardID >= 10 ? "0" + cardID : "00" + cardID;
        ImageView cardImage = new ImageView(new Image(cardSide + cardIdStr + ".png", 120, 80, true, true));
        cardImage.setTranslateX(posX);
        cardImage.setTranslateY(posY);
        playerBoard.getChildren().add(cardImage);
        if(playerNickname.equals(thisPlayerNickname)){
            availableSpaces = availablePos; // Update available spaces
        }
        });
    }


    @Override
    public void handleEvent(Event event, String message) {
        switch (event) {
            case Event.NEW_PLAYER_JOIN -> {
                this.players.add(message);
                Label player = createLabel("Player " + message + " joined the lobby",18);
                player.setTranslateX(20);
                player.setTranslateY(50);
                Platform.runLater(() ->
                {
                    selectionPane.getChildren().add(player);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        waitingRoot.getChildren().remove(player);
                    });
                    pause.play();
                });
            }
            case Event.GAME_START -> {
                notice.getChildren().add(new Label("> The game starts now!\n"));
            }
            case Event.GAME_JOINED -> {
                Platform.runLater(() ->
                {
                    selectionPane.getChildren().removeLast();
                    waitingRoot = new StackPane();
                    this.players.add(thisPlayerNickname);
                    currentEvent = Event.WAITING_FOR_START; // enter the waiting for start event
                    Status = Event.LOBBY;

                    Label id = createLabel("ID: " + gameID, -80, -80);
                    Label waiting = createLabel("Waiting...", 130, 100);

                    playerListView = createTextField(null, 135, 360, 0, 0);
                    playerListView.setText("Players: \n" + String.join("\n", players));
                    playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                            "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
                    playerListView.setTranslateX(0);
                    playerListView.setTranslateY(0);
                    playerListView.setEditable(false);
                    waitingRoot.getChildren().addAll(id, waiting, playerListView);
                    selectionPane.getChildren().add(waitingRoot);
                });
            }
            case Event.PLAYER_DISCONNECTED -> {
                    Platform.runLater(() ->
                    {
                    if(!Status.equals(Event.LOBBY)){
                        publicInfo.get(message).updateOnline(false);
                        playerViews.get(message).setColour(imagesMap.get("GREY"));
                        notice.getChildren().add(new Label("> Player " + message + " disconnected.\n"));
                    }
                    else {
                        Label player = createLabel("Player " + message + " disconnected\n", 18);
                        player.setTranslateX(20);
                        player.setTranslateY(80);
                        waitingRoot.getChildren().add(player);
                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(e -> {
                            waitingRoot.getChildren().remove(player);
                        });
                        pause.play();
                    }
                });
            }
        }
    }

    @Override
    public String convertToColour(int colour) {
        switch (colour) {
            case 0 -> {
                return "RED";
            }
            case 1 -> {
                return "GREEN";
            }
            case 2 -> {
                return "BLUE";
            }
            case 3 -> {
                return "YELLOW";
            }
            case 4 -> {
                return "BLACK";
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Generates the selection area for the initial card side selection.
     * The selection area is composed of a prompt label, 2 cards to choose from
     *
     * @return a VBox containing the selection area for the initial card side selection
     */
    public VBox setupInitialCardSideSelectionArea(int imageNumber) {
        VBox selectionArea = new VBox(); // Entire selection area

        Label promptLabel = createLabel("Choose a starter card side:", 20); // Text label prompting user to pick a card
        HBox cardPairArea = new HBox(); // Area displaying the 2 cards to choose from

        ImageView firstCard = new ImageView(new Image("cards_back_0" + imageNumber + ".png", 240, 160, true, false)); // Load the images of the cards to display
        ImageView secondCard = new ImageView(new Image("cards_front_0" + imageNumber + ".png", 240, 160, true, false));

        firstCard.setOnMouseClicked(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), firstCard); // Add scaling animation to the card when selected
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.setOnFinished(event -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(50.0);
                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                firstCard.setEffect(dropShadow);
            }); // When animation finishes, add a drop shadow effect to the card to highlight which card was selected
            st.play(); // Play the animation

            firstCard.setOnMouseClicked(null); // Disable card selection functionality
            secondCard.setOnMouseClicked(null);

            notifyAskListener(new SelectedStarterCardSideMessage(thisPlayerNickname, true)); // Notify the controller that the front side of the card was selected
        }); // Front side of starting card was selected
        secondCard.setOnMouseClicked(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), secondCard); // Add scaling animation to the card when selected
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.setOnFinished(event -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(50.0);
                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                secondCard.setEffect(dropShadow);
            }); // When animation finishes, add a drop shadow effect to the card to highlight which card was selected
            st.play(); // Play the animation

            firstCard.setOnMouseClicked(null); // Disable card selection functionality
            secondCard.setOnMouseClicked(null);

            notifyAskListener(new SelectedStarterCardSideMessage(thisPlayerNickname, false)); // Notify the controller that the back side of the card was selected
        }); // Back side of starting card was selected

        selectionArea.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179, 0.35), new CornerRadii(0), new Insets(0))));
        selectionArea.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179, 0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(30))));

        selectionArea.translateXProperty().bind(masterPane.widthProperty().subtract(selectionArea.widthProperty()).divide(2)); // Set position of selectionArea
        selectionArea.translateYProperty().bind(masterPane.heightProperty().subtract(selectionArea.heightProperty()).divide(2));

        cardPairArea.setSpacing(20); // Add spacing between cards in the cardPairArea

        // Compose elements
        selectionArea.getChildren().addAll(promptLabel, cardPairArea);
        cardPairArea.getChildren().addAll(firstCard, secondCard);

        return selectionArea;
    }

    /**
     * Generates the selection area for the secret objective card selection.
     * The selection area is composed of a prompt label, 2 cards to choose from
     *
     * @return a VBox containing the selection area for the secret objective card selection
     */
    public VBox setupSecretObjectiveCardSelectionArea(int card1, int card2) {
        VBox selectionArea = new VBox(); // Entire selection area

        Label promptLabel = createLabel("Choose a secret objective card:", 20); // Text label prompting user to pick a card
        HBox cardPairArea = new HBox(); // Area displaying the 2 cards to choose from
        String card1Path = card1 > 99 ? "cards_front_" + card1 : "cards_front_0" + card1;
        String card2Path = card2 > 99 ? "cards_front_" + card2 : "cards_front_0" + card2;
        ImageView firstCard = new ImageView(new Image(card1Path + ".png", 240, 160, true, false));
        ImageView secondCard = new ImageView(new Image(card2Path + ".png", 240, 160, true, false));

        firstCard.setOnMouseClicked(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), firstCard); // Add scaling animation to the card when selected
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.setOnFinished(event -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(50.0);
                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                firstCard.setEffect(dropShadow);
            }); // When animation finishes, add a drop shadow effect to the card to highlight which card was selected
            st.play(); // Play the animation

            firstCard.setOnMouseClicked(null); // Disable card selection functionality
            secondCard.setOnMouseClicked(null);

            notifyAskListener(new SelectedSecretObjectiveCardMessage(thisPlayerNickname, card1)); // Notify the controller that the first card was selected
        }); // First card was selected
        secondCard.setOnMouseClicked(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), secondCard); // Add scaling animation to the card when selected
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.setOnFinished(event -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(50.0);
                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                secondCard.setEffect(dropShadow);
            }); // When animation finishes, add a drop shadow effect to the card to highlight which card was selected
            st.play(); // Play the animation

            firstCard.setOnMouseClicked(null);
            secondCard.setOnMouseClicked(null);

            notifyAskListener(new SelectedSecretObjectiveCardMessage(thisPlayerNickname, card2)); // Notify the controller that the second card was selected
        });

        selectionArea.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179, 0.35), new CornerRadii(0), new Insets(0))));
        selectionArea.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179, 0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(30))));

        selectionArea.translateXProperty().bind(masterPane.widthProperty().subtract(selectionArea.widthProperty()).divide(2)); // Set position of selectionArea
        selectionArea.translateYProperty().bind(masterPane.heightProperty().subtract(selectionArea.heightProperty()).divide(2));

        cardPairArea.setSpacing(20); // Add spacing between cards in the cardPairArea

        // Compose elements
        selectionArea.getChildren().addAll(promptLabel, cardPairArea);
        cardPairArea.getChildren().addAll(firstCard, secondCard);

        return selectionArea;
    }
    private void handleButtonClick(Button clickedButton, Button button1, Button button3, Effect effect) {
        clickedButton.setEffect(effect);
        button1.setDisable(true);
        button3.setDisable(true);
    }

    /**
     * Getter method.
     *
     * @return the nickname of the player
     */
    public String getThisPlayerNickname() {
        return thisPlayerNickname;
    }
}
