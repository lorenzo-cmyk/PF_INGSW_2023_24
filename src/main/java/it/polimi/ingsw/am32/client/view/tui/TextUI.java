package it.polimi.ingsw.am32.client.view.tui;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.*;
import it.polimi.ingsw.am32.message.ClientToServer.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class TextUI is a one of the two User Interface of the game that allows the player to interact with the game, which
 * is a text-based interface. It extends the abstract {@link View} class and implements the{@link Runnable}interface.
 * <p>
 *     The class includes the following methods, which could be divided into three categories: connection, flow of the
 *     game, and design of printed elements. The connection methods are used to establish the connection between the
 *     client and the server. The flow of the game methods are used to manage the game's flow, such as creating a new
 *     game, joining a game, and reconnecting to a game etc. The design methods are used to print the game's elements,
 *     such as cards.
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
 * //FIXME need to update all javadoc
 *
 */
public class TextUI extends View implements Runnable {
    private static final Logger logger = LogManager.getLogger("TUILogger");
    private final Scanner in;
    private final PrintStream out;
    private final IsValid isValid = new IsValid();
    private HashMap<String, BoardView> boards;
    private static final int ANIMALCARD = 0X1F7E6; // Unicode for the card
    private static final int PLANTCARD = 0X1F7E9;
    private static final int FUNGICARD = 0X1F7E5;
    private static final int INSECARD = 0X1F7EA;
    private static final int STARTERCARD = 0x1F7EB;
    private static final String ANSI_RESET = "\u001B[0m"; // ANSI escape code to set the color
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String INSECT = "\uD83E\uDD8B";   // Unicode for the corner type, object and resource of the card
    private static final String PLANT = "\uD83C\uDF3F";
    private static final String FUNGI = "\uD83C\uDF44";
    private static final String ANIMAL = "\uD83D\uDC3A";
    private static final String QUILL = "\uD83E\uDEB6";
    private static final String INKWELL = "\uD83C\uDF6F";
    private static final String MANUSCRIPT = "\uD83D\uDCDC";
    private static final String NON_COVERABLE = "\u274C";
    private static final String EMPTY = "  ";

    /**
     * Constructor of the class TextUI
     */
    public TextUI() {
        super();
        in = new Scanner(System.in);
        out = new PrintStream(System.out);
        boards = new HashMap<>();
    }

    /**
     * Method that launches the TextUI
     */
    @Override
    public void launch() {
        showWelcome();
        chooseConnection();
        askSelectGameMode();
    }

    //-------------------Connection-------------------

