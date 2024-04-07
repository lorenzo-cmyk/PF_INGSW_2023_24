package it.polimi.ingsw.am32.model.match;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MatchStatusTest {
    @DisplayName("MatchStatus values should not be null")
    @Test
    void matchStatusValuesShouldNotBeNull() {
        for (MatchStatus status : MatchStatus.values()) {
            assertNotNull(status);
        }
    }
}
