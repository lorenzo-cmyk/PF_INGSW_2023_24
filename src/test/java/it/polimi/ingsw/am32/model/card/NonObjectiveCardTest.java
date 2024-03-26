package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonObjectiveCardTest {
    @DisplayName("Top left corner getter should return topLeft attribute")
    @Test
    void getTopLeftShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, corner, null, null, null, null, null, null, null, null, null, null);
        assertEquals(corner, c.getTopLeft());
    }

    @DisplayName("Top right corner getter should return topRight attribute")
    @Test
    void getTopRightShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, corner, null, null, null, null, null, null, null, null, null);
        assertEquals(corner, c.getTopRight());
    }

    @DisplayName("Bottom left corner getter should return bottomLeft attribute")
    @Test
    void getBottomLeftShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, corner, null, null, null, null, null, null, null, null);
        assertEquals(corner, c.getBottomLeft());
    }

    @DisplayName("Bottom right corner getter should return bottomRight attribute")
    @Test
    void getBottomRightShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, corner, null, null, null, null, null, null, null);
        assertEquals(corner, c.getBottomRight());
    }

    @DisplayName("Top left back corner getter should return topLeftBack attribute")
    @Test
    void getTopLeftBackShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, corner, null, null, null, null, null, null);
        assertEquals(corner, c.getTopLeftBack());
    }

    @DisplayName("Top right back corner getter should return topRightBack attribute")
    @Test
    void getTopRightBackShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, corner, null, null, null, null, null);
        assertEquals(corner, c.getTopRightBack());
    }

    @DisplayName("Bottom left back corner getter should return bottomLeftBack attribute")
    @Test
    void getBottomLeftBackShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, null, corner, null, null, null, null);
        assertEquals(corner, c.getBottomLeftBack());
    }

    @DisplayName("Bottom right back corner getter should return bottomRightBack attribute")
    @Test
    void getBottomRightBackShouldWork() {
        CornerType corner = CornerType.ANIMAL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, null, null, corner, null, null, null);
        assertEquals(corner, c.getBottomRightBack());
    }

    @DisplayName("Permanent resource getter should return permRes attribute")
    @Test
    void getPermResShouldWork() {
        int[] ar = {0,1,1,1};
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, null, null, null, ar, null, null);
        assertEquals(ar, c.getPermRes());
    }

    @DisplayName("Condition count getter should return conditionCount attribute")
    @Test
    void getConditionCountShouldWork() {
        int[] ar = {0,2,2,1};
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, null, null, null, null, ar, null);
        assertEquals(ar, c.getConditionCount());
    }

    @DisplayName("Kingdom getter should return kingdom attribute")
    @Test
    void getKingdomShouldWork() {
        ObjectType ob = ObjectType.QUILL;
        NonObjectiveCard c = new NonObjectiveCard(0, 0, null, null, null, null, null, null, null, null, null, null, null, ob);
        assertEquals(ob, c.getKingdom());
    }
}