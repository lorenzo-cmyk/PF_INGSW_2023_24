package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllSpecialTest {
    // Setting up field

    int initialId = 0;
    int initialValue = 0;
    PointStrategy initialPointStrategy = new Empty();
    int[] initialPermRes = {0, 0, 0, 0};
    int[] initialConditionCount = {0, 0, 0, 0};
    ObjectType initialKingdom = null;

    NonObjectiveCard initialCard = new NonObjectiveCard(initialId, initialValue, initialPointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, initialPermRes, initialConditionCount, initialKingdom);
    Field f = new Field(initialCard, true); // Create a new field with face-up initial card

    // Setting up strategy object

    AllSpecial strategy = new AllSpecial();

    // Setting up parameters for cards to be placed in the field that are irrelevant for testing

    int value = 0;
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 0, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};
    ObjectType kingdom = ObjectType.FUNGI;

    // Begin testing
    // Note: "Empty field" means a field containing just the initial card

    @DisplayName("Strategy called on empty field containing just initial card should return 0")
    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card (excluding initial card) showing no objects should return 0")
    @Test
    void occurrencesOnFieldWithOneCardShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card showing a special object should return 0")
    @Test
    void occurrencesOnFieldWithOneSpecialCardShouldBeZero() {
        NonObjectiveCard c1 = new NonObjectiveCard(0, 2, null, null, null, null, null, null, null, null, null, permRes, conditionCount, null);
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }
}