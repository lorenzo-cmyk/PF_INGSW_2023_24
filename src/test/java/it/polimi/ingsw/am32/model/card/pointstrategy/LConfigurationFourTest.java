package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LConfigurationFourTest {
    // Setting up parameters for initial card
    int initialId = 0;
    int initialValue = 0;
    PointStrategy initialPointStrategy = new Empty();
    int[] initialPermRes = {0, 0, 0, 0};
    int[] initialConditionCount = {0, 0, 0, 0};
    ObjectType initialKingdom = null;
    NonObjectiveCard initialCard = new NonObjectiveCard(initialId, initialValue, initialPointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, initialPermRes, initialConditionCount, initialKingdom);

    // Setting up field with initial card
    Field f = new Field(initialCard, true); // Create a new field with face-up initial card

    // Setting up strategy object
    PointStrategy strategy = new LConfigurationFour();

    // Setting up parameters for cards to be placed in the field that are irrelevant for testing
    // For this test, only fungi (red) cards are used of value 0 requiring no resources present in order to be placed
    int value = 0;
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 0, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};

    // Begin testing

    @DisplayName("Strategy called on empty field containing just initial card should return 0")
    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing 2 stacked animal cards should return 0")
    @Test
    void occurrencesOnFieldWithAlmostOneLVariant1ShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, 1, 1, true);
        f.placeCardInField(c2, 1, -1, true);
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing animal card and fungi card that almost make a pattern should return 0")
    @Test
    void occurrencesOnFieldWithAlmostOneLVariant2ShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing single full pattern should return 1")
    @Test
    void occurrencesOnFieldWithOneLVariant1ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing single full pattern with one card turned should return 1")
    @Test
    void occurrencesOnFieldWithOneLVariant2ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, false);
        f.placeCardInField(c3, 2, 2, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing single full pattern with all cards turned should return 1")
    @Test
    void occurrencesOnFieldWithOneLVariant3ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, false);
        f.placeCardInField(c2, 1, 1, false);
        f.placeCardInField(c3, 2, 2, false);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing single full pattern and a partial pattern overlapping it should return 1")
    @Test
    void occurrencesOnFieldWithOneLWithOverlapVariant1ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, true);
        f.placeCardInField(c5, 2, 4, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing single full pattern and a partial pattern overlapping it with one card turned should return 1")
    @Test
    void occurrencesOnFieldWithOneLWithOverlapVariant2ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, false);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, true);
        f.placeCardInField(c5, 2, 4, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing almost single full pattern and a partial pattern overlapping it should return 0")
    @Test
    void occurrencesOnFieldWithAlmostOneLWithOverlapVariant1ShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, true);
        f.placeCardInField(c5, 2, 4, true);
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing triple overlapping pattern should return 2")
    @Test
    void occurrencesOnFieldWithTripleOverlappingLVariant1ShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, true);
        f.placeCardInField(c5, 2, 4, true);
        f.placeCardInField(c6, 1, 5, true);
        f.placeCardInField(c7, 2, 6, true);
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing triple overlapping pattern and one card turned should return 2")
    @Test
    void occurrencesOnFieldWithTripleOverlappingLVariant2ShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, false);
        f.placeCardInField(c5, 2, 4, true);
        f.placeCardInField(c6, 1, 5, true);
        f.placeCardInField(c7, 2, 6, true);
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing triple overlapping pattern and one non animal card breaking the pattern should return 1")
    @Test
    void occurrencesOnFieldWithTripleOverlappingLVariant3ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, false);
        f.placeCardInField(c5, 2, 4, true);
        f.placeCardInField(c6, 1, 5, true);
        f.placeCardInField(c7, 2, 6, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing 5-fold overlapping pattern should return 3")
    @Test
    void occurrencesOnFieldWithFiveFoldOverlappingLVariant1ShouldBeThree() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c8 = new NonObjectiveCard(8, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c9 = new NonObjectiveCard(9, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c10 = new NonObjectiveCard(10, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c11 = new NonObjectiveCard(11, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, false);
        f.placeCardInField(c5, 2, 4, true);
        f.placeCardInField(c6, 1, 5, true);
        f.placeCardInField(c7, 2, 6, true);
        f.placeCardInField(c8, 1, 7, true);
        f.placeCardInField(c9, 2, 8, true);
        f.placeCardInField(c10, 1, 9, true);
        f.placeCardInField(c11, 2, 10, true);
        assertEquals(3, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing 5-fold overlapping pattern and one non animal card breaking the pattern should return 2")
    @Test
    void occurrencesOnFieldWithFiveFoldOverlappingLVariant2ShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c8 = new NonObjectiveCard(8, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c9 = new NonObjectiveCard(9, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c10 = new NonObjectiveCard(10, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c11 = new NonObjectiveCard(11, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        f.placeCardInField(c1, 1, -1, true);
        f.placeCardInField(c2, 1, 1, true);
        f.placeCardInField(c3, 2, 2, true);
        f.placeCardInField(c4, 1, 3, false);
        f.placeCardInField(c5, 2, 4, true);
        f.placeCardInField(c6, 1, 5, true);
        f.placeCardInField(c7, 2, 6, true);
        f.placeCardInField(c8, 1, 7, true);
        f.placeCardInField(c9, 2, 8, true);
        f.placeCardInField(c10, 1, 9, true);
        f.placeCardInField(c11, 2, 10, true);
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing double full pattern side to side should return 2")
    @Test
    void occurrencesOnFieldWithTwoLSideToSideVariant1ShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, -1, -1, true);
        f.placeCardInField(c2, -1, 1, true);
        f.placeCardInField(c3, 0, 2, true);
        f.placeCardInField(c4, -2, 0, true);
        f.placeCardInField(c5, -3, -1, true);
        f.placeCardInField(c6, -4, -2, true);
        f.placeCardInField(c7, -3, -3, true);
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing double full pattern side to side and one non animal card breaking one pattern should return 1")
    @Test
    void occurrencesOnFieldWithTwoLSideToSideVariant2ShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.INSECT);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, -1, -1, true);
        f.placeCardInField(c2, -1, 1, true);
        f.placeCardInField(c3, 0, 2, true);
        f.placeCardInField(c4, -2, 0, true);
        f.placeCardInField(c5, -3, -1, true);
        f.placeCardInField(c6, -4, -2, true);
        f.placeCardInField(c7, -3, -3, true);
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field containing complex pattern should return 2")
    @Test
    void occurrencesOnFieldWithComplexPatternVariant1ShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.FUNGI);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        NonObjectiveCard c8 = new NonObjectiveCard(8, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.ANIMAL);
        f.placeCardInField(c1, -1, -1, true);
        f.placeCardInField(c2, -1, 1, true);
        f.placeCardInField(c3, 0, 2, true);
        f.placeCardInField(c4, -2, -2, true);
        f.placeCardInField(c5, -2, 0, true);
        f.placeCardInField(c6, -3, -1, true);
        f.placeCardInField(c7, -3, -3, true);
        f.placeCardInField(c8, -2, -4, true);
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }
}