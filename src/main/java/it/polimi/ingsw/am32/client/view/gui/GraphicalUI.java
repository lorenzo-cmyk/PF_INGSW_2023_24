package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.PlayerPub;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.AccessGameMessage;
import it.polimi.ingsw.am32.message.ClientToServer.NewGameMessage;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

public class GraphicalUI extends View {
    private GraphicalUIApplication app;
    private StackPane masterPane;
    private StackPane welcomeRoot;
    private StackPane selectionPane;
    private StackPane connectionRoot;
    private StackPane waitingRoot;
    private TextField playerListView;
    private Label matchStatus;
    private HBox playerOrder;
    private ImageView [] labelPlayerOrder;
    private HashMap<String, PlayerPubView> playerViews = new HashMap<>();
    private HashMap<String,Image> imagesMap = new HashMap<>();
    private final Font jejuHallasanFont = Font.loadFont(getClass().getResourceAsStream("/JejuHallasan.ttf"), 20);
    private final String [] ruleBookImages = {"/codex_rulebook_it_01.png", "/codex_rulebook_it_02.png", "/codex_rulebook_it_03.png",
            "/codex_rulebook_it_04.png", "/codex_rulebook_it_05.png", "/codex_rulebook_it_06.png", "/codex_rulebook_it_07.png",
            "/codex_rulebook_it_08.png", "/codex_rulebook_it_09.png", "/codex_rulebook_it_10.png", "/codex_rulebook_it_11.png",
            "/codex_rulebook_it_12.png"};

    @Override
    public void launch() {
        Application.launch(GraphicalUIApplication.class);
    }
    public GraphicalUI() {
        super();
    }
    public void setApp(GraphicalUIApplication app) {
        this.app = app;
    }
    public StackPane getWelcomeRoot() {
        showWelcome();
        return welcomeRoot;
    }
    public StackPane getSelectionRoot() {
        chooseConnection();
        return selectionPane;
    }
    @Override
    public void showWelcome() {
        welcomeRoot = new StackPane();
        Button ruleButton = createTransButton("[Rule Book]",30,"#E6DEB3",0,300);
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
    public void showRuleBook() {
        StackPane ruleBookRoot = new StackPane();
        Stage ruleBookStage = new Stage();
        Image [] ruleBookImage = new Image[ruleBookImages.length];
        for(int i = 0; i < ruleBookImages.length; i++) {
            ruleBookImage[i] = new Image(ruleBookImages[i],750, 750, true, true);
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
            ruleBook.setImage(new Image(ruleBookImages[index.get()],750, 750, true, true));
        });
        previousButton.setOnAction(e -> {
            index.set((index.get() - 1 + ruleBookImages.length) % ruleBookImages.length);
            ruleBook.setImage(new Image(ruleBookImages[index.get()],750, 750, true, true));
        });
        return new Button[]{previousButton, nextButton};
    }

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
        Label label = createLabel("Connection",-80,-70); // add the title of the connection root
        Button socketButton = createButton("[Socket]",100,0); // create the button to choose the socket connection
        Button rmiButton = createButton("[RMI]",-100,0); // create the button to choose the RMI connection
        connectionRoot.getChildren().addAll(label,socketButton,rmiButton);

        // add the connection root to the selection pane in this way set the choose connection page for the player
        selectionPane.getChildren().add(connectionRoot);

        // create the socket connection root
        StackPane socketRoot = new StackPane();
        Label labelIP = createLabel("Socket",-80,-80); // add the title
        TextField ip = createTextField("Enter the IP", 35, 250, -50, -30); // create the text field asks the player to enter the IP address
        TextField port = createTextField("Enter the port", 35, 250, -50, 50); // create the text field asks the player to enter the port number
        Button OkButton = createButton("[OK]",160,0); // create the button Ok to confirm the input.
        socketRoot.getChildren().addAll(OkButton,labelIP,ip,port); // the view when the player choose the socket connection

        // set the action of the buttons
        socketButton.setOnAction(e -> {
            selectionPane.getChildren().remove(connectionRoot); // exit from the choose connection page
            selectionPane.getChildren().add(socketRoot); // enter the socket connection page
        });

