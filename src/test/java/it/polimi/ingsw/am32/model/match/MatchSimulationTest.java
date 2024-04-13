package it.polimi.ingsw.am32.model.match;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MatchSimulationTest {
    Match myMatch = new Match();

    @DisplayName("Run a, partial, game simulation in order to test the game mechanics")
    @Test
    public void runGameSimulation() {
        //Lobby phase
        myMatch.enterLobbyPhase();

        // Initialize the list of players

        assertTrue(myMatch.addPlayer("Alice"));
        assertTrue(myMatch.addPlayer("Bob"));
        assertTrue(myMatch.addPlayer("Carlo"));
        assertTrue(myMatch.addPlayer("Daniel"));

        // Preparation phase
        myMatch.enterPreparationPhase();
        myMatch.assignRandomColoursToPlayers();

        myMatch.assignRandomStartingInitialCardsToPlayers();

        // Create Field for players
        for (Player player : myMatch.getPlayers()) {
            Random rand = new Random();
            boolean randomSide = rand.nextBoolean();
            myMatch.createFieldPlayer(player.getNickname(), randomSide);
        }

        myMatch.assignRandomStartingResourceCardsToPlayers();
        myMatch.assignRandomStartingGoldCardsToPlayers();
        myMatch.pickRandomCommonObjectives();
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        myMatch.randomizePlayersOrder();

        // Playing phase
        myMatch.enterPlayingPhase();
        myMatch.startTurns();

        while (myMatch.getMatchStatus() != MatchStatus.TERMINATED.getValue()) { // We keep playing until we've rea
            // Simulate a game turn
            for (Player player : myMatch.getPlayers()) { // Loops through each player
                if (!player.getNickname().equals(myMatch.getCurrentPlayerID()))
                    continue; // The selected player doesn't have playing rights

                if (myMatch.isFirstPlayer()) { // The first player is playing
                    if (myMatch.areWeTerminating()) { // We are in the terminating phase
                        myMatch.setLastTurn(); // If we were in the terminating phase, and the first player has played, we play the last turn
                    } else if (myMatch.getMatchStatus() == MatchStatus.LAST_TURN.getValue()) { // If we were in the last turn phase, and we looped back to the first player, we've finished the game
                        myMatch.enterTerminatedPhase();
                        break;
                    }
                }

                boolean successful = false; // Flag indicating whether card placement was successful
                Random rand = new Random(); // Crate new random number generator
                while (!successful) { // Keep looping until a valid move is found
                    ArrayList<int[]> availablePos = availableSpacesPlayer(player); // Get all the available positions in the player's field
                    int[] randomCoordinate = availablePos.get(rand.nextInt(availablePos.size())); // Get a random available space

                    NonObjectiveCard randomHandCard = player.getHand().get(rand.nextInt(player.getHand().size())); // Get a random card from the player's hand
                    boolean randomSide = rand.nextBoolean();// Get a random side in which to place the card
                    successful = player.performMove(randomHandCard.getId(),randomCoordinate[0], randomCoordinate[1], randomSide);// Attempt to place the card
                }

                // Draw a random card
                int randomType = rand.nextInt(4); // Randomly select the type of card to draw
                int randomCurrentCard;

                switch (randomType) {
                    case 2: // If the random type is 2, draw a resource card from the current resource cards
                        randomCurrentCard = myMatch.getCurrentResourcesCards().get(rand.nextInt(2)); // Randomly select a resource card
                        break;
                    case 3: // If the random type is 3, draw a gold card from the current gold cards
                        randomCurrentCard = myMatch.getCurrentGoldCards().get(rand.nextInt(2)); // Randomly select a gold card
                        break;
                    default:
                        randomCurrentCard = 0;
                        break;
                }

                assertTrue(myMatch.drawCard(randomType, randomCurrentCard));

                myMatch.nextTurn();
            }
        }

        // Terminated phase
        assertTrue(myMatch.addObjectivePoints());
        ArrayList<String> winners = myMatch.getWinners();
    }

    /**
     * Returns the available spaces where a card can be played in the given player's field
     * @param player The player whose field we want to check
     * @return An ArrayList of int arrays containing the available spaces
     */
    public ArrayList<int[]> availableSpacesPlayer (Player player) {
        int[] temp = new int[2];
        ArrayList<int[]> availableCoordinate = new ArrayList<>();
                for (int j = 0; j < player.getField().getFieldCards().size(); j++) {
                    int Ax, Ay;
                    Ax = player.getField().getFieldCards().get(j).getX();
                    Ay = player.getField().getFieldCards().get(j).getY();
                    if (player.getField().availableSpace(Ax + 1, Ay + 1)) {
                        temp[0] = Ax + 1;
                        temp[1] = Ay + 1;
                        availableCoordinate.add(temp);
                    }
                    if (player.getField().availableSpace(Ax - 1, Ay - 1)) {
                        temp[0] = Ax - 1;
                        temp[1] = Ay - 1;
                        availableCoordinate.add(temp);
                    }
                    if (player.getField().availableSpace(Ax + 1, Ay - 1)) {
                        temp[0] = Ax + 1;
                        temp[1] = Ay - 1;
                        availableCoordinate.add(temp);
                    }
                    if (player.getField().availableSpace(Ax - 1, Ay + 1)) {
                        temp[0] = Ax - 1;
                        temp[1] = Ay + 1;
                        availableCoordinate.add(temp);
                    }
                }
        return availableCoordinate;
    }
}