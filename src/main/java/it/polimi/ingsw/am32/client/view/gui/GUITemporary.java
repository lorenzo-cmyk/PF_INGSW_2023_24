package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.ObjectiveCardFactory;
import it.polimi.ingsw.am32.client.PlayerPub;
import it.polimi.ingsw.am32.client.listener.AskListener;
import it.polimi.ingsw.am32.client.view.tui.BoardView;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GUITemporary extends Application {
    protected final IsValid isValid = new IsValid();
    protected ClientNodeInterface clientNode;
    protected String thisPlayerNickname;
    protected int startCard;
    protected int gameID; //save the game ID received from the NewGameConfirmationMessage or AccessGameConfirmMessage.
    protected int playerNum; //number of players connected to the game, if the player is disconnected, the number will
    // decrease.
    protected ArrayList<String> players; //save and update the players in the game.
    protected String currentPlayer; //save and update the current player by receiving the message from the server.
    protected volatile Event currentEvent; //TODO: not sure if this is useful
    protected int indexCardPlaced=0;
    protected ArrayList<Integer> commonObjCards;
    protected ArrayList<Integer> secretObjCards;
    protected int secretObjCardSelected;
    protected ArrayList<Integer> hand;
    protected ArrayList<Integer> currentResourceCards;
    protected ArrayList<Integer>  currentGoldCards;
    protected int resourceDeckSize;
    protected int goldDeckSize;
    protected int resourceCardDeckFacingKingdom;
    protected int goldCardDeckFacingKingdom;
    protected volatile Event Status;
    protected AskListener askListener;
    protected ArrayList<int[]> availableSpaces;
    protected HashMap<String, PlayerPub> publicInfo; //save the colour, nickname, points and resources of the player.
    protected static final ArrayList<ObjectiveCardFactory> objectiveCards = ObjectiveCardFactory.setObjectiveCardArray();
    protected static final ArrayList<NonObjCardFactory> nonObjCards = NonObjCardFactory.setNonObjCardArray();
    protected List<ChatMessage> chatHistory;
    protected boolean chatMode = false;
    protected volatile boolean isMyTurn = true;
    protected volatile boolean isInThread = false;
    private final Label matchStatus= createLabel(String.valueOf(Status));
    private ChatArea chatArea;
    /**
     * Used to associate token colours to the respective images
     */
    private final HashMap<String,Image> colourToImage = new HashMap<>();
    Font jejuHallasanFont = Font.loadFont(getClass().getResourceAsStream("/JejuHallasan.ttf"), 20);

    public GUITemporary() {
        this.playerNum = 0;
        this.currentPlayer = null;
        this.thisPlayerNickname = null;
        this.gameID = 1258;
        this.currentEvent = null;
        this.commonObjCards = null;
        this.players = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.publicInfo = new HashMap<>();
        this.chatHistory = Collections.synchronizedList(new ArrayList<>());


        this.colourToImage.put("BLUE",new Image("/CODEX_pion_bleu.png",15,15,true,true));
        this.colourToImage.put("YELLOW",new Image("/CODEX_pion_jaune.png",15,15,true,true));
        this.colourToImage.put("BLACK", new Image("/CODEX_pion_noir.png",15,15,true,true));
        this.colourToImage.put("RED", new Image("/CODEX_pion_rouge.png",15,15,true,true));
        this.colourToImage.put("GREEN", new Image("CODEX_pion_vert.png",15,15,true,true));

    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        thisPlayerNickname = "player10000000000000";
        players=new ArrayList<>();
        players.add("player10000000000000");
        players.add("player2");
        publicInfo.put("player10000000000000",new PlayerPub("RED",0,new ArrayList<>(),new int[]{0,0,0,0,0,0,0},true));
        publicInfo.put("player2",new PlayerPub("GREEN",0,new ArrayList<>(),new int[]{0,0,0,0,0,0,0},true));
        publicInfo.put("player3",new PlayerPub("BLUE",0,new ArrayList<>(),new int[]{0,0,0,0,0,0,0},true));
        publicInfo.put("player4",new PlayerPub("YELLOW",0,new ArrayList<>(),new int[]{0,0,0,0,0,0,0},true));

        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(1250);
        primaryStage.setFullScreen(true);

        Pane preparationPhase = new Pane();
        preparationPhase.setPrefHeight(primaryStage.getHeight());
        preparationPhase.setPrefWidth(primaryStage.getWidth());
        preparationPhase.setBackground(new Background(new BackgroundFill(Color.rgb(246, 243, 228), new CornerRadii(0), new Insets(0))));
        preparationPhase.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179,0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(15))));

        Label labelID = createLabel("ID: " + gameID);
        HBox StatusBox = new HBox();
        Label statusTitle= createLabel("Status: ");
        StatusBox.getChildren().addAll(statusTitle,matchStatus);
        Label labelPlayerOrder = createLabel("Order: " + players);
        HBox topLine = new HBox();
        topLine.setSpacing(100);
        topLine.getChildren().addAll(labelID,StatusBox,labelPlayerOrder);
        topLine.translateXProperty().bind(preparationPhase.widthProperty().subtract(preparationPhase.widthProperty().subtract(20)));
        topLine.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(20)));
        preparationPhase.getChildren().add(topLine);

        VBox playerInfoPanel = createPlayerInfoPanel();
        playerInfoPanel.translateXProperty().bind(preparationPhase.widthProperty().subtract(preparationPhase.widthProperty().subtract(20)));
        playerInfoPanel.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(60)));
        preparationPhase.getChildren().addAll(playerInfoPanel);




        GridPane boardReal = new GridPane();
        ScrollPane board = new ScrollPane();
        //board.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179,0.35), new CornerRadii(0), new Insets(0))));
        board.setPrefSize(770, 525);
        boardReal.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179,0.35), new CornerRadii(0), new Insets(0))));
        ImageView card = new ImageView(new Image("/cards_front_075.png", 120, 80, true, false));
        card.setEffect(new DropShadow(20, Color.rgb(58, 33, 17)));
        ImageView card20 = new ImageView(new Image("/cards_front_065.png", 120, 80, true, false));
        int x=-1;
        int y=-1;


        board.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(50)));
        board.translateXProperty().bind(preparationPhase.widthProperty().subtract(board.widthProperty().add(20)));
        board.setContent(boardReal);
        boardReal.setOnScroll(e -> {
            e.consume();
            if (e.getDeltaY() == 0) {
                   return;
            }
            double scaleFactor = (e.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
            if(boardReal.getScaleX() * scaleFactor > 1 || boardReal.getScaleY() * scaleFactor > 1)
                return;
            boardReal.setScaleX(boardReal.getScaleX() * scaleFactor);
            boardReal.setScaleY(boardReal.getScaleY() * scaleFactor);
            });
        double[] dragPos = new double[2];
        boardReal.setOnMousePressed(e -> {
            dragPos[0]=e.getSceneX();
            dragPos[1]=e.getSceneY();
        });
        boardReal.setOnMouseDragged(e -> {
            boardReal.setTranslateX( e.getSceneX() - dragPos[0]);
            boardReal.setTranslateY( e.getSceneY() - dragPos[1]);

        });

        ImageView card1 = new ImageView(new Image("/cards_front_075.png", 120, 80, true, true));
        ImageView card2 = new ImageView(new Image("/cards_front_065.png", 120, 80, true, false));
        ImageView card3 = new ImageView(new Image("/cards_front_035.png", 120, 80, true, false));
        ImageView card4 = new ImageView(new Image("/cards_front_045.png", 120, 80, true, false));
        ImageView card5 = new ImageView(new Image("/cards_back_055.png", 120, 80, true, false));
        ImageView card6 = new ImageView(new Image("/cards_back_045.png", 120, 80, true, false));

        VBox deckArea = new VBox();
        deckArea.setSpacing(10);
        HBox resourceDeck = new HBox();
        resourceDeck.setSpacing(10);
        resourceDeck.getChildren().addAll(card5,card6,card2);
        HBox goldDeck = new HBox();
        goldDeck.setSpacing(10);
        goldDeck.getChildren().addAll(card3,card4,card1);
        deckArea.getChildren().addAll(goldDeck,resourceDeck);

        deckArea.translateXProperty().bind(preparationPhase.widthProperty().subtract(preparationPhase.widthProperty().subtract(40)));
        deckArea.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(deckArea.getHeight()+400)));
        ImageView card7 = new ImageView(new Image("/cards_front_024.png", 120, 80, true, true));
        ImageView card8 = new ImageView(new Image("/cards_front_028.png", 120, 80, true, false));
        ImageView card9 = new ImageView(new Image("/cards_front_030.png", 120, 80, true, false));
        ImageView card10 = new ImageView(new Image("/cards_front_035.png", 120, 80, true, false));
        ImageView card11 = new ImageView(new Image("/cards_front_045.png", 120, 80, true, false));
        ImageView card12 = new ImageView(new Image("/cards_front_055.png", 120, 80, true, false));

        TextArea notice = new TextArea();
        notice.setWrapText(true);
        notice.setPrefSize(380, 115);
        notice.setEditable(false);
        notice.setStyle("-fx-control-inner-background: #E6DEB3FF; -fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
        notice.appendText("Welcome to Codex Naturalis! \n");
        notice.translateXProperty().bind(preparationPhase.widthProperty().subtract(preparationPhase.widthProperty().subtract(40)));
        notice.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(notice.getHeight()+280)));
        HBox bottomLine = new HBox();
        bottomLine.setSpacing(10);
        bottomLine.getChildren().addAll(card7,card8,card9,card10,card11,card12);
        bottomLine.translateXProperty().bind(preparationPhase.widthProperty().subtract(bottomLine.widthProperty().add(20)));
        bottomLine.translateYProperty().bind(preparationPhase.heightProperty().subtract(bottomLine.heightProperty().add(20)));
        preparationPhase.getChildren().addAll(board,deckArea,bottomLine,chatArea.getChatArea(),notice);

        Label commonObjLabel = createLabel("Common Objective Cards");
        Label secretObjLabel = createLabel("Secret");
        Label handLabel = createLabel("Hand Cards");
        HBox cardLabels = new HBox();
        cardLabels.setSpacing(80);
        cardLabels.getChildren().addAll(commonObjLabel,secretObjLabel,handLabel);
        cardLabels.translateXProperty().bind(preparationPhase.widthProperty().subtract(bottomLine.widthProperty().add(10)));
        cardLabels.translateYProperty().bind(preparationPhase.heightProperty().subtract(bottomLine.heightProperty().add(50)));
        preparationPhase.getChildren().add(cardLabels);
        VBox selectionArea = setupInitialCardSideSelectionArea();

        preparationPhase.getChildren().add(selectionArea);

        Group root = new Group();
        root.setAutoSizeChildren(true);
        ImageView background = new ImageView(new Image("/popNotice.png"));
        background.setFitHeight(150);
        background.setFitWidth(150);
        Label noticeLabel = createLabel("It's your turn!");
        noticeLabel.setTranslateX(25);
        noticeLabel.setTranslateY(70);
        root.getChildren().addAll(background,noticeLabel);
        root.setTranslateX(375);
        root.setTranslateY(265);
        root.setEffect(new Glow(0));
        root.setEffect(new DropShadow(15, Color.rgb(236, 197, 123)));
        root.setOnMouseClicked(e -> {
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), root);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(1);
            rotateTransition.setAxis(Rotate.Y_AXIS);

            // Avvia la transizione di rotazione
            rotateTransition.play();
        });
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), root);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        preparationPhase.getChildren().add(root);
        rotateTransition.play();

        Button returnToMyField = createTransButton("Return to my field", 15, "#3A2111", 0, 0);
        returnToMyField.translateXProperty().bind(preparationPhase.widthProperty().subtract(200));
        returnToMyField.translateYProperty().bind(preparationPhase.heightProperty().subtract(preparationPhase.heightProperty().subtract(80)));
        returnToMyField.setVisible(true);
        preparationPhase.getChildren().add(returnToMyField);


        primaryStage.setScene(new Scene(preparationPhase));
        primaryStage.show();
    }
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 15px;-fx-font-family: 'JejuHallasan';");
        return label;
    }
    public VBox createPlayerInfoPanel() {

        HashMap<String,HBox> playerResources = new HashMap<>();
        VBox playerInfoGroup = new VBox();
        playerInfoGroup.setSpacing(10);
        for (String playerString : publicInfo.keySet()) {
            PlayerPub playerPub = publicInfo.get(playerString);
            String playerColour = playerPub.getColour();
            ImageView tokenImage = new ImageView(colourToImage.get(playerColour));
            Label playerName = createLabel(playerString);

            Image plantSymbol = new Image("kingdom_plant.png",15,15,true,false);
            Image fungiSymbol = new Image("kingdom_fungi.png",15,15,true,false);
            Image animalSymbol = new Image("kingdom_animal.png",15,15,true,false);
            Image insectSymbol = new Image("kingdom_insect.png",15,15,true,false);
            Image quillSymbol = new Image("kingdom_quill.png",15,15,true,false);
            Image inkwellSymbol = new Image("kingdom_inkwell.png",15,15,true,false);
            Image manuscriptSymbol = new Image("kingdom_manuscript.png",15,15,true,false);

            int [] resources = playerPub.getResources();

            Label plantCount = createLabel(String.valueOf(resources[0]));
            Label fungiCount = createLabel(String.valueOf(resources[1]));
            Label animalCount = createLabel(String.valueOf(resources[2]));
            Label insectCount = createLabel(String.valueOf(resources[3]));
            Label quillCount = createLabel(String.valueOf(resources[4]));
            Label inkwellCount = createLabel(String.valueOf(resources[5]));
            Label manuscriptCount = createLabel(String.valueOf(resources[6]));

            Label playerScore =createLabel(" Score: "+playerPub.getPoints());
            HBox playerInfo = new HBox();
            playerInfo.setSpacing(10);
            playerInfo.setMaxWidth(300);
            playerInfo.setMinWidth(300);
            playerInfo.getChildren().addAll(tokenImage, playerName);

            HBox playerResource = new HBox();
                playerResource.setSpacing(10);
                playerResource.getChildren().addAll(new Label("     "),new ImageView(plantSymbol), plantCount, new ImageView(fungiSymbol), fungiCount,
                        new ImageView(animalSymbol), animalCount, new ImageView(insectSymbol), insectCount,
                        new ImageView(quillSymbol), quillCount, new ImageView(inkwellSymbol), inkwellCount,
                        new ImageView(manuscriptSymbol), manuscriptCount);

            HBox playerInfoAndResource = new HBox();
            //((Label)((HBox)playerInfoAndResource.getChildren().get(0)).getChildren().get(0)).setText(" "+playerString);
            playerInfoAndResource.setSpacing(20);
            playerInfoAndResource.getChildren().addAll(playerInfo, playerScore);
            playerResources.put(playerString,playerInfoAndResource);
            playerInfoGroup.getChildren().addAll(playerInfoAndResource,playerResource);

        }
        return playerInfoGroup;
    }

    public VBox setupInitialCardSideSelectionArea() {
        VBox selectionArea = new VBox(); // Area containing everything

        Label promptLabel = createLabel("Choose a secret objective card:"); // Text label prompting user to pick card
        HBox cardPairArea = new HBox(); // Area at the bottom, containing the 2 cards to choose from

        ImageView firstCard = new ImageView(new Image("cards_back_019.png", 240, 160, true, false)); // Load the images of the cards to display
        ImageView secondCard = new ImageView(new Image("cards_back_027.png", 240, 160, true, false));

        firstCard.setOnMouseClicked(e -> {
            // Highlight the image once clicked
            Glow glow = new Glow();
            glow.setLevel(0.5);  // set the level of glow
            firstCard.setEffect(glow);

            firstCard.setOnMouseClicked(null); // Disable the card once selected
            secondCard.setOnMouseClicked(null); // Disable the other card
        });
        secondCard.setOnMouseClicked(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), secondCard);
            st.setByX(0.15);
            st.setByY(0.15);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.setOnFinished(event -> {
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(50.0);
                dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
                secondCard.setEffect(dropShadow);
            });
            st.play();

            firstCard.setOnMouseClicked(null);
            secondCard.setOnMouseClicked(null);
        });

        selectionArea.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179,0.35), new CornerRadii(0), new Insets(0))));
        selectionArea.setBorder(new Border(new BorderStroke(Color.rgb(230, 222, 179,0.2), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(30))));
        selectionArea.setTranslateX(700); // Set position of selectionArea
        selectionArea.setTranslateY(400);

        cardPairArea.setSpacing(20); // Add spacing between cards in the cardPairArea

        // Compose elements

        selectionArea.getChildren().addAll(promptLabel, cardPairArea);
        cardPairArea.getChildren().addAll(firstCard, secondCard);

        return selectionArea;
    }
    private Button createTransButton(String buttonName, int size, String color, int X, int Y) {
        Button button = new Button(buttonName);
        button.setStyle("-fx-background-color: transparent;-fx-text-fill:" + color + ";-fx-alignment: center;" +
                "-fx-font-size: " + size + "px;-fx-font-family: 'JejuHallasan';-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,10 );");
        button.setTranslateX(X);
        button.setTranslateY(Y);
        return button;
    }
}