package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllSpecialTest {
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
    PointStrategy strategy = new AllSpecial();

    // Setting up parameters for cards to be placed in the field that are irrelevant for testing
    // For this test, only fungi (red) cards are used of value 0 requiring no resources present in order to be placed
    int value = 0;
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 1, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};
    ObjectType kingdom = ObjectType.FUNGI;

    // Begin testing
    // Note: "Empty field" means a field containing just the initial card

    @DisplayName("Strategy called on empty field containing just initial card should return 0")
    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card (excluding initial card) showing no objects should return 0")
    @Test
    void occurrencesOnFieldWithOneCardShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card showing a special object should return 0")
    @Test
    void occurrencesOnFieldWithOneSpecialCardShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards showing the same special object should return 0")
    @Test
    void occurrencesOnFieldWithThreeMatchingSpecialCardsShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards containing single set of all specials (one special symbol on each card) should return 1")
    @Test
    void occurrencesOnFieldWithSingleCompleteSetShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 3 cards containing almost a single set of all specials (one special symbol on each card) should return 0")
    @Test
    void occurrencesOnFieldWithAlmostSingleCompleteSetShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 4 cards containing single set of all specials plus an additional card (one special symbol on each card) should return 1")
    @Test
    void occurrencesOnFieldWithSingleCompleteSetPlusOneShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        f.placeCardInField(c4, 4, 4, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 6 cards containing double set of all specials (one special symbol on each card) should return 2")
    @Test
    void occurrencesOnFieldWithDoubleCompleteSetShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        f.placeCardInField(c4, 4, 4, true); // Card placed side-up
        f.placeCardInField(c5, 5, 5, true); // Card placed side-up
        f.placeCardInField(c6, 6, 6, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 6 cards containing almost a double set of all specials (one special symbol on each card) should return 1")
    @Test
    void occurrencesOnFieldWithAlmostDoubleCompleteSetShouldBeOne() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        f.placeCardInField(c4, 4, 4, true); // Card placed side-up
        f.placeCardInField(c5, 5, 5, true); // Card placed side-up
        f.placeCardInField(c6, 6, 6, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 7 cards containing double set of all specials plus an additional card (one special symbol on each card) should return 2")
    @Test
    void occurrencesOnFieldWithDoubleCompleteSetPlusOneShouldBeTwo() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.QUILL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c5 = new NonObjectiveCard(5, value, pointStrategy, CornerType.INKWELL, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c6 = new NonObjectiveCard(6, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c7 = new NonObjectiveCard(7, value, pointStrategy, CornerType.MANUSCRIPT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 3, 3, true); // Card placed side-up
        f.placeCardInField(c4, 4, 4, true); // Card placed side-up
        f.placeCardInField(c5, 5, 5, true); // Card placed side-up
        f.placeCardInField(c6, 6, 6, true); // Card placed side-up
        f.placeCardInField(c7, 7, 7, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurrences(f, 0, 0));
    }
}