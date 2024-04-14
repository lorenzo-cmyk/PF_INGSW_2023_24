package it.polimi.ingsw.am32.model.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourTest {
    @DisplayName("Colour value should match the expected value")
    @Test
    void colourValueShouldMatchExpectedValue() {
        assertEquals(0, Colour.RED.getValue());
        assertEquals(1, Colour.GREEN.getValue());
        assertEquals(2, Colour.BLUE.getValue());
        assertEquals(3, Colour.YELLOW.getValue());
        assertEquals(4, Colour.BLACK.getValue());
    }

    @DisplayName("Colour should not be null when value is valid")
    @Test
    void colourShouldNotBeNullWhenValueIsValid() {
        for (Colour colour : Colour.values()) {
            assertNotNull(colour);
        }
    }

    @DisplayName("Colour value should not be negative")
    @Test
    void colourValueShouldNotBeNegative() {
        for (Colour colour : Colour.values()) {
            assertTrue(colour.getValue() >= 0);
        }
    }

}
