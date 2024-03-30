package it.polimi.ingsw.am32.model.card.pointstrategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectTypeTest {

    @DisplayName("ObjectType PLANT should return value 0")
    @Test
    void objectTypePlantShouldReturnValueZero() {
        assertEquals(0, ObjectType.PLANT.getValue());
    }

    @DisplayName("ObjectType FUNGI should return value 1")
    @Test
    void objectTypeFungiShouldReturnValueOne() {
        assertEquals(1, ObjectType.FUNGI.getValue());
    }

    @DisplayName("ObjectType ANIMAL should return value 2")
    @Test
    void objectTypeAnimalShouldReturnValueTwo() {
        assertEquals(2, ObjectType.ANIMAL.getValue());
    }

    @DisplayName("ObjectType INSECT should return value 3")
    @Test
    void objectTypeInsectShouldReturnValueThree() {
        assertEquals(3, ObjectType.INSECT.getValue());
    }

    @DisplayName("ObjectType QUILL should return value 4")
    @Test
    void objectTypeQuillShouldReturnValueFour() {
        assertEquals(4, ObjectType.QUILL.getValue());
    }

    @DisplayName("ObjectType INKWELL should return value 5")
    @Test
    void objectTypeInkwellShouldReturnValueFive() {
        assertEquals(5, ObjectType.INKWELL.getValue());
    }

    @DisplayName("ObjectType MANUSCRIPT should return value 6")
    @Test
    void objectTypeManuscriptShouldReturnValueSix() {
        assertEquals(6, ObjectType.MANUSCRIPT.getValue());
    }
}