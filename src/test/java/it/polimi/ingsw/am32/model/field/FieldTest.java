package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    /**
     * Execute an Edge and Condition Coverage Structural Test on resourceCornersConverter method
     */
    @Test
    void doStructuralTestingOfResourceCornersConverter() {

        // Edge and Condition Coverage

        int[] expectedResults = new int[]{1,1,1,1,0,0,0};

        int[] actualResults = Field.resourceCornersConverter(CornerType.PLANT, CornerType.FUNGI,
                CornerType.ANIMAL, CornerType.INSECT);

        assertArrayEquals(expectedResults, actualResults);

        expectedResults = new int[]{0, 0, 0, 0, 1, 1, 1};

        actualResults = Field.resourceCornersConverter(CornerType.QUILL, CornerType.INKWELL,
                CornerType.MANUSCRIPT, CornerType.EMPTY);

        assertArrayEquals(expectedResults, actualResults);
    }

    /**
     * Execute an Edge and Condition Coverage Structural Test on checkResRequirements method
     */
    @Test
    void doStructuralTestingCheckResRequirements() {

        // Edge and Condition Coverage

        int[] inputResources = new int[]{10};
        int[] inputRequirements = new int[]{0,0};

        boolean expectedResult = false;
        boolean actualResult = Field.checkResRequirements(inputResources, inputRequirements);

        assertEquals(expectedResult, actualResult);


        inputResources = new int[]{10,10,10,10,0,0,0};
        inputRequirements = new int[]{20,0,0,0,0,0,0};

        expectedResult = false;
        actualResult = Field.checkResRequirements(inputResources, inputRequirements);

        assertEquals(expectedResult, actualResult);


        inputResources = new int[]{10,10,10,10,0,0,0};
        inputRequirements = new int[]{0,0,0,0,0,0,0};

        expectedResult = true;
        actualResult = Field.checkResRequirements(inputResources, inputRequirements);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Execute a Path Coverage Structural Test on resourcesObtained method
     */
    @Test
    void doStructuralTestingResourcesObtained() {

        // Path Coverage

        int[] permRes = new int[]{0,0,2,1};
        int[] conditionCount = new int[]{0,0,0,0};

        NonObjectiveCard testCard = new NonObjectiveCard(0, 0, null, CornerType.PLANT,
                CornerType.EMPTY, CornerType.QUILL, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.FUNGI, permRes, conditionCount, ObjectType.ANIMAL);

        int[] expectedResult = new int[]{1,0,0,0,1,1,0};

        int[] actualResult = Field.resourcesObtained(testCard, true);

        assertArrayEquals(expectedResult, actualResult);

        expectedResult = new int[]{0,1,2,1,0,0,0};

        actualResult = Field.resourcesObtained(testCard, false);

        assertArrayEquals(expectedResult, actualResult);

    }

    /**
     * Execute a Path Coverage Structural Test on constructor method
     */
    @Test
    void doStructuralTestingConstructor() {

        // Path Coverage

        int[] permRes = new int[]{0,0,2,1};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = false;

        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.PLANT,
                CornerType.EMPTY, CornerType.QUILL, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.FUNGI, permRes, conditionCount, ObjectType.ANIMAL);

        Field field = new Field(testInitialCard, isUp);

        ArrayList<CardPlaced> resultingFieldCards = new ArrayList<>();
        resultingFieldCards.addFirst(new CardPlaced(testInitialCard, 0,0, isUp));

        assertArrayEquals(field.getAllRes(), new int[]{0,1,2,1,0,0,0});
        assertEquals(resultingFieldCards, field.getFieldCards());
    }

    /**
     * Execute a Path Coverage Structural Test on availableSpace method
     */
    @Test
    void doStructuralTestingAvailableSpace() {

        // Path Coverage

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = false;

        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);

        Field field = new Field(testInitialCard, isUp);

        assertFalse(field.availableSpace(0,0));
        assertTrue(field.availableSpace(1,1));
    }
}