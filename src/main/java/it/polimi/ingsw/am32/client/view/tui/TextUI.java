package it.polimi.ingsw.am32.client.view.tui;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.chat.ChatMessage;
import it.polimi.ingsw.am32.client.*;
import it.polimi.ingsw.am32.message.ClientToServer.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

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
 * //TODO fix the order of the deck after the player draws a card
 * //TODO fix the print of the order of the game
 * //TODO simulate the chat
 */
public class TextUI extends View{
    private static final Logger logger = LogManager.getLogger("TUILogger");
    private final Scanner in;
    private final PrintStream out;
    private final IsValid isValid = new IsValid();
    private final HashMap<String, BoardView> boards;
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
    private static final String BLANK = "   ";
    private final Object lock = new Object();

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
        currentEvent = Event.CHOOSE_CONNECTION; // Update the state of the client

        boolean isConnected = false; // Indicates whether the connection is established
        do {
            out.println("""
                Choose the connection type,type[1 or 2]:
                1. Socket
                2. RMI"""); // Print the prompt for the player

            int connectionChoice = getInputInt(); // Ask the player to choose the connection type

            // At this point, the user will have inputted an integer, but it might not be a valid choice
            switch (connectionChoice) {
                case 1: { // Player chooses socket connection
                    // Ask the player to insert the server IP
                    out.println("Insert the server IP:"); // Ask the player to insert the server IP

                    String ServerIP = in.nextLine(); // Read the player's input
                    while (!isValid.isIpValid(ServerIP)) { // Check if the IP address is valid
                        out.println("Invalid IP, please try again"); // Print an error message
                        ServerIP = in.nextLine(); // Ask the player to re-enter the IP address
                    }

                    // Ask the player to insert the server port
                    out.println("Insert the server port:"); // Ask the player to insert the server port

                    int port = getInputInt(); // Read the player's input
                    while (!isValid.isPortValid(port)) { // Check if the port number is valid
                        out.println("Invalid port, please try again"); // Print an error message
                        port = getInputInt(); // Ask the player to re-enter the port number
                    }

                    try {
                        setSocketClient(ServerIP, port); // Set the socket client
                        isConnected = true; // Set the connection status to true
                    } catch (IOException e) { // If an I/O error occurs
                        Thread.currentThread().interrupt(); // Interrupt the current thread
                        // TODO Should probably log this
                    }

                    break;
                }
                case 2: { // Player chooses RMI connection
                    // Ask the player to insert the server URL
                    out.println("Insert the server URL"); // Ask the player to insert the server URL
                    // TODO Should ask the player to insert the server URL in a specific format

                    String ServerURL = in.nextLine(); // Read the player's input
                    while (!isValid.isURLValid(ServerURL)) { // Check if the URL is valid
                        out.println("Invalid URL, please try again"); // Print an error message
                        ServerURL = in.nextLine(); // Ask the player to re-enter the URL
                    }

                    setRMIClient(ServerURL); // Set the RMI client
                    isConnected = true; // Set the connection status to true

                    break;
                }
                default: { // If the player's input is not one of the valid options
                    logger.info("Invalid input, please select 1 or 2");
                    out.println("Invalid input, please select 1 or 2");
                    continue; // Continue here to avoid printing connection failed message
                }
            }

            // We have now attempted to establish a connection, but it may have failed
            if (!isConnected) { // If the connection is not established
                out.println("Connection failed, please try again"); // Print an error message
            }
        } while (!isConnected); // Keep looping until the connection is established
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
     * menu of the game mode and asks the player to select the action to perform.
     */
    @Override
    public void askSelectGameMode() {
        currentEvent = Event.SELECT_GAME_MODE;

        out.println("""
                Menu:
                1. Create new game
                2. Join game with game ID
                3. Reconnect game
                """);
        out.println("Which action do you want to perform, type[1,2 or 3]:");

        while (true) { // Keep looping until the player enters a valid choice
            int choice = getInputInt(); // Read the player's input
            switch (choice) {
                case 1 -> askCreateGame();
                case 2 -> askJoinGame();
                case 3 -> askReconnectGame();
                default -> { // If the player's input is not one of the valid options
                    logger.info("Invalid input, please select 1, 2 or 3");
                    out.println("Invalid input, please select 1, 2 or 3");
                    continue;
                }
            }
            // If we get to this point, the player has entered a valid choice
            break;
        }
    }

