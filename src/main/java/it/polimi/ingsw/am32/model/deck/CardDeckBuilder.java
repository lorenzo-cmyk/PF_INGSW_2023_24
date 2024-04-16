package it.polimi.ingsw.am32.model.deck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;
import it.polimi.ingsw.am32.model.exceptions.MissingJSONException;
import it.polimi.ingsw.am32.model.exceptions.WrongDeckTypeException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is responsible for building a deck of Cards.
 * It uses the ObjectsBuilder class to perform Strings to Objects conversion.
 *
 * @author Lorenzo
 */
public class CardDeckBuilder {
    /**
     * The ObjectsBuilder object used to perform Strings to Objects conversion.
     */
    private final ObjectsBuilder objectsBuilder = new ObjectsBuilder();

    /**
     * Builds a deck of Cards of the specified type.
     *
     * @param deckType The type of the deck to be built.
     * @return A CardDeck object containing the cards of the specified type.
     * @exception WrongDeckTypeException Thrown if the deckType parameter does not match any DeckType
     */
    public CardDeck buildCardDeck(DeckType deckType) {
        if (deckType != DeckType.OBJECTIVE) {
            throw new WrongDeckTypeException("Attempted to build a NonObjectiveCard deck using CardDeckBuilder.");
        } else {
            CardDeck deck = new CardDeck(loadCardsFromDisk(deckType), deckType);
            deck.shuffle();
            return deck;
        }
    }

    /**
     * Loads the cards from the disk.
     * The cards are stored in a JSON file, which is read and parsed to create the Card objects.
     *
     * @param deckType The type of the deck to be loaded.
     * @return An ArrayList of Card objects
     * @exception MissingJSONException Thrown if the JSON could not be located
     */
    private ArrayList<Card> loadCardsFromDisk(DeckType deckType) {
        // Initialize the ArrayList to store the cards
        ArrayList<Card> cards = new ArrayList<>();
        // Initialize the ObjectMapper object needed to unpack the JSON file
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file
            String fileContent = new String(Files.readAllBytes(Paths.get(deckType.getJSONPath())));
            // Perform the unpacking
            JsonNode jsonNode = objectMapper.readTree(fileContent);

            // Iterate over the JSON array
            if (jsonNode.isArray()) {
                for (JsonNode currentNode : jsonNode) {
                    // Build the card object
                    Card card = new Card(
                            currentNode.get("ID").asInt(),
                            currentNode.get("Value").asInt(),
                            objectsBuilder.stringsToPointStrategy(
                                    currentNode.get("PointStrategy").asText(),
                                    currentNode.get("PointStrategy_Type").asText(),
                                    currentNode.get("PointStrategy_Count").asInt(),
                                    currentNode.get("PointStrategy_LeftToRight").asBoolean()
                            )
                    );
                    // Add the card to the ArrayList
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            throw new MissingJSONException("Unable to locate JSON file.");
        }
        // Return the ArrayList
        return cards;
    }

}
