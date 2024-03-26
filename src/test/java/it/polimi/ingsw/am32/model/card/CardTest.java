package it.polimi.ingsw.am32.model.card;

import it.polimi.ingsw.am32.model.card.pointstrategy.Empty;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @DisplayName("Id getter should return id attribute")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void getIdShouldWork(int id) {
        Card c = new Card(id, 0, null);
        assertEquals(id, c.getId());
    }

    @DisplayName("Value getter should return value attribute")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void getValueShouldWork(int value) {
        Card c = new Card(0, value, null);
        assertEquals(value, c.getValue());
    }

    @DisplayName("Strategy getter should return strategy attribute")
    @Test
    void getStrategyShouldWork() {
        PointStrategy strategy = new Empty();
        Card c = new Card(0, 0, strategy);
        assertEquals(strategy, c.getPointStrategy());
    }
}