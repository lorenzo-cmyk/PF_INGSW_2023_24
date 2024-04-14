package it.polimi.ingsw.am32.model.match;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.player.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MatchSimulationTest {
    Match myMatch = new Match(); // Create a new match

    @DisplayName("Run a, partial, game simulation in order to test the game mechanics")
    @Test
    public void runGameSimulation() {
        Random rand = new Random(); // Crate new random number generator

        double flippedCardWeight = 0.15; // Probability that a card is placed on its back (excluding starting card)
        double pickingResourceCardWeight = 0.3; // Probability of picking a resource card after placing

        // Lobby phase
        myMatch.enterLobbyPhase();
        // Initialize the list of players
        int numPlayers = rand.nextInt(3) + 2; // Randomly select the number of players;
        System.out.println("Number of players: " + numPlayers); //Todo: remove
        switch (numPlayers) {
            case 2:
                assertTrue(myMatch.addPlayer("Alice"));
                assertTrue(myMatch.addPlayer("Bob"));
                break;
            case 3:
                assertTrue(myMatch.addPlayer("Alice"));
                assertTrue(myMatch.addPlayer("Bob"));
                assertTrue(myMatch.addPlayer("Carlo"));
                break;
            case 4:
                assertTrue(myMatch.addPlayer("Alice"));
                assertTrue(myMatch.addPlayer("Bob"));
                assertTrue(myMatch.addPlayer("Carlo"));
                assertTrue(myMatch.addPlayer("Daniel"));
                break;
        }

        // Preparation phase
        myMatch.enterPreparationPhase();
        myMatch.assignRandomColoursToPlayers();
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Create Field for players
        for (Player player : myMatch.getPlayers()) {
            boolean randomSide = rand.nextBoolean();
            myMatch.createFieldPlayer(player.getNickname(), randomSide);
        }
        // Assign random starting resource cards, gold cards, common objectives and secret objectives to players
        myMatch.assignRandomStartingResourceCardsToPlayers();
        myMatch.assignRandomStartingGoldCardsToPlayers();
        myMatch.pickRandomCommonObjectives();
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        for(Player player : myMatch.getPlayers()) {
            myMatch.receiveSecretObjectiveChoiceFromPlayer(player.getNickname(), myMatch.getSecretObjectiveCardsPlayer(player.getNickname()).get(1));
        }

        myMatch.randomizePlayersOrder();

        // Playing phase
        myMatch.enterPlayingPhase();
        myMatch.startTurns();
        System.out.println("The first player is: " + myMatch.getCurrentPlayerID()); //Todo: remove

        while (myMatch.getMatchStatus() != MatchStatus.TERMINATED.getValue()) { // Keep looping until the match is terminated
            // Simulate a game turn
            for (Player player : myMatch.getPlayers()) { // Loops through each player
                if (!player.getNickname().equals(myMatch.getCurrentPlayerID()))
                    continue; // The selected player doesn't have playing rights
                if (myMatch.isFirstPlayer()) { // The first player is playing
                    if (myMatch.areWeTerminating()) { // We are in the terminating phase
                        myMatch.setLastTurn(); // If we were in the terminating phase, and the first player has played, we play the last turn
                    } else if (myMatch.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) { // If we were in the last turn phase, and we looped back to the first player, we've finished the game
                        myMatch.enterTerminatedPhase();
                        break; // Game is finished
                    }
                }
                System.out.println("The current player is: " + player.getNickname()); //Todo: remove
                System.out.println("The match status is: " + myMatch.getMatchStatus()); //Todo: remove
                boolean successful = false; // Flag indicating whether card placement was successful
                boolean noPossibleMove = false; // Flag indicating whether the player cannot make a move on their field due to lack of space

                while (!successful) { // Keep looping until a valid move is found
                    ArrayList<int[]> availablePos = availableSpacesPlayer(player); // Get all the available positions in the player's field
                    int[] randomCoordinate = availablePos.get(rand.nextInt(availablePos.size())); // Get a random available space

                    if (availablePos.isEmpty()) { // If the player cannot make any move
                        noPossibleMove = true;
                        break;
                    }

                    NonObjectiveCard randomHandCard = player.getHand().get(rand.nextInt(player.getHand().size())); // Get a random card from the player's hand
                    boolean randomSide = Math.random() > flippedCardWeight; // Get a random placement side for the card

                    successful = myMatch.placeCard(randomHandCard.getId(), randomCoordinate[0], randomCoordinate[1], randomSide); // Attempt to place the card
                }
                System.out.println("The match status after placedCard is " + myMatch.getMatchStatus()); //Todo: remove
                // Drawing phase
                if(myMatch.getMatchStatus()!=MatchStatus.LAST_TURN.getValue()) { // If we are not in the last turn phase
                    if (!noPossibleMove) { // Enter drawing phase only if the player managed to put down a card
                        // Draw a random card
                        boolean validDraw = false; // Flag indicating whether the draw was valid

                        while (!validDraw) { // Keep looping until a valid draw is made
                            boolean expectedOutcome;
                            int randomType; // Randomly select the type of card to draw

                            double num = Math.random(); // Get a number between 0 and 1

                            if (num < pickingResourceCardWeight / 2)
                                randomType = 0; // Randomly select the type of card to draw
                            else if (num < pickingResourceCardWeight) randomType = 2;
                            else if (num < (pickingResourceCardWeight + 1) / 2) randomType = 1;
                            else randomType = 3;

                            int randomCurrentCard;

                            switch (randomType) {
                                case 0:
                                    randomCurrentCard = 0;
                                    if (myMatch.getResourceCardsDeck().isEmpty()) {
                                        expectedOutcome = false;
                                        break;
                                    }
                                    expectedOutcome = true;
                                    break;
                                case 1:
                                    randomCurrentCard = 0;
                                    if (myMatch.getGoldCardsDeck().isEmpty()) {
                                        expectedOutcome = false;
                                        break;
                                    }
                                    expectedOutcome = true;
                                    break;
                                case 2:
                                    // If the random type is 2, draw a resource card from the current resource cards.
                                    // If the current resource cards are empty check that resource deck is also empty otherwise we have a problem with drawCard.
                                    if (myMatch.getCurrentResourcesCards().isEmpty()) {
                                        expectedOutcome = false;
                                        randomCurrentCard = 0;
                                        break;
                                    }
                                    randomCurrentCard = myMatch.getCurrentResourcesCards().get(rand.nextInt(myMatch.getCurrentResourcesCards().size())); // Randomly select a resource card
                                    expectedOutcome = true;
                                    break;
                                case 3: // If the random type is 3, draw a gold card from the current gold cards
                                    if (myMatch.getCurrentGoldCards().isEmpty()) {
                                        expectedOutcome = false;
                                        randomCurrentCard = 0;
                                        break;
                                    }
                                    randomCurrentCard = myMatch.getCurrentGoldCards().get(rand.nextInt(myMatch.getCurrentGoldCards().size())); // Randomly select a gold card
                                    expectedOutcome = true;
                                    break;
                                default:
                                    randomCurrentCard = 0;
                                    expectedOutcome = false;
                                    break;
                            }
                            //Todo: remove
                            System.out.println(myMatch.getResourceCardsDeck().size());
                            System.out.println(myMatch.getGoldCardsDeck().size());
                            System.out.println(myMatch.getCurrentResourcesCards().size());
                            System.out.println(myMatch.getCurrentGoldCards().size());
                            System.out.println("Looping inside draw!");

                            assert (myMatch.drawCard(randomType, randomCurrentCard) == expectedOutcome);
                            // If the all decks are empty, we have a problem with drawCard
                            if(myMatch.getResourceCardsDeck().isEmpty()&&myMatch.getGoldCardsDeck().isEmpty()&&myMatch.getCurrentResourcesCards().isEmpty()&&myMatch.getCurrentGoldCards().isEmpty()) {
                                fail();
                            }
                            validDraw = expectedOutcome;
                            System.out.println("The match status is: " + myMatch.getMatchStatus()); //Todo: remove
                        }
                    }
                }
                myMatch.nextTurn();
                System.out.println("-----------"); //Todo: remove
                }
            }

            // Terminated phase
            System.out.println("The match status is: " + myMatch.getMatchStatus()); //Todo: remove
            assertTrue(myMatch.addObjectivePoints());

            ArrayList<String> winners = myMatch.getWinners();
            System.out.println("The winners are: " + winners); //Todo: remove
            for(Player player : myMatch.getPlayers()) { //Todo: remove
                System.out.println(player.getNickname() + " has " + player.getPoints() + " points" + " and has " + player.getPointsGainedFromObjectives()); //Todo: remove
            }

        }

        /**
         * Returns the available spaces where a card can be played in the given player's field
         * @param player The player whose field we want to check
         * @return An ArrayList of int arrays containing the available spaces
         */
        public ArrayList<int[]> availableSpacesPlayer (@NotNull Player player){
            ArrayList<int[]> availableCoordinate = new ArrayList<>(); // Create a new ArrayList of int arrays to store the available coordinates
            for (int j = 0; j < player.getField().getFieldCards().size(); j++) { // Loop through all the cards in the player's field
                int Ax, Ay;
                Ax = player.getField().getFieldCards().get(j).getX();
                Ay = player.getField().getFieldCards().get(j).getY();
                if (player.getField().availableSpace(Ax+1,Ay+1)) { // Check if the space to the right and below the card is available
                    int[] temp = new int[2];
                    temp[0] = Ax + 1;
                    temp[1] = Ay + 1;
                    availableCoordinate.add(temp);
                }
                if (player.getField().availableSpace(Ax - 1, Ay - 1)) { // Check if the space to the left and above the card is available
                    int[] temp = new int[2];
                    temp[0] = Ax - 1;
                    temp[1] = Ay - 1;
                    availableCoordinate.add(temp);
                }
                if (player.getField().availableSpace(Ax + 1, Ay - 1)) { // Check if the space to the right and above the card is available
                    int[] temp = new int[2];
                    temp[0] = Ax + 1;
                    temp[1] = Ay - 1;
                    availableCoordinate.add(temp);
                }
                if (player.getField().availableSpace(Ax - 1, Ay + 1)) { // Check if the space to the left and below the card is available
                    int[] temp = new int[2];
                    temp[0] = Ax - 1;
                    temp[1] = Ay + 1;
                    availableCoordinate.add(temp);
                }
            }
            return availableCoordinate;
        }
}