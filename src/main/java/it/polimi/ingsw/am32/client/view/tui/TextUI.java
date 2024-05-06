package it.polimi.ingsw.am32.client.view.tui;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.client.Event;
import it.polimi.ingsw.am32.client.UI;
import it.polimi.ingsw.am32.message.ClientToServer.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import it.polimi.ingsw.am32.message.ServerToClient.StoCMessage;
import it.polimi.ingsw.am32.message.ClientToServer.CtoSMessage.*;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.*;
import it.polimi.ingsw.am32.model.player.Colour;
import it.polimi.ingsw.am32.model.player.Player;
import it.polimi.ingsw.am32.network.ClientNodeInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class TextUI is a one of the two User Interface of the game that allows the player to interact with the game, which
 * is a text-based interface. It extends the abstract {@link UI} class and implements the{@link Runnable}interface.
 * <p>
 *     The class includes the following methods, which could be divided into three categories: connection, flow of the
 *     game, and design of printed elements. The connection methods are used to establish the connection between the
 *     client and the server. The flow of the game methods are used to manage the game's flow, such as creating a new game,
 *     joining a game, and reconnecting to a game etc. The design methods are used to print the game's elements, such as cards.
 *     To simplify the code, the class includes a method to check the input of the player, a method to clear the command
 *     line, and a method to handle the events. Finally, the class includes a method to update the view and a method to
 *     notify the listener.
 * <p>
 *     For the connection, the class includes a method to choose the connection type, a method to set the socket client
 *     and a method to set the RMI client. Also, the class uses the {@link IsValid} class to check the validity of the
 *     IP address and the port number entered by the player.
 *     For the design of the cards, the class includes a method to print the card, a method to convert the corner type
 *     of the card to an icon and a method to convert the object type of the card to an icon as well. The class uses
 *     Unicode characters to represent the icons of the cards and the objects. In addition, the class
 *     includes ASCI escape codes to set the color of the printed cards and the printed text.
 *
 * @author Jie
 *
 */
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
    private NonObjectiveCard starterCard;
    private final IsValid isValid = new IsValid();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final int INSECT = 0x1F98B;
    private static final int PLANT = 0x1F33F;
    private static final int FUNGI = 0x1F344;
    private static final int ANIMAL = 0x1F43A;
    private static final int QUILL = 0x1FAB6;
    private static final int INKWELL = 0X1F36F;
    private static final int MANUSCRIPT = 0x1F4DC;
    private static final int NON_COVERABLE = 0x274C;
    private static final String EMPTY = "  ";

    /**
     * Constructor of the class TextUI
     */
    public TextUI() {
        super();
        in = new Scanner(System.in);
        out = new PrintStream(System.out);
        InitializeViewElement();
    }
    /**
     * Method that launches the TextUI
     */
    @Override
    public void launch(){
        run();
    }

    /**
     * //TODO
     */
    @Override
    public void run() {
        showWelcome();
        chooseConnection();
        //TODO
    }

    /**
     * Method that initializes the view elements with default values to avoid null pointer exceptions and other errors.
     */
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
    /**
     * Method that allows the player to choose the connection type, either socket or RMI. The method asks the player to
     * insert the server IP and the server port if the player chooses the socket connection. If the player chooses the
     * RMI connection, the method asks the player to insert the server URL.
     * The method uses the {@link IsValid} class to check the validity of the IP address and the port number entered by the player.
     * @throws InputMismatchException if the input is mismatched
     * @throws IOException if an I/O error occurs
     * @see IsValid
     * @see #setSocketClient(String, int)
     * @see #setRMIClient(String)
     * @see #inputCheckInt()
     * @see #inputCheckString()
     * @see #showWelcome()
     */
    @Override
    public void chooseConnection() {
        out.println("""
                Choose the connection type:
                1. Socket
                2. RMI""");
        int connectionChoice= inputCheckInt(); // read the input from the player and check if the input is mismatched or not
        switch (connectionChoice) { // if the input is 1, set the socket client, if the input is 2, set the RMI client
            case 1: {
                out.println("Insert the server IP");
                String ServerIP =inputCheckString(); // read the input from the player and check if the input is mismatched or not
                while (!isValid.isIpValid(ServerIP)) { // if the IP address is invalid, print the error message and ask the player to re-enter the IP address
                    out.println("Invalid IP, please try again");
                    ServerIP=inputCheckString();
                }
                out.println("Insert the server port");
                int port = inputCheckInt();
                while (!isValid.isPortValid(port)) { // if the port number is invalid, print the error message and ask the player to re-enter the port number
                    out.println("Invalid port, please try again");
                    port = inputCheckInt();
                }
                try {
                    setSocketClient(ServerIP, port); // set the socket client
                } catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            }
            case 2: {
                out.println("Insert the server URL"); //TODO show the format of the URL to the player
                String ServerURL = in.nextLine(); //
                while (!isValid.isURLValid(ServerURL)) {
                    out.println("Invalid URL, please try again");
                    ServerURL = in.nextLine();
                }
                setRMIClient(ServerURL);// set the RMI client
                break;
            }
            default: {
                logger.error("Invalid input, please select 1 or 2");
                out.println("Invalid input, please select 1 or 2"); // if the input is neither 1 nor 2, print the error message and ask the player to re-enter the input
                chooseConnection();
            }
        }
    }
    /**
     * Method that sets the socket client with the server IP and the server port entered by the player and attempts to
     * establish the connection between the client and the server.
     * @param ServerIP the server IP entered by the player
     * @param port the server port entered by the player
     * @throws IOException if an I/O error occurs
     * @see UI#setSocketClient(String, int)
     */
    @Override
    public void setSocketClient(String ServerIP, int port) throws IOException {
        super.setSocketClient(ServerIP, port); // see the method in the superclass
    }
    /**
     * Method that sets the RMI client with the server URL entered by the player and attempts to establish the connection
     * between the client and the server.
     * @param ServerURL the server URL entered by the player
     * @see UI#setSocketClient(String, int)
     */
    @Override
    public void setRMIClient(String ServerURL){
        super.setRMIClient(ServerURL); // see the method in the superclass
    }
    //-------------------Title-------------------
    /**
     * Method that prints the welcome message and link to the game rules.
     */
    @Override
    public void showWelcome() {
        out.println(
                """
                         ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗
                        ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝
                        ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗
                        ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║
                        ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║
                          ═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝""");
        out.println("Game rule:https://it.boardgamearena.com/link?url=https%3A%2F%2Fcdn.1j1ju.com%2Fmedias%2Fa7%2Fd7%2F66-codex-naturalis-rulebook.pdf&id=9212");//TODO change the URL of the rules with the real URL
    }
    //-------------------Game mode-------------------
    /**
     * The method uses the {@link Event} enum to set the current event to select the game mode. The method prints the
     * menu of the game mode and asks the player to select the action to perform using the {@link #handleChoiceEvent} method.
     * @see #inputCheckInt()
     */
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
        handleChoiceEvent(currentEvent, choice);
    }
    /**
     * Method that asks the player to insert the nickname they want to use in the game.
     * @see #inputCheckString()
     */
    @Override
    public void askNickname(){
        out.println("Insert the nickname you want to use in the game");
        playerNickname=inputCheckString();
    }

    /**
     * Method that asks the player to insert the number of players and the nickname desired to create a new game.
     * @see #inputCheckInt()
     * @see #askNickname()
     * @see #notifyAskListenerLobby(CtoSLobbyMessage)
     * @see NewGameMessage
     *
     */
    @Override
    public void askCreateGame() {
        currentEvent= Event.CREATE_GAME;
        askNickname();
        out.println("Insert the number of players you want to play with");
        while (true) {
        playerNum=inputCheckInt();
        if (playerNum < 2 || playerNum > 4) {
            out.println("Invalid number of players, please insert a number between 2 and 4");
            in.nextInt();
            continue;
        }
        break;
        }
        notifyAskListenerLobby(new NewGameMessage(playerNickname, playerNum));// notify the listener with the new game message
        //TODO wait for the response from the server
    }
    /**
     * Method that asks the player to insert the nickname they want to use in the game and the Access ID of the game they
     * want to join.
     * @see #inputCheckString()
     * @see #inputCheckInt()
     */
    @Override
    public void askJoinGame() {
        currentEvent= Event.JOIN_GAME;
        out.println("Insert the nickname you want to use in the game");
        playerNickname=inputCheckString();
        out.println("Insert the Access ID of the game you want to join");
        gameID=inputCheckInt();
        notifyAskListenerLobby(new AccessGameMessage(gameID, playerNickname)); // notify the listener with the access game message
        //TODO wait for the response from the server
    }
    /**
     * Use this method to ask the player if they want to reconnect to the game.
     * @see #inputCheckString()
     */
    @Override
    public void askReconnectGame() {
        currentEvent= Event.RECONNECT_GAME;
        notifyAskListener(new RequestGameStatusMessage(playerNickname));// don't need to ask the player nickname because it is already stored
        //TODO wait for the response from the server
    }
    //-------------------Game start-----------------------
    @Override
    public void showInitialView() {
        //TODO show players colour, show the empty field view, show the list of the players
    }
    @Override
    public void showHelpInfo(){
        //TODO show the help information: exit command, start chat command, view the player list command, view the
        // game status command, view the game rule command,view the card command, view other players' field command,
        // view the secret objective command, view the game order command, view the game ID command ecc.
        //TODO NEED TO DICUSS WITH THE TEAM
    }
    /**
     * Method that asks the player to select the side of the starter card received from the server.
     */
    @Override
    public void requestSelectStarterCardSide(){
        currentEvent= Event.SELECT_STARTER_CARD_SIDE;
        //after receiving the starter card from the server and store it in the starterCard
        out.println("The starter card received has:");
        out.println("Front side");
        printNonObjCard(starterCard, true);
        out.println("Back side");
        printNonObjCard(starterCard, false);
        out.println("""
                Select the side of the starter card
                1. Front
                2. Back
                """);
        int side =inputCheckInt();
        handleChoiceEvent(currentEvent, side);
        boolean isUP = side == 1;
        notifyAskListener(new SelectedStarterCardSideMessage(playerNickname,isUP));
        //TODO wait for the response from the server
    }
    @Override
    public void requestSelectSecretObjCard() {

    }
    @Override
    public void requestPlaceCard() {
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







    //-------------------View of the game-------------------
    public void showCardSelected(Card card){
        out.println("The card selected is: " + card.getId());
        //TODO show the card selected by the player
    }


    //-------------------Card Factory-------------------
    /**
     * Method used to print the card given with the side selected by the player, using the Unicode characters to represent
     * the elements and using the ASCI to set the color of the printed card.
     * @param card the card to be printed
     * @param isUp the side of the card selected by the player
     *             true: front side, false: back side
     * @see #icon(CornerType)
     * @see #icon(ObjectType)
     * @see #iconStrategy(PointStrategy)
     * @see #icon (int[])
     */
    private void printNonObjCard(NonObjectiveCard card,boolean isUp) {
        String kingdom = String.valueOf(card.getKingdom()); // get the kingdom of the card and convert it to a string
        String strategy = iconStrategy(card.getPointStrategy()); // stabilize the way to present different strategies
        String requirements = icon(card.getConditionCount()); // stored the requirements of the card in one string
        String permanent = icon(card.getPermRes()); // stored the permanent resources of the card in one string
        int padding1 = (18 - strategy.length()) / 2; // calculate the padding
        int padding2 = (16 - requirements.length()) / 2;
        int padding3 = (28 - permanent.length()) / 2;
        if(kingdom.equals("null")) {
            kingdom = "STARTER";
        }
        int padding4= 26-kingdom.length();
        String colour = null;
        switch (kingdom) { // set the color of the card based on the kingdom of the card
            case "PLANT"-> colour = ANSI_GREEN;
            case "FUNGI" -> colour = ANSI_RED;
            case "ANIMAL" -> colour = ANSI_BLUE;
            case "INSECT" -> colour = ANSI_PURPLE;
            case "STARTER" -> colour = ANSI_RESET;
        }
        // print the card based on the side
        if (isUp) { // if the side is the front side
            out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
            out.printf(colour + "| %s |%" + padding1 + "s%s%" + padding1 + "s| %s |\n" + ANSI_RESET, icon(card.getTopLeft()), "", strategy, "", icon(card.getTopRight())); // print topRight corner, strategy, and topLeft corner of the card.
            out.printf(colour + "+----+%18s+----+\n", "");
            out.printf(colour + "| %s %"+padding4+"s|\n" + ANSI_RESET, kingdom,"");
            out.printf(colour + "+----+%18s+----+\n" + ANSI_RESET, "");
            if(requirements.length()<3) { // adjust the padding based on the length of the requirements, because the unicode characters have different visual width
                out.printf(colour + "| %s | %" + padding2 + "s%s%" + padding2 + "s | %s |\n" + ANSI_RESET, icon(card.getBottomLeft()), "", requirements, "", icon(card.getBottomRight())); // print bottomRight corner, requirements, and bottomLeft corner of the card.
            }
            else {
                out.printf(colour + "| %s | %" + padding2 + "s%s%" + padding2 + "s| %s |\n" + ANSI_RESET, icon(card.getBottomLeft()), "", requirements, "", icon(card.getBottomRight()));
            }
            out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
        } else { // if the side is the back side
            if (!kingdom.equals("STARTER")) {
                out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
                out.printf(colour + "|%4s|%18s|%4s|\n" + ANSI_RESET, "", "", "");
                out.printf(colour + "+----+%18s+----+\n", "");
                out.printf(colour + "|%" + padding3 + "s%s%" + padding3 + "s|\n" + ANSI_RESET, "",permanent, ""); // print the permanent resources of the back side of the card
                out.printf(colour + "+----+%18s+----+\n" + ANSI_RESET, "");
                out.printf(colour + "|%4s|%18s|%4s|\n" + ANSI_RESET, "", "", "");
                out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
            } else {
                out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
                out.printf(colour + "| %s |%18s| %s |\n" + ANSI_RESET, icon(card.getTopLeftBack()),"", icon(card.getTopRightBack()));
                out.printf(colour + "+----+%18s+----+\n", "");
                out.printf(colour + "|%" + padding3 + "s%s%" + padding3 + "s|\n" + ANSI_RESET, "", permanent, "");
                out.printf(colour + "+----+%18s+----+\n" + ANSI_RESET, "");
                out.printf(colour + "| %s |%18s| %s |\n" + ANSI_RESET, icon(card.getBottomLeftBack()), "", icon(card.getBottomRightBack()));
                out.printf(colour + "+----+------------------+----+\n" + ANSI_RESET);
            }
        }
    }
    private void printObjCard(Card card){
        //TODO
    }

    /**
     * Method used to convert the integer array of the condition count of the card to a string of icons, using the Unicode
     * characters and added it in one string.
     * @param conditionCount the integer array of the requirement count of the card.
     * @return the string of icons which contains the icons of the requirements of the card.
     */
    private String icon(int[] conditionCount) {
        StringBuilder condition= new StringBuilder();
        for (int i = 0; i < conditionCount.length; i++) {
            if (conditionCount[i] != 0) {
                for (int j = 0; j < conditionCount[i]; j++) {
                    condition.insert(0, icon(CornerType.values()[i]));
                }
            }
        }
        return condition.toString();
    }
    /**
     * Method used to convert the corner type of the card to an icon, using the Unicode characters.
     * @param type the corner type of the card
     * @return the icon of the corner type of the card
     * @see CornerType
     */
    private static String icon(CornerType type) {
        String icon;
        switch (type) {
            case INSECT -> icon= new String(Character.toChars(INSECT));
            case PLANT ->  icon= new String(Character.toChars(PLANT));
            case FUNGI -> icon= new String(Character.toChars(FUNGI));
            case ANIMAL ->  icon= new String(Character.toChars(ANIMAL));
            case QUILL -> icon= new String(Character.toChars(QUILL));
            case INKWELL ->  icon= new String(Character.toChars(INKWELL));
            case MANUSCRIPT -> icon= new String(Character.toChars(MANUSCRIPT));
            case NON_COVERABLE -> icon= new String(Character.toChars(NON_COVERABLE));
            default -> icon= EMPTY;
        }
        return icon;
    }

    /**
     * Method used to convert the object type of the card to an icon, using the Unicode characters.
     * @param type the object type of the card
     * @return the icon of the object type of the card
     * @see ObjectType
     */
    private static String icon(ObjectType type) {
        String icon = "";
        switch (type) {
            case INSECT -> icon= new String(Character.toChars(INSECT));
            case PLANT ->  icon= new String(Character.toChars(PLANT));
            case FUNGI -> icon= new String(Character.toChars(FUNGI));
            case ANIMAL ->  icon= new String(Character.toChars(ANIMAL));
            case QUILL -> icon= new String(Character.toChars(QUILL));
            case INKWELL ->  icon= new String(Character.toChars(INKWELL));
            case MANUSCRIPT -> icon= new String(Character.toChars(MANUSCRIPT));
        }
        return icon;
    }
    /**
     * Method used to set how to present the different strategies of the card.
     * @param strategy the point strategy of the card.
     * @return the way to present the strategy of the card.
     * @see PointStrategy
     */
    private String iconStrategy(PointStrategy strategy){
        String icon = "";
        if (strategy instanceof CountResource){
            icon= ((CountResource) strategy).getCount()+"|"+icon(((CountResource) strategy).getType());
        }
        if (strategy instanceof AnglesCovered){
            icon= "2"+"|"+"AngCover";
        }
        if (strategy instanceof Empty){
            icon= "";
        }
        if (strategy instanceof Diagonals){
            //TODO
        }
        if (strategy instanceof LConfigurationOne){
            //TODO
        }
        if (strategy instanceof LConfigurationTwo){
            //TODO
        }
        if (strategy instanceof LConfigurationThree){
            //TODO
        }
        if (strategy instanceof LConfigurationFour){
            //TODO
        }
        if (strategy instanceof AllSpecial){
            //TODO
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
                    default -> {
                        logger.error("Invalid input, please select 1, 2 or 3");
                        out.println("Invalid input, please select 1, 2 or 3");
                        askSelectGameMode();
                    }
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






