package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        Button ruleButton = new Button(" [Rule Book] ");
        ruleButton.setStyle("-fx-background-color: transparent;-fx-text-fill: #E6DEB3;-fx-alignment: center;" +
                "-fx-font-size: 30px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,10 );");
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
        Label label = new Label("Choose Connection");
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
        label.setTranslateY(-50);
        Button socketButton = new Button("[Socket]");
        socketButton.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 35px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(58,33,17,100,0.2) , 10,0,0,10 );");
        socketButton.setTranslateX(100);
        Button rmiButton = new Button("[RMI]");
        rmiButton.setStyle("-fx-text-fill:#3A2111;-fx-alignment: center;" +
                "-fx-font-size: 35px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(58,33,17,100,0.2) , 10,0,0,10 );");
        rmiButton.setTranslateX(-100);

        connectionRoot.getChildren().addAll(label,socketButton,rmiButton);

        selectionPane.getChildren().add(connectionRoot);

        StackPane socketRoot = new StackPane();

        Label labelIP = new Label("Server IP");
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 30px;-fx-font-family: 'JejuHallasan';");
        label.setTranslateY(-50);

        TextField textField = new TextField();
        textField.setPromptText("Please insert the server IP");
        textField.setStyle("-fx-background-color: #E6DEB3;-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 35px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , " +
                "rgba(58,33,17,100,0.2) , 10,0,0,10 );-fx-border-color: #3A2111; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;");

        Button OkButton = new Button("[OK]");
        OkButton.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center;" +
                "-fx-font-size: 35px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(58,33,17,100,0.2) , 10,0,0,10 );");
        OkButton.setTranslateY(100);

        socketRoot.getChildren().addAll(OkButton,labelIP,textField);

        socketButton.setOnAction(e -> {
            selectionPane.getChildren().remove(connectionRoot);
            selectionPane.getChildren().add(socketRoot);
        });
        OkButton.setOnAction(e->{
            String ServerIP = textField.getText(); // Read the player's input
            if (!isValid.isIpValid(ServerIP)) { // Check if the IP address is valid
                alert("Invalid IP address");
            }
        });
        //TODO
    }

    private void alert(String reason){
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
    public void askSelectGameMode() {
        //TODO
    }

    @Override
    public void askNickname() {
        //TODO
    }

    @Override
    public void askCreateGame() {
        //TODO
    }

    @Override
    public void updateNewGameConfirm(int gameID, String recipientNickname) {

    }


    @Override
    public void askJoinGame() {
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