    /**
     * Method that asks the player to insert the nickname they want to use in the game.
     * Nickname limited to 20 characters.
     */
    @Override
    public void askNickname() {
        while (true) {
            out.println("Insert the nickname you want to use in the game:");
            thisPlayerNickname = in.nextLine();

            if (thisPlayerNickname.isBlank()) {
                out.println("Invalid nickname, cannot be left blank");
                continue;
            }
            else if (thisPlayerNickname.length() > 20) {
                out.println("Invalid nickname, must be less than 20 characters");
                continue;
            }

            return;
        }
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
        currentEvent = Event.CREATE_GAME;
        askNickname(); // Ask the player to insert his nickname

        out.println("Insert the number of players you want to play with, type[2, 3 or 4]:");

        while (true) { // Keep looping until the player enters a valid choice
            playerNum = getInputInt();

            if (playerNum < 2 || playerNum > 4) {
                out.println("Invalid number of players, please insert a number between 2 and 4");
            } else { // If the number of players is valid
                break;
            }
        }

        // Notify the listener with the new game message
        notifyAskListenerLobby(new NewGameMessage(thisPlayerNickname, playerNum));
    }

    /**
     * Method that asks the player to insert the nickname they want to use in the game and the Access ID of the game
     * they want to join.
     */
    @Override
    public void askJoinGame() {
        currentEvent = Event.JOIN_GAME;
        askNickname(); // Ask the player to insert his nickname

        out.println("Insert the Access ID of the game you want to join:");

        gameID = getInputInt();

        // notify the listener with the access game message
        notifyAskListenerLobby(new AccessGameMessage(gameID, thisPlayerNickname));
    }

    /**
     * Use this method to ask the player if they want to reconnect to the game.
     */
    @Override
    public void askReconnectGame() {
        currentEvent = Event.RECONNECT_GAME;
        askNickname();

        gameID = getInputInt();

        // notify the listener with the reconnect game message
        notifyAskListenerLobby(new ReconnectGameMessage(thisPlayerNickname,gameID));
    }

    /**
     * Once the player receives the NewGameConfirmationMessage from the server, the method is called by processMessage
     * to store the gameID, the nickname of the player who created the game, and add it in the list of players.
     * @param gameID the game ID returned by the server after the confirmation of the new game
     * @param recipientNickname the nickname of the player who asked to create the new game
     */
    @Override
    public void updateNewGameConfirm(int gameID, String recipientNickname) {
        this.gameID = gameID;
        this.thisPlayerNickname = recipientNickname;
        this.players.add(recipientNickname);
        handleEvent(Event.GAME_CREATED,null); // print the message to notify the player that the game is created correctly
        currentEvent = Event.WAITING_FOR_START; // enter the waiting for start event
        Status = Event.LOBBY;
    }
    /**
     * Once the player receives the LobbyPlayerList message from the server, the method is called by
     * processMessage, to update the player's list in the Lobby phase and print the player's list updated.
     * @param players the list updated of players in the game.
     */
    @Override
    public void updatePlayerList(ArrayList<String> players) {
        this.players = players;
        showPlayerInGame();
    }

