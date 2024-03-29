package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagonalsTest {
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
        Diagonals strategy = new Diagonals(ObjectType.FUNGI,true);
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card which has Kingdom requested by given card should return 0")
    @Test
    void occurrencesOnFieldWithOneCardKingdomRequiredShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card which has Kingdom different from the request of given card should return 0")
    @Test
    void occurrencesOnFieldWithOneCardFalseKingdomShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.FUNGI,true);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 2 cards of required kingdom placed in correct orientation should return 0")
    @Test
    void occurrencesOnFieldWithTwoCardsKingdomRequiredCorrectOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 0, 2, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 2 cards of different Kingdom from the request placed in correct orientation should return 0")
    @Test
    void occurrencesOnFieldWithTwoCardsFalseKingdomCorrectOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.FUNGI,true);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 2 cards of required kingdom placed in wrong orientation should return 0")
    @Test
    void occurrencesOnFieldWithTwoCardsKingdomRequiredWrongOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 2 cards of different Kingdom from the request placed in wrong orientation should return 0")
    @Test
    void occurrencesOnFieldWithTwoCardsFalseKingdomWrongOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.FUNGI,true);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true);
        f.placeCardInField(c2, -1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards of required kingdom placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithThreeCardsKingdomRequiredCorrectOrientationShouldBeOne() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -3, 3, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards of different Kingdom from the request should return 0")
    @Test
    void occurrencesOnFieldWithThreeCardsFalseKingdomShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.FUNGI,true);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 3 cards of required kingdom placed in wrong orientation should return 0")
    @Test
    void occurrencesOnFieldWithThreeCardsKingdomRequiredWrongOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards of required kingdom placed in almost correct orientation should return 0")
    @Test
    void occurrencesOnFieldWithThreeCardsKingdomRequiredAlmostCorrectOrientationShouldBeZero() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 0, 2, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 4 cards of required kingdom placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFourCardsKingdomRequiredCorrectOrientationShouldBeOne() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(16, 0, pointStrategy, CornerType.FUNGI, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -3, 3, true); // Card placed side-up
        f.placeCardInField(c4, -4, 4, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 5 cards of required kingdom placed in correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFiveCardsKingdomRequiredCorrectOrientationShouldBeOne() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(16, 0, pointStrategy, CornerType.FUNGI, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c5 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.MANUSCRIPT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -3, 3, true); // Card placed side-up
        f.placeCardInField(c4, -4, 4, true); // Card placed side-up
        f.placeCardInField(c5, -5, 5, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 6 cards of required kingdom placed in almost correct orientation should return 1")
    @Test
    void occurrencesOnFieldWithFiveCardsKingdomRequiredAlmostCorrectOrientationShouldBeOne() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(16, 0, pointStrategy, CornerType.FUNGI, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c5 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.MANUSCRIPT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c6 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -3, 3, true); // Card placed side-up
        f.placeCardInField(c4, -4, 4, true); // Card placed side-up
        f.placeCardInField(c5, -5, 5, true); // Card placed side-up
        f.placeCardInField(c6, 1, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with 6 cards of required kingdom placed in correct orientation should return 2")
    @Test
    void occurrencesOnFieldWithFiveCardsKingdomRequiredCorrectOrientationShouldBeTwo() {
        Diagonals strategy = new Diagonals(ObjectType.PLANT,false);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(13, 0, pointStrategy, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c4 = new NonObjectiveCard(16, 0, pointStrategy, CornerType.FUNGI, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c5 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.MANUSCRIPT, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.ANIMAL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c6 = new NonObjectiveCard(17, 0, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        f.placeCardInField(c2, -2, 2, true); // Card placed side-up
        f.placeCardInField(c3, -3, 3, true); // Card placed side-up
        f.placeCardInField(c4, -4, 4, true); // Card placed side-up
        f.placeCardInField(c5, -5, 5, true); // Card placed side-up
        f.placeCardInField(c6, -6, 6, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurences(f, 0, 0));
    }
}
