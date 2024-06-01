package it.polimi.ingsw.am32.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am32.client.exceptions.MissingJSONException;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class is used to create the Non-Objective Cards from the JSON file, in the side of the client. In this way
 * the client can have a copy of the cards that can be used to display the cards in the UI.
 * @author Jie
 */
public class ObjectiveCardFactory {
    private final int ID;
    private final int Value;
    private final String PointStrategy;
    private final String PointStrategyType;
    private final int PointStrategyCount;
    private final boolean PointStrategyLeftToRight;
/**
 * This is the constructor of the class. It is used to create the cards from the JSON file.
 * @param ID defines the ID of the card
 * @param Value defines the value of the card
 * @param PointStrategy defines the strategy of the card to get the points
 * @param PointStrategyType indicates which type of resource or object should be counted if the PointStrategy is
 *                          “CountResource”, for the other PointStrategy it is null
 * @param PointStrategyCount indicates for “CountResource” card how many resources or objects should be counted to
 *                           get the points
 * @param PointStrategyLeftToRight for the “Diagonal” PointStrategy, it indicated the direction of the diagonal:
 *                                 true for left to right y=x, false for right to left y=-x.
 */
    public ObjectiveCardFactory(int ID, int Value, String PointStrategy, String PointStrategyType,
                                int PointStrategyCount, boolean PointStrategyLeftToRight){
        this.ID=ID;
        this.Value=Value;
        this.PointStrategy=PointStrategy;
        this.PointStrategyType=PointStrategyType;
        this.PointStrategyCount=PointStrategyCount;
        this.PointStrategyLeftToRight=PointStrategyLeftToRight;
    }
    /**
     * This method is used to create the cards from the JSON file.
     * @return an ArrayList containing the cards
     */
    public static ArrayList<ObjectiveCardFactory> setObjectiveCardArray() {
        // Initialize the ArrayList to store the cards
        ArrayList<ObjectiveCardFactory> ObjectiveCards = new ArrayList<>();
        // Initialize the ObjectMapper object needed to unpack the JSON file
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file: Objective Cards
            InputStream resourceStreamObjective = ObjectiveCardFactory.class.getResourceAsStream("/it/polimi/ingsw/am32/model/deck/ObjectiveCards.json");
            String fileContentObjective = new String(resourceStreamObjective.readAllBytes());
            // Perform the unpacking
            JsonNode jsonNode = objectMapper.readTree(fileContentObjective);

            // Iterate over the JSON array
            if (jsonNode.isArray()) {
                for (JsonNode currentNode : jsonNode) {
                    // Build the card object
                    ObjectiveCardFactory card= new ObjectiveCardFactory(
                            currentNode.get("ID").asInt(),
                            currentNode.get("Value").asInt(),
                            currentNode.get("PointStrategy").asText(),
                            currentNode.get("PointStrategy_Type").asText(),
                            currentNode.get("PointStrategy_Count").asInt(),
                            currentNode.get("PointStrategy_LeftToRight").asBoolean());
                    // Add the card to the ArrayList
                    ObjectiveCards.add(card);
                }
            }
        } catch (Exception e) {
            throw new MissingJSONException("Unable to locate JSON file.");
        }
        // Return the ArrayList
        return ObjectiveCards;
    }
    // Getters

    /**
     * This method is used to get the ID of the card.
     * @return the ID of the card
     */
    public int getID() {
        return ID;
    }
    /**
     * This method is used to get the value of the card.
     * @return the value of the card
     */
    public int getValue() {
        return Value;
    }
    /**
     * This method is used to get the strategy of the card to get the points.
     * @return the strategy of the card to get the points
     */
    public String getPointStrategy() {
        return PointStrategy;
    }
    /**
     * This method is used to get the type of resource or object should be counted if the PointStrategy is
     * “CountResource”.
     * @return the type of resource or object should be counted.
     */
    public String getPointStrategyType() {
        return PointStrategyType;
    }
    /**
     * This method is used to get how many resources or objects should be counted to get the points.
     * @return how many resources or objects should be counted to get the points
     */
    public int getPointStrategyCount() {
        return PointStrategyCount;
    }
    /**
     * This method is used to get the direction of the diagonal for the “Diagonal” PointStrategy.
     * @return the direction of the diagonal
     */
    public boolean getPointStrategyLeftToRight() {
        return PointStrategyLeftToRight;
    }

}
