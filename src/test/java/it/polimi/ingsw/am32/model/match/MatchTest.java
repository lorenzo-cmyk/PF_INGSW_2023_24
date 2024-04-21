package it.polimi.ingsw.am32.model.match;

import it.polimi.ingsw.am32.model.exceptions.DuplicateNicknameException;
import it.polimi.ingsw.am32.model.exceptions.NullColourException;
import it.polimi.ingsw.am32.model.exceptions.NullFieldException;
import it.polimi.ingsw.am32.model.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.am32.model.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    @DisplayName("Test Match.java constructor")
    @Test
    public void checkConstructor() {
        Match myMatch = new Match();
        // The constructor should not return a null object
        assertNotNull(myMatch);
        // Both currentResourceCards and currentGoldCards should not be null and should contain 2 elements each
        assertNotNull(myMatch.getCurrentResourcesCards());
        assertNotNull(myMatch.getCurrentGoldCards());
        assert (myMatch.getCurrentResourcesCards().size() == 2);
        assert (myMatch.getCurrentGoldCards().size() == 2);
        // commonObjectives should not be null but should be empty
        assertNotNull(myMatch.getCommonObjectives());
        assert (myMatch.getCommonObjectives().isEmpty());
        // players should not be null but should be empty
        assertNotNull(myMatch.getPlayersNicknames());
        assert (myMatch.getPlayersNicknames().isEmpty());
        // matchStatus should not be defined yet (null)
        assertEquals(-1, myMatch.getMatchStatus());
        // currentTurnNumber should be 0
        assert (myMatch.getCurrentTurnNumber() == 0);

        // This test also checks the following methods:
        // - getCurrentResourcesCards
        // - getCurrentGoldCards
    }

    @DisplayName("enterLobbyPhase should set matchStatus to LOBBY")
    @Test
    public void enterLobbyPhaseShouldSetMatchStatusToLOBBY() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check enterLobbyPhase method
        myMatch.enterLobbyPhase();
        assert (myMatch.getMatchStatus() == MatchStatus.LOBBY.getValue());

        // This test also checks the following methods:
        // - getMatchStatus
    }

    @DisplayName("addPlayer should add a new Player only if its nickname is unique")
    @Test
    public void addPlayerShouldAddANewPlayerOnlyIfItsNicknameIsUnique() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Add a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        // The new Player should be now present
        ArrayList<String> nicknames = myMatch.getPlayersNicknames();
        assertTrue(nicknames.stream().anyMatch(p -> p.equals("TestPlayerOne")));
        // I will try to add this Player again but addPlayer should not add it
        assertThrows(DuplicateNicknameException.class, () -> myMatch.addPlayer("TestPlayerOne"));
        nicknames = myMatch.getPlayersNicknames();
        assert (nicknames.size() == 1);

        // This test also checks the following methods:
        // - getPlayersNicknames
    }

    @DisplayName("deletePlayer should remove a Player only if it is present")
    @Test
    public void deletePlayerShouldRemoveAPlayerOnlyIfItIsPresent() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Add a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assert (myMatch.getPlayersNicknames().size() == 1);
        // I will try to remove a non-existing Player
        assertThrows(PlayerNotFoundException.class, () -> myMatch.deletePlayer("TestPlayerTwo"));
        assert (myMatch.getPlayersNicknames().size() == 1);
        // I will try to remove an exiting Player
        assertDoesNotThrow(() -> myMatch.deletePlayer("TestPlayerOne"));
        assert (myMatch.getPlayersNicknames().isEmpty());
    }

    @DisplayName("enterPreparationPhase should set matchStatus to PREPARATION")
    @Test
    public void enterPreparationPhaseShouldSetMatchStatusToPREPARATION() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check enterLobbyPhase method
        myMatch.enterPreparationPhase();
        assert (myMatch.getMatchStatus() == MatchStatus.PREPARATION.getValue());
    }

    @DisplayName("assignRandomColoursToPlayers should assign a random colour to each Player")
    @Test
    public void assignRandomColoursToPlayersShouldAssignARandomColourToEachPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerThree"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerFour"));
        // Assign random colours to each Player
        myMatch.assignRandomColoursToPlayers();
        // Check that each Player has a unique colour
        for (Player p : myMatch.getPlayers()) {
            for (Player p2 : myMatch.getPlayers()) {
                if (p != p2) {
                    assert(p.getColour() != p2.getColour());
                }
            }
        }
        // What happens if I try to get the colour from a non-existing Player?
        assertThrows(PlayerNotFoundException.class, () ->myMatch.getPlayerColour("TestPlayerFive"));
        // If now I add the Player, but I don't assign a colour, the method should return -1
        assertDoesNotThrow(() ->myMatch.addPlayer("TestPlayerFive"));
        assertThrows(NullColourException.class, () -> myMatch.getPlayerColour("TestPlayerFive"));

        // This test also checks the following methods:
        // - getPlayerColour
    }

    @DisplayName("assignRandomStartingInitialCardsToPlayers should assign a random starting initial card to each Player")
    @Test
    public void assignRandomStartingInitialCardsToPlayersShouldAssignARandomStartingInitialCardToEachPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerThree"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerFour"));
        // Assign random starting initial cards to each Player
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Check that each Player has a unique starting initial card
        ArrayList<?> startingCardsID = myMatch.getPlayersNicknames()
                .stream().map(nickname -> {
                    try {
                        return myMatch.getInitialCardPlayer(nickname);
                    } catch (PlayerNotFoundException e) {
                        fail();
                    }
                    return 0; // To force the flatmap to have a return value in case of failure. To make the compiler happy mostly.
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        // We should have 4 starting cards
        assert (startingCardsID.size() == 4);
        // We should have 4 different starting cards
        assert (startingCardsID.stream().distinct().count() == 4);
    }

    @DisplayName("getInitialCardPlayer should return the ID of the initial card of a Player. If the Player has not " +
            "any we expect -1")
    @Test
    public void getInitialCardPlayerShouldReturnTheIDOfTheInitialCardOfAPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Test the behaviour of getInitialCardPlayer
        assertDoesNotThrow(() -> myMatch.getInitialCardPlayer("TestPlayerOne"));
        assertThrows(PlayerNotFoundException.class,  () -> {
            myMatch.getInitialCardPlayer("TestPlayerTwo");
        });
    }

    @DisplayName("createFieldPlayer should initialize the Player field")
    @Test
    public void createFieldPlayerShouldInitializeThePlayerField() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        // Assign a random starting initial card to the Player
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Test the behaviour of createFieldPlayer
        assertDoesNotThrow(() -> myMatch.createFieldPlayer("TestPlayerOne", true));
        assertThrows(PlayerNotFoundException.class, () -> myMatch.createFieldPlayer("TestPlayerTwo", true));
        // Check that the Player field has been initialized correctly
        try {
            assertEquals(1, myMatch.getPlayerField("TestPlayerOne").size());
        } catch (PlayerNotFoundException e) {
            fail();
        }
        assertThrows(PlayerNotFoundException.class, () -> myMatch.getPlayerField("TestPlayerTwo"));

        // This test also checks the following methods:
        // - createFieldPlayer
    }

    @DisplayName("assignRandomStartingResourceCardsToPlayers should assign two random Resource card to each Player")
    @Test
    public void assignRandomStartingResourceCardsToPlayersShouldAssignTwoRandomResourceCardToEachPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerThree"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerFour"));
        // Assign random starting card to each Player (we need to assign the initial card first)
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Assign random starting resource cards to each Player
        myMatch.assignRandomStartingResourceCardsToPlayers();
        // Check the number of cards assigned to each Player
        List<Integer> cards = myMatch.getPlayersNicknames().stream()
                .flatMap(nickname -> {
                    try {
                        return myMatch.getPlayerHand(nickname)
                                .stream();
                    } catch (PlayerNotFoundException e) {
                        fail();
                    }
                    return null; // To force the flatmap to have a return value in case of failure. To make the compiler happy mostly.
                }).toList();
        // We should have 12 different cards
        assert (cards.stream().distinct().count() == 12);
    }

    @DisplayName("assignRandomStartingGoldCardsToPlayers should assign one random Gold card to each Player")
    @Test
    public void assignRandomStartingGoldCardsToPlayersShouldAssignOneRandomGoldCardToEachPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerThree"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerFour"));
        // Assign random starting card to each Player (we need to assign the initial card first)
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Assign random starting gold cards to each Player
        myMatch.assignRandomStartingGoldCardsToPlayers();
        // Check the number of cards assigned to each Player
        List<Integer> cards = myMatch.getPlayersNicknames().stream()
                .flatMap(nickname -> {
                    try {
                        return myMatch.getPlayerHand(nickname)
                            .stream();
                    } catch (PlayerNotFoundException e) {
                        fail();
                    }
                    return null; // To force the flatmap to have a return value in case of failure. To make the compiler happy mostly.
                }).toList();
        // We should have 8 different cards
        assert (cards.stream().distinct().count() == 8);
    }

    @DisplayName("pickRandomCommonObjectives should populate the commonObjectives array with 2 random cards")
    @Test
    public void pickRandomCommonObjectivesShouldPopulateTheCommonObjectivesArrayWith2RandomCards() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Pick random common objectives
        myMatch.pickRandomCommonObjectives();
        // Check the number of cards assigned to each Player
        List<Integer> cards = myMatch.getCommonObjectives();
        // We should have 2 different cards
        assert (cards.stream().distinct().count() == 2);

        // This test also checks the following methods:
        // - getCommonObjectives
    }

    @DisplayName("assignRandomStartingSecretObjectivesToPlayers should assign two random Secret Objective card " +
            "to each Player")
    @Test
    public void assignRandomStartingSecretObjectivesToPlayersShouldAssignTwoRandomSecretObjectiveCardToEachPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerThree"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerFour"));
        // Assign random starting secret objectives to each Player
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        // Check the number of cards assigned to each Player
        List<Integer> cards = myMatch.getPlayersNicknames().stream()
                .flatMap(nickname -> myMatch.getSecretObjectiveCardsPlayer(nickname)
                        .stream()).toList();
        // We should have 8 different cards
        assert (cards.stream().distinct().count() == 8);
    }

    @DisplayName("getSecretObjectiveCardsPlayer should return the ID of the secret objective card of a Player. If the " +
            "Player has not any we expect an empty list")
    @Test
    public void getSecretObjectiveCardsPlayerShouldReturnTheIDOfTheSecretObjectiveCardOfAPlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        // Test the behaviour of getSecretObjectiveCardsPlayer
        assert (myMatch.getSecretObjectiveCardsPlayer("TestPlayerOne").size() == 2);
        assert (myMatch.getSecretObjectiveCardsPlayer("TestPlayerTwo").isEmpty());
    }

    @DisplayName("receiveSecretObjectiveChoiceFromPlayer should assign the chosen secret objective card to the Player")
    @Test
    public void receiveSecretObjectiveChoiceFromPlayerShouldAssignTheChosenSecretObjectiveCardToThePlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        // Assign two random starting secret objectives to the Player
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        // Retrieve the secret objectives of the Player
        List<Integer> secretObjectives = myMatch.getSecretObjectiveCardsPlayer("TestPlayerOne");
        // Test the behaviour of receiveSecretObjectiveChoiceFromPlayer
        assertDoesNotThrow(() -> myMatch.receiveSecretObjectiveChoiceFromPlayer("TestPlayerOne", secretObjectives.getFirst()));
        assertThrows(PlayerNotFoundException.class, () ->  myMatch.receiveSecretObjectiveChoiceFromPlayer("TestPlayerTwo", 0));
    }

    @DisplayName("randomizePlayersOrder should shuffle the players order")
    @Test
    public void randomizePlayersOrderShouldShuffleThePlayersOrder() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        List<String> nicknamesCopy = Stream.of("TestPlayerOne", "TestPlayerTwo", "TestPlayerThree",
                "TestPlayerFour").peek(nickname -> {
            try {
                myMatch.addPlayer(nickname);
            } catch (DuplicateNicknameException e) {
                fail();
            }
        }).toList();
        // Randomize the players order
        myMatch.randomizePlayersOrder();
        // Check that the players order has been shuffled
        assert (!nicknamesCopy.equals(myMatch.getPlayersNicknames()));
    }

    @DisplayName("enterPlayingPhase should set matchStatus to PLAYING")
    @Test
    public void enterPlayingPhaseShouldSetMatchStatusToPLAYING() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check enterPlayingPhase method
        myMatch.enterPlayingPhase();
        assert (myMatch.getMatchStatus() == MatchStatus.PLAYING.getValue());
    }

    @DisplayName("startTurns should set the first player and start the turns")
    @Test
    public void startTurnsShouldSetTheFirstPlayerAndStartTheTurns() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        for (String s : Arrays.asList("TestPlayerOne", "TestPlayerTwo", "TestPlayerThree", "TestPlayerFour")) {
            try {
                myMatch.addPlayer(s);
            } catch (DuplicateNicknameException e) {
                fail();
            }
        }
        // Randomize the players order
        myMatch.randomizePlayersOrder();
        // Start the turns
        myMatch.startTurns();
        // Check that the first player is the first in the list
        assertTrue(myMatch.isFirstPlayer());
        assertEquals(myMatch.getCurrentPlayerNickname(), myMatch.getPlayersNicknames().getFirst());
        // Check that the current turn number is 1
        assertEquals(1, myMatch.getCurrentTurnNumber());

        // This test also checks the following methods:
        // - isFirstPlayer
        // - getCurrentTurnNumber
        // - getCurrentPlayerNickname
    }

    @DisplayName("nextTurn should increment the current turn number")
    @Test
    public void nextTurnShouldIncrementTheCurrentTurnNumber() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create four new Player
        for (String s : Arrays.asList("TestPlayerOne", "TestPlayerTwo", "TestPlayerThree", "TestPlayerFour")) {
            try {
                myMatch.addPlayer(s);
            } catch (DuplicateNicknameException e) {
                fail();
            }
        }
        // Randomize the players order
        myMatch.randomizePlayersOrder();
        // Start the turns
        myMatch.startTurns();
        // Check that the first player is the first in the list
        assertTrue(myMatch.isFirstPlayer());
        // Check that the current turn number is 1
        assertEquals(1, myMatch.getCurrentTurnNumber());
        // Go to the next turn
        myMatch.nextTurn();
        // Check that the current turn number is 2
        assertEquals(2, myMatch.getCurrentTurnNumber());
        // isFirstPlayer should return false now
        assertFalse(myMatch.isFirstPlayer());
    }

    @DisplayName("setTerminating should set the match status to TERMINATING")
    @Test
    public void setTerminatingShouldSetTheMatchStatusToTERMINATING() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check setTerminating method
        myMatch.setTerminating();
        assert (myMatch.getMatchStatus() == MatchStatus.TERMINATING.getValue());
    }

    @DisplayName("areWeTerminating should return true if the match status is TERMINATING")
    @Test
    public void areWeTerminatingShouldReturnTrueIfTheMatchStatusIsTERMINATING() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check areWeTerminating method
        myMatch.setTerminating();
        assertTrue(myMatch.areWeTerminating());
    }

    @DisplayName("setLastTurn should set the match status to LAST_TURN")
    @Test
    public void setLastTurnShouldSetTheMatchStatusToLAST_TURN() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check setLastTurn method
        myMatch.setLastTurn();
        assert (myMatch.getMatchStatus() == MatchStatus.LAST_TURN.getValue());
    }

    @DisplayName("enterTerminatedPhase should set the match status to TERMINATED")
    @Test
    public void enterTerminatedPhaseShouldSetTheMatchStatusToTERMINATED() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Check enterTerminatedPhase method
        myMatch.enterTerminatedPhase();
        assert (myMatch.getMatchStatus() == MatchStatus.TERMINATED.getValue());
    }

    @DisplayName("getPlayerHand should return the IDs in the Player's hand. If the Player has not any we expect " +
            "a null object")
    @Test
    public void getPlayerHandShouldReturnTheIDsInThePlayersHand() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        // Assign a random starting initial card to the Player
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Test the behaviour of getPlayerHand
        try {
            assertEquals(1, myMatch.getPlayerHand("TestPlayerOne").size());
        } catch (PlayerNotFoundException e) {
            fail();
        }
        assertThrows(PlayerNotFoundException.class, () -> myMatch.getPlayerHand("TestPlayerTwo"));
    }

    @DisplayName("getPlayerSecretObjective should return the ID of the secret objective card chosen by the Player. " +
            "If the Player has not any we expect -1")
    @Test
    public void getPlayerSecretObjectiveShouldReturnTheIDOfTheSecretObjectiveCardChosenByThePlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        // Assign two random starting secret objectives to the Player
        myMatch.assignRandomStartingSecretObjectivesToPlayers();
        // Retrieve the secret objectives of the Player
        List<Integer> secretObjectives = myMatch.getSecretObjectiveCardsPlayer("TestPlayerOne");
        // Test the behaviour of receiveSecretObjectiveChoiceFromPlayer
        assertDoesNotThrow(() -> myMatch.receiveSecretObjectiveChoiceFromPlayer("TestPlayerOne", secretObjectives.getFirst()));
        try {
            assertEquals(myMatch.getPlayerSecretObjective("TestPlayerOne"), secretObjectives.getFirst());
        } catch (PlayerNotFoundException e) {
            fail();
        }
        try {
            assertEquals(-1, myMatch.getPlayerSecretObjective("TestPlayerTwo"));
        } catch (PlayerNotFoundException e) {
            fail();
        }
    }

    @DisplayName("getPlayerResources should return the resources of the Player. If the Player has not field " +
            "initialized yet we expect a null object")
    @Test
    public void getPlayerResourcesShouldReturnTheResourcesOfThePlayer() {
        Match myMatch = new Match();
        assertNotNull(myMatch);
        // Create a new Player
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerOne"));
        assertDoesNotThrow(() -> myMatch.addPlayer("TestPlayerTwo"));
        // Assign a random starting initial card to the Player
        myMatch.assignRandomStartingInitialCardsToPlayers();
        // Initialize the Player field (but only for the first Player)
        try {
            myMatch.createFieldPlayer("TestPlayerOne", true);
        } catch (PlayerNotFoundException e) {
            fail();
        }
        // Test the behaviour of getPlayerResources
        // The first Player has field initialized. We expect 7 resources allocated.
        try {
            assertEquals(7, myMatch.getPlayerResources("TestPlayerOne").length);
        } catch (PlayerNotFoundException e) {
            fail();
        }
        // The second Player has not field initialized yet
        assertThrows(NullFieldException.class, () -> myMatch.getPlayerResources("TestPlayerTwo"));
    }

}
