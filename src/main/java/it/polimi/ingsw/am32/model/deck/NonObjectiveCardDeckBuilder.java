package it.polimi.ingsw.am32.model.deck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.deck.utils.DeckType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is responsible for building a deck of NonObjectiveCards.
 * It uses the ObjectsBuilder class to perform Strings to Objects conversion.
 *
 * @author Lorenzo
 */
public class NonObjectiveCardDeckBuilder {
    /**
     * The ObjectsBuilder object used to perform Strings to Objects conversion.
     */
    private final ObjectsBuilder objectsBuilder = new ObjectsBuilder();

    /**
     * Builds a deck of NonObjectiveCards of the specified type.
     *
     * @param deckType The type of the deck to be built.
     * @return A NonObjectiveCardDeck object containing the cards of the specified type.
     * @author Lorenzo
     */
    public NonObjectiveCardDeck buildNonObjectiveCardDeck(DeckType deckType) {
        if (deckType == DeckType.OBJECTIVE) {
            return null;
        } else {
            NonObjectiveCardDeck deck = new NonObjectiveCardDeck(loadCardsFromDisk(deckType), deckType);
            deck.shuffle();
            return deck;
        }
    }

    /**
     * Loads the cards from the disk.
     * The cards are stored in a JSON file, which is read and parsed to create the NonObjectiveCard objects.
     *
     * @param deckType The type of the deck to be loaded.
     * @return An ArrayList of NonObjectiveCard objects
     * @author Lorenzo
     */
    private ArrayList<NonObjectiveCard> loadCardsFromDisk(DeckType deckType) {
        // Initialize the ArrayList to store the cards
        ArrayList<NonObjectiveCard> cards = new ArrayList<>();
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
                    int[] permRes = new int[7];
                    permRes[ObjectType.PLANT.getValue()] = currentNode.get("PermRes.PLANT").asInt();
                    permRes[ObjectType.FUNGI.getValue()] = currentNode.get("PermRes.FUNGI").asInt();
                    permRes[ObjectType.ANIMAL.getValue()] = currentNode.get("PermRes.ANIMAL").asInt();
                    permRes[ObjectType.INSECT.getValue()] = currentNode.get("PermRes.INSECT").asInt();
                    permRes[ObjectType.QUILL.getValue()] = currentNode.get("PermRes.QUILL").asInt();
                    permRes[ObjectType.INKWELL.getValue()] = currentNode.get("PermRes.INKWELL").asInt();
                    permRes[ObjectType.MANUSCRIPT.getValue()] = currentNode.get("PermRes.MANUSCRIPT").asInt();

                    int[] conditionCount = new int[7];
                    conditionCount[ObjectType.PLANT.getValue()] = currentNode.get("ConditionCount.PLANT").asInt();
                    conditionCount[ObjectType.FUNGI.getValue()] = currentNode.get("ConditionCount.FUNGI").asInt();
                    conditionCount[ObjectType.ANIMAL.getValue()] = currentNode.get("ConditionCount.ANIMAL").asInt();
                    conditionCount[ObjectType.INSECT.getValue()] = currentNode.get("ConditionCount.INSECT").asInt();
                    conditionCount[ObjectType.QUILL.getValue()] = currentNode.get("ConditionCount.QUILL").asInt();
                    conditionCount[ObjectType.INKWELL.getValue()] = currentNode.get("ConditionCount.INKWELL").asInt();
                    conditionCount[ObjectType.MANUSCRIPT.getValue()] = currentNode.get("ConditionCount.MANUSCRIPT").asInt();

                    NonObjectiveCard card = new NonObjectiveCard(
                            currentNode.get("ID").asInt(),
                            currentNode.get("Value").asInt(),
                            objectsBuilder.stringsToPointStrategy(
                                    currentNode.get("PointStrategy").asText(),
                                    currentNode.get("PointStrategy_Type").asText(),
                                    currentNode.get("PointStrategy_Count").asInt(),
                                    currentNode.get("PointStrategy_LeftToRight").asBoolean()
                            ),
                            objectsBuilder.stringToCornerType(currentNode.get("TopLeft").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("TopRight").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("BottomLeft").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("BottomRight").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("TopLeftBack").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("TopRightBack").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("BottomLeftBack").asText()),
                            objectsBuilder.stringToCornerType(currentNode.get("BottomRightBack").asText()),
                            permRes,
                            conditionCount,
                            objectsBuilder.stringToObjectType(currentNode.get("Kingdom").asText())
                    );
                    // Add the card to the ArrayList
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            // TODO: The game has not yet a logging system.
            e.printStackTrace();
            // A card game without cards is not playable. Terminate the program.
            System.exit(1);
        }
        // Return the ArrayList
        return cards;
    }

}
