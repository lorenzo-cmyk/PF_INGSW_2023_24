package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.am32.model.exceptions.MissingRequirementsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

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

        assertArrayEquals(field.getAllRes(), new int[]{0,1,2,1,0,0,0});
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
    void doStructuralTestingPlaceCardInField() throws MissingRequirementsException, InvalidPositionException {

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

        assertThrows(MissingRequirementsException.class, ()-> mainTestfield.placeCardInField(testCardBasic, 1, 1, isUp));

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
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, -1, 1, isUp));// {1}
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 1, 1, isUp));
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, -1, -1, isUp));
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 1, -1, isUp));

        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 0, 2, isUp));          // {2}
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 2, 0, isUp));          // {2}
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, 0, -2, isUp));         // {2}
        assertThrows(InvalidPositionException.class, ()->mainTestfield.placeCardInField(testCardBasic, -2, 0, isUp));         // {2}
    }

    @DisplayName("Execute a Path Coverage Structural Test on all Getters")
    @Test
    void doStructuralTestingGetters() {

        // Path Coverage

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = true;

        NonObjectiveCard testInitialCard = new NonObjectiveCard(0, 0, null, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY,
                CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);

        Field field = new Field(testInitialCard, isUp);

        ArrayList<CardPlaced> tmpCards = field.getFieldCards();

        int tmpResource = field.getActiveRes(ObjectType.ANIMAL);

        int [] tmpAllRes = field.getAllRes();
    }

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

        Field field = new Field(testCard1, false);

        int[] expectedRes = new int[7];

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        Field finalField = field;
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 0,0, true));
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 0,0, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 1,0, true));
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 1,0, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 0,40, true));
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 0,40, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 100,100, true));
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 100,100, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 1,1, true));
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard1, 1,1, false));

        assertEquals(testCard1, field.getCardFromPosition(0,0));
        assertEquals(1, field.getFieldCards().size());
        assertArrayEquals(expectedRes, field.getAllRes());


        // 2. checking other placeability and stuff ( more details later)

        NonObjectiveCard testCard2 = new NonObjectiveCard(0,0,null,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount,ObjectType.ANIMAL);

        field = new Field(testCard2, false);

        int[] expectedArray = new int[]{0,0,0,0,0,0,0};

        // 2.1. checking normal placing

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, 1,1, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(2, field.getFieldCards().size());
        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, 0,2, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(3, field.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, -1,1, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(4, field.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, -1,3, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(5, field.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, 100,100, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(5, field.getFieldCards().size());

        // 2.2. checking overlap not possible

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, 0,0, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(5, field.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, 1,1, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(5, field.getFieldCards().size());

        assertThrows(InvalidPositionException.class, () -> finalField.placeCardInField(testCard2, -1,3, true));

        assertArrayEquals(expectedArray, (field.getAllRes()));
        assertEquals(5, field.getFieldCards().size());

        // 2.3. checking no strange card variation and that getCardFromPosition method works

        assertEquals(field.getCardFromPosition(0,0), field.getCardFromPosition(0,2));
        assertEquals(field.getCardFromPosition(0,2), field.getCardFromPosition(-1,3));

        // 2.4. checking impossible placement in non valid positions

        assertNull(field.getCardFromPosition(0,1));

        assertNull(field.getCardFromPosition(100,100));

        // 2.5. checking availableSpace method works

        assertFalse(field.availableSpace(0,0));

        assertFalse(field.availableSpace(1,1));

        assertFalse(field.availableSpace(1,2));

        assertTrue(field.availableSpace(1,3));

        assertFalse(field.availableSpace(1,0));

        assertFalse(field.availableSpace(100,100));
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
}