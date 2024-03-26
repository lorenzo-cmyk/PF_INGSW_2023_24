package it.polimi.ingsw.am32.model.deck;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DecksFromDisk {

    private final static String startingCardsJSONPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/StartingCards.json";
    private final static String objectiveCardsJSONPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json";
    private final static String resourceCardsJSONPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/ResourceCards.json";
    private final static String goldCardsJSONPath = "src/main/resources/it/polimi/ingsw/am32/model/deck/GoldCards.json";

    /**
     * This method is used to create a deck of resource Cards from a JSON file.
     * It calls the createNonObjectiveCardDeck method with the path to the JSON file that contains the resource card data.
     *
     * @return An ArrayList of NonObjectiveCard objects, each representing a resource card from the JSON file.
     * @author Lorenzo
     */
    public ArrayList<NonObjectiveCard> createResourcesDeck() {
        return createNonObjectiveCardDeck(resourceCardsJSONPath);
    }

    /**
     * This method is used to create a deck of gold Cards from a JSON file.
     * It calls the createNonObjectiveCardDeck method with the path to the JSON file that contains the gold card data.
     *
     * @return An ArrayList of NonObjectiveCard objects, each representing a gold card from the JSON file.
     * @author Lorenzo
     */
    public ArrayList<NonObjectiveCard> createGoldDeck() {
        return createNonObjectiveCardDeck(goldCardsJSONPath);
    }

    /**
     * This method is used to create a deck of starting Cards from a JSON file.
     * It calls the createNonObjectiveCardDeck method with the path to the JSON file that contains the starting card data.
     *
     * @return An ArrayList of NonObjectiveCard objects, each representing a starting card from the JSON file.
     * @author Lorenzo
     */
    public ArrayList<NonObjectiveCard> createStartCardsDeck() {
        return createNonObjectiveCardDeck(startingCardsJSONPath);
    }

    /**
     * This method is used to create a deck of objective Cards from a JSON file.
     * It calls the createCardDeck method with the path to the JSON file that contains the objective card data.
     *
     * @return An ArrayList of Card objects, each representing an objective card from the JSON file.
     * @author Lorenzo
     */
    public ArrayList<Card> objectiveCardsDeck() {
        return createCardDeck(objectiveCardsJSONPath);
    }

    /**
     * This method is used to create a deck of NonObjectiveCards from a JSON file.
     * It reads the JSON file, unpacks it, and creates a NonObjectiveCard object for each JSON object in the array.
     * Each NonObjectiveCard object is then added to an ArrayList, which is returned at the end of the method.
     *
     * @param JSONPath The path to the JSON file that contains the card data.
     * @return An ArrayList of NonObjectiveCard objects, each representing a card from the JSON file.
     * @throws Exception If there is an error reading the JSON file or creating the NonObjectiveCard objects.
     * @author Lorenzo
     */
    private ArrayList<NonObjectiveCard> createNonObjectiveCardDeck(String JSONPath) {
        // Initialize the ArrayList to store the cards
        ArrayList<NonObjectiveCard> cards = new ArrayList<>();
        // Initialize the ObjectMapper object needed to unpack the JSON file
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file
            String fileContent = new String(Files.readAllBytes(Paths.get(JSONPath)));
            // Perform the unpacking
            JsonNode jsonNode = objectMapper.readTree(fileContent);

            // Iterate over the JSON array
            if (jsonNode.isArray()) {
                for (JsonNode currentNode : jsonNode) {
                    // Build the card object

                    int[] permRes = new int[]{
                            currentNode.get("PermRes.PLANT").asInt(), currentNode.get("PermRes.FUNGI").asInt(),
                            currentNode.get("PermRes.ANIMAL").asInt(), currentNode.get("PermRes.INSECT").asInt(),
                            currentNode.get("PermRes.QUILL").asInt(), currentNode.get("PermRes.INKWELL").asInt(),
                            currentNode.get("PermRes.MANUSCRIPT").asInt()
                    };

                    int[] conditionCount = new int[]{
                            currentNode.get("ConditionCount.PLANT").asInt(), currentNode.get("ConditionCount.FUNGI").asInt(),
                            currentNode.get("ConditionCount.ANIMAL").asInt(), currentNode.get("ConditionCount.INSECT").asInt(),
                            currentNode.get("ConditionCount.QUILL").asInt(), currentNode.get("ConditionCount.INKWELL").asInt(),
                            currentNode.get("ConditionCount.MANUSCRIPT").asInt()
                    };

                    NonObjectiveCard card = new NonObjectiveCard(
                            currentNode.get("ID").asInt(),
                            currentNode.get("Value").asInt(),
                            stringsToPointStrategy(
                                    currentNode.get("PointStrategy").asText(),
                                    currentNode.get("PointStrategy_Type").asText(),
                                    currentNode.get("PointStrategy_Count").asInt(),
                                    currentNode.get("PointStrategy_LeftToRight").asBoolean()
                            ),
                            stringToCornerType(currentNode.get("TopLeft").asText()),
                            stringToCornerType(currentNode.get("TopRight").asText()),
                            stringToCornerType(currentNode.get("BottomLeft").asText()),
                            stringToCornerType(currentNode.get("BottomRight").asText()),
                            stringToCornerType(currentNode.get("TopLeftBack").asText()),
                            stringToCornerType(currentNode.get("TopRightBack").asText()),
                            stringToCornerType(currentNode.get("BottomLeftBack").asText()),
                            stringToCornerType(currentNode.get("BottomRightBack").asText()),
                            permRes,
                            conditionCount,
                            stringToObjectType(currentNode.get("Kingdom").asText())
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

    /**
     * Converts a string to its corresponding CornerType enum value.
     *
     * @param str the string to be converted. It should match one of the CornerType enum values.
     * @return The corresponding CornerType enum value, if the string matches, null if it doesn't.
     * @author Lorenzo
     */
    private CornerType stringToCornerType(String str) {
        return switch (str) {
            case "PLANT" -> CornerType.PLANT;
            case "FUNGI" -> CornerType.FUNGI;
            case "ANIMAL" -> CornerType.ANIMAL;
            case "INSECT" -> CornerType.INSECT;
            case "QUILL" -> CornerType.QUILL;
            case "INKWELL" -> CornerType.INKWELL;
            case "MANUSCRIPT" -> CornerType.MANUSCRIPT;
            case "EMPTY" -> CornerType.EMPTY;
            case "NON_COVERABLE" -> CornerType.NON_COVERABLE;
            default -> null;
        };
    }

    /**
     * This method is used to create a deck of Cards from a JSON file.
     * It reads the JSON file, unpacks it, and creates a Card object for each JSON object in the array.
     * Each Card object is then added to an ArrayList, which is returned at the end of the method.
     *
     * @param JSONPath The path to the JSON file that contains the card data.
     * @return An ArrayList of Card objects, each representing a card from the JSON file.
     * @throws Exception If there is an error reading the JSON file or creating the Card objects.
     * @author Lorenzo
     */
    private ArrayList<Card> createCardDeck(String JSONPath) {
        // Initialize the ArrayList to store the cards
        ArrayList<Card> cards = new ArrayList<>();
        // Initialize the ObjectMapper object needed to unpack the JSON file
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file
            String fileContent = new String(Files.readAllBytes(Paths.get(JSONPath)));
            // Perform the unpacking
            JsonNode jsonNode = objectMapper.readTree(fileContent);

            // Iterate over the JSON array
            if (jsonNode.isArray()) {
                for (JsonNode currentNode : jsonNode) {
                    // Build the card object
                    Card card = new Card(
                            currentNode.get("ID").asInt(),
                            currentNode.get("Value").asInt(),
                            stringsToPointStrategy(
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
            // TODO: The game has not yet a logging system.
            e.printStackTrace();
            // A card game without cards is not playable. Terminate the program.
            System.exit(1);
        }
        // Return the ArrayList
        return cards;
    }

    /**
     * Converts the attributes extracted from the JSON to a usable PointStrategy object.
     *
     * @param pointStrategy            PointStrategy type. It should match one of the PointStrategy implementation.
     * @param pointStrategyType        The ObjectType needed by some of the strategies.
     * @param pointStrategyCount       The number of a given object to be counted. @see CountResource
     * @param pointStrategyLeftToRight The axis on which the strategy is going to perform the search.
     * @return The corresponding PointStrategy object, if the parameters provided are valid, null if they don't.
     * @author Lorenzo
     */
    private PointStrategy stringsToPointStrategy(String pointStrategy, String pointStrategyType,
                                                 int pointStrategyCount, boolean pointStrategyLeftToRight) {
        switch (pointStrategy) {
            case "AllSpecial" -> {
                return new AllSpecial();
            }
            case "AnglesCovered" -> {
                return new AnglesCovered();
            }
            case "CountResource" -> {
                return new CountResource(stringToObjectType(pointStrategyType), pointStrategyCount);
            }
            case "Diagonals" -> {
                return new Diagonals(stringToObjectType(pointStrategyType), pointStrategyLeftToRight);
            }
            case "Empty" -> {
                return new Empty();
            }
            case "LConfigurationOne" -> {
                return new LConfigurationOne();
            }
            case "LConfigurationTwo" -> {
                return new LConfigurationTwo();
            }
            case "LConfigurationThree" -> {
                return new LConfigurationThree();
            }
            case "LConfigurationFour" -> {
                return new LConfigurationFour();
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Converts a string to its corresponding ObjectType enum value.
     *
     * @param str the string to be converted. It should match one of the ObjectType enum values.
     * @return The corresponding ObjectType enum value, if the string matches, null if it doesn't.
     * @author Lorenzo
     */
    private ObjectType stringToObjectType(String str) {
        return switch (str) {
            case "PLANT" -> ObjectType.PLANT;
            case "FUNGI" -> ObjectType.FUNGI;
            case "ANIMAL" -> ObjectType.ANIMAL;
            case "INSECT" -> ObjectType.INSECT;
            case "QUILL" -> ObjectType.QUILL;
            case "INKWELL" -> ObjectType.INKWELL;
            case "MANUSCRIPT" -> ObjectType.MANUSCRIPT;
            default -> null;
        };
    }

}
