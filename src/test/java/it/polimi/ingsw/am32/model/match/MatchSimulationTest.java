package it.polimi.ingsw.am32.model.match;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MatchSimulationTest {

    @DisplayName("Run a, partial, game simulation in order to test the game mechanics")
    @Test
    public void runGameSimulation() {
        // Create the match
        Match myMatch = new Match();
        assertNotNull(myMatch);
        //Lobby phase
        myMatch.enterLobbyPhase();
        assertEquals(0, myMatch.getMatchStatus());


        // Initialize the list of players

        myMatch.addPlayer("Alice");
        myMatch.addPlayer("Bob");
        myMatch.addPlayer("Carlo");
        myMatch.addPlayer("Daniel");
        assertEquals("Alice", myMatch.getPlayers().get(0).getNickname());
        assertEquals("Bob", myMatch.getPlayers().get(1).getNickname());
        assertEquals("Carlo", myMatch.getPlayers().get(2).getNickname());
        assertEquals("Daniel", myMatch.getPlayers().get(3).getNickname());
        assertEquals(4, myMatch.getPlayers().size());

        //Preparation phase
        myMatch.enterPreparationPhase();
        assertEquals(1, myMatch.getMatchStatus());
        myMatch.assignRandomColoursToPlayers();

        myMatch.assignRandomStartingInitialCardsToPlayers();

        //create Field for players
        for (Player player : myMatch.getPlayers()) {
            myMatch.createFieldPlayer(player.getNickname(), true);
        }

        myMatch.assignRandomStartingResourceCardsToPlayers();
        myMatch.assignRandomStartingGoldCardsToPlayers();
        myMatch.pickRandomCommonObjectives();
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        myMatch.randomizePlayersOrder();

        // Playing phase
        myMatch.enterPlayingPhase();
        myMatch.startTurns();
        //

        /*for (Player player : myMatch.getPlayers()) {
            boolean successful = false;
            Random rand = new Random();
            while(!successful){
                int[] randomCoordinate = availableSpaceCurrentPlayer().get(rand.nextInt(availableSpaceCurrentPlayer().size()));
                NonObjectiveCard randomHandCard = player.getHand().get(rand.nextInt(player.getHand().size()));
                boolean randomSide = rand.nextBoolean();
                successful=player.performMove(randomCoordinate[0], randomCoordinate[1], randomHandCard.getId(), randomSide);
            }
            int randomType= rand.nextInt(4);
            int randomCurrentCard=0;
            if(randomType==2){
                randomCurrentCard=myMatch.getCurrentResourcesCards().get(rand.nextInt(2));}
            if(randomType==3){
                randomCurrentCard=myMatch.getCurrentGoldCards().get(rand.nextInt(2));
            }
            myMatch.drawCard(randomType,randomCurrentCard);

        }*/


    }
    /*public ArrayList<int[]> availableSpaceCurrentPlayer () {
        int[] temp = new int[2];
        ArrayList<int[]> availableCoordinate = new ArrayList<>();
        for (int j = 0; j < myMatch.getPlayers().size(); j++) {
            if (myMatch.getPlayers().get(j).getNickname().equals(myMatch.getCurrentPlayerID())) {
                int Ax, Ay;
                Ax = myMatch.getPlayerField(myMatch.getCurrentPlayerID()).get(j)[0];
                Ay = myMatch.getPlayerField(myMatch.getCurrentPlayerID()).get(j)[1];
                if (myMatch.getPlayers().get(j).getField().availableSpace(Ax + 1, Ay + 1)) {
                    temp[0] = Ax + 1;
                    temp[1] = Ay + 1;
                    availableCoordinate.add(temp);
                }
                if (myMatch.getPlayers().get(j).getField().availableSpace(Ax - 1, Ay - 1)) {
                    temp[0] = Ax - 1;
                    temp[1] = Ay - 1;
                    availableCoordinate.add(temp);
                }
                if (myMatch.getPlayers().get(j).getField().availableSpace(Ax + 1, Ay - 1)) {
                    temp[0] = Ax + 1;
                    temp[1] = Ay - 1;
                    availableCoordinate.add(temp);
                }
                if (myMatch.getPlayers().get(j).getField().availableSpace(Ax - 1, Ay + 1)) {
                    temp[0] = Ax - 1;
                    temp[1] = Ay + 1;
                    availableCoordinate.add(temp);
                }
            }
        }
        return availableCoordinate;
    }*/
}