        OkButton.setOnAction(e->{
            String ServerIP = ip.getText(); // Read the player's input and save it the server IP address
            String ServerPort = port.getText();
            try {
                int portNumber = Integer.parseInt(ServerPort);
                if (isValid.isIpValid(ServerIP)&&isValid.isPortValid(portNumber)) { // Check if the IP address is valid
                    setSocketClient(ServerIP, portNumber);
                    selectionPane.getChildren().remove(socketRoot);
                    askSelectGameMode();
                }else {
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
        //TODO RMI
    }

    @Override
    public void askSelectGameMode() {
        StackPane gameModeRoot = new StackPane();
        Label label = createLabel("Game \n Mode",150,10);
        Button newGameButton = createButton("[New Game]",-70,-50);
        Button joinGameButton = createButton("[Join Game]",-70,10);
        Button reconnectGameButton = createButton("[Reconnect]",-70,70);
        gameModeRoot.getChildren().addAll(label,newGameButton,joinGameButton,reconnectGameButton);
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

    @Override
    public void askCreateGame() {
        currentEvent = Event.CREATE_GAME;
        StackPane createGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname",-90,-80);
        Label labelPlayers = createLabel("Players number",-60,30);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);

        Button twoButton = createTransButton("[2]",30,"#3A2111",-150,70);
        Button threeButton3 = createTransButton("[3]",30,"#3A2111",-70,70);
        Button fourButton = createTransButton("[4]",30,"#3A2111",10,70);
        Button createButton = createButton("[Create]",140,65);

        createGameRoot.getChildren().addAll(labelNickname,labelPlayers,nickname,twoButton,threeButton3,fourButton,createButton);

        twoButton.setOnAction(e-> playerNum = 2);
        threeButton3.setOnAction(e-> playerNum = 3);
        fourButton.setOnAction(e-> playerNum = 4);
        createButton.setOnAction(e -> {
            thisPlayerNickname = nickname.getText();
            if (thisPlayerNickname.isBlank()) {
                createAlert("Nickname cannot be left blank");
                nickname.clear();
            }
            else if (thisPlayerNickname.length() > 20) {
                createAlert("Nickname must be less than 20 characters");
                nickname.clear();
            }
            else {
                if (playerNum==2||playerNum==3||playerNum==4) { //TODO ADD ANIMATION
                    notifyAskListenerLobby(new NewGameMessage(thisPlayerNickname, playerNum));
                }
                else {
                    createAlert("Please select the number of players");
                }
            }
        });
        selectionPane.getChildren().add(createGameRoot);
    }

    @Override
    public void askJoinGame() {
        currentEvent = Event.JOIN_GAME;
        StackPane joinGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname&GameID",-80,-80);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);
        TextField accessID = createTextField("Enter the game ID", 35, 220, -60, 35);

        Button joinButton = createButton("[Join]",140,65);

        joinGameRoot.getChildren().addAll(labelNickname,nickname,accessID,joinButton);

        joinButton.setOnAction(e -> {
            thisPlayerNickname = nickname.getText();
            String ID = accessID.getText();
            if (thisPlayerNickname.isBlank()) {
                createAlert("Nickname cannot be left blank");
                nickname.clear();
            }
            else if (thisPlayerNickname.length() > 20) {
                createAlert("Nickname must be less than 20 characters");
                nickname.clear();
            }
            else {
                try {
                    gameID = Integer.parseInt(ID);
                    notifyAskListenerLobby(new AccessGameMessage(gameID, thisPlayerNickname));
                } catch (NumberFormatException ex) {
                    createAlert("Game ID must be a number");
                    accessID.clear();
                }
            }
        });
        selectionPane.getChildren().add(joinGameRoot);
    }

    @Override
    public void updateNewGameConfirm(int gameID, String recipientNickname) {
        Platform.runLater(() -> {
            selectionPane.getChildren().removeLast();
            waitingRoot = new StackPane();

            this.gameID = gameID;
            this.thisPlayerNickname = recipientNickname;
            this.players.add(recipientNickname);
            currentEvent = Event.WAITING_FOR_START; // enter the waiting for start event
            Status = Event.LOBBY;

            Label id = createLabel("ID: " + gameID, -80, -80);
            Label waiting = createLabel("Waiting...", 130, 100);

            playerListView = createTextField(null, 135, 360, 0, 0);
            playerListView.setText("Players: \n" + String.join("\n", players));
            playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                    "-fx-font-size: 25px;-fx-font-family: 'JejuHallasan';");
            playerListView.setTranslateX(0);
            playerListView.setTranslateY(0);
            playerListView.setEditable(false);
            waitingRoot.getChildren().addAll(id, waiting, playerListView);
            selectionPane.getChildren().add(waitingRoot);
        });
    }

    @Override
    public void setSocketClient(String ServerIP, int portNumber) throws IOException {
        super.setSocketClient(ServerIP, portNumber); // see the method in the superclass
        Thread thread = new Thread((clientNode));
        thread.start();
        Thread askListener = new Thread(super.askListener);
        askListener.start();
    }

    @Override
    public void setUpPlayersData() {
        if(this.matchStatus == null){
            this.matchStatus = createLabelMasterPane(String.valueOf(Status),20);
        }
        for (String player : players) {
            // set up the data of the players and initialize the board of the players
            publicInfo.put(player, new PlayerPub(null, 0, new ArrayList<>(), new int[]{0, 0, 0, 0, 0, 0, 0},
                    true));

        }
    }

    @Override
    public void updatePlayerList(ArrayList<String> players) {
        this.players = players;
        //playerList.setText("Players:\n" + String.join("\n", players));
        if(playerListView==null){
            playerListView = createTextField(null, 135, 360, 0, 0);
            playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                    "-fx-font-size: 25px;-fx-font-family: 'JejuHallasan';");
            playerListView.setTranslateX(0);
            playerListView.setTranslateY(0);
            playerListView.setEditable(false);
        }
        playerListView.setText("Players: \n" + String.join(",\n", players));
    }


    @Override
    public void updateMatchStatus(int matchStatus) {
        this.Status = Event.getEvent(matchStatus); // update the match status of the player.
        Platform.runLater(()->this.matchStatus.setText(String.valueOf(Status)));
        if(Status.equals(Event.TERMINATING)){ // if the match status is TERMINATING show the points of all players.
            for (String player : players) {
                showResource(player);
            }
        }
    }
    private void setGameView(){
        imagesMap.put("BLUE", new Image("/CODEX_pion_bleu.png", 20, 20, true, false));
        imagesMap.put("YELLOW", new Image("/CODEX_pion_jaune.png", 20, 20, true, false));
        imagesMap.put("BLACK", new Image("/CODEX_pion_noir.png", 20, 20, true, false));
        imagesMap.put("RED", new Image("/CODEX_pion_rouge.png", 20, 20, true, false));
        imagesMap.put("GREEN", new Image("CODEX_pion_vert.png", 20, 20, true, false));
        imagesMap.put("PLANT", new Image("kingdom_plant.png", 20, 20, true, false));
        imagesMap.put("FUNGI", new Image("kingdom_fungi.png", 20, 20, true, false));
        imagesMap.put("ANIMAL", new Image("kingdom_animal.png", 20, 20, true, false));
        imagesMap.put("INSECT", new Image("kingdom_insect.png", 20, 20, true, false));
        imagesMap.put("QUILL", new Image("kingdom_quill.png", 20, 20, true, false));
        imagesMap.put("INKWELL", new Image("kingdom_inkwell.png", 20, 20, true, false));
        imagesMap.put("MANUSCRIPT", new Image("kingdom_manuscript.png", 20, 20, true, false));

        masterPane = new StackPane();
        masterPane.setBackground(new Background(new BackgroundFill(Color.rgb(246, 243, 228), new CornerRadii(0), new Insets(0))));
        VBox playerInfoPanel = new VBox();
        for (String player : publicInfo.keySet()) {
            Label nickNameLabel = createLabelMasterPane(player, 20);
            ImageView colour = new ImageView(imagesMap.get("BLACK"));
            Label points = createLabelMasterPane("0", 20);
            Label[] resourceLabels = new Label[]{
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20),
                    createLabelMasterPane("0", 20)};
            PlayerPubView playerPubView = new PlayerPubView(nickNameLabel, colour, points, resourceLabels);
            playerViews.put(player, playerPubView);
            HBox playerInfo = new HBox();
            playerInfo.setSpacing(10);
            playerInfo.setMaxWidth(300);
            playerInfo.setMinWidth(300);
            playerInfo.getChildren().addAll(playerViews.get(player).getColour(), playerViews.get(player).getNickname());

            HBox playerResource = new HBox();
            playerResource.setSpacing(10);
            playerResource.getChildren().addAll(
                    new ImageView(imagesMap.get("PLANT")), playerPubView.getResourceLabels()[0],
                    new ImageView(imagesMap.get("FUNGI")), playerPubView.getResourceLabels()[1],
                    new ImageView(imagesMap.get("ANIMAL")), playerPubView.getResourceLabels()[2],
                    new ImageView(imagesMap.get("INSECT")), playerPubView.getResourceLabels()[3],
                    new ImageView(imagesMap.get("QUILL")), playerPubView.getResourceLabels()[4],
                    new ImageView(imagesMap.get("INKWELL")), playerPubView.getResourceLabels()[5],
                    new ImageView(imagesMap.get("MANUSCRIPT")), playerPubView.getResourceLabels()[6]);
            playerInfo.setOnMouseEntered(e -> {
                playerInfo.getChildren().add(playerResource);
            });
            playerInfo.setOnMouseExited(e -> {
                playerInfo.getChildren().remove(playerResource);
            });
            playerInfoPanel.getChildren().add(playerInfo);
        }
        playerInfoPanel.setTranslateX(20);
        playerInfoPanel.setTranslateY(50);
        Label labelID = createLabelMasterPane("ID: " + gameID, 20);
        HBox StatusBox = new HBox();
        Label statusTitle = createLabelMasterPane("Status: ", 20);
        StatusBox.getChildren().addAll(statusTitle, matchStatus);
        labelPlayerOrder = new ImageView[]{playerViews.get(thisPlayerNickname).getColour()};
        Label OrderTitle = createLabelMasterPane("Order:", 20);

        playerOrder = new HBox();
        playerOrder.getChildren().addAll(OrderTitle);

        HBox topLine = new HBox();
        topLine.setSpacing(100);
        topLine.getChildren().addAll(labelID, StatusBox, playerOrder);
        topLine.setTranslateX(60);
        topLine.setTranslateY(20);

        masterPane.getChildren().addAll(topLine, playerInfoPanel);
        Platform.runLater(() -> {
            app.updateScene(masterPane);
            app.getPrimaryStage().setMinWidth(1200);
            app.getPrimaryStage().setFullScreen(true);
        });
    }





