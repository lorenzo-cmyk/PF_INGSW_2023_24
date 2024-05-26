package it.polimi.ingsw.am32.client.view.gui;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.NonObjCardFactory;
import it.polimi.ingsw.am32.client.ObjectiveCardFactory;
import it.polimi.ingsw.am32.client.PlayerPub;
import it.polimi.ingsw.am32.client.listener.AskListener;
import it.polimi.ingsw.am32.network.ClientNode.ClientNodeInterface;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static javafx.scene.layout.CornerRadii.*;

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


        this.colourToImage.put("BLUE",new Image("/CODEX_pion_bleu.png",20,20,true,true));
        this.colourToImage.put("YELLOW",new Image("/CODEX_pion_jaune.png",20,20,true,true));
        this.colourToImage.put("BLACK", new Image("/CODEX_pion_noir.png",20,20,true,true));
        this.colourToImage.put("RED", new Image("/CODEX_pion_rouge.png",20,20,true,true));
        this.colourToImage.put("GREEN", new Image("CODEX_pion_vert.png",20,20,true,true));

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

        primaryStage.setTitle("Codex Naturalis");
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(1200);
        primaryStage.setFullScreen(true);
        StackPane preparationPhase = new StackPane();
        preparationPhase.setBackground(new Background(new BackgroundFill(Color.rgb(246, 243, 228), new CornerRadii(0), new Insets(0))));

        Label labelID = createLabel("ID: " + gameID);
        HBox StatusBox = new HBox();
        Label statusTitle= createLabel("Status: ");
        StatusBox.getChildren().addAll(statusTitle,matchStatus);
        Label labelPlayerOrder = createLabel("Order: " + players);
        HBox topLine = new HBox();
        topLine.setSpacing(100);
        topLine.getChildren().addAll(labelID,StatusBox,labelPlayerOrder);
        topLine.setTranslateX(60);
        topLine.setTranslateY(20);
        preparationPhase.getChildren().add(topLine);

        VBox playerInfoPanel = createPlayerInfoPanel();
        playerInfoPanel.setTranslateX(20);
        playerInfoPanel.setTranslateY(50);
        preparationPhase.getChildren().addAll(playerInfoPanel);

        GridPane board = new GridPane();
        board.setBackground(new Background(new BackgroundFill(Color.rgb(230, 222, 179,0.35), new CornerRadii(0), new Insets(0))));
        board.setGridLinesVisible(true);
        board.setMaxSize(600, 500);
        ImageView card = new ImageView(new Image("/cards_front_075.png", 140, 80, true, false));
        board.add(card, 50, 50);
        board.setTranslateX(preparationPhase.getWidth() / 2 - board.getMaxWidth() / 2);
        chatArea = new ChatArea(0,0,250,100);
        chatArea.getChatArea().translateXProperty().bind(primaryStage.widthProperty().subtract(400));
        chatArea.getChatArea().translateYProperty().bind(primaryStage.heightProperty().subtract(200));
        preparationPhase.getChildren().addAll(board,chatArea.getChatArea());

        primaryStage.setScene(new Scene(preparationPhase));

        primaryStage.show();
    }
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #3A2111;-fx-alignment: center; -fx-font-size: 20px;-fx-font-family: 'JejuHallasan';");
        return label;
    }
    public VBox createPlayerInfoPanel() {
        HashMap<String,HBox> playerResources = new HashMap<>();
        for (String playerString : publicInfo.keySet()) {
            PlayerPub playerPub = publicInfo.get(playerString);
            String playerColour = playerPub.getColour();
            ImageView tokenImage = new ImageView(colourToImage.get(playerColour));
            Label playerName = createLabel(playerString);

            Image plantSymbol = new Image("kingdom_plant.png",20,20,true,false);
            Image fungiSymbol = new Image("kingdom_fungi.png",20,20,true,false);
            Image animalSymbol = new Image("kingdom_animal.png",20,20,true,false);
            Image insectSymbol = new Image("kingdom_insect.png",20,20,true,false);
            Image quillSymbol = new Image("kingdom_quill.png",20,20,true,false);
            Image inkwellSymbol = new Image("kingdom_inkwell.png",20,20,true,false);
            Image manuscriptSymbol = new Image("kingdom_manuscript.png",20,20,true,false);

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
            playerInfo.setOnMouseEntered(e -> {
                HBox playerResource = new HBox();
                playerResource.setSpacing(10);
                playerResource.getChildren().addAll(new ImageView(plantSymbol), plantCount, new ImageView(fungiSymbol), fungiCount,
                        new ImageView(animalSymbol), animalCount, new ImageView(insectSymbol), insectCount,
                        new ImageView(quillSymbol), quillCount, new ImageView(inkwellSymbol), inkwellCount,
                        new ImageView(manuscriptSymbol), manuscriptCount);
                playerInfo.getChildren().add(playerResource);
            });
            playerInfo.setOnMouseExited(e -> {
                playerInfo.getChildren().remove(playerInfo.getChildren().size()-1);
            });

            HBox playerInfoAndResource = new HBox();
            //((Label)((HBox)playerInfoAndResource.getChildren().get(0)).getChildren().get(0)).setText(" "+playerString);
            playerInfoAndResource.setSpacing(120);
            playerInfoAndResource.getChildren().addAll(playerInfo, playerScore);
            playerResources.put(playerString,playerInfoAndResource);

        }
        VBox playerInfoGroup = new VBox();
        playerInfoGroup.setSpacing(10);
        playerResources.forEach((k,v) -> {
            playerInfoGroup.getChildren().add(v);
        });
        return playerInfoGroup;
    }
}