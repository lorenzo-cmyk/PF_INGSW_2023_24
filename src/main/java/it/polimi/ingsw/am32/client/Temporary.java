package it.polimi.ingsw.am32.client;

import it.polimi.ingsw.am32.Utilities.IsValid;
import it.polimi.ingsw.am32.client.view.tui.BoardView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Temporary implements Runnable{
    //TODO: SHOULD BE REMOVED THIS CLASS, USED ONLY FOR TEST THE PRINT OF THE TUI
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
    private static final String INSECT ="\uD83E\uDD8B";   // Unicode for the corner type, object and resource of the card
    private static final String PLANT = "\uD83C\uDF3F";
    private static final String FUNGI = "\uD83C\uDF44";
    private static final String ANIMAL = "\uD83D\uDC3A";
    private static final String QUILL = "\uD83E\uDEB6";
    private static final String INKWELL = "\uD83C\uDF6F";
    private static final String MANUSCRIPT = "\uD83D\uDCDC";
    private static final String NON_COVERABLE = "\u274C";
    private static final String EMPTY = "  ";
    protected String thisPlayerNickname;
    protected int gameID; //save the game ID received from the NewGameConfirmationMessage or AccessGameConfirmMessage.
    protected int playerNum; //number of players connected to the game, if the player is disconnected, the number will
    // decrease.
    protected ArrayList<String> players; //save and update the players in the game.
    protected String currentPlayer; //save and update the current player by receiving the message from the server.
    protected Event currentEvent; //TODO: not sure if this is useful
    protected ArrayList<Integer> commonObjCards;
    protected int[] secretObjCards;
    protected int secretObjCardSelected;
    protected int starterCard;
    protected ArrayList<Integer> hand;
    protected ArrayList<Integer> currentResourceCards;
    protected ArrayList<Integer>  currentGoldCards;
    protected int resourceDeckSize;
    protected int goldDeckSize;
    protected String Status;
    protected ArrayList<int[]> availableSpaces;
    protected HashMap<String,PlayerPub> publicInfo; //save the colour, nickname, points and resources of the player.
    protected static ArrayList<ObjectiveCardFactory> objectiveCards = ObjectiveCardFactory.setObjectiveCardArray();
    protected static final ArrayList<NonObjCardFactory> nonObjCards = NonObjCardFactory.setNonObjCardArray();
    protected static final HashMap<Integer, ArrayList<String>> cardImg = setImg();
    private static final Logger logger = LogManager.getLogger("TUILogger");

    public Temporary(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
        this.boards = new HashMap<>();
        this.publicInfo = new HashMap<>();
    }
    public static void main(String[] args) {
        new Temporary(new Scanner(System.in), System.out).run();
    }
    @Override
    public void run() {
        //test the print in one line
        for(int i = 0; i<7;i++){
            out.println(cardImg.get(87).get(i)+" "+cardImg.get(88).get(i)+" "+cardImg.get(89).get(i));
        }
        //test the print of NonObjCards
        for(int i = 1; i<87;i++) {
            for (int j = 0; j < 7; j++) {
                out.println(cardImg.get(i).get(j));
            }
            for (int j = 7; j < 14; j++) {
                out.println(cardImg.get(i).get(j));
            }
        }
        //test the print of ObjectiveCards
        for(int i = 87; i<102;i++) {
            for (int j = 0; j < 7; j++) {
                out.println(cardImg.get(i).get(j));
            }
        }

        thisPlayerNickname = "player1";
        players=new ArrayList<>();
        players.add("player1");
        players.add("player2");
        publicInfo.put("player1",new PlayerPub("null",0,new ArrayList<>(),new int[]{0,0,0,0,0,0,0},true));
        boards.put("player1",new BoardView(new int[]{80,80,80,80},new String[160][160]));
        ArrayList<int[]>availablePos = new ArrayList<>();
        availablePos.add(new int[]{0,0});
        availablePos.add(new int[]{1,1});
        availablePos.add(new int[]{1,-1});
        availablePos.add(new int[]{-1,1});
        availablePos.add(new int[]{-1,-1});
        availablePos.add(new int[]{2,2});
        availablePos.add(new int[]{3,3});
        availableSpaces= availablePos;
        availablePos.add(new int[]{4,4});
        availablePos.add(new int[]{-2,-2});
        availablePos.add(new int[]{-3,-3});
        updateAfterPlacedCard("player1",nonObjCards.get(85),0,0,false,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(5),1,1,false,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(15),1,-1,false,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(25),-1,1,false,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(35),-1,-1,false,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(45),2,2,true,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(55),3,3,true,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(65),4,4,true,availablePos,new int[]{0,0,0},0);
        updateAfterPlacedCard("player1",nonObjCards.get(75),-2,-2,true,availablePos,new int[]{0,0,0},0);
        printBoard("player1");
        out.println("The players in the game are: "+players);
        int res[] = new int[]{0,0,2,0,1,0,0};
        publicInfo.get("player1").updateResources(res);
        showPoints("player1");
        out.println("Your colour of this game is: "+convertToColour(2));
    }
    public void showPoints(String playerNickname) {
        int [] resources = publicInfo.get(playerNickname).getResources();
        out.println("Player"+playerNickname+" has: "+publicInfo.get(playerNickname).getPoints()+" points"+
                "\nwith "+resources[0] + iconArrayElement(0)+resources[1]+iconArrayElement(1)+resources[2] +
                iconArrayElement(2)+resources[3] + iconArrayElement(3)+resources[4] +
                iconArrayElement(4)+resources[5] + iconArrayElement(5)+resources[6]+
                iconArrayElement(6)+" in field");
    }
    private void printBoard(String nickname){
        BoardView boardView= this.boards.get(nickname);
        String[][] board= boardView.getBoard();
        int[] limits = boardView.getLimits();
        for(int i=limits[1];i<=limits[0];i++){
            for(int j=limits[3];j<=limits[2];j++){
                if(board[i][j]==null){
                    board[i][j]="   ";
                }
                out.print(board[i][j]);
            }
            out.println();
        }
    }
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

    /**
     *
     * @param ID
     * @return
     */
    private ObjectiveCardFactory searchObjCardById(int ID) {
        try{
            for( ObjectiveCardFactory objectiveCard: objectiveCards){
                if(objectiveCard.getID()==ID){
                    return objectiveCard;
                }
            }
        }catch (RuntimeException e){
            logger.error("The card is not found");
        }
        return null;
    }

    /**
     *
     * @param ID
     * @return
     */
    private NonObjCardFactory searchNonObjCardById(int ID){
        try{
            for( NonObjCardFactory nonObjCard: nonObjCards){
                if (nonObjCard.getID()==ID){
                    return nonObjCard;
                }
            }
        }catch (RuntimeException e){
            logger.error("The card is not found");
        }
        return null;
    }



    private static HashMap<Integer, ArrayList<String>> setImg() {
        HashMap<Integer, ArrayList<String>> Img = new HashMap<>();
        for (NonObjCardFactory card: nonObjCards) {
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
        for( ObjectiveCardFactory card: objectiveCards) {
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
     * @param conditionCount the integer array of the requirement count of the card.
     * @return the string of icons which contains the icons of the requirements of the card.
     */
    private static String iconArray(int[] conditionCount) {
        StringBuilder condition= new StringBuilder();
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
     * @param type the corner type of the card.
     * @return the icon of the corner type of the card.
     */
    private static String icon(String type) {
        String icon;
        switch (type) {
            case "INSECT" -> icon= INSECT;
            case "PLANT" ->  icon= PLANT;
            case "FUNGI" -> icon= FUNGI;
            case "ANIMAL" ->  icon= ANIMAL;
            case "QUILL" -> icon= QUILL;
            case "INKWELL" ->  icon= INKWELL;
            case "MANUSCRIPT" -> icon= MANUSCRIPT;
            case "NON_COVERABLE" -> icon= NON_COVERABLE;
            default -> icon= "  ";
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
            case 0 -> icon= PLANT;
            case 1 -> icon= FUNGI;
            case 2 -> icon= ANIMAL;
            case 3 -> icon= INSECT;
            case 4 -> icon= QUILL;
            case 5 -> icon= INKWELL;
            case 6 -> icon=MANUSCRIPT;
            default -> icon= EMPTY;
        }
        return icon;
    }
    /**
     * Method used to convert the colour of the card to an icon, using the Unicode characters.
     */
    private static String iconCard(String type) {
        String icon;
        switch (type) {
            case "PLANT" -> icon= new String(Character.toChars(PLANTCARD));
            case "FUNGI" -> icon= new String(Character.toChars(FUNGICARD));
            case "ANIMAL" -> icon= new String(Character.toChars(ANIMALCARD));
            case "INSECT" -> icon= new String(Character.toChars(INSECARD));
            default -> icon= new String(Character.toChars(STARTERCARD));
        }
        return icon;
    }
    //-------------------utilities-------------------

    /**
     * Used method to update the dimensions of the board view of the player after placing a card on the board. The
     * method is called by the {@link #updateAfterPlacedCard} method.
     * @param posX the x coordinate of the card placed.
     * @param posY the y coordinate of the card placed.
     * @param limits the array of the limits contains the maximum x and y and the minimum x and y updated.
     */
    private void updateBoardViewLimits(int posX, int posY, int[] limits){
        limits[0]=Math.max(limits[0],posX+1);
        limits[1]=Math.min(limits[1],posX-1);
        limits[2]=Math.max(limits[2],posY+1);
        limits[3]=Math.min(limits[3],posY-1);
    }
    public String convertToColour(int colour){ //FIXME should I create a enum for the colours?
        switch (colour){
            case 0 -> {
                return ANSI_RED+"RED"+ANSI_RESET;
            }
            case 1 -> {
                return ANSI_GREEN+"GREEN"+ANSI_RESET;
            }
            case 2 -> {
                return ANSI_BLUE+"BLUE"+ANSI_RESET;
            }
            case 3 -> {
                return ANSI_YELLOW+"YELLOW"+ANSI_RESET;
            }
            case 4 -> {
                return ANSI_BLACK+"BLACK"+ANSI_RESET;
            }
            default -> {
                return null;
            }
        }
    }
}
