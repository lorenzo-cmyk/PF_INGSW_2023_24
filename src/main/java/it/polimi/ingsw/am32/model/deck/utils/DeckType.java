package it.polimi.ingsw.am32.model.deck.utils;

/**
 * This enum represents the types of decks available in the game.
 * Each enum constant corresponds to a specific type of deck and is associated with the path to the JSON file that
 * contains the card data for that deck.
 *
 * @author Lorenzo
 */
public enum DeckType {
    /**
     * The deck that contains the resource cards.
     */
    RESOURCE("/it/polimi/ingsw/am32/model/deck/ResourceCards.json"),
    /**
     * The deck that contains the gold cards.
     */
    GOLD("/it/polimi/ingsw/am32/model/deck/GoldCards.json"),
    /**
     * The deck that contains the starting cards.
     */
    STARTING("/it/polimi/ingsw/am32/model/deck/StartingCards.json"),
    /**
     * The deck that contains the objective cards.
     */
    OBJECTIVE("/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json");

    /**
     * The path to the JSON file that contains the card data for the deck.
     */
    private final String JSONPath;

    /**
     * Constructor for the DeckType enum.
     *
     * @param JSONPath The path to the JSON file that contains the card data for the deck.
     */
    DeckType(String JSONPath) {
        this.JSONPath = JSONPath;
    }

    /**
     * Returns the path to the JSON file that contains the card data for the deck.
     *
     * @return the path to the JSON file
     */
    public String getJSONPath() {
        return JSONPath;
    }

}