    /**
     * Method that allows the player to choose the connection type, either socket or RMI. The method asks the player to
     * insert the server IP and the server port if the player chooses the socket connection. If the player chooses the
     * RMI connection, the method asks the player to insert the server URL.
     * The method uses the {@link IsValid} class to check the validity of the IP address and the port number entered by
     * the player.
     *
     * @throws InputMismatchException if the input is mismatched
     * @see IsValid
     * @see #setSocketClient(String, int)
     * @see #setRMIClient(String)
     * @see #showWelcome()
     */
    @Override
    public void chooseConnection() {
        logger.info("Enter chooseConnection");
        out.println("""
                Choose the connection type:
                1. Socket
                2. RMI""");
        int connectionChoice = getInputInt();
        boolean isConnected = false;
        switch (connectionChoice) { // if the input is 1, set the socket client, if the input is 2, set the RMI client
            case 1: {
                String ServerIP;
                out.println("Insert the server IP");
                in.nextLine();// read the input from the player
                ServerIP = getInput();
                while (!isValid.isIpValid(ServerIP)) {
                    out.println("Invalid IP, please try again");
                    ServerIP = in.nextLine();
                }// if the IP address is invalid, print the error message and ask the player to re-enter the IP address
                out.println("Insert the server port");
                int port = getInputInt();
                // if the port number is invalid, print the error message and ask the player to re-enter the port number
                while (!isValid.isPortValid(port)) {
                    out.println("Invalid port, please try again");
                    in.nextLine();
                    port = getInputInt();
                }
                try {
                    setSocketClient(ServerIP, port); // set the socket client
                    isConnected = true;
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
                isConnected = true;
                break;
            }
            default: {
                // if the input is neither 1 nor 2, print the error message and ask the player to re-enter the input
                logger.info("Invalid input, please select 1 or 2");
                out.println("Invalid input, please select 1 or 2");
                chooseConnection();
                break;
            }
        }
        if (!isConnected) {
            out.println("Connection failed, please try again");
            chooseConnection();
        }
        logger.info("Out chooseConnection");
    }

    /**
     * Method that sets the socket client with the server IP and the server port entered by the player and attempts to
     * establish the connection between the client and the server.
     *
     * @param ServerIP the server IP entered by the player
     * @param port     the server port entered by the player
     * @throws IOException if an I/O error occurs
     * @see View#setSocketClient(String, int)
     */
    @Override
    public void setSocketClient(String ServerIP, int port) throws IOException {
        super.setSocketClient(ServerIP, port); // see the method in the superclass
        Thread thread = new Thread((clientNode));
        thread.start();
        Thread askListener = new Thread(super.askListener);
        askListener.start();
    }

    /**
     * Method that sets the RMI client with the server URL entered by the player and attempts to establish the
     * connection between the client and the server.
     *
     * @param ServerURL the server URL entered by the player
     * @see View#setSocketClient(String, int)
     */
    @Override
    public void setRMIClient(String ServerURL) {
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
        out.println("Game rule:https://it.boardgamearena.com/link?url=https%3A%2F%2Fcdn.1j1ju.com%2Fmedias%2Fa7%2Fd7%2F66-codex-naturalis-rulebook.pdf&id=9212");
    }
    //-------------------Game mode-------------------

    /**
     * The method uses the {@link Event} enum to set the current event to select the game mode. The method prints the
     * menu of the game mode and asks the player to select the action to perform using the {@link #handleChoiceEvent}
     * method.
     */
    @Override
    public void askSelectGameMode() { //From this method, the player can select to use the command Service
        logger.info("Enter askSelectGameMode");
        currentEvent = Event.SELECT_GAME_MODE;
        out.println("""
                Menu:
                1. Create new game
                2. Join game with game ID
                3. Reconnect game
                """);
        out.println("Which action do you want to perform?");
        in.nextLine();
        String choice = getInput();
        switch (choice) {
            case "1" -> askCreateGame();
            case "2" -> askJoinGame();
            case "3" -> askReconnectGame();
            default -> {
                logger.info("Invalid input, please select 1, 2 or 3");
                out.println("Invalid input, please select 1, 2 or 3");
                askSelectGameMode();
            }
        }
        logger.info("Out askSelectGameMode");
    }

    /**
     * Method that asks the player to insert the nickname they want to use in the game.
     */
    @Override
    public void askNickname() {
        out.println("Insert the nickname you want to use in the game");
        thisPlayerNickname = getInput();
    }

    /**
     * Method that asks the player to insert the number of players and the nickname desired to create a new game.
     *
     * @see #askNickname()
     * @see #notifyAskListenerLobby(CtoSLobbyMessage)
     * @see NewGameMessage
     */
    @Override
    public void askCreateGame() {
        logger.info("Enter askCreateGameMode");
        currentEvent = Event.CREATE_GAME;
        askNickname();
        out.println("Insert the number of players you want to play with");
        while (true) {
            playerNum = Integer.parseInt(getInput());
            if (playerNum < 2 || playerNum > 4) {
                out.println("Invalid number of players, please insert a number between 2 and 4");
                playerNum = Integer.parseInt(getInput());
                continue;
            }
            break;
        } // notify the listener with the new game message
        notifyAskListenerLobby(new NewGameMessage(thisPlayerNickname, playerNum));
        logger.info("Out askCreateGameMode");
    }

    /**
     * Method that asks the player to insert the nickname they want to use in the game and the Access ID of the game
     * they want to join.
     */
    @Override
    public void askJoinGame() {
        currentEvent = Event.JOIN_GAME;
        out.println("Insert the nickname you want to use in the game");
        thisPlayerNickname =getInput();
        out.println("Insert the Access ID of the game you want to join");
        gameID = Integer.parseInt(getInput());
        // notify the listener with the access game message
        notifyAskListenerLobby(new AccessGameMessage(gameID, thisPlayerNickname));
    }

    /**
     * Use this method to ask the player if they want to reconnect to the game.
     */
    @Override
    public void askReconnectGame() {
        currentEvent = Event.RECONNECT_GAME;
        // don't need to ask the player nickname because it is already stored
        notifyAskListener(new RequestGameStatusMessage(thisPlayerNickname));
        //TODO wait for the response from the server
    }

    @Override
    public void updateNewGameConfirm(int gameID, String recipientNickname) {
        logger.info("Received the NewGameConfirmationMessage from the server");
        // once received the NewGameConfirmationMessage from the server
        this.gameID = gameID;
        this.thisPlayerNickname = recipientNickname;
        this.players.add(recipientNickname);
        handleEvent(Event.GAME_CREATED);
        currentEvent = Event.WAITING_FOR_START;
    }

    @Override
    public void updateNewPlayerJoin(ArrayList<String> players) {
        for (String player : players) {
            if (!this.players.contains(player)) {
                this.players.add(player);
            }
        }// FIXME is LOBBYPLAYERLISTMESSAGE also used to update the player list when a player leaves the game?
        if (currentEvent.equals(Event.WAITING_FOR_START)) { // if the player is waiting for the game to start.
            handleEvent(Event.NEW_PLAYER_JOIN);
            logger.info("Received the LOBBYPLAYERLISTMessage from the server when a new player joins the game");
        } else {
            logger.info("Received the LOBBYPLAYERLISTMessage from the server when joined" +
                    " the game");
            setCurrentEvent(Event.WAITING_FOR_START); // if the player is the player who just joined the game.
        }
        showPlayerInGame();
    }

    //-------------------Game start-----------------------
    @Override
    public void setUpPlayersData() {
        // after receiving the message from the server, the method is called to set up/initiate the view of the player
        handleEvent(Event.GAME_START);
        for (String player : players) {
            publicInfo.put(player, new PlayerPub(null, 0, new ArrayList<>(), new int[]{0, 0, 0, 0, 0, 0, 0},true));
            boards.put(player, new BoardView(new int[]{80, 80, 80, 80}, new String[160][160]));
        }
    }

    @Override
    public void updateMatchStatus(int matchStatus) {
        // once received the MatchStatusMessage from the server
        this.Status = convertToMatchStatus(matchStatus);
        out.println("The match status is: " + convertToMatchStatus(matchStatus));
    }

    @Override
    public void requestSelectStarterCardSide(int ID) {
        // once received the AssignStarterCardMessage from the server
        currentEvent = Event.SELECT_STARTER_CARD_SIDE;
        out.println("The starter card received is following");
        out.println("Front side");
        showCard(ID, true);
        out.println("Back side");
        showCard(ID, false);
        out.println("Please select the side of the card you want to use, type[FRONT or BACK]:");
        String side = getInput();
        while (!side.equals("FRONT") && !side.equals("BACK")) {
            logger.info("Invalid input, please select FRONT or BACK");
            out.println("Invalid input, please select FRONT or BACK");
            side = getInput();
        }
        notifyAskListener(new SelectedStarterCardSideMessage(thisPlayerNickname, true));
        logger.info("Out requestSelectStarterCardSide");
    }

    @Override
    public void updateConfirmStarterCard(int colour, int cardID, boolean isUp, ArrayList<int[]> availablePos, int[] resources) {
        // once received the StarterCardConfirmationMessage from the server
        publicInfo.get(thisPlayerNickname).updateColour(convertToColour(colour));
        out.println("Your colour of this game is: " + convertToColour(colour));
        updateAfterPlacedCard(thisPlayerNickname, searchNonObjCardById(cardID), 0, 0, isUp, availablePos,
                resources, 0);
        out.println("Your field after placing the starter card is following:");
        showPlayersField(thisPlayerNickname);
    }

    @Override
    public void requestSelectSecretObjCard(ArrayList<Integer> secrets,ArrayList<Integer> common,ArrayList<Integer> hand) {
        // once received the AssignedSecretObjectiveCardMessage from the server
        currentEvent = Event.SELECT_SECRET_OBJ_CARD;
        out.println("You received 3 cards(resources/gold cards), 2 card as a common objective card of the game:");
        out.println();
        this.hand = hand;
        out.println("Your common objective cards for this game are following:");
        this.commonObjCards = common;
        showHand(hand);
        showObjectiveCards(common);
        out.println("Please select one of the objective cards in following to be your secret objective card type[LEFT or RIGHT]:");
        out.println("LEFT");
        out.println("RIGHT");
        showObjectiveCards(secrets);
        String cardID =getInput();
        while (!cardID.equals("LEFT") && !cardID.equals("RIGHT")) {
            logger.info("Invalid input, please select a card from the list");
            out.println("Invalid input, please select a card from the list, type[LEFT or RIGHT]");
            cardID = getInput();
        }
        notifyAskListener(new SelectedSecretObjectiveCardMessage(thisPlayerNickname,cardID.equals("LEFT") ? secrets.get(0) : secrets.get(1)));
    }

    @Override
    public void updateConfirmSelectedSecretCard(int chosenSecretObjectiveCard) {
        // once received the SecretObjCardConfirmationMessage from the server
        secretObjCardSelected = chosenSecretObjectiveCard;
        out.println("The secret objective card is selected successfully, here is your secret objective card:");
        showCard(chosenSecretObjectiveCard, true);
    }


    @Override
    public void updatePlayerDate(ArrayList<String> playerNicknames, ArrayList<Boolean> playerConnected,
                                 ArrayList<Integer> playerColours, ArrayList<Integer> playerHand,
                                 int playerSecretObjective, int[] playerPoints,
                                 ArrayList<ArrayList<int[]>> playerFields, int[] playerResources,
                                 ArrayList<Integer> gameCommonObjectives, ArrayList<Integer> gameCurrentResourceCards,
                                 ArrayList<Integer> gameCurrentGoldCards, int gameResourcesDeckSize,
                                 int gameGoldDeckSize, int matchStatus, ArrayList<ChatMessage> chatHistory,
                                 String currentPlayer, ArrayList<int[]> newAvailableFieldSpaces) {
        //The field is represented as an ArrayList of integer arrays, where each array represents a card placed on
        //     * the field. The elements of the array are the x-coordinate, y-coordinate, id of the card, and a boolean value
        //     * (1 for true, 0 for false) indicating whether the card is face up.
        //     *
        // after receiving the message from the server, the method is called to set up/initiate the view of the player
        this.currentEvent = Event.PLAYING;
        this.players = playerNicknames;
        this.hand = playerHand;
        this.commonObjCards = gameCommonObjectives;
        this.currentResourceCards = gameCurrentResourceCards;
        this.currentGoldCards = gameCurrentGoldCards;
        this.resourceDeckSize = gameResourcesDeckSize;
        this.goldDeckSize = gameGoldDeckSize;
        showObjectiveCards(currentGoldCards);
        this.Status = convertToMatchStatus(matchStatus);
        this.currentPlayer = currentPlayer;
        for (int i = 0; i < playerNicknames.size(); i++){
                publicInfo.put(playerNicknames.get(i), new PlayerPub(convertToColour(playerColours.get(i)),
                        playerPoints[i], new ArrayList<>(), playerResources, playerConnected.get(i)));
                boards.put(playerNicknames.get(i), new BoardView(new int[]{80, 80, 80, 80}, new String[160][160]));
            int finalI = i;
            playerFields.get(i).forEach(card -> {
                    publicInfo.get(playerNicknames.get(finalI)).getField().clear();
                    publicInfo.get(playerNicknames.get(finalI)).addToField(new CardPlacedView(card[2], cardImg.get(card[2]), card[0], card[1], card[3] == 1));
                    updateAfterPlacedCard(playerNicknames.get(finalI), searchNonObjCardById(card[2]), card[0], card[1],
                            card[3] == 1, newAvailableFieldSpaces, playerResources, playerPoints[finalI]);});
        }
    }

    @Override
    public void updatePlayerTurn(String playerNickname) {
        // once received the PlayerTurnMessage from the server
        if(playerNickname.equals(thisPlayerNickname)){
            out.println("It is your turn, please select one card from your hand to place on the field:");
            requestPlaceCard();
        }else{
            out.println("It is " + playerNickname + "'s turn");
        }
    }

    //------------playing-------------
    @Override
    public void requestPlaceCard() {
        currentEvent = Event.PLACE_CARD;
        out.println("Here is your field right now:");
        showBoard(thisPlayerNickname);
        showHand(hand);
        out.println("""
                To choose the left one, type LEFT
                To choose the middle one, type MIDDLE
                To choose the right one, type RIGHT
                """);
        String choice = getInput();
        while (!choice.equals("LEFT") && !choice.equals("MIDDLE") && !choice.equals("RIGHT")) {
            logger.info("Invalid input, please select LEFT, MIDDLE or RIGHT");
            out.println("Invalid input, please select LEFT, MIDDLE or RIGHT");
            choice = getInput();
        }
        out.println("You selected the card in the " + choice + " one of your hand:");
        int index = choice.equals("LEFT") ? 0 : choice.equals("MIDDLE") ? 1 : 2; // index of the card selected in hand
        showCard(hand.get(index), true);
        out.println("Do you want to see the back side of the card? type[Y or N]");
        String isUp = getInput();
        while(!isUp.equals("Y") && !isUp.equals("N")) {
            logger.info("Invalid input, please select Y or N");
            out.println("Invalid input, please select Y or N");
            isUp = getInput();
        }
        if( "Y".equals(isUp)) {
            showCard(hand.get(index), false);
        }
       out.println("Which side do you want to place the card? type[FRONT or BACK]");
       isUp = getInput();
       while(!isUp.equals("FRONT") && !isUp.equals("BACK")) {
       logger.info("Invalid input, please select FRONT or BACK");
       out.println("Invalid input, please select FRONT or BACK");
       isUp = getInput();
       }
       showBoard(thisPlayerNickname);
         out.println("Please select one of the available positions in the board to place the card,type[x,y]");
            String pos = getInput();
            while( !pos.matches("-?\\d{1,3},-?\\d{1,3}")){
                logger.info("Invalid input, please select a position in the board");
                out.println("Invalid input, please select a position in the board, type[x,y]:");
                pos = getInput();
            }
            String [] posArray = pos.split(",");
            notifyAskListener(new PlaceCardMessage(thisPlayerNickname, hand.get(index), Integer.parseInt(posArray[0]),
                    Integer.parseInt(posArray[1]), isUp.equals("FRONT")));
    }

    @Override
    public void updatePlacedCardConfirm(String playerNickname, int placedCard, int[] placedCardCoordinates,
                                        boolean placedSide, int playerPoints, int[] playerResources,
                                        ArrayList<int[]> newAvailableFieldSpaces) {
        updateAfterPlacedCard(playerNickname, searchNonObjCardById(placedCard), placedCardCoordinates[0],
                placedCardCoordinates[1], placedSide, newAvailableFieldSpaces, playerResources, playerPoints);
        if(playerNickname.equals(thisPlayerNickname)){
            out.println("The card is placed successfully, here is your field after placing the card:");
            showPlayersField(thisPlayerNickname);
            requestDrawCard();
        }
    }


    @Override
    public void requestDrawCard() {
        currentEvent = Event.DRAW_CARD;
        out.println("Now please select one card to add to your hand:");
        out.println("""
                To draw a resource card left one, type RESOURCE1
                To draw a resource card right one, type RESOURCE2
                To draw a gold card left one, type GOLD1
                To draw a gold card right one, type GOLD2
                To draw from the resource deck, type RESOURCE
                To draw from the gold deck, type GOLD
                """);
        out.println("Resources deck size: " + resourceDeckSize + " Gold deck size: " + goldDeckSize);
        out.println("Resource cards:");
        showObjectiveCards(currentResourceCards);
        out.println("Gold cards:");
        showObjectiveCards(currentGoldCards);
        String choice = getInput();
        while (!choice.equals("RESOURCE1") && !choice.equals("RESOURCE2") && !choice.equals("GOLD1") &&
                !choice.equals("GOLD2") && !choice.equals("RESOURCE") && !choice.equals("GOLD")) {
            logger.info("Invalid input, please select a card from the list");
            out.println("Invalid input, please try again");
            choice = getInput();
        }
        if (choice.equals("RESOURCE1") || choice.equals("RESOURCE2")) {
            notifyAskListener(new DrawCardMessage(thisPlayerNickname, 0, choice.equals("RESOURCE1") ?
                    currentResourceCards.get(0) : currentResourceCards.get(1)));
        } else if (choice.equals("GOLD1") || choice.equals("GOLD2")) {
            notifyAskListener(new DrawCardMessage(thisPlayerNickname, 1, choice.equals("GOLD1") ?
                    currentGoldCards.get(0) : currentGoldCards.get(1)));
        } else {
            notifyAskListener(new DrawCardMessage(thisPlayerNickname, choice.equals("RESOURCE") ? 0 : 1, -1));
        }
    }

    @Override
    public void updateAfterPlacedCard(String playerNickname, NonObjCardFactory card, int x, int y, boolean isUp,
                                      ArrayList<int[]> availablePos, int[] resources, int points) {
        /*once received the PlacedCardConfirmationMessage from the server, the card is searched in the
        hand/ObjectiveCards by ID and then using this method to store the card in the arraylist field, and add it in
        the board of the player.*/
        // update the field of the player
        publicInfo.get(playerNickname).addToField(new CardPlacedView(card.getID(), cardImg.get(card.getID()), x, y, isUp));
        publicInfo.get(playerNickname).updateResources(resources); // update the resources
        publicInfo.get(playerNickname).updatePoints(points); // update the points
        // represents the sequence of the card placed in the field.
        int num = publicInfo.get(playerNickname).getField().size();
        BoardView boardView = boards.get(playerNickname);
        String[][] board = boardView.getBoard();
        int[] limits = boardView.getLimits();
        //convert the x and y coordinates to the position in the board.
        int posX = -2 * y + 80;
        int posY = 2 * x + 80;
        updateBoardViewLimits(posX, posY, limits);

        String[] cornerType;
        String EMPTY = iconCard(card.getKingdom()); // set the empty space of the card based on the colour of kingdom.
        int[] permRes = card.getPermRes(); // get the permanent resources of the card.
        String[] permResString = new String[]{EMPTY, EMPTY, EMPTY};
        if (!card.getType().equals("STARTING")) {  // if the card is not a starting card.
            for (int i = 0; i < permRes.length; i++) { // search the permanent resources of the card.
                if (permRes[i] != 0) {
                    permResString[0] = iconArrayElement(i); //convert the permanent resources to the icon.
                    permResString[1] = String.format("%2s", num);
                    break; // because the resource/gold card has only one permanent resource.
                }
            }
        } else { // if the card is a starting card
            int count = 0;
            for (int i = 0; i < permRes.length; i++) {
                if (permRes[i] != 0) {
                    permResString[count] = iconArrayElement(i); // convert the permanent resources to the icon.
                    count++;
                }
            }
        }
        // update the board of the player
        if (isUp) {
            board[posX][posY] = EMPTY;
            board[posX][posY - 1] = ColourCard(card.getKingdom()) + "|" + EMPTY + ANSI_RESET;
            // stored the number which represents the sequence number of the card placed in the field.
            board[posX][posY + 1] = ColourCard(card.getKingdom()) + String.format("%2s", num) + "|" + ANSI_RESET;
            cornerType = card.getCorner();
        } else {
            board[posX][posY] = permResString[1];
            board[posX][posY - 1] = ColourCard(card.getKingdom()) + "|" + permResString[0] + ANSI_RESET;
            board[posX][posY + 1] = ColourCard(card.getKingdom()) + permResString[2] + "|" + ANSI_RESET;
            cornerType = card.getCornerBack();
        }
        String topLeft = ColourCard(card.getKingdom()) + "|" + icon(cornerType[0]) + ANSI_RESET; // TopLeft 1)
        String topRight = ColourCard(card.getKingdom()) + icon(cornerType[1]) + "|" + ANSI_RESET; // TopRight 2)
        String bottomLeft = ColourCard(card.getKingdom()) + "|" + icon(cornerType[2]) + ANSI_RESET; // BottomLeft 3)
        String bottomRight = ColourCard(card.getKingdom()) + icon(cornerType[3]) + "|" + ANSI_RESET; // BottomRight 4)
        board[posX - 1][posY] = EMPTY;
        board[posX + 1][posY] = EMPTY;
        board[posX - 1][posY + 1] = topRight;
        board[posX + 1][posY + 1] = bottomRight;
        board[posX - 1][posY - 1] = topLeft;
        board[posX + 1][posY - 1] = bottomLeft;
        /* if player is the owner of this UI, store the available positions in the board of the player, and update the
        board limits. In this way, the player can see the available positions for the next turn of the placement.*/
        if (playerNickname.equals(thisPlayerNickname)) {
            for (int[] pos : availablePos) {
                posX = -2 * pos[1] + 80;
                posY = 2 * pos[0] + 80;
                updateBoardViewLimits(posX, posY, limits);
                board[posX][posY] = " , ";
                board[posX][posY - 1] = String.format("%3d", (pos[0]));
                board[posX][posY + 1] = String.format("%-3d", (pos[1]));
            }
        }
    }

    @Override
    public void updateAfterDrawCard(int[]hand) {
        this.hand.clear();
        for(int card : hand){
            this.hand.add(card);
        }
        out.println("The card is added in your hand successfully, here is your hand after drawing the card:");
        showHand(this.hand);
    }
    @Override
    public void updateDeck(int resourceDeckSize, int goldDeckSize,int[]currentResourceCards,
                           int[]currentGoldCards) {
        this.resourceDeckSize = resourceDeckSize;
        this.goldDeckSize = goldDeckSize;
        this.currentResourceCards.clear();
        this.currentGoldCards.clear();
        this.currentResourceCards.add(currentResourceCards[0]);
        this.currentResourceCards.add(currentResourceCards[1]);
        this.currentGoldCards.add(currentGoldCards[0]);
        this.currentGoldCards.add(currentGoldCards[1]);
        showDeck();
    }

    //-------------------Last turn-------------------


    //-------------------Game end-------------------


    //-------------------View of the game-------------------

    @Override
    public void showDeck() {
        out.println("Resource deck size: " + resourceDeckSize + " Gold deck size: " + goldDeckSize);
        out.println("Resource cards:");
        showObjectiveCards(currentResourceCards);
        out.println("Gold cards:");
        showObjectiveCards(currentGoldCards);
    }
    @Override
    public void showHelpInfo() {
        //TODO show the help information: exit command, start chat command, view the player list command, view the
        // game status command, view the game rule command,view the card command, view other players' field command,
        // view the secret objective command, view the game order command, view the game ID command ecc.
        out.println("""
                Entering the Service Mode, please type the one of the following commands:
                HELP:
                EXIT:
                QUIT:
                ShowPlayerList:
                ShowGameStatus:
                ShowGameRULE:
                ShowHand:
                ShowCommonObjCard:
                ShowSecretObjCard:
                ShowPlacedCard:
                ShowPlayerField:
                ShowScoreBoard:
                ShowCurrentPlayer:                        
                """);
    }

    public void showPlayerInGame() {
        out.println("The players in the game are: " + players);
    }

    public void showCardSelected() {
        // show the version details of the card selected by the player --> call printNonObjCard or printObjCard
        out.println("The card selected is: ");
        //TODO show the card selected by the player
    }

    public void showPlacedCard() {
        // when the player wants to see the details of the card placed --> call printNonObjCard
        //TODO use printNonObjCard to show the card placed by request of the player
    }

    public void showPlayersField(String playerNickname) {
        showBoard(playerNickname);
        showPoints(playerNickname);
    }

    @Override
    public void showPoints(String playerNickname) {
        int[] resources = publicInfo.get(playerNickname).getResources();
        out.println("Player " + playerNickname + " has: " + publicInfo.get(playerNickname).getPoints() + " points" +
                "\nwith " + resources[0] + iconArrayElement(0) + resources[1] + iconArrayElement(1) + resources[2] +
                iconArrayElement(2) + resources[3] + iconArrayElement(3) + resources[4] +
                iconArrayElement(4) + resources[5] + iconArrayElement(5) + resources[6] +
                iconArrayElement(6) + " in field");
    }

    /**
     * Print the zone of the board where the player placed his cards and the available positions for the next
     * placement.
     *
     * @param nickname the nickname of the player, the owner of the board that should be printed.
     */
    private void showBoard(String nickname) {
        BoardView boardView = this.boards.get(nickname); // get the boardView of the player by nickname
        String[][] board = boardView.getBoard(); // get the matrix of the board of the player
        int[] limits = boardView.getLimits(); // get the limits of the view of the board
        for (int i = limits[1]; i <= limits[0]; i++) { // print the zone of the board enclosed by the limits
            for (int j = limits[3]; j <= limits[2]; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "   "; // if the position is empty will be printed three spaces to remain the layout
                }
                out.print(board[i][j]);
            }
            out.println();
        }
    }