    /**
     * Method used to update the player's board and public info when the player disconnects from the game after the
     * placement of the card.
     * @param playerNickname the nickname of the player whose board should be updated with the rollback
     * @param removedCard the ID of the card that should be removed from the board and the public info of the player
     * @param playerPoints the points of the player after the rollback
     * @param playerResources the resources of the player after the rollback
     */
    @Override
    public void updateRollback(String playerNickname, int removedCard, int playerPoints, int[] playerResources) {
        int x = publicInfo.get(playerNickname).getField().getLast().x();
        int y = publicInfo.get(playerNickname).getField().getLast().y();
        String[][]board = boards.get(playerNickname).getBoard();
        int posX = -2 * y + 80;
        int posY = 2 * x + 80;
        board[posX][posY] = BLANK; //TODO CHECK PRINT WITH THE TEMPORARY
        board[posX - 1][posY] = BLANK;
        board[posX + 1][posY] = BLANK;
        board[posX][posY - 1] = BLANK;
        board[posX][posY + 1] = BLANK;
        board[posX - 1][posY + 1] = BLANK;
        board[posX + 1][posY + 1] = BLANK;
        board[posX - 1][posY - 1] = BLANK;
        board[posX + 1][posY - 1] = BLANK;
        publicInfo.get(playerNickname).getField().removeLast();
        publicInfo.get(playerNickname).updatePoints(playerPoints);
        publicInfo.get(playerNickname).updateResources(playerResources);
    }

    //-------------------Game start-----------------------

    /**
     * After receiving the GameStarted message from the server, the method is called to set up the view of the player
     * and initialize the data and the boards of the players.
     */
    @Override
    public void setUpPlayersData() {
        handleEvent(Event.GAME_START,null);  // notify the player that the game is started
        for (String player : players) {
            // set up the data of the players and initialize the board of the players
            publicInfo.put(player, new PlayerPub(null, 0, new ArrayList<>(), new int[]{0, 0, 0, 0, 0, 0, 0},
                    true));
            boards.put(player, new BoardView(new int[]{80, 80, 80, 80}, new String[160][160]));
        }
    }
    /**
     * Once the player receives the MatchStatus message from the server, the method is called by processMessage to
     * update the match status of the player, and print the message to notify the player of the current match status.
     * And if the match status is TERMINATING, the method is called to show the points of all players in the game.
     * @param matchStatus the current match status received from the server
     */
    @Override
    public void updateMatchStatus(int matchStatus) {
        // once received the MatchStatusMessage from the server
        this.Status = Event.getEvent(matchStatus);
        out.println("The match status is: " + Status);
        if(Status.equals(Event.PLAYING)){
            readInputThread();
        }
        if(Status.equals(Event.TERMINATING)){
            for (String player : players) {
                showPoints(player);
            }
        }
    }

    /**
     * Once the player receives the AssignedStarterCardMessage from the server, the method is called by processMessage
     * to request the player to select the side of the starter card they want to use. The player will be able to see
     * the front and back side of the card and select the side they want to use.
     * @param ID the ID of the starter card received from the server and assigned to the player.
     */
    @Override
    public void requestSelectStarterCardSide(int ID) {
        out.println("The starter card received is following");
        showCard(ID, true);
        showCard(ID, false);
        out.println("Please select the side of the card you want to use, type[FRONT or BACK]:");
        String side = in.nextLine();
        while (!side.equals("FRONT") && !side.equals("BACK")) {
            logger.info("Invalid input, please select FRONT or BACK");
            out.println("Invalid input, please select FRONT or BACK");
            side = in.nextLine();
        }
        // If we get to this point, the player has written either FRONT or BACK
        notifyAskListener(new SelectedStarterCardSideMessage(thisPlayerNickname, side.equals("FRONT")));
    }

