package it.polimi.ingsw.am32.model.match;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchStatusTest {
    @DisplayName("MatchStatus values should not be null")
    @Test
    void matchStatusValuesShouldNotBeNull() {
        for (MatchStatus status : MatchStatus.values()) {
            assertNotNull(status);
        }
    }

    @DisplayName("MatchStatus values should correspond to correct integers")
    @Test
    void matchStatusValuesShouldCorrespondToCorrectIntegers() {
        assertEquals(0, MatchStatus.LOBBY.getValue());
        assertEquals(1, MatchStatus.PREPARATION.getValue());
        assertEquals(2, MatchStatus.PLAYING.getValue());
        assertEquals(3, MatchStatus.TERMINATING.getValue());
        assertEquals(4, MatchStatus.LAST_TURN.getValue());
        assertEquals(5, MatchStatus.TERMINATED.getValue());
    }

    @DisplayName("MatchStatus should return correct value for each status")
    @Test
    void matchStatusShouldReturnCorrectValueForEachStatus() {
        for (MatchStatus status : MatchStatus.values()) {
            assertTrue(status.getValue() >= 0 && status.getValue() <= 5);
        }
    }
}