    @Override
    public void showHand(ArrayList<Integer> hand) {
        out.println("Hand cards: ");
        ArrayList<String> card1 = cardImg.get(hand.get(0));
        ArrayList<String> card2 = cardImg.get(hand.get(1));
        ArrayList<String> card3 = cardImg.get(hand.get(2));
        for (int i = 0; i < 7; i++) {
            out.println(card1.get(i) + "  " + card2.get(i)+" "+card3.get(i));
        }
    }

    @Override
    public void showObjectiveCards(ArrayList<Integer> ObjCards) {
        ArrayList<String> card1 = cardImg.get(ObjCards.get(0));
        ArrayList<String> card2 = cardImg.get(ObjCards.get(1));
        for (int i = 0; i < 7; i++) {
            out.println(card1.get(i) + "  " + card2.get(i));
        }
    }

    @Override
    public void showCard(int ID, boolean isUp) {
        cardImg.get(ID);
        if (isUp) {
            out.println("Front side");
            for (int i = 0; i < 7; i++) {
                out.println(cardImg.get(ID).get(i));
            }
        } else {
            out.println("Back side");
            for (int i = 7; i < 14; i++) {
                out.println(cardImg.get(ID).get(i));
            }
        }
    }
    //-------------------Card Factory-------------------

    /**
     * @param ID
     * @return
     */
    private ObjectiveCardFactory searchObjCardById(int ID) {
        try {
            for (ObjectiveCardFactory objectiveCard : objectiveCards) {
                if (objectiveCard.getID() == ID) {
                    return objectiveCard;
                }
            }
        } catch (RuntimeException e) {
            logger.error("The card is not found");
        }
        return null;
    }

