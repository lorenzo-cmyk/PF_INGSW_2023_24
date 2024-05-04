package it.polimi.ingsw.am32.client.view.tui;

import it.polimi.ingsw.am32.Utilities.isValid;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.UI;
import it.polimi.ingsw.am32.client.listener.AskListener;
import it.polimi.ingsw.am32.message.ClientToServer.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.AnglesCovered;
import it.polimi.ingsw.am32.model.card.pointstrategy.CountResource;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;
import it.polimi.ingsw.am32.model.player.Colour;
import it.polimi.ingsw.am32.model.player.Player;
import it.polimi.ingsw.am32.network.ClientNodeInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TextUI extends UI implements Runnable {
    private static final Logger logger = LogManager.getLogger("TUILogger");
    ClientNodeInterface clientNode;
    private final Scanner in;
    private final PrintStream out;
    private String playerNickname;
    private int gameID;
    private int playerNum;
    private String currentPlayer;
    private Colour playerColour;
    private Event currentEvent;
    private Card[] commonObjCards;
    private ArrayList<Player> players;
    private ArrayList<NonObjectiveCard> hand;
    private final isValid isValid = new isValid();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final int INSECT = 0x1F98B;
    private static final int PLANT = 0x1F33F;
    private static final int FUNGI = 0x1F344;
    private static final int ANIMAL = 0x1F43A;
    private static final int EMPTY = 0x2B1C;
    private static final int QUILL = 0x1FAB6;
    private static final int INKWELL = 0X1F36F;
    private static final int MANUSCRIPT = 0x1F4DC;
    private static final int BLANK = 0x2800;

    /**
     * Constructor TUI
     */
    public TextUI() {
        super();
        in = new Scanner(System.in);
        out = new PrintStream(System.out);
        InitializeViewElement();
    }
    @Override
    public void launch(){
        run();
    }


    @Override
    public void run() {
        showWelcome();
        chooseConnection();
        //TODO
    }

    @Override
    public void InitializeViewElement() {
        this.playerNum=0;
        this.currentPlayer=null;
        this.playerNickname=null;
        this.gameID=0;
        this.playerColour=null;
        this.currentEvent=null;
        this.commonObjCards=null;
        this.players=new ArrayList<>();
        this.hand=new ArrayList<>();

    }
    //-------------------Connection-------------------
    public void chooseConnection() {
        out.println("""
                Choose the connection type:
                1. Socket
                2. RMI """);
        int connectionChoice= inputCheckInt();
        if(connectionChoice!=1 && connectionChoice!=2) {
            out.println("! Invalid input, please try again");
            chooseConnection();
        }
        switch (connectionChoice) {
            case 1: {
                out.println("Insert the server IP");
                in.nextLine();
                String ServerIP =inputCheckString();
                while (!isValid.isIpValid(ServerIP)) {
                    out.println("Invalid IP, please try again");
                    ServerIP=inputCheckString();
                }
                out.println("Insert the server port");
                int port = inputCheckInt();
                while (!isValid.isPortValid(port)) {
                    out.println("Invalid port, please try again");
                    port = inputCheckInt();
                }
                try {
                    setSocketClient(ServerIP, port);
                } catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            }
            case 2: {
                out.println("Insert the server URL");
                String ServerURL = in.nextLine();
                while (!isValid.isURLValid(ServerURL)) {
                    out.println("Invalid URL, please try again");
                    ServerURL = in.nextLine();
                }
                setRMIClient(ServerURL);
                break;
            }
            default: {
                break;
            }
        }
    }
    @Override
    public void setSocketClient(String ServerIP, int port) throws IOException {
        super.setSocketClient(ServerIP, port);
    }
    public void setRMIClient(String ServerURL){
        super.setRMIClient(ServerURL);
    }
    //-------------------Design-------------------
    @Override
    public void showWelcome() {
        System.out.println(
                """
                         ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗
                        ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝
                        ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗
                        ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║
                        ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║
                          ═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝""");
        System.out.println("Game rule:https://it.boardgamearena.com/link?url=https%3A%2F%2Fcdn.1j1ju.com%2Fmedias%2Fa7%2Fd7%2F66-codex-naturalis-rulebook.pdf&id=9212");//TODO change the URL of the rules with the real URL
    }
    
    @Override
    public void askSelectGameMode(){
        currentEvent= Event.SELECT_GAME_MODE;
        out.println("""
                Menu:
                1. Create new game
                2. Join game with game ID
                3. Reconnect game
                """);
        out.println("Which action do you want to perform?");
        int choice =inputCheckInt();
        handleEvent(currentEvent, choice);
    }
    @Override
    public void askNickname(){
        out.println("Insert the nickname you want to use in the game");
        playerNickname=inputCheckString();
    }
    @Override
    public void askCreateGame() {
        currentEvent= Event.CREATE_GAME;
        askNickname();
        out.println("Insert the number of players you want to play with");
        while (true) {
        playerNum=inputCheckInt();
        if (playerNum < 2 || playerNum > 4) {
            out.println("! Invalid number of players, please try again");
            in.nextInt();
            continue;
        }
        break;
        }
        notifyAskListenerLobby(new NewGameMessage(playerNickname, playerNum));
    }

    @Override
    public void handleEvent(Event event, int Choice) {

    }

    @Override
    public void askJoinGame() {
        currentEvent= Event.JOIN_GAME;
        out.println("Insert the nickname you want to use in the game");
        playerNickname=inputCheckString();
        out.println("Insert the Access ID of the game you want to join");
        gameID=inputCheckInt();
        notifyAskListenerLobby(new AccessGameMessage(gameID, playerNickname));
    }
    @Override
    public void askReconnectGame() {
        currentEvent= Event.RECONNECT_GAME;
        notifyAskListener(new RequestGameStatusMessage(playerNickname));// don't need to ask the player nickname because it is already stored
    }
    //-------------------Game-------------------
    public void showCardSelected(Card card){
        out.println("The card selected is: " + card.getId());
    }
    @Override
    public void askPlaceCard() {
        currentEvent= Event.PLACE_CARD;
        out.println("""
                Insert the card you want to place" 
                "1. " + hand.get(1).getId()  
                "2. " + hand.get(1).getId()
                "3. " + hand.get(1).getId()""");
        //TODO change the code to show all the cards in the hand
        int cardID =inputCheckInt();
        out.println("""
                Which side do you want to place the card?
                1. Front
                2. Back
                """);
        //TODO show the card selected by the player: front side and back side
        int cardSide =inputCheckInt();
        //TODO
    }

    //-------------------Card Factory-------------------
    private void printCard(NonObjectiveCard card) {
        String kingdom = card.getKingdom().toString();
        int[] cardConditionCount = card.getConditionCount();
        String strategy = iconStrategy(card.getPointStrategy());
        String condition= icon(card.getConditionCount());
        int padding1 = (16 - strategy.length()) / 2;
        int padding2 = (18 - kingdom.length()) / 2;
        int padding3 = (17 - condition.length()) / 2;
        String colour = null;
        switch (card.getKingdom()) {
            case PLANT -> colour = ANSI_GREEN;
            case FUNGI -> colour = ANSI_RED;
            case ANIMAL -> colour = ANSI_BLUE;
            case INSECT -> colour = ANSI_PURPLE;
        }

        out.printf(colour + "+----+-----------------------+\n" + ANSI_RESET);
        out.printf(colour + "| %s | %" + padding1 + "s%s%" + padding1 + "s | %s |\n" + ANSI_RESET, icon(card.getTopRight()), "", strategy, "", icon(card.getTopLeft()));
        out.printf(colour + "+----+%18s+----+\n", "");
        out.printf(colour + "| %4s %" + padding2 + "s%s%" + padding2 + "s%4s |\n" + ANSI_RESET, "", "", kingdom, "", "");
        out.printf(colour + "+----+%18s+----+\n" + ANSI_RESET, "");
        out.printf(colour + "| %s |%"+padding3+"s%s%"+padding3 +"s | %s |\n" + ANSI_RESET,icon(card.getBottomRight()),"", icon(card.getConditionCount()), "",icon(card.getBottomLeft()));
        out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
    }

    public String icon(int[] conditionCount) {
        String conditionIcon[] = new String[5];
        String condition = "";
        int count = 0;
        for (int i = 0; i < conditionCount.length; i++) {
            if (conditionCount[i] != 0) {
                for (int j = 0; j < conditionCount[i]; j++) {
                    conditionIcon[count] = icon(CornerType.values()[i]);
                    condition=conditionIcon[count]+condition;
                }
            }
        }
        for (int j = 0; j < 5; j++) {
            if (conditionIcon[j] == null) {
                conditionIcon[j] = new String(Character.toChars(BLANK));
            }
        }
        return condition;
    }



    protected static String icon(CornerType type) {
        String icon = "";
        switch (type) {
            case INSECT -> icon= new String(Character.toChars(INSECT));
            case PLANT ->  icon= new String(Character.toChars(PLANT));
            case FUNGI -> icon= new String(Character.toChars(FUNGI));
            case ANIMAL ->  icon= new String(Character.toChars(ANIMAL));
            case QUILL -> icon= new String(Character.toChars(QUILL));
            case INKWELL ->  icon= new String(Character.toChars(INKWELL));
            case MANUSCRIPT -> icon= new String(Character.toChars(MANUSCRIPT));
            default -> icon= new String(Character.toChars(EMPTY));
        }
        return icon;
    }
    protected static String icon(ObjectType type) {
        String icon = "";
        switch (type) {
            case INSECT -> icon= new String(Character.toChars(INSECT));
            case PLANT ->  icon= new String(Character.toChars(PLANT));
            case FUNGI -> icon= new String(Character.toChars(FUNGI));
            case ANIMAL ->  icon= new String(Character.toChars(ANIMAL));
            case QUILL -> icon= new String(Character.toChars(QUILL));
            case INKWELL ->  icon= new String(Character.toChars(INKWELL));
            case MANUSCRIPT -> icon= new String(Character.toChars(MANUSCRIPT));
            default -> icon= new String(Character.toChars(EMPTY));
        }
        return icon;
    }
    public String iconStrategy(PointStrategy strategy){
        String icon = "";
        if (strategy instanceof CountResource){
            icon= ((CountResource) strategy).getCount()+"|"+icon(((CountResource) strategy).getType());
        }
        if (strategy instanceof AnglesCovered){
            icon= "2"+"|"+"AngCover";
        }
        return icon;
    }

    //-------------------utilities-------------------
    @Override
    public void handleEvent(Event event) {
        //TODO
    }

    @Override
    public void handleChoiceEvent(Event event, int choice){
        switch (event) {
            case SELECT_GAME_MODE -> {
                switch (choice) {
                    case 1 -> askCreateGame();
                    case 2 -> askJoinGame();
                    case 3 -> askReconnectGame();
                    default -> out.println("! Invalid input, please try again ");
                }
            }
            case CREATE_GAME -> System.out.println("Creating game...");
            case JOIN_GAME -> System.out.println("Joining game...");
            case RECONNECT_GAME -> System.out.println("Reconnecting game...");
            case PLACE_CARD -> System.out.println("Placing card...");
            case DRAW_CARD -> System.out.println("Drawing card...");
        }

    }
    private String inputCheckString() {
        String input;
        while (true) {
            try {
                input = in.nextLine();
            } catch (InputMismatchException e) {
                out.println("! Invalid input, please try again");
                continue;
            }
            break;
        }
        return input;
    }
    private int inputCheckInt() {
        int input;
        while (true) {
            try {
                input = in.nextInt();
            } catch (InputMismatchException e) {
                out.println("! Invalid input, please try again");
                in.nextLine();
                continue;
            }
            break;
        }
        return input;
    }

    private void clearCMD() {
        try{
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    @Override
    public void updateView(StoCMessage message) {

    }

    @Override
    public void notifyAskListener(CtoSMessage message) {
        //TODO
    }
    @Override
    public void notifyAskListenerLobby(CtoSLobbyMessage message) {
        //TODO
    }


}






