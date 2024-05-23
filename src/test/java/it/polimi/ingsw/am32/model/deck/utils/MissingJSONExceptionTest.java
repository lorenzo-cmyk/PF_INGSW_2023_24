package it.polimi.ingsw.am32.model.deck.utils;

import it.polimi.ingsw.am32.model.exceptions.MissingJSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissingJSONExceptionTest {
    @DisplayName("Exception should retain detail message")
    @Test
    void exceptionShouldRetainDetailMessage() {
        String detailMessage = "Unable to locate JSON file.";
        MissingJSONException exception = new MissingJSONException(detailMessage);
        assertEquals(detailMessage, exception.getMessage());
    }
}
