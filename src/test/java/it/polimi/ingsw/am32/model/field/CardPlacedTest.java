package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPlacedTest {

    //@SuppressWarnings("All")
    @DisplayName("Execute a Path Coverage Structural Test on all methods")
    @Test
    void testCardPlaced() {

        // Path Coverage

        int[] permRes = new int[]{0,0,0,0};
        int[] conditionCount = new int[]{0,0,0,0};
        boolean isUp = true;
        int x = 0, y = 0;

        NonObjectiveCard testCard = new NonObjectiveCard(0,0,null, CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount, ObjectType.ANIMAL);

        NonObjectiveCard testCard2 = new NonObjectiveCard(0,0,null, CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,CornerType.EMPTY,
                CornerType.EMPTY,CornerType.EMPTY,permRes, conditionCount, ObjectType.ANIMAL);

        CardPlaced testCardPlaced = new CardPlaced(testCard, x,y, isUp);

        CardPlaced testCardPlaced2 = new CardPlaced(testCard, x,y, isUp);
        CardPlaced testCardPlaced3 = new CardPlaced(testCard, x + 1,y, isUp);
        CardPlaced testCardPlaced4 = new CardPlaced(testCard, x,y + 1, isUp);
        CardPlaced testCardPlaced5 = new CardPlaced(testCard, x,y, !isUp);
        CardPlaced testCardPlaced6 = new CardPlaced(testCard2, x,y, !isUp);

        assertEquals(testCard,testCardPlaced.getNonObjectiveCard());
        assertEquals(x,testCardPlaced.getX());
        assertEquals(y,testCardPlaced.getY());
        assertEquals(isUp,testCardPlaced.getIsUp());

        assertTrue(testCardPlaced.equals(testCardPlaced));

        assertFalse(testCardPlaced.equals(null));
        assertFalse(testCardPlaced.equals(testCard));

        assertFalse(testCardPlaced.equals(testCardPlaced3));
        assertFalse(testCardPlaced.equals(testCardPlaced4));
        assertFalse(testCardPlaced.equals(testCardPlaced5));
        assertFalse(testCardPlaced.equals(testCardPlaced6));

        assertTrue(testCardPlaced.equals(testCardPlaced2));
        testCardPlaced.hashCode();
    }
}