    // Methods to create the components of the GUI
    private Group createPlayerInfoPanel() {
        return null;
    }
    private Label createLabelMasterPane(String text, int size) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: "+size+"px;-fx-font-family: 'JejuHallasan';");
        return label;
    }
    private Label createLabel(String text, int X, int Y) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
        label.setTranslateX(X);
        label.setTranslateY(Y);
        return label;
    }
    private Button createTransButton(String buttonName,int size,String color,int X, int Y) {
        Button button = new Button(buttonName);
        button.setStyle("-fx-background-color: transparent;-fx-text-fill:"+color+";-fx-alignment: center;" +
                "-fx-font-size: "+size+"px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,10 );");
        button.setTranslateX(X);
        button.setTranslateY(Y);
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

    private void createAlert(String reason){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = new DialogPane();
        Label label1 = new Label(reason);
        label1.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 20px;-fx-font-family: 'JejuHallasan';");
        dialogPane.setContent(label1);
        dialogPane.setStyle("-fx-pref-height: 120px;-fx-pref-width: 400px;-fx-background-image: " +
                "url('/NoticeDisplay.png');-fx-background-position: center;-fx-background-size: 400px 120px;" );
        dialogPane.setMinHeight(120);
        dialogPane.setMinWidth(400);
        dialogPane.setMaxHeight(400);
        dialogPane.setMaxWidth(400);
        alert.setDialogPane(dialogPane);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }


    @Override
    public void showPlayersField(String playerNickname) {

    }

    @Override
    public void showResource(String playerNickname) {

    }

    @Override
    public void setCardsReceived(ArrayList<Integer> secrets, ArrayList<Integer> common, ArrayList<Integer> hand) {

    }

    @Override
    public void showHand(ArrayList<Integer> hand) {

    }

    @Override
    public void showObjectiveCards(ArrayList<Integer> ObjCards) {

    }


    @Override
    public void showCard(int ID, boolean isUp) {

    }

    @Override
    public HashMap<Integer, ArrayList<String>> setImg() {
        return null;
    }

    @Override
    public void updatePlacedCardConfirm(String playerNickname, int placedCard, int[] placedCardCoordinates, boolean placedSide, int playerPoints, int[] playerResources, ArrayList<int[]> newAvailableFieldSpaces) {

    }

    @Override
    public void showMatchWinners(ArrayList<String> players, ArrayList<Integer> points, ArrayList<Integer> secrets, ArrayList<Integer> pointsGainedFromSecrets, ArrayList<String> winners) {

    }

    @Override
    public void updateRollback(String playerNickname, int removedCard, int playerPoints, int[] playerResources) {

    }

    @Override
    public void showChatHistory(List<ChatMessage> chatHistory) {

    }

    @Override
    public void updateChat(String recipientString, String senderNickname, String content) {

    }


    @Override
    public void askNickname() {
        //TODO
    }


    @Override
    public void askReconnectGame() {
        //TODO
    }



    @Override
    public void requestSelectStarterCardSide(int ID) {

    }

    @Override
    public void updateConfirmStarterCard(int colour, int cardID, boolean isUp, ArrayList<int[]> availablePos, int[] resources) {

    }

    @Override
    public void requestDrawCard() {

    }

    @Override
    public void updateAfterDrawCard(ArrayList<Integer> hand) {

    }

    @Override
    public void updateDeck(int resourceDeckSize, int goldDeckSize, int[] currentResourceCards, int[] currentGoldCards, int resourceDeckFace, int goldDeckFace) {

    }

    @Override
    public void handleFailureCase(Event event, String reason) {

    }

    @Override
    public void startChatting() {

    }

    @Override
    public void showDeck() {

    }

    @Override
    public void showHelpInfo() {

    }

    @Override
    public void requestSelectSecretObjectiveCard() {

    }

    @Override
    public void updateConfirmSelectedSecretCard(int chosenSecretObjectiveCard) {

    }


    @Override
    public void requestPlaceCard() {

    }

    @Override
    public void updateAfterPlacedCard(String playerNickname, NonObjCardFactory card, int x, int y, boolean isUp, ArrayList<int[]> availablePos, int[] resources, int points) {

    }


    @Override
    public void handleEvent(Event event, String message) {
        switch (event) {
            case Event.NEW_PLAYER_JOIN ->{
                this.players.add(message);
                Label player = createLabel("Player " + message+" joined the lobby",20,20);
                Platform.runLater(()->
                {
                    selectionPane.getChildren().add(player);
                    PauseTransition pause = new PauseTransition(Duration.seconds(5));
                    pause.setOnFinished(e -> {
                        selectionPane.getChildren().remove(player);
                    });
                });
            }
            case Event.GAME_START->{
                setGameView();
            }
            case Event.GAME_JOINED->{
                Platform.runLater(()->
                {   selectionPane.getChildren().removeLast();
                    waitingRoot = new StackPane();
                    this.players.add(thisPlayerNickname);
                    currentEvent = Event.WAITING_FOR_START; // enter the waiting for start event
                    Status = Event.LOBBY;

                    Label id = createLabel("ID: " + gameID, -80, -80);
                    Label waiting = createLabel("Waiting...", 130, 100);

                    playerListView = createTextField(null, 135, 360, 0, 0);
                    playerListView.setText("Players: \n" + String.join("\n", players));
                    playerListView.setStyle("-fx-background-color: transparent;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                            "-fx-font-size: 25px;-fx-font-family: 'JejuHallasan';");
                    playerListView.setTranslateX(0);
                    playerListView.setTranslateY(0);
                    playerListView.setEditable(false);
                    waitingRoot.getChildren().addAll(id, waiting, playerListView);
                    selectionPane.getChildren().add(waitingRoot);
                    });
            }
            //TODO
        }
    }
}
