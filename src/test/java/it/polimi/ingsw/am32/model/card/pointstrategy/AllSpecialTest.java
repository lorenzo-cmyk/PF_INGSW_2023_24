package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllSpecialTest {
    Field f = new Field();
    AllSpecial strategy = new AllSpecial();

    @Test
    void occurrencesOnEmptyFieldShouldBeZero() {
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }

    @Test
    void occurrencesOnFieldWithOneCardShouldBeZero() {
        int[] permRes = {0, 0, 0, 0};
        int[] conditionCount = {0, 0, 0, 0};
        NonObjectiveCard c1 = new NonObjectiveCard(0, 2, null, null, null, null, null, null, null, null, null, permRes, conditionCount, null);
        assertEquals(0, strategy.calculateOccurences(f, 0, 0));
    }
}