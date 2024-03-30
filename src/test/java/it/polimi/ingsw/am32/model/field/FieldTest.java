package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    /**
     * Execute an Edge and Condition Structural Test on resourceCornersConverter method
     */
    @Test
    void doStructuralTestingOfResourceCornersConverter(){

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
     * Execute an Edge and Condition Structural Test on checkResRequirements method
     */
    @Test
    void doStructuralTestingCheckResRequirements(){

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
}