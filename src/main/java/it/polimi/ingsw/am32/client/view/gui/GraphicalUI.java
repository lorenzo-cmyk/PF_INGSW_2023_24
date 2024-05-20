package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.View;
import it.polimi.ingsw.am32.message.ClientToServer.AccessGameMessage;
import it.polimi.ingsw.am32.message.ClientToServer.NewGameMessage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphicalUI extends View {
    private StackPane welcomeRoot;
    private StackPane selectionPane;
    private StackPane connectionRoot;
    Font jejuHallasanFont = Font.loadFont(getClass().getResourceAsStream("/JejuHallasan.ttf"), 20);
    private final String [] ruleBookImages = {"/CODEX_RuleBook_IT/01.png","/CODEX_RuleBook_IT/02.png",
            "/CODEX_RuleBook_IT/03.png","/CODEX_RuleBook_IT/04.png","/CODEX_RuleBook_IT/05.png",
            "/CODEX_RuleBook_IT/06.png","/CODEX_RuleBook_IT/07.png","/CODEX_RuleBook_IT/08.png",
            "/CODEX_RuleBook_IT/09.png","/CODEX_RuleBook_IT/10.png","/CODEX_RuleBook_IT/11.png",
            "/CODEX_RuleBook_IT/12.png"};

    @Override
    public void launch() {
        Application.launch(GraphicalUIApplication.class);
    }
    public GraphicalUI() {
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

        Image backgroundSelectionPage = new Image("/SelectionDisplay.png");
        BackgroundImage backgroundImg = new BackgroundImage(backgroundSelectionPage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(975, 925, false, false, false, false));
        Background background = new Background(backgroundImg);
        selectionPane.setBackground(background);

        Label label = createLabel("Connection",-80,-70);

        Button socketButton = createButton("[Socket]",100,0);

        Button rmiButton = createButton("[RMI]",-100,0);

        connectionRoot.getChildren().addAll(label,socketButton,rmiButton);

        selectionPane.getChildren().add(connectionRoot);

        StackPane socketRoot = new StackPane();

        Label labelIP = createLabel("Socket",-80,-80);

        TextField ip = createTextField("Enter the IP", 35, 250, -50, -30);

        TextField port = createTextField("Enter the port", 35, 250, -50, 50);

        Button OkButton = createButton("[OK]",160,0);

        socketRoot.getChildren().addAll(OkButton,labelIP,ip,port);

        socketButton.setOnAction(e -> {
            selectionPane.getChildren().remove(connectionRoot);
            selectionPane.getChildren().add(socketRoot);
        });

        OkButton.setOnAction(e->{
            String ServerIP = ip.getText(); // Read the player's input
            String ServerPort = port.getText();
            int portNumber = 0;
            try {
                portNumber = Integer.parseInt(ServerPort);
            } catch (NumberFormatException ex) {
                createAlert("Invalid port number");
                port.clear();
            }
            if (isValid.isIpValid(ServerIP)&&isValid.isPortValid(portNumber)) { // Check if the IP address is valid
                try {
                    setSocketClient(ServerIP, portNumber);
                    selectionPane.getChildren().remove(socketRoot);
                    askSelectGameMode();
                } catch (IOException ex) {
                    createAlert("Connection failed");
                    ip.clear();
                    port.clear();
                }
            }else {
                createAlert("Invalid IP/port number");
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
        StackPane createGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname",-90,-80);
        Label labelPlayers = createLabel("Players number",-60,30);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);

        Button twoButton = createTransButton("[2]",30,"#3A2111",-150,70);
        Button threeButton3 = createTransButton("[3]",30,"#3A2111",-70,70);
        Button fourButton = createTransButton("[4]",30,"#3A2111",10,70);
        Button createButton = createButton("[Create]",140,65);

        createGameRoot.getChildren().addAll(labelNickname,labelPlayers,nickname,twoButton,threeButton3,fourButton,createButton);

        twoButton.setOnAction(e->{
            playerNum = 2;
        });
        threeButton3.setOnAction(e->{
            playerNum = 3;
        });
        fourButton.setOnAction(e->{
            playerNum = 4;
        });
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
        StackPane joinGameRoot = new StackPane();

        Label labelNickname = createLabel("Nickname&AccessID",-90,-80);

        TextField nickname = createTextField("Enter the nickname", 35, 350, -40, -30);
        TextField accessID = createTextField("Enter the accessID", 35, 350, -40, -30);

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
    public void setSocketClient(String ServerIP, int portNumber) throws IOException {
        super.setSocketClient(ServerIP, portNumber); // see the method in the superclass
        Thread thread = new Thread((clientNode));
        thread.start();
        Thread askListener = new Thread(super.askListener);
        askListener.start();
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
    public void showPoints(String playerNickname) {

    }

    @Override
    public void requestSelectSecretObjCard(ArrayList<Integer> secrets, ArrayList<Integer> common, ArrayList<Integer> hand) {

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
    public void updatePlayerList(ArrayList<String> players) {
        
    }

    @Override
    public void setUpPlayersData() {

    }

    @Override
    public void updateMatchStatus(int matchStatus) {

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
    public void updateDeck(int resourceDeckSize, int goldDeckSize, int[] currentResourceCards, int[] currentGoldCards) {

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
        //TODO
    }
}
