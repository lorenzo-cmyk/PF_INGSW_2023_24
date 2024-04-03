package it.polimi.ingsw.am32.model.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ColourTest {

    @DisplayName("Colour RED should not be null")
    @Test
    void colourRedShouldNotBeNull() {
        assertNotNull(Colour.RED);
    }

    @DisplayName("Colour GREEN should not be null")
    @Test
    void colourGreenShouldNotBeNull() {
        assertNotNull(Colour.GREEN);
    }

    @DisplayName("Colour BLACK should not be null")
    @Test
    void colourBlackShouldNotBeNull() {
        assertNotNull(Colour.BLACK);
    }

    @DisplayName("Colour BLUE should not be null")
    @Test
    void colourBlueShouldNotBeNull() {
        assertNotNull(Colour.BLUE);
    }

    @DisplayName("Colour YELLOW should not be null")
    @Test
    void colourYellowShouldNotBeNull() {
        assertNotNull(Colour.YELLOW);
    }
}