    /**
     * @param ID
     * @return
     */
    private NonObjCardFactory searchNonObjCardById(int ID) {
        try {
            for (NonObjCardFactory nonObjCard : nonObjCards) {
                if (nonObjCard.getID() == ID) {
                    return nonObjCard;
                }
            }
        } catch (RuntimeException e) {
            logger.error("The card is not found");
        }
        return null;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> setImg() {
        HashMap<Integer, ArrayList<String>> Img = new HashMap<>();
        for (NonObjCardFactory card : nonObjCards) {
            ArrayList<String> cardImage = new ArrayList<>();
            String kingdom = card.getKingdom();
            if (kingdom.equals("null")) { // if the kingdom is null, set the kingdom to STARTER
                kingdom = "STARTER";
            }
            int value = card.getValue();
            String strategy = "|" + card.getPointStrategy();
            String requirements = iconArray(card.getConditionCount()); // stored the requirements of the card in one string
            String permanent = iconArray(card.getPermRes()); // stored the permanent resources of the card in one string
            String colour = ColourCard(kingdom); // set the colour of the card based on the kingdom
            String[] corner = card.getCorner();
            String[] cornerBack = card.getCornerBack();

            // set the different paddings to remain the layout of the card
            int padding1 = 18 - (value + strategy).length();
            int padding2 = (16 - requirements.length()) / 2;
            int padding3 = (28 - permanent.length()) / 2;
            int padding4 = 26 - kingdom.length();

            // print the card based on the side
            // if the side is the front side
            cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
            if (value != 0) { //if the card don't have any value.
                // in particular, with CountResource strategy should print also the type of the resource/object that
                // should be counted in the field to get the points.
                if (strategy.equals("|CountResource")) {
                    padding1 = 18 - (value + card.getPointStrategyType()).length();
                    cardImage.add(String.format(colour + "| %s |%s%" + padding1 + "s| %s |" + ANSI_RESET, icon(corner[0]),
                            value + card.getPointStrategyType(), "", icon(corner[1])));
                } else {
                    if (strategy.equals("|Empty")) { // if the strategy is empty, set the strategy to Point.
                        strategy = " Point";
                    }
                    cardImage.add(String.format(colour + "| %s |%s%" + padding1 + "s| %s |" + ANSI_RESET, icon(corner[0]),
                            value + strategy, "", icon(corner[1])));
                }
            } else { // if the card don't have any value, print only the corner of the card and the kingdom of the card.
                cardImage.add(String.format(colour + "| %s |%18s| %s |" + ANSI_RESET, icon(corner[0]), "", icon(corner[1])));
            }
            cardImage.add(String.format(colour + "+----+%18s+----+", ""));
            cardImage.add(String.format(colour + "| %s %" + padding4 + "s|" + ANSI_RESET, kingdom, ""));
            cardImage.add(String.format(colour + "+----+%18s+----+" + ANSI_RESET, ""));
            // “if(requirements.length<3)” is used to adjust the padding based on the length of the requirements,
            // because the unicode characters have different visual width that can affect the layout of the card.
            if (requirements.length() < 3) {
                cardImage.add(String.format(colour + "| %s | %" + padding2 + "s%s%" + padding2 + "s | %s |" + ANSI_RESET,
                        icon(corner[2]), "", requirements, "", icon(corner[3])));
            } else {
                cardImage.add(String.format(colour + "| %s | %" + padding2 + "s%s%" + padding2 + "s| %s |" + ANSI_RESET,
                        icon(corner[2]), "", requirements, "", icon(corner[3])));
            }
            cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
            // if the side is the back side
            if (!kingdom.equals("STARTER")) {
                cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
                cardImage.add(String.format(colour + "|%4s|%18s|%4s|" + ANSI_RESET, "", "", ""));
                cardImage.add(String.format(colour + "+----+%18s+----+", ""));
                // print the permanent resources of the back side of the card
                cardImage.add(String.format(colour + "|%" + padding3 + "s%s%" + padding3 + "s|" + ANSI_RESET, "", permanent, ""));
                cardImage.add(String.format(colour + "+----+%18s+----+" + ANSI_RESET, ""));
                cardImage.add(String.format(colour + "|%4s|%18s|%4s|" + ANSI_RESET, "", "", ""));
                cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
            } else {
                cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
                cardImage.add(String.format(colour + "| %s |%18s| %s |" + ANSI_RESET, icon(cornerBack[0]), "", icon(cornerBack[1])));
                cardImage.add(String.format(colour + "+----+%18s+----+", ""));
                cardImage.add(String.format(colour + "|%" + padding3 + "s%s%" + padding3 + "s|" + ANSI_RESET, "", permanent, ""));
                cardImage.add(String.format(colour + "+----+%18s+----+" + ANSI_RESET, ""));
                cardImage.add(String.format(colour + "| %s |%18s| %s |" + ANSI_RESET, icon(cornerBack[2]), "", icon(cornerBack[3])));
                cardImage.add(String.format(colour + "+----+------------------+----+" + ANSI_RESET));
            }
            Img.put(card.getID(), cardImage);
        }
        for (ObjectiveCardFactory card : objectiveCards) {
            ArrayList<String> cardImage = new ArrayList<>();
            int value = card.getValue();
            String strategy = card.getPointStrategy();
            String type = card.getPointStrategyType();
            int count = card.getPointStrategyCount();
            String icon = icon(type); // get the icon of the resource/object that should be counted to get the points.
            String description; // create a string to store the description of the strategy.

            // set the different paddings to remain the layout of the card
            int paddingPoint = 26 - (value + " POINTS" + "LConfig.").length(); // padding default for L configuration card
            int paddingDescription;
            int paddingIcon;
            String colour;
            switch (strategy) {
                case "CountResource": {
                    description = "every " + count + " " + type; // set the description.
                    paddingIcon = 26 - count * icon.length();
                    paddingDescription = 26 - description.length();
                    paddingPoint = 26 - (value + "POINTS|" + strategy).length();
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT|%s%" + paddingPoint + "s |", value, strategy, ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    if (count == 2) { // if we need to count 2 objects.
                        cardImage.add(String.format("| %s%s%" + paddingIcon + "s |", icon, icon, ""));
                    } else if (count == 3) { // if we need to count 3 objects.
                        cardImage.add(String.format("| %s%s%s%" + paddingIcon + "s |", icon, icon, icon, ""));
                    }
                    cardImage.add(String.format("|%28s|", ""));
                    cardImage.add(String.format("|%28s|", ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
                case "Diagonals": {
                    String iconCard = iconCard(type); // get the type of the card that should be placed in the diagonal.
                    colour = ColourCard(type); // set the colour of the card based on the type.
                    description = "3 " + type + " cards";
                    paddingDescription = 26 - description.length();
                    paddingPoint = 26 - (value + " POINTS" + strategy).length();
                    paddingIcon = 26 - iconCard.length() - 4;
                    cardImage.add(String.format(colour + "+----------------------------+" + ANSI_RESET));
                    cardImage.add(String.format(colour + "| %s POINT %s%" + paddingPoint + "s |" + ANSI_RESET, value, strategy, ""));
                    cardImage.add(String.format(colour + "| %s%" + paddingDescription + "s |" + ANSI_RESET, description, ""));
                    // if the diagonal is from right to left: y=-x
                    if (!card.getPointStrategyLeftToRight()) {
                        cardImage.add(String.format(colour + "|     %s%" + paddingIcon + "s |" + ANSI_RESET, iconCard, ""));
                        cardImage.add(String.format(colour + "|         %s%" + (paddingIcon - 4) + "s |" + ANSI_RESET, iconCard, ""));
                        cardImage.add(String.format(colour + "|             %s%" + (paddingIcon - 8) + "s |" + ANSI_RESET, iconCard, ""));
                        // if the diagonal is from left to right: y=x
                    } else {
                        cardImage.add(String.format(colour + "|             %s%" + (paddingIcon - 8) + "s |" + ANSI_RESET, iconCard, ""));
                        cardImage.add(String.format(colour + "|         %s%" + (paddingIcon - 4) + "s |" + ANSI_RESET, iconCard, ""));
                        cardImage.add(String.format(colour + "|     %s%" + paddingIcon + "s |" + ANSI_RESET, iconCard, ""));
                    }
                    cardImage.add(String.format(colour + "+----------------------------+" + ANSI_RESET));
                    break;
                }
                // Four type of L Configuration cards
                case "LConfigurationOne": {
                    description = "2 FUNGI + 1 PLANT cards";
                    paddingDescription = 26 - description.length();
                    paddingIcon = 26 - iconCard("FUNGI").length() - 4;
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT %s%" + paddingPoint + "s |", value, "LConfig.", ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("FUNGI"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("FUNGI"), ""));
                    cardImage.add(String.format("|          %s%" + (paddingIcon - 5) + "s |", iconCard("PLANT"), ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
                case "LConfigurationTwo": {
                    description = "2 PLANT + 1 INSECT cards";
                    paddingDescription = 26 - description.length();
                    paddingIcon = 26 - iconCard("FUNGI").length() - 4;
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT %s%" + paddingPoint + "s |", value, "LConfig.", ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("PLANT"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("PLANT"), ""));
                    cardImage.add(String.format("|        %s%" + (paddingIcon - 3) + "s |", iconCard("INSECT"), ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
                case "LConfigurationThree": {
                    description = "2 INSECT + 1 ANIMAL cards";
                    paddingDescription = 26 - description.length();
                    paddingIcon = 26 - iconCard("FUNGI").length() - 4;
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT %s%" + paddingPoint + "s |", value, "LConfig.", ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    cardImage.add(String.format("|        %s%" + (paddingIcon - 3) + "s |", iconCard("ANIMAL"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("INSECT"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("INSECT"), ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
                case "LConfigurationFour": {
                    description = "2 ANIMAL + 1 FUNGI cards";
                    paddingDescription = 26 - description.length();
                    paddingIcon = 26 - iconCard("FUNGI").length() - 4;
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT %s%" + paddingPoint + "s |", value, "LConfig.", ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    cardImage.add(String.format("|          %s%" + (paddingIcon - 5) + "s |", iconCard("FUNGI"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("ANIMAL"), ""));
                    cardImage.add(String.format("|         %s%" + (paddingIcon - 4) + "s |", iconCard("ANIMAL"), ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
                case "AllSpecial": {
                    // description of the three objects that should be count in the field.
                    description = "INSKELL+QUILL+MANUSCRIPT";
                    paddingDescription = 26 - description.length();
                    paddingIcon = 26 - 3 * icon("INKWELL").length();
                    paddingPoint = 26 - (value + " POINTS" + strategy).length();
                    cardImage.add("+----------------------------+");
                    cardImage.add(String.format("| %s POINT %s%" + paddingPoint + "s |", value, strategy, ""));
                    cardImage.add(String.format("| %s%" + paddingDescription + "s |", description, ""));
                    cardImage.add(String.format("| %s%s%s%" + paddingIcon + "s |", //print the icon of the three objects specified.
                            icon("INKWELL"), icon("QUILL"), icon("MANUSCRIPT"), ""));
                    cardImage.add(String.format("|%28s|", ""));
                    cardImage.add(String.format("|%28s|", ""));
                    cardImage.add("+----------------------------+");
                    break;
                }
            }
            Img.put(card.getID(), cardImage);
        }
        return Img;
    }

    /**
     * Use this method to set the color of the card based on the kingdom of the card.
     *
     * @param kingdom the kingdom of the card.
     * @return the string of the color in ASCI escape code.
     */
    private static String ColourCard(String kingdom) {
        String colour = "";
        switch (kingdom) { // set the color of the card based on the kingdom of the card
            case "PLANT" -> colour = ANSI_GREEN;
            case "FUNGI" -> colour = ANSI_RED;
            case "ANIMAL" -> colour = ANSI_BLUE;
            case "INSECT" -> colour = ANSI_PURPLE;
            case "STARTER" -> colour = ANSI_RESET;
        }
        return colour;
    }

    /**
     * Method used to convert the integer array of the condition count of the card to a string of icons, using the
     * Unicode characters and added it in one string.
     *
     * @param conditionCount the integer array of the requirement count of the card.
     * @return the string of icons which contains the icons of the requirements of the card.
     */
    private static String iconArray(int[] conditionCount) {
        StringBuilder condition = new StringBuilder();
        for (int i = 0; i < conditionCount.length; i++) {  // iterate through the condition count array
            if (conditionCount[i] != 0) {
                for (int j = 0; j < conditionCount[i]; j++) {
                    // if exist element in the condition count array, convert the element to an icon and add it to the
                    // string condition.
                    condition.insert(0, iconArrayElement(i));
                }
            }
        }
        return condition.toString(); // return the string which contains the icons of the requirements of the card.
    }

    /**
     * Method used to convert the corner type of the card to an icon, using the Unicode characters.
     *
     * @param type the corner type of the card.
     * @return the icon of the corner type of the card.
     */
    private static String icon(String type) {
        String icon;
        switch (type) {
            case "INSECT" -> icon = INSECT;
            case "PLANT" -> icon = PLANT;
            case "FUNGI" -> icon = FUNGI;
            case "ANIMAL" -> icon = ANIMAL;
            case "QUILL" -> icon = QUILL;
            case "INKWELL" -> icon = INKWELL;
            case "MANUSCRIPT" -> icon = MANUSCRIPT;
            case "NON_COVERABLE" -> icon = NON_COVERABLE;
            default -> icon = "  ";
        }
        return icon;
    }

    /**
     * Method used to convert the type of the resource/object that stored in the array of requirements of the card or
     * in the array of the permanent resources of the card to an icon, using the Unicode characters.
     */
    private static String iconArrayElement(int type) {
        String icon;
        switch (type) {
            case 0 -> icon = PLANT;
            case 1 -> icon = FUNGI;
            case 2 -> icon = ANIMAL;
            case 3 -> icon = INSECT;
            case 4 -> icon = QUILL;
            case 5 -> icon = INKWELL;
            case 6 -> icon = MANUSCRIPT;
            default -> icon = EMPTY;
        }
        return icon;
    }

    /**
     * Method used to convert the colour of the card to an icon, using the Unicode characters.
     */
    private static String iconCard(String type) {
        String icon;
        switch (type) {
            case "PLANT" -> icon = new String(Character.toChars(PLANTCARD));
            case "FUNGI" -> icon = new String(Character.toChars(FUNGICARD));
            case "ANIMAL" -> icon = new String(Character.toChars(ANIMALCARD));
            case "INSECT" -> icon = new String(Character.toChars(INSECARD));
            default -> icon = new String(Character.toChars(STARTERCARD));
        }
        return icon;
    }
    //-------------------utilities-------------------

    /**
     * Used method to update the dimensions of the board view of the player after placing a card on the board. The
     * method is called by the {@link #updateAfterPlacedCard} method.
     *
     * @param posX   the x coordinate of the card placed.
     * @param posY   the y coordinate of the card placed.
     * @param limits the array of the limits contains the maximum x and y and the minimum x and y updated.
     */
    private void updateBoardViewLimits(int posX, int posY, int[] limits) {
        limits[0] = Math.max(limits[0], posX + 1);
        limits[1] = Math.min(limits[1], posX - 1);
        limits[2] = Math.max(limits[2], posY + 1);
        limits[3] = Math.min(limits[3], posY - 1);
    }

    @Override
    public void handleEvent(Event event) {
        switch (event) {
            case GAME_CREATED -> {
                out.println("Game " + gameID + " created successfully, waiting for other players to join...");
                showPlayerInGame();
            }
            case NEW_PLAYER_JOIN -> {
                out.println("New player join the game :)");
            }
            case JOINED_GAME -> {
                out.println("Joined the game successfully,waiting for the game to start...");
            }
            case GAME_START -> {
                out.println("YEAH!!! Let's start the game!");
            }
        }
    }

    @Override
    public void handleChoiceEvent(Event event, int choice) {

    }
    private void clearCMD() {
        try {
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

    public String convertToColour(int colour) {
        switch (colour) {
            case 0 -> {
                return ANSI_RED + "RED" + ANSI_RESET;
            }
            case 1 -> {
                return ANSI_GREEN + "GREEN" + ANSI_RESET;
            }
            case 2 -> {
                return ANSI_BLUE + "BLUE" + ANSI_RESET;
            }
            case 3 -> {
                return ANSI_YELLOW + "YELLOW" + ANSI_RESET;
            }
            case 4 -> {
                return ANSI_BLACK + "BLACK" + ANSI_RESET;
            }
            default -> {
                return null;
            }
        }
    }

    public String convertToMatchStatus(int status) { //FIXME should I create a enum for the status?
        switch (status) {
            case 0 -> {
                return "LOBBY";
            }
            case 1 -> {
                return "PREPARATION";
            }
            case 2 -> {
                return "PLAYING";
            }
            case 3 -> {
                return "TERMINATING";
            }
            case 4 -> {
                return "LAST_TURN";
            }
            case 5 -> {
                return "TERMINATED";
            }
            default -> {
                return null;
            }
        }
    }

    public String getInput() {
        String input = in.nextLine();
        switch (input) {
            case "HELP" -> showHelpInfo();
            case "QUIT" -> System.exit(0);
            case "ShowPlayerList" -> showPlayerInGame();
            case "ShowGameStatus" -> out.println("The match status is: " + Status);
            case "ShowGameRULE" -> out.println("Game rule:https://it.boardgamearena.com/link?url=https%3A%2F%2Fcdn.1j1ju.com%2Fmedias%2Fa7%2Fd7%2F66-codex-naturalis-rulebook.pdf&id=9212");
            case "ShowHand" -> showCardSelected();
            case "ShowCommonObjCard" -> showObjectiveCards(commonObjCards);
            case "ShowSecretObjCard" -> showCard(secretObjCardSelected, true);
            case "ShowPlacedCard" -> {
            }
            case "ShowPlayerField" -> {
                out.println("Whose field do you want to see?");
                in.nextLine();
                String Nickname = in.nextLine();
                while (!players.contains(Nickname)) {
                    out.println("Invalid nickname, can't find the player, please try again");
                    in.nextLine();
                    Nickname = in.nextLine();
                }
                showPlayersField(Nickname);
            }
            case "ShowScoreBoard" -> {
                out.println("The players have the following points:");
                for (String player : players) {
                    showPoints(player);
                }
            }
            case "ShowCurrentPlayer" -> {
                out.println("Is " + currentPlayer + "'s turn.");
            }
        }
        return input;
    }

    private int getInputInt() {
        try {
            return in.nextInt();
        } catch (InputMismatchException e) {
            out.println("Invalid input, please type a number");
            in.nextLine();
            return getInputInt();
        }
    }
    @Override
    public void run() {

    }
}







