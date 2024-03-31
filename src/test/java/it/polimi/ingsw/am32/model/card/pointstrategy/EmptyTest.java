package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptyTest {
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
    @DisplayName("Strategy called on empty field containing just initial card should return 1")
    @Test
    void occurrencesOnEmptyFieldShouldBeOne() {
        Empty strategy = new Empty();
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on field with 1 card not initial card should return 1")
    @Test
    void occurrencesOnFieldWithOneCardKingdomRequiredShouldBeOne() {
        Empty strategy = new Empty();
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }
}