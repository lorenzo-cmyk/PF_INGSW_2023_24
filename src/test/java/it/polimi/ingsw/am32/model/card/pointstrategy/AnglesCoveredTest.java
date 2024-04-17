package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.am32.model.exceptions.MissingRequirementsException;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnglesCoveredTest {
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
    PointStrategy strategy = new AnglesCovered();

    // Setting up parameters for dummy cards to be placed in the field that are irrelevant for testing
    // For this test, only fungi (red) cards are used of value 0 requiring no resources present in order to be placed
    int value = 0;
    PointStrategy pointStrategy = new Empty();
    int[] permRes = {0, 1, 0, 0};
    int[] conditionCount = {0, 0, 0, 0};
    ObjectType kingdom = ObjectType.FUNGI;

    // Begin testing
    // Note: "Empty field" means a field containing just the initial card
    // Note: In the field we place a collection of cards whose parameters have been defined above. One such card will be
    // the card the strategy will reference.

    @DisplayName("Strategy called on field with strategy card placed on initial card at the top right (excluding initial card) should return 1")
    @Test
    void occurrencesOnFieldWithOneCardAtTopRightOfInitialCardShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 1, 1));
    }

    @DisplayName("Strategy called on field with strategy card placed on initial card at the top left (excluding initial card) should return 1")
    @Test
    void occurrencesOnFieldWithOneCardAtTopLeftOfInitialCardShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, -1, 1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, -1, 1));
    }

    @DisplayName("Strategy called on field with strategy card placed on initial card at the bottom right (excluding initial card) should return 1")
    @Test
    void occurrencesOnFieldWithOneCardAtBottomRightOfInitialCardShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, -1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, 1, -1));
    }

    @DisplayName("Strategy called on field with strategy card placed on initial card at the bottom left (excluding initial card) should return 1")
    @Test
    void occurrencesOnFieldWithOneCardAtBottomLeftOfInitialCardShouldBeOne() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, -1, -1, true); // Card placed side-up
        assertEquals(1, strategy.calculateOccurrences(f, -1, -1));
    }

    @DisplayName("Strategy called on field with strategy card covering the bottom right of one card, and the top right of another should return 2")
    @Test
    void occurrencesOnFieldWithStrategyCardCoveringBottomRightAndTopRightShouldBeTwo() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, 1, true); // Card placed side-up
        f.placeCardInField(c2, 1, -1, true); // Card placed side-up
        f.placeCardInField(c3, 2, 0, true); // Card placed side-up
        assertEquals(2, strategy.calculateOccurrences(f, 2, 0));
    }

    @DisplayName("Strategy called on field with strategy card covering the bottom right, bottom left, and top left of various cards should return 3")
    @Test
    void occurrencesOnFieldWithStrategyCardCoveringBottomRightAndBottomLeftAndTopLeftShouldBeThree() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, -1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, -2, true); // Card placed side-up
        assertEquals(3, strategy.calculateOccurrences(f, 1, -1));
    }

    @DisplayName("Strategy called on field with strategy card covering the bottom right, bottom left, top left, and top right of various cards should return 4")
    @Test
    void occurrencesOnFieldWithStrategyCardCoveringBottomRightAndBottomLeftAndTopLeftAndTopRightShouldBeFour() throws MissingRequirementsException, InvalidPositionException {
        NonObjectiveCard c1 = new NonObjectiveCard(1, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c2 = new NonObjectiveCard(2, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c3 = new NonObjectiveCard(3, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        NonObjectiveCard c4 = new NonObjectiveCard(4, value, pointStrategy, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, CornerType.EMPTY, permRes, conditionCount, kingdom);
        f.placeCardInField(c1, 1, -1, true); // Card placed side-up
        f.placeCardInField(c2, 2, 0, true); // Card placed side-up
        f.placeCardInField(c3, 2, -2, true); // Card placed side-up
        f.placeCardInField(c4, 0, -2, true); // Card placed side-up
        assertEquals(4, strategy.calculateOccurrences(f, 1, -1));
    }
}