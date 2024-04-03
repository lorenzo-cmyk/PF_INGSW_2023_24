package it.polimi.ingsw.am32.model.card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CornerTypeTest {

    @DisplayName("CornerType PLANT should return value 0")
    @Test
    void cornerTypePlantShouldReturnValueZero() {
        assertEquals(0, CornerType.PLANT.getValue());
    }

    @DisplayName("CornerType FUNGI should return value 1")
    @Test
    void cornerTypeFungiShouldReturnValueOne() {
        assertEquals(1, CornerType.FUNGI.getValue());
    }

    @DisplayName("CornerType ANIMAL should return value 2")
    @Test
    void cornerTypeAnimalShouldReturnValueTwo() {
        assertEquals(2, CornerType.ANIMAL.getValue());
    }

    @DisplayName("CornerType INSECT should return value 3")
    @Test
    void cornerTypeInsectShouldReturnValueThree() {
        assertEquals(3, CornerType.INSECT.getValue());
    }

    @DisplayName("CornerType QUILL should return value 4")
    @Test
    void cornerTypeQuillShouldReturnValueFour() {
        assertEquals(4, CornerType.QUILL.getValue());
    }

    @DisplayName("CornerType INKWELL should return value 5")
    @Test
    void cornerTypeInkwellShouldReturnValueFive() {
        assertEquals(5, CornerType.INKWELL.getValue());
    }

    @DisplayName("CornerType MANUSCRIPT should return value 6")
    @Test
    void cornerTypeManuscriptShouldReturnValueSix() {
        assertEquals(6, CornerType.MANUSCRIPT.getValue());
    }

    @DisplayName("CornerType EMPTY should return value 7")
    @Test
    void cornerTypeEmptyShouldReturnValueSeven() {
        assertEquals(7, CornerType.EMPTY.getValue());
    }

    @DisplayName("CornerType NON_COVERABLE should return value 8")
    @Test
    void cornerTypeNonCoverableShouldReturnValueEight() {
        assertEquals(8, CornerType.NON_COVERABLE.getValue());
    }
}