    /**
     * Once received the ConfirmedStarterCardSideSelectionMessage from the server, the method is called by processMessage to
     * update the view of the player and print the message to notify the player that the starter card is selected
      * @param colour the colour of the player in the game.
     * @param cardID the ID of the starter card selected by the player and received from the server.
     * @param isUp indicates the side of the card selected by the player to be placed.
     * @param availablePos the available positions in the field after the placement of the starter card.
     * @param resources the resources of the player in the field after the placement of the starter card.
     */
    @Override
    public void updateConfirmStarterCard(int colour, int cardID, boolean isUp, ArrayList<int[]> availablePos,
                                         int[] resources) {
        publicInfo.get(thisPlayerNickname).updateColour(convertToColour(colour));
        out.println("Your colour of this game is: " + convertToColour(colour));
        availableSpaces = availablePos;
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
        this.hand = hand;
        out.println("Your common objective cards for this game are following:");
        this.commonObjCards = common;
        showHand(hand);
        showObjectiveCards(common);
        out.println("Please select one of the objective cards in following to be your secret objective card type[LEFT or RIGHT]:");
        showObjectiveCards(secrets);
        String cardID = in.nextLine();
        while (!cardID.equals("LEFT") && !cardID.equals("RIGHT")) {
            logger.info("Invalid input, please select a card from the list");
            out.println("Invalid input, please select a card from the list, type[LEFT or RIGHT]");
            cardID = in.nextLine();
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
    public void updatePlayerData(ArrayList<String> playerNicknames, ArrayList<Boolean> playerConnected,
                                 ArrayList<Integer> playerColours, ArrayList<Integer> playerHand,
                                 int playerSecretObjective, int[] playerPoints,
                                 ArrayList<ArrayList<int[]>> playerFields, int[] playerResources,
                                 ArrayList<Integer> gameCommonObjectives, ArrayList<Integer> gameCurrentResourceCards,
                                 ArrayList<Integer> gameCurrentGoldCards, int gameResourcesDeckSize,
                                 int gameGoldDeckSize, int matchStatus, ArrayList<ChatMessage> chatHistory,
                                 String currentPlayer, ArrayList<int[]> newAvailableFieldSpaces) {
        // after receiving the message from the server, the method is called to set up/initiate the view of the player
        if(currentEvent.equals(Event.RECONNECT_GAME)) {
            this.players = playerNicknames;
            this.hand = playerHand;
            this.commonObjCards = gameCommonObjectives;
            this.secretObjCardSelected = playerSecretObjective;
            this.currentResourceCards = gameCurrentResourceCards;
            this.currentGoldCards = gameCurrentGoldCards;
            this.resourceDeckSize = gameResourcesDeckSize;
            this.goldDeckSize = gameGoldDeckSize;
            this.Status = Event.getEvent(matchStatus);
            this.chatHistory = chatHistory;
            this.currentPlayer = currentPlayer;
            for (int i = 0; i < playerNicknames.size(); i++) {
                publicInfo.put(playerNicknames.get(i), new PlayerPub(convertToColour(playerColours.get(i)),
                        playerPoints[i], new ArrayList<>(), playerResources, playerConnected.get(i)));
                boards.put(playerNicknames.get(i), new BoardView(new int[]{80, 80, 80, 80}, new String[160][160]));
                int finalI = i;
                playerFields.get(i).forEach(card -> {
                    publicInfo.get(playerNicknames.get(finalI)).getField().clear();
                    publicInfo.get(playerNicknames.get(finalI)).addToField(new CardPlacedView(card[2], cardImg.get(card[2]), card[0], card[1], card[3] == 1));
                    updateAfterPlacedCard(playerNicknames.get(finalI), searchNonObjCardById(card[2]), card[0], card[1],
                            card[3] == 1, newAvailableFieldSpaces, playerResources, playerPoints[finalI]);
                });
            }
        }else {
            this.currentResourceCards = gameCurrentResourceCards;
            this.currentGoldCards = gameCurrentGoldCards;
            this.resourceDeckSize = gameResourcesDeckSize;
            this.goldDeckSize = gameGoldDeckSize;
            this.chatHistory = chatHistory;
            this.currentPlayer = currentPlayer;
            int []card;
            PlayerPub playerSpecific;
            for (int i = 1; i < playerNicknames.size(); i++) {
                playerSpecific=publicInfo.get(playerNicknames.get(i));
                playerSpecific.updateColour(convertToColour(playerColours.get(i)));
                playerSpecific.updateResources(playerResources);
                card=playerFields.get(i).get(0);
                playerSpecific.addToField(new CardPlacedView(card[2], cardImg.get(card[2]),
                        card[0], card[1], card[3] == 1));
                updateAfterPlacedCard(playerNicknames.get(i), searchNonObjCardById(card[2]), card[0], card[1],
                        card[3] == 1, newAvailableFieldSpaces, playerResources, playerPoints[i]);
            }
        }
    }
    @Override
    public void updatePlayerTurn(String playerNickname) {
        // once received the PlayerTurnMessage from the server
        out.println("Order of the game: " + players);
        this.currentPlayer = playerNickname;
        if (this.currentPlayer.equals(thisPlayerNickname)) {
            isMyTurn = true;
            requestPlaceCard();
        } else {
            isMyTurn = false;
            out.println("It is " + currentPlayer + "'s turn");
            // create a new thread to get the input from the player when it is not the player's turn
            synchronized (lock) {
                lock.notify();
            }
        }
    }
    public void readInputThread() {
        Thread service = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (isMyTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    isInThread = true;
                    getInput();
                    System.out.println("Exit from service mode");
                }
            }
        });
        service.start();
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
                To choose the right one, type RIGHT""");
        if (isInThread) {
            out.println("It is your turn now, please type something to exit from the service mode,then" +
                    "please select one card from your hand to place on the field:\");");
        }else{
            out.println("Please select one card from your hand to place on the field:");
        }
        String choice = getInput();
        while (!choice.equals("LEFT") && !choice.equals("MIDDLE") && !choice.equals("RIGHT")) {
            logger.info("Invalid input, please select LEFT, MIDDLE or RIGHT");
            out.println("Invalid input, please select LEFT, MIDDLE or RIGHT");
            choice = getInput();
        }
        out.println("You selected the card in the " + choice + " one of your hand:");
        indexCardPlaced = choice.equals("LEFT") ? 0 : choice.equals("MIDDLE") ? 1 : 2; // index of the card selected in hand
        out.println("index should be replaced"+indexCardPlaced);
        showCard(hand.get(indexCardPlaced), true);
        out.println("Do you want to see the back side of the card? type[Y or N]");
        String isUp = getInput();
        while(!isUp.equals("Y") && !isUp.equals("N")) {
            logger.info("Invalid input, please select Y or N");
            out.println("Invalid input, please select Y or N");
            isUp = getInput();
        }
        if( "Y".equals(isUp)) {
            showCard(hand.get(indexCardPlaced), false);
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
            notifyAskListener(new PlaceCardMessage(thisPlayerNickname, hand.get(indexCardPlaced), Integer.parseInt(posArray[0]),
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
            if(Status.equals(Event.PLAYING)||Status.equals(Event.TERMINATING)){
                requestDrawCard();
            }
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
        showDeck();
        String choice = getInput();
        while (!choice.equals("RESOURCE1") && !choice.equals("RESOURCE2") && !choice.equals("GOLD1") &&
                !choice.equals("GOLD2") && !choice.equals("RESOURCE") && !choice.equals("GOLD")) {
            logger.info("Invalid input, please select a card from the list");
            out.println("Invalid input, please try again");
            choice = getInput();
        }
        if (choice.equals("RESOURCE1") || choice.equals("RESOURCE2")) {
            notifyAskListener(new DrawCardMessage(thisPlayerNickname, 2, choice.equals("RESOURCE1") ?
                    currentResourceCards.get(0) : currentResourceCards.get(1)));
            out.println(currentResourceCards.get(0)+currentResourceCards.get(1));
        } else if (choice.equals("GOLD1") || choice.equals("GOLD2")) {
            notifyAskListener(new DrawCardMessage(thisPlayerNickname, 3, choice.equals("GOLD1") ?
                    currentGoldCards.get(0) : currentGoldCards.get(1)));
            out.println(currentGoldCards.get(0)+currentGoldCards.get(1));
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
        int num = publicInfo.get(playerNickname).getField().size();
        publicInfo.get(playerNickname).addToField(new CardPlacedView(card.getID(), cardImg.get(card.getID()), x, y, isUp));
        publicInfo.get(playerNickname).updateResources(resources); // update the resources
        publicInfo.get(playerNickname).updatePoints(points); // update the points
        // represents the sequence of the card placed in the field.
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
            availableSpaces.removeIf(pos -> pos[0] == x && pos[1] == y); //TODO check if exists another way to do this
            for(int[] pos : availableSpaces){ // delete the old available positions in the board.
                posX = -2 * pos[1] + 80;
                posY = 2 * pos[0] + 80;
                updateBoardViewLimits(posX, posY, limits);
                board[posX][posY] = "   ";
                board[posX][posY - 1] = "   ";
                board[posX][posY + 1] = "   ";
            }
            availableSpaces = availablePos; // update the available positions for the next turn of the placement.
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
    public void updateAfterDrawCard(ArrayList<Integer> hand) {
        //out.println("index should be replaced"+indexCardPlaced);
        //out.println("Hand from server"+hand);
        this.hand.remove(indexCardPlaced);
        this.hand.add(indexCardPlaced, hand.getLast());
        //out.println("Hand after added the card from server:"+this.hand);
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
        out.println("The situation of the deck after this turn is following:");
        showDeck();
    }
    @Override
    public void startChatting() {
        chatMode = true;
        out.println("You can start chatting now, your chat history is following:");
        if (chatHistory.isEmpty()) {
            out.println("No chat history yet");
        } else {
            showChatHistory(chatHistory);
        }
        out.println("Please the nickname of the player you want to chat with, type[ALL or specific player nickname] or " +
                "type [EXIT to exit the chat]:");
        out.println("The players in the game are: " + players);
        String recipient = in.nextLine();
        while(true){
            if (!players.contains(recipient) && !recipient.equals("ALL")) {
                if(recipient.equals("EXIT")){
                    chatMode = false;
                    return;
                }
                out.println("Invalid input, please select a player from the list or type ALL");
                recipient = in.nextLine();
            }else break;
        }
        out.println("Please type your message or type[EXIT to exit the chat]:");
        String messageContent = in.nextLine();
        while(true){
            if(messageContent.equals("EXIT")){
                chatMode = false;
                return;
            }
            if (recipient.equals("ALL")) {
                ChatMessage message = new ChatMessage(thisPlayerNickname, "ALL", true, messageContent);
                chatHistory.add(message);
                notifyAskListener(new InboundChatMessage(thisPlayerNickname, recipient, true, messageContent));
            } else {
                ChatMessage message = new ChatMessage(thisPlayerNickname, recipient, false, messageContent);
                chatHistory.add(message);
                notifyAskListener(new InboundChatMessage(thisPlayerNickname, recipient,  false, messageContent));
            }
            messageContent = in.nextLine();
        }
    }
    @Override
    public void showChatHistory(List<ChatMessage> chatHistory){
        for (ChatMessage chat : chatHistory) {
            if(chat.getSenderNickname().equals(thisPlayerNickname)){
                if (!chat.isMulticastFlag()){
                    out.println("You --> " + chat.getRecipientNickname()+":\" "+chat.getMessageContent()+"\"");
                }else {
                    out.println("You --> all players:\" "+chat.getMessageContent()+"\"");
                }
            }
            else if(chat.getRecipientNickname().equals(thisPlayerNickname)){
                out.println(chat.getSenderNickname() + " --> You:\" "+chat.getMessageContent()+"\"");
            }
        }
    }
    @Override
    public void updateChat(String recipientString, String senderNickname, String content){
        ChatMessage message = new ChatMessage(senderNickname, recipientString,false, content);
        chatHistory.add(message);
        if(chatMode){
            out.println("New message received, here is the chat history updated:");
            showChatHistory(this.chatHistory);
        }
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
                Chat:                       
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
                    board[i][j] = BLANK; // if the position is empty will be printed three spaces to remain the layout
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
    @Override
    public void showMatchWinners(ArrayList<String> players, ArrayList<Integer> points, ArrayList<Integer> secrets,
                                 ArrayList<Integer> pointsGainedFromSecrets, ArrayList<String> winners) {
        out.println("The match is ended !!!");
        out.println("The winners of the match are: "+winners);
        out.println("The final points of the players are following:");
        for(int i=0; i<players.size(); i++) {
            out.println("Player " + players.get(i) + " has total points: " + points.get(i)+" with "+
                    pointsGainedFromSecrets.get(i)+ " points gained from this secret objective card:");
            showCard(secrets.get(i), true);
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
    public void handleFailureCase(Event event,String reason){
        out.println(reason);
        switch (event){
            case CHOOSE_CONNECTION -> { // Connection failure
                out.println("Please try again:");
                chooseConnection();
            }
            case CREATE_GAME-> { // Create game failure
                out.println("Please try again:");
                askCreateGame();
            }
            case JOIN_GAME-> { // Join game failure
                out.println("Please try again:");
                askJoinGame();
            }
            case RECONNECT_GAME -> { // Reconnect game failure
                out.println("Please try again:");
                askReconnectGame();
            }
        }
    }

    @Override
    public void handleEvent(Event event,String nickname) { //FIXME IT IS NECESSARY TO IMPLEMENT THIS METHOD in TUI?
        switch (event) {
            case GAME_CREATED -> {
                out.println("Game " + gameID + " created successfully, waiting for other players to join...");
                showPlayerInGame();
            }
            case NEW_PLAYER_JOIN -> {
                out.println("New player join the game :)");
            }
            case GAME_JOINED -> {
                Status = Event.LOBBY;
                out.println("Joined the game "+gameID+ " successfully,waiting for the game to start...");
            }
            case GAME_START -> {
                out.println("YEAH!!! Let's start the game!");
            }
            case GAME_RECONNECTED -> out.println("Reconnected to the game successfully");
            case PLAYER_DISCONNECTED-> {
                out.println("Player "+nickname+" exit from the game");
                publicInfo.get(nickname).updateOnline(false);
            }
            case PLAYER_RECONNECTED -> {
                out.println("Player "+nickname+" reconnected to the game");
                publicInfo.get(nickname).updateOnline(true);
            }
        }
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

    public synchronized String getInput() {
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
                out.println("Which card place in the field, you want to see?");
                /*String card = getInput();
                CardPlacedView cardPlaced = publicInfo.get(thisPlayerNickname).getField().get(Integer.parseInt(card));
                showCard(cardPlaced.ID(),cardPlaced.side());*/
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
            case "Chat" -> startChatting();
        }
        return input;
    }

    /**
     * Loops until the user enters a valid single integer.
     * Inputs such as "string 5", "4 string", "string" are all invalid.
     *
     * @return Integer input from the user
     */
    private int getInputInt() {
        int choice;
        while (true) { // Loop until the user enters a valid integer
            try {
                choice = in.nextInt();

                String remainder = in.nextLine(); // Consume the rest of the line
                if (!remainder.isBlank()) { // If there is anything left on the line
                    out.println("Invalid input, please type a single integer");
                    continue;
                }

                // If the input is valid, return it
                return choice;
            } catch (InputMismatchException e) { // If the input is not an integer
                out.println("Invalid input, please type a number");
                in.nextLine(); // Consume the invalid input
            }
        }
    }
}







