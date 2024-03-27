package it.polimi.ingsw.am32.model.deck.utils;

/**
 * This enum represents the types of decks available in the game.
 * Each enum constant corresponds to a specific type of deck and is associated with the path to the JSON file that
 * contains the card data for that deck.
 *
 * @author Lorenzo
 */
public enum DeckType {
    RESOURCE("src/main/resources/it/polimi/ingsw/am32/model/deck/ResourceCards.json"),
    GOLD("src/main/resources/it/polimi/ingsw/am32/model/deck/GoldCards.json"),
    STARTING("src/main/resources/it/polimi/ingsw/am32/model/deck/StartingCards.json"),
    OBJECTIVE("src/main/resources/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json");

    private final String JSONPath;

    DeckType(String JSONPath) {
        this.JSONPath = JSONPath;
    }

    /**
     * Returns the path to the JSON file that contains the card data for the deck.
     *
     * @return the path to the JSON file
     * @author Lorenzo
     */
    public String getJSONPath() {
        return JSONPath;
    }

}
