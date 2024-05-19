package it.polimi.ingsw.am32.model.match;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.exceptions.*;
import it.polimi.ingsw.am32.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.TestTemplate;

class MatchSimulationTest {
    Match myMatch = new Match(); // Create a new match
    private static final Logger LOGGER = LogManager.getLogger("MatchSimulationLogger");

    @DisplayName("Run a, partial, game simulation in order to test the game mechanics")
    @TestTemplate
    @RepeatedTest(value = 16, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    public void runGameSimulation() {
        LOGGER.info("-------------------- Starting game simulation --------------------");

        Random rand = new Random(); // Crate new random number generator

        double flippedCardWeight = 0.15; // Probability that a card is placed on its back (excluding starting card)
        double pickingResourceCardWeight = 0.3; // Probability of picking a resource card after placing

        // Lobby phase
        myMatch.enterLobbyPhase(); LOGGER.info("Entered lobby phase");
        // Initialize the list of players
        int numPlayers = rand.nextInt(3) + 2; // Randomly select the number of players;
        switch (numPlayers) {
            case 2:
                assertDoesNotThrow(()->myMatch.addPlayer("Alice"));
                assertDoesNotThrow(()->myMatch.addPlayer("Bob"));
                break;
            case 3:
                assertDoesNotThrow(()->myMatch.addPlayer("Alice"));
                assertDoesNotThrow(()->myMatch.addPlayer("Bob"));
                assertDoesNotThrow(()->myMatch.addPlayer("Carlo"));
                break;
            case 4:
                assertDoesNotThrow(()->myMatch.addPlayer("Alice"));
                assertDoesNotThrow(()->myMatch.addPlayer("Bob"));
                assertDoesNotThrow(()->myMatch.addPlayer("Carlo"));
                assertDoesNotThrow(()->myMatch.addPlayer("Daniel"));
                break;
        } LOGGER.info("Generated players");

        // Preparation phase
        myMatch.enterPreparationPhase(); LOGGER.info("Entered preparation phase");
        myMatch.assignRandomColoursToPlayers(); LOGGER.info("Assigned random colours to players");
        myMatch.assignRandomStartingInitialCardsToPlayers(); LOGGER.info("Assigned random initial cards to players");
        // Create Field for players
        try {
            for (Player player : myMatch.getPlayers()) {
                boolean randomSide = rand.nextBoolean();
                myMatch.createFieldPlayer(player.getNickname(), randomSide);
            } LOGGER.info("Created player fields");
        } catch(PlayerNotFoundException e) {
            fail();
        }
        // Assign random starting resource cards, gold cards, common objectives and secret objectives to players
        myMatch.assignRandomStartingResourceCardsToPlayers(); LOGGER.info("Assigned random starting resource cards to players");
        myMatch.assignRandomStartingGoldCardsToPlayers(); LOGGER.info("Assigned random starting gold cards to players");
        myMatch.pickRandomCommonObjectives(); LOGGER.info("Picked random common objective");
        myMatch.assignRandomStartingSecretObjectivesToPlayers(); LOGGER.info("Assigned random starting secret objective cards to players");
        for(Player player : myMatch.getPlayers()) {
            try{
                myMatch.receiveSecretObjectiveChoiceFromPlayer(player.getNickname(), myMatch.getSecretObjectiveCardsPlayer(player.getNickname()).getFirst());
            } catch (InvalidSelectionException | PlayerNotFoundException e) {
                fail();
            }
        } LOGGER.info("Received secret objective choice from players");

        myMatch.randomizePlayersOrder(); LOGGER.info("Randomized player order");

        // Playing phase
        myMatch.enterPlayingPhase(); LOGGER.info("Entered playing phase");
        myMatch.startTurns(); logGameState("Started game");

        while (myMatch.getMatchStatus() != MatchStatus.TERMINATED.getValue()) {
            // Keep looping until the match is terminated
            // Simulate a game turn
            for (Player player : myMatch.getPlayers()) { logGameState("Looping through players");
                // Loops through each player
                if (!player.getNickname().equals(myMatch.getCurrentPlayerNickname())) { logGameState("Access violation detected");
                    continue; // The selected player doesn't have playing rights
                }
                if (myMatch.isFirstPlayer()) { // The first player is playing
                    if (myMatch.areWeTerminating()) { // We are in the terminating phase
                        myMatch.setLastTurn(); logGameState("Entered last turn"); // If we were in the terminating phase, and the first player has played, we play the last turn
                    } else if (myMatch.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) { // If we were in the last turn phase, and we looped back to the first player, we've finished the game
                        myMatch.enterTerminatedPhase(); logGameState("The game is ended");
                        break; // Game is finished
                    }
                } logGameState("We are starting the player's turn");
                boolean successful = false; // Flag indicating whether card placement was successful
                boolean noPossibleMove = false; // Flag indicating whether the player cannot make a move on their field due to lack of space

                // Placing card phase
                while (!successful) { // Keep looping until a valid move is found
                    ArrayList<int[]> availablePos = player.availableSpacesPlayer(); // Get all the available positions in the player's field
                    int[] randomCoordinate = availablePos.get(rand.nextInt(availablePos.size())); // Get a random available space

                    if (availablePos.isEmpty()) { // If the player cannot make any move
                        noPossibleMove = true; LOGGER.info("No possible move are left!");
                        break;
                    }

                    NonObjectiveCard randomHandCard = player.getHand().get(rand.nextInt(player.getHand().size())); // Get a random card from the player's hand
                    boolean randomSide = Math.random() > flippedCardWeight; // Get a random placement side for the card

                    // Attempt to place a card
                    try {
                        myMatch.placeCard(randomHandCard.getId(), randomCoordinate[0], randomCoordinate[1], randomSide);
                        successful = true;
                    } catch (InvalidSelectionException | MissingRequirementsException | InvalidPositionException |
                             PlayerNotFoundException e) {
                        successful = false;
                    }
                    LOGGER.info("Attempted to place card: " + randomHandCard.getId() + " at " + randomCoordinate[0] + ", " + randomCoordinate[1] + " with side " + randomSide);
                } logGameState("Placed card");

                boolean revertedPlacement = Math.random() < 0.2; // Probability of reverting the placement
                if (revertedPlacement) { // If the placement is reverted
                    try {
                        myMatch.rollbackPlacement();
                    } catch (PlayerNotFoundException | RollbackException e) {
                        fail();
                    }
                    logGameState("Reverted placement");
                } else {
                    logGameState("Confirmed placement");
                }

                // Drawing phase
                if (myMatch.getMatchStatus()!=MatchStatus.LAST_TURN.getValue() && !noPossibleMove && !revertedPlacement) {
                    // We enter the drawing phase only if we are not on the last round of turns, and if we previously managed to place a card
                    // Draw a random card
                    boolean validDraw = false; // Flag indicating whether the draw was valid
                    while (!validDraw) { // Keep looping until a valid draw is made
                        int randomType; // Randomly select the type of card to draw (from which deck to draw)

                        double num = Math.random(); // Get a number between 0 and 1
                        // Randomly select the type of card to draw with set weight
                        if (num < pickingResourceCardWeight / 2)
                            randomType = 0; // Trying to draw from resource cards deck
                        else if (num < pickingResourceCardWeight) randomType = 2; // Trying to draw from resource cards on field
                        else if (num < (pickingResourceCardWeight + 1) / 2) randomType = 1; // Trying to draw from gold cards deck
                        else randomType = 3; // Trying to draw from gold cards on field

                        int randomCurrentCard;

                        switch (randomType) {
                            case 2:
                                // If the random type is 2, draw a resource card from the current resource cards.

                                // If the current resource cards are empty the corresponding deck must also be empty.
                                if(myMatch.getCurrentResourcesCards().isEmpty()){
                                    // If the current gold cards are also empty we don't have any card to draw
                                    if(myMatch.getCurrentGoldCards().isEmpty()){
                                        LOGGER.fatal("All decks are empty");
                                        fail();
                                    }
                                    randomType = 3;
                                    randomCurrentCard = myMatch.getCurrentGoldCards().get(rand.nextInt(myMatch.getCurrentGoldCards().size())); // Randomly select a gold card
                                    break;
                                }

                                randomCurrentCard = myMatch.getCurrentResourcesCards().get(rand.nextInt(myMatch.getCurrentResourcesCards().size())); // Randomly select a resource card
                                break;
                            case 3:
                                // If the random type is 3, draw a gold card from the current gold card

                                // If the current gold cards are empty the corresponding deck must also be empty.
                                if(myMatch.getCurrentGoldCards().isEmpty()){
                                    // If the current resource card are also empty we don't have any card to draw
                                    if(myMatch.getCurrentResourcesCards().isEmpty()){
                                        LOGGER.fatal("All decks are empty");
                                        fail();
                                    }
                                    randomType = 2;
                                    randomCurrentCard = myMatch.getCurrentResourcesCards().get(rand.nextInt(myMatch.getCurrentResourcesCards().size())); // Randomly select a resource card
                                    break;
                                }

                                randomCurrentCard = myMatch.getCurrentGoldCards().get(rand.nextInt(myMatch.getCurrentGoldCards().size())); // Randomly select a gold card
                                break;
                            default:
                                randomCurrentCard = 0;
                                break;
                        }

                        try {
                            myMatch.drawCard(randomType, randomCurrentCard);
                        } catch (PlayerNotFoundException e) {
                            fail();
                        } catch (DrawException e) {
                            continue;
                        }

                        // If the all decks are empty, we have a problem with drawCard
                        if (myMatch.getResourceCardsDeck().isEmpty() && myMatch.getGoldCardsDeck().isEmpty() &&
                            myMatch.getCurrentResourcesCards().isEmpty() && myMatch.getCurrentGoldCards().isEmpty()) { LOGGER.fatal("All decks are empty");
                            fail();
                        }

                        validDraw = true;
                    } logGameState("Drew card");
                }
            myMatch.nextTurn();
            }
        } logGameState("Terminated game");

        // Terminated phase
        assertDoesNotThrow(() -> myMatch.addObjectivePoints()); logGameState("Added objective points to players");

        ArrayList<String> winners = myMatch.getWinners(); logGameState("Calculated winners");
        LOGGER.info("The winner is : " + winners);
    }

    /**
     * A special method used to log the entire state of the game
     * @param debugString Info string used to understand at what line of code game was logged
     *
     * @author Lorenzo
     */
    public void logGameState(String debugString){
        try {
            LOGGER.info("#########################################################");

            // Log the match status
            LOGGER.info("Debugger status: " + debugString);
            LOGGER.info("Match status: " + myMatch.getMatchStatus());

            // Log the current turn number
            LOGGER.info("Current turn number: " + myMatch.getCurrentTurnNumber());
            // Log the current player
            LOGGER.info("Current player: " + myMatch.getCurrentPlayerNickname());
            // Log if the current player is the first player
            LOGGER.info("Is first player: " + myMatch.isFirstPlayer());

            // Log the common objectives
            LOGGER.info("Common objectives: " + myMatch.getCommonObjectives());
            // Log the current resource cards
            LOGGER.info("Current resource cards: " + myMatch.getCurrentResourcesCards());
            // Log the current gold cards
            LOGGER.info("Current gold cards: " + myMatch.getCurrentGoldCards());

            // Log how many cards are in the resource deck
            LOGGER.info("Resource deck size: " + myMatch.getResourceCardsDeck().size());
            // Log how many cards are in the gold deck
            LOGGER.info("Gold deck size: " + myMatch.getGoldCardsDeck().size());

            // Log the players
            for (Player player : myMatch.getPlayers()) {
                LOGGER.info("=====================================");
                LOGGER.info("Player: " + player.getNickname());
                LOGGER.info("Player's colour: " + myMatch.getPlayerColour(player.getNickname()));
                LOGGER.info("Player's initial cards: " + myMatch.getInitialCardPlayer(player.getNickname()));
                LOGGER.info("Player's secret objectives: " + myMatch.getSecretObjectiveCardsPlayer(player.getNickname()));
                LOGGER.info("Player's resources: " + Arrays.toString(myMatch.getPlayerResources(player.getNickname())));
                LOGGER.info("Player's hand: " + myMatch.getPlayerHand(player.getNickname()));
                LOGGER.info("Player's points: " + myMatch.getPlayerPoints(player.getNickname()));
                LOGGER.info("Player's field: ");
                for (int[] subarray : myMatch.getPlayerField(player.getNickname())) {
                    LOGGER.info("\t" + Arrays.toString(subarray));
                }
            }
            LOGGER.info("=====================================");
        } catch (Exception e) {
            LOGGER.fatal(e.getMessage());
        }
    }
}
