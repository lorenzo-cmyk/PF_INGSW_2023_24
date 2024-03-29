package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountResourceTest {
    // Setting up field
    ObjectType initialType;
    int initialCount;
    int initialId = 0;
    int initialValue = 0;
    PointStrategy initialPointStrategy = new Empty();
    int[] initialPermRes = {0, 0, 0, 0};
    int[] initialConditionCount = {0, 0, 0, 0};

    ObjectType initialKingdom = null;

    NonObjectiveCard initialNonObjCard = new NonObjectiveCard(initialId, initialValue, initialPointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, initialPermRes, initialConditionCount, initialKingdom);
    Card initialCard = new Card(initialId, initialValue, initialPointStrategy);

    Field f = new Field(initialNonObjCard, true); // Create a new field with face-up initial card

    // Setting up parameters for cards to be placed in the field that are irrelevant for testing

    int value = 0;
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 0, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};
    ObjectType kingdom = null;

    // Begin testing
    // Note: "Empty field" means a field containing just the initial card
    @DisplayName("Strategy called on empty field containing just initial card should return 0")
    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        // Setting up strategy object
        for (ObjectType ob : ObjectType.values()) {
            for (int i = 1; i <= 3; i++) {
                CountResource strategy = new CountResource(ob, i);
                assertEquals(0, strategy.calculateOccurences(f, 0, 0));
            }
        }
    }

    @DisplayName("Strategy called on field with 1 card with different type requested should return 0")
    @Test
    void occurrencesOnFieldWithOneDifferentTypeCardShouldBeZero() {
        CountResource strategy = new CountResource(ObjectType.FUNGI, 3);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on the field with 1 card with the quantity of resources or objects visible lower than the quantity requested should return 0")
    @Test
    void occurrencesOnFieldWithOneCardNotEnoughResourceShouldBeZero() {
        CountResource strategy = new CountResource(ObjectType.PLANT, 3);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true);
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @DisplayName("Strategy called on the field with cards satisfied the requirements of the given card for once should return 1")
    @Test
    void occurrencesOnFieldWithRequirementsSatisfiedOnceShouldBeOne() {
        CountResource strategy = new CountResource(ObjectType.PLANT, 3);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurences(f, 0, 0));
    }
    @DisplayName("Strategy called on field with cards satisfied the requirements of the given card for twice should return 2")
    @Test
    void occurrencesOnFieldWithRequirementsSatisfiedTwiceShouldBeTwo() {
        CountResource strategy = new CountResource(ObjectType.PLANT, 3);
        NonObjectiveCard c1 = new NonObjectiveCard(11, 0, pointStrategy, CornerType.PLANT, CornerType.EMPTY, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c2 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.PLANT, CornerType.PLANT, CornerType.NON_COVERABLE, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        NonObjectiveCard c3 = new NonObjectiveCard(12, 0, pointStrategy, CornerType.EMPTY, CornerType.NON_COVERABLE, CornerType.PLANT, CornerType.PLANT, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, ObjectType.PLANT);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 2, true); // Card placed side-up
        f.placeCardInField(c3, 2, 2, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurences(f, 0, 0));
    }

}


