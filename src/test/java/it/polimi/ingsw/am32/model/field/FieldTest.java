package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.am32.model.exceptions.MissingRequirementsException;
import it.polimi.ingsw.am32.model.exceptions.RollbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    /**
     * This method is used to generate a random NonObjectiveCard
     */
    private NonObjectiveCard generateRandomNonObjectiveCard(){
        Random rand = new Random();
        int[] permRes = new int[]{0, 0, 0, 0, 0, 0, 0};
        int[] conditionCount = new int[]{0, 0, 0, 0, 0, 0, 0};
        return new NonObjectiveCard(
                // NonObjectiveCard ID goes from 1 to 86 (inclusive)
                rand.nextInt(86) + 1,
                // NonObjectiveCard Value goes from 0 to 5 (inclusive)
                rand.nextInt(6),
                new Empty(),
                CornerType.PLANT,
                CornerType.ANIMAL,
                CornerType.FUNGI,
                CornerType.INSECT,
                CornerType.QUILL,
                CornerType.INKWELL,
                CornerType.MANUSCRIPT,
                CornerType.NON_COVERABLE,
                permRes,
                conditionCount,
                ObjectType.INSECT
        );
    }

    // Structural Testing

    @DisplayName("Execute an Edge and Condition Coverage Structural Test on resourceCornersConverter method")
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


    @DisplayName("Execute an Edge and Condition Coverage Structural Test on checkResRequirements method")
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

        actualResult = Field.checkResRequirements(inputResources, inputRequirements);

        assertEquals(expectedResult, actualResult);


        inputResources = new int[]{10,10,10,10,0,0,0};
        inputRequirements = new int[]{0,0,0,0,0,0,0};

        expectedResult = true;
        actualResult = Field.checkResRequirements(inputResources, inputRequirements);

        assertEquals(expectedResult, actualResult);
    }


    @DisplayName("Execute a Path Coverage Structural Test on resourcesObtained method")
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


    @DisplayName("Execute a Path Coverage Structural Test on constructor method")
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

        assertArrayEquals(new int[]{0,1,2,1,0,0,0}, field.getAllRes());
        assertEquals(resultingFieldCards, field.getFieldCards());
    }


    @DisplayName("Execute a Path Coverage Structural Test on availableSpace method")
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

        assertFalse(field.availableSpace(1,0));

        assertFalse(field.availableSpace(0,0));
        assertTrue(field.availableSpace(-1,1));
        assertTrue(field.availableSpace(1,1));
    }

    @DisplayName("Execute a Path Coverage Structural Test on availableSpace method")
    @Test
    void doStructuralTestingGetCardFromPosition() {

        // Path Coverage

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = false;

        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);

        Field field = new Field(testInitialCard, isUp);

        assertEquals(testInitialCard, field.getCardFromPosition(0,0));
        assertNull(field.getCardFromPosition(0,2));
        assertNull(field.getCardFromPosition(1,1));
    }

    @DisplayName("Execute a Path Coverage Structural Test on availableSpace method")
    @Test
    void doStructuralTestingPlaceCardInField() {

        // Edge and Condition Coverage

        // Setup

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = true;

        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);

        NonObjectiveCard testCardBasic = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);

        Field mainTestfield = new Field(testInitialCard, isUp);

        // Verify valid position

        assertThrows(InvalidPositionException.class, ()-> mainTestfield.placeCardInField(testCardBasic, 41, 1, isUp));
        assertThrows(InvalidPositionException.class, ()-> mainTestfield.placeCardInField(testCardBasic, -41, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 1, 41, isUp));
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 1, -41, isUp));
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 1, 0, isUp));

        int[] unreachableRequirements = new int[]{2,2,2,2};

        NonObjectiveCard testCardRequirements = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, unreachableRequirements, ObjectType.ANIMAL);

        // Verify resources requirements (second part later: index {1})

        assertThrows(MissingRequirementsException.class, ()-> mainTestfield.placeCardInField(testCardRequirements, 1, 1, isUp));

        // Verify space availability
        assertThrows(InvalidPositionException.class, ()-> mainTestfield.placeCardInField(testCardBasic, 0,0,isUp));
        assertThrows(InvalidPositionException.class, ()-> mainTestfield.placeCardInField(testCardBasic, 38, 0, isUp));

        // Verify diagonal availability (second part later: index {2})

        NonObjectiveCard testInitialCardNotLinkable = new NonObjectiveCard(0, 0, null,
                CornerType.NON_COVERABLE, CornerType.NON_COVERABLE, CornerType.NON_COVERABLE, CornerType.NON_COVERABLE,
                CornerType.NON_COVERABLE, CornerType.NON_COVERABLE, CornerType.NON_COVERABLE, CornerType.NON_COVERABLE,
                permRes, conditionCount, ObjectType.ANIMAL);

        Field tmpField1 = new Field(testInitialCardNotLinkable, isUp);
        assertThrows(InvalidPositionException.class, ()->tmpField1.placeCardInField(testCardBasic, -1, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField1.placeCardInField(testCardBasic, 1, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField1.placeCardInField(testCardBasic, -1, -1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField1.placeCardInField(testCardBasic, 1, -1, isUp));

        Field tmpField2 = new Field(testInitialCardNotLinkable, !isUp);

        assertThrows(InvalidPositionException.class, ()->tmpField2.placeCardInField(testCardBasic, -1, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField2.placeCardInField(testCardBasic, 1, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField2.placeCardInField(testCardBasic, -1, -1, isUp));
        assertThrows(InvalidPositionException.class, ()->tmpField2.placeCardInField(testCardBasic, 1, -1, isUp));

        // verify placement process executed successfully (includes extra part {1} and {2})
        try {
            mainTestfield.placeCardInField(testCardBasic, -1, 1, !isUp);
            mainTestfield.placeCardInField(testCardBasic, 1, 1, isUp);
            mainTestfield.placeCardInField(testCardBasic, -1, -1, isUp);
            mainTestfield.placeCardInField(testCardBasic, 1, -1, isUp);

            mainTestfield.placeCardInField(testCardBasic, 0, 2, isUp);
            mainTestfield.placeCardInField(testCardBasic, 2, 0, isUp);
            mainTestfield.placeCardInField(testCardBasic, 0, -2, isUp);
            mainTestfield.placeCardInField(testCardBasic, -2, 0, isUp);
        } catch (Exception e) {
            fail();
        }
    }

//    Warning: the following test is disabled because it has not any assertions in it.
//    @DisplayName("Execute a Path Coverage Structural Test on all Getters")
//    @Test
//    void doStructuralTestingGetters() {
//
//        // Path Coverage
//
//        int[] permRes = new int[]{0,0,0,0};
//        int[] conditionCount = new int[]{0,0,0,0};
//        boolean isUp = true;
//
//        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
//                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
//                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
//
//        Field field = new Field(testInitialCard, isUp);
//
//        ArrayList<CardPlaced> tmpCards = field.getFieldCards();
//
//        int tmpResource = field.getActiveRes(ObjectType.ANIMAL);
//
//        int [] tmpAllRes = field.getAllRes();
//    }

    // Functional Testing

    @DisplayName("Execute Some placement cases and functional verification")
    @Test
    void doPlacementFunctionalTesting() {

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};

        // 1. verification of non placeability

        NonObjectiveCard testCard1 = new NonObjectiveCard(0,0,null,CornerType.NON_COVERABLE,
                CornerType.NON_COVERABLE,CornerType.NON_COVERABLE,CornerType.NON_COVERABLE,CornerType.NON_COVERABLE,
                CornerType.NON_COVERABLE,CornerType.NON_COVERABLE,CornerType.NON_COVERABLE,permRes, conditionCount,
                ObjectType.ANIMAL);

        final Field field = new Field(testCard1, false);

        int[] expectedRes = new int[7];

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());

        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 0,0, true));
        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 0,0, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 1,0, true));
        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 1,0, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 0,40, true));
        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 0,40, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 100,100, true));
        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 100,100, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 1,1, true));
        assertThrows(InvalidPositionException.class, () -> field.placeCardInField(testCard1, 1,1, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        // 2. checking other placeability and stuff ( more details later)

        NonObjectiveCard testCard2 = new NonObjectiveCard(0,0,null,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount,ObjectType.ANIMAL);

        final Field field2 = new Field(testCard2, false);

        int[] expectedArray = new int[]{0,0,0,0,0,0,0};

        // 2.1. checking normal placing

        assertDoesNotThrow(() -> field2.placeCardInField(testCard2, 1,1, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(2, field2.getFieldCards().size());

        assertDoesNotThrow(() -> field2.placeCardInField(testCard2, 0,2, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(3, field2.getFieldCards().size());

        assertDoesNotThrow(() -> field2.placeCardInField(testCard2, -1,1, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(4, field2.getFieldCards().size());

        assertDoesNotThrow(() -> field2.placeCardInField(testCard2, -1,3, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(5, field2.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> field2.placeCardInField(testCard2, 100,100, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(5, field2.getFieldCards().size());

        // 2.2. checking overlap not possible

        assertThrows(InvalidPositionException.class, () -> field2.placeCardInField(testCard2, 0,0, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(5, field2.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> field2.placeCardInField(testCard2, 1,1, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));
        assertEquals(5, field2.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> field2.placeCardInField(testCard2, -1,3, true));

        assertArrayEquals(expectedArray, (field2.getAllRes()));

        assertEquals(expectedRes[ObjectType.ANIMAL.getValue()], field2.getActiveRes(ObjectType.ANIMAL));
        assertEquals(expectedRes[ObjectType.FUNGI.getValue()], field2.getActiveRes(ObjectType.FUNGI));
        assertEquals(expectedRes[ObjectType.INSECT.getValue()], field2.getActiveRes(ObjectType.INSECT));
        assertEquals(expectedRes[ObjectType.PLANT.getValue()], field2.getActiveRes(ObjectType.PLANT));
        assertEquals(expectedRes[ObjectType.QUILL.getValue()], field2.getActiveRes(ObjectType.QUILL));
        assertEquals(expectedRes[ObjectType.INKWELL.getValue()], field2.getActiveRes(ObjectType.INKWELL));
        assertEquals(expectedRes[ObjectType.MANUSCRIPT.getValue()], field2.getActiveRes(ObjectType.MANUSCRIPT));

        assertEquals(5, field2.getFieldCards().size());

        // 2.3. checking no strange card variation and that getCardFromPosition method works

        assertEquals(field2.getCardFromPosition(0,0), field2.getCardFromPosition(0,2));
        assertEquals(field2.getCardFromPosition(0,2), field2.getCardFromPosition(-1,3));

        // 2.4. checking impossible placement in non valid positions

        assertNull(field2.getCardFromPosition(0,1));

        assertNull(field2.getCardFromPosition(100,100));

        // 2.5. checking availableSpace method works

        assertFalse(field2.availableSpace(0,0));

        assertFalse(field2.availableSpace(1,1));

        assertFalse(field2.availableSpace(1,2));

        assertTrue(field2.availableSpace(1,3));

        assertFalse(field2.availableSpace(1,0));

        assertFalse(field2.availableSpace(100,100));
    }

    @DisplayName("Verify purity of getCardFromPosition")
    @Test
    void doPurityOfGetCardFromPosition() {

        int[] permRes = new int[]{1,2,0,2};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = false;

        NonObjectiveCard testCard1 = new NonObjectiveCard(0,0,null,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount,ObjectType.ANIMAL);

        Field field = new Field(testCard1, isUp);

        int[] expectedRes = field.getAllRes();
        CardPlaced expectedCardPlaced = field.getFieldCards().getFirst();
        int expectedSize = field.getFieldCards().size();

        assertNotNull(field.getCardFromPosition(0,0));

        assertEquals(expectedRes, field.getAllRes());
        assertEquals(expectedCardPlaced, field.getFieldCards().getFirst());
        assertEquals(expectedSize, field.getFieldCards().size());

        assertNull(field.getCardFromPosition(1,1));

        assertEquals(expectedRes, field.getAllRes());
        assertEquals(expectedCardPlaced, field.getFieldCards().getFirst());
        assertEquals(expectedSize, field.getFieldCards().size());
    }

    @DisplayName("Verify purity of availableSpace")
    @Test
    void doPurityOfAvailableSpace() {

        int[] permRes = new int[]{1,2,0,2};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = false;

        NonObjectiveCard testCard1 = new NonObjectiveCard(0,0,null,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount,ObjectType.ANIMAL);

        Field field = new Field(testCard1, isUp);

        int[] expectedRes = field.getAllRes();
        CardPlaced expectedCardPlaced = field.getFieldCards().getFirst();
        int expectedSize = field.getFieldCards().size();

        assertFalse(field.availableSpace(0,0));

        assertEquals(expectedRes, field.getAllRes());
        assertEquals(expectedCardPlaced, field.getFieldCards().getFirst());
        assertEquals(expectedSize, field.getFieldCards().size());

        assertTrue(field.availableSpace(1,1));

        assertEquals(expectedRes, field.getAllRes());
        assertEquals(expectedCardPlaced, field.getFieldCards().getFirst());
        assertEquals(expectedSize, field.getFieldCards().size());
    }

    @DisplayName("Verify the rollback process - Field class")
    @Test
    void doRollbackFunctionalTesting() {

        // Generate the initial card
        NonObjectiveCard startingCard = generateRandomNonObjectiveCard();
        // Initialize the field
        Field field = new Field(startingCard, false);
        int[] expectedResources = field.getAllRes();

        // Test the rollback process. It should fail as we can't remove the starting card
        assertThrows(RollbackException.class, field::rollback);
        // Check that the field is still the same
        assertEquals(expectedResources, field.getAllRes());
        assertEquals(1, field.getFieldCards().size());
        assertEquals(startingCard, field.getCardFromPosition(0,0));

        // Generate a new card
        NonObjectiveCard newCard = generateRandomNonObjectiveCard();
        // Place the new card in the field
        try {
            field.placeCardInField(newCard, 1, 1, false);
            // Check that the field has been updated
            assertEquals(2, field.getFieldCards().size());
            assertEquals(newCard, field.getCardFromPosition(1,1));
            // Rollback the last move. It should work as we can remove the last card placed.
            NonObjectiveCard removedCard = field.rollback();
            // Check the removed card
            assertEquals(newCard, removedCard);
            // Check that the field has been updated
            assertEquals(1, field.getFieldCards().size());
            assertNull(field.getCardFromPosition(1,1));
            // Check that the resources are the same as before
            assertEquals(expectedResources, field.getAllRes());
        } catch (InvalidPositionException | MissingRequirementsException | RollbackException e) {
            fail();
        }
    }

}