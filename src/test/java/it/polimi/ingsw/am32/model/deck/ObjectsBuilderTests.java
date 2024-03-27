package it.polimi.ingsw.am32.model.deck;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.pointstrategy.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsBuilderTests {
    ObjectsBuilder objectsBuilder = new ObjectsBuilder();

    @DisplayName("stringToCornerType should return the correct object")
    @Test
    void stringToCornerTypeReturnsCorrectEnum() {
        assertEquals(CornerType.PLANT, objectsBuilder.stringToCornerType("PLANT"));
        assertEquals(CornerType.FUNGI, objectsBuilder.stringToCornerType("FUNGI"));
        assertEquals(CornerType.ANIMAL, objectsBuilder.stringToCornerType("ANIMAL"));
        assertEquals(CornerType.INSECT, objectsBuilder.stringToCornerType("INSECT"));
        assertEquals(CornerType.QUILL, objectsBuilder.stringToCornerType("QUILL"));
        assertEquals(CornerType.INKWELL, objectsBuilder.stringToCornerType("INKWELL"));
        assertEquals(CornerType.MANUSCRIPT, objectsBuilder.stringToCornerType("MANUSCRIPT"));
        assertEquals(CornerType.EMPTY, objectsBuilder.stringToCornerType("EMPTY"));
        assertEquals(CornerType.NON_COVERABLE, objectsBuilder.stringToCornerType("NON_COVERABLE"));
    }

    @DisplayName("stringToCornerType should return null for invalid string")
    @Test
    void stringToCornerTypeReturnsNullForInvalidString() {
        assertNull(objectsBuilder.stringToCornerType("INVALID"));
    }

    @DisplayName("stringToObjectType should return the correct object")
    @Test
    void stringToObjectTypeReturnsCorrectEnum() {
        assertEquals(ObjectType.PLANT, objectsBuilder.stringToObjectType("PLANT"));
        assertEquals(ObjectType.FUNGI, objectsBuilder.stringToObjectType("FUNGI"));
        assertEquals(ObjectType.ANIMAL, objectsBuilder.stringToObjectType("ANIMAL"));
        assertEquals(ObjectType.INSECT, objectsBuilder.stringToObjectType("INSECT"));
        assertEquals(ObjectType.QUILL, objectsBuilder.stringToObjectType("QUILL"));
        assertEquals(ObjectType.INKWELL, objectsBuilder.stringToObjectType("INKWELL"));
        assertEquals(ObjectType.MANUSCRIPT, objectsBuilder.stringToObjectType("MANUSCRIPT"));
    }

    @DisplayName("stringToObjectType should return null for invalid string")
    @Test
    void stringToObjectTypeReturnsNullForInvalidString() {
        assertNull(objectsBuilder.stringToObjectType("INVALID"));
    }

    @DisplayName("stringsToPointStrategy should return the correct object")
    @Test
    void stringsToPointStrategyReturnsCorrectObject() {
        assertInstanceOf(AllSpecial.class, objectsBuilder.stringsToPointStrategy("AllSpecial", "", 0, false));
        assertInstanceOf(AnglesCovered.class, objectsBuilder.stringsToPointStrategy("AnglesCovered", "", 0, false));
        assertInstanceOf(CountResource.class, objectsBuilder.stringsToPointStrategy("CountResource", "PLANT", 1, false));
        assertInstanceOf(Diagonals.class, objectsBuilder.stringsToPointStrategy("Diagonals", "PLANT", 0, false));
        assertInstanceOf(Empty.class, objectsBuilder.stringsToPointStrategy("Empty", "", 0, false));
        assertInstanceOf(LConfigurationOne.class, objectsBuilder.stringsToPointStrategy("LConfigurationOne", "", 0, false));
        assertInstanceOf(LConfigurationTwo.class, objectsBuilder.stringsToPointStrategy("LConfigurationTwo", "", 0, false));
        assertInstanceOf(LConfigurationThree.class, objectsBuilder.stringsToPointStrategy("LConfigurationThree", "", 0, false));
        assertInstanceOf(LConfigurationFour.class, objectsBuilder.stringsToPointStrategy("LConfigurationFour", "", 0, false));
    }

    @DisplayName("stringsToPointStrategy should return null for invalid string")
    @Test
    void stringsToPointStrategyReturnsNullForInvalidString() {
        assertNull(objectsBuilder.stringsToPointStrategy("INVALID", "", 0, false));
    }
}
