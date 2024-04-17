package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.am32.model.exceptions.MissingRequirementsException;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LConfigurationOneTest {
    // Two FUNGI+ One Plant
    // Setting up field
    int initialId = 0;
    int initialValue = 0;
    PointStrategy initialPointStrategy = new Empty();
    int[] initialPermRes = {0, 0, 0, 0};
    int[] initialConditionCount = {0, 0, 0, 0};


    NonObjectiveCard initialNonObjCard = new NonObjectiveCard(initialId, initialValue, initialPointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, initialPermRes, initialConditionCount, null);

    Field f = new Field(initialNonObjCard, true); // Create a new field with face-up initial card

    // Setting up parameters for cards to be placed in the field that are irrelevant for testing
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 0, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};

    // Begin testing
    // Note: "Empty field" means a field containing just the initial card
    @DisplayName("Strategy called on empty field containing just initial card should return 0")
    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    //---There are less than three cards in the field---
    @DisplayName("Strategy called on field with 1 card should return 0")
    @Test
    void occurrencesOnFieldWithOneCardShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 2 cards should return 0")
    @Test
    void occurrencesOnFieldWithTwoCardsShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 0, 2, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    //---There are at least three cards on the field---
    @DisplayName("Strategy called on field with 3 cards placed in the wrong orientation should return 0")
    @Test
    void occurrencesOnFieldWithThreeCardsShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c3 = new NonObjectiveCard(3, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 0, 2, true); // Card placed side-up
            f.placeCardInField(c3, -1, 1, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 3 cards placed in the correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithThreeCardsShouldBeOne() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(3, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c3 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 1, -1, true); // Card placed side-up
            f.placeCardInField(c3, 2, -2, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 4 cards(different type from FUNGI and PLANT)should return 0")
    @Test
    void occurrencesOnFieldWithThreeCardsCase1ShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(24, 0, pointStrategy, CornerType.EMPTY, CornerType.ANIMAL, CornerType.NON_COVERABLE, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(31, 0, pointStrategy, CornerType.INSECT, CornerType.INSECT, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c3 = new NonObjectiveCard(23, 0, pointStrategy, CornerType.ANIMAL, CornerType.NON_COVERABLE, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c4 = new NonObjectiveCard(37, 0, pointStrategy, CornerType.INSECT, CornerType.PLANT, CornerType.INKWELL, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 2, 2, true); // Card placed side-up
            f.placeCardInField(c3, 1, 3, true); // Card placed side-up
            f.placeCardInField(c4, 2, 0, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 4 cards but the amount of FUNGI cards is not enough should return 0")
    @Test
    void occurrencesOnFieldWithFourCardsCase2ShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(37, 0, pointStrategy, CornerType.INSECT, CornerType.PLANT, CornerType.INKWELL, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 2, 0, true); // Card placed side-up
            f.placeCardInField(c3, 2, 2, true); // Card placed side-up
            f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 4 cards but the amount of PLANT cards is not enough should return 0")
    @Test
    void occurrencesOnFieldWithFourCardsCase3ShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(37, 0, pointStrategy, CornerType.INSECT, CornerType.PLANT, CornerType.INKWELL, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 2, 0, true); // Card placed side-up
            f.placeCardInField(c3, 2, 2, true); // Card placed side-up
            f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 4 cards placed in false orientation should return 0")
    @Test
    void occurrencesOnFieldWithFourCardsCase4ShouldBeZero() {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        try {
            f.placeCardInField(c1, 1, 1, true); // Card placed side-up
            f.placeCardInField(c2, 2, 0, true); // Card placed side-up
            f.placeCardInField(c3, 2, 2, true); // Card placed side-up
            f.placeCardInField(c4, 0, 2, true); // Card placed side-up
        } catch (InvalidPositionException | MissingRequirementsException e) {
            fail();
        }
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 4 cards placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFourCardsCase5ShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 5 cards(two FUNGI Card+ two PLANT card + one Casual card) placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFiveCardsCase1ShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c5 = new NonObjectiveCard(24, 0, pointStrategy, CornerType.EMPTY, CornerType.ANIMAL, CornerType.NON_COVERABLE, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        f.placeCardInField(c5, 3, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 5 cards(three FUNGI Card+ one PLANT card + one Casual card) placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFiveCardsCase2ShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(3, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c5 = new NonObjectiveCard(24, 0, pointStrategy, CornerType.EMPTY, CornerType.ANIMAL, CornerType.NON_COVERABLE, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        f.placeCardInField(c5, 3, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 6 cards placed in correct orientation should return 2")
    @Test
    void occurrencesOnFieldWithSixCardsCaseShouldBeTwo() throws MissingRequirementsException, InvalidPositionException {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c3 = new NonObjectiveCard(4, 0, pointStrategy, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(10, 0, pointStrategy, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c5 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c6 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.MANUSCRIPT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -1, 1, true); // Card placed side-up
        f.placeCardInField(c3, 1, -1, true); // Card placed side-up
        f.placeCardInField(c4, -1, -1, true); // Card placed side-up
        f.placeCardInField(c5, 0, -2, true); // Card placed side-up
        f.placeCardInField(c6, 2, -2, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 8 cards placed in correct orientation should return 2")
    @Test
    void occurrencesOnFieldWithEightCardsCaseShouldBeTwo() throws MissingRequirementsException, InvalidPositionException {
        LConfigurationOne strategy = new LConfigurationOne();
        NonObjectiveCard c1 = new NonObjectiveCard(2, 0, pointStrategy, CornerType.FUNGI, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c2 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(1, 0, pointStrategy, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c5 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.MANUSCRIPT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c6 = new NonObjectiveCard(4, 0, pointStrategy, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.EMPTY, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c7 = new NonObjectiveCard(18, 0, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c8 = new NonObjectiveCard(10, 0, pointStrategy, CornerType.NON_COVERABLE, CornerType.FUNGI, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        f.placeCardInField(c4, 1, 3, true); // Card placed side-up
        f.placeCardInField(c5, 2, 4, true); // Card placed side-up
        f.placeCardInField(c6, 1, 5, true); // Card placed side-up
        f.placeCardInField(c7, 2, 6, true); // Card placed side-up
        f.placeCardInField(c8, 1, 7, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

}