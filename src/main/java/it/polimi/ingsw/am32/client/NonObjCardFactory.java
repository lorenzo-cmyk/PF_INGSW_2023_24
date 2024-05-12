package it.polimi.ingsw.am32.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am32.client.exceptions.MissingJSONException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Use this class to create a Non Objective card (Resource, Gold or Starting card) using the card data stored in the
 * JSON file, in the side of the client. In this way the client can have a copy of the cards that can be used to display
 * the cards in the UI.
 * @author Jie
 */
public class NonObjCardFactory {
    private final String CardType;
    private final int ID;
    private final int Value;
    private final String PointStrategy;
    private final String PointStrategyType;
    private final int PointStrategyCount;
    private final String Kingdom;
    private final String[] Corner;
    private final String[] CornerBack;
    private final int[] PermRes;
    private final int[] ConditionCount;

    /**
     * Constructor
     * @param cardType the type of the card
     * @param ID the ID of the card
     * @param Value the value of the card
     * @param PointStrategy the strategy of the card to get the points
     * @param PointStrategyType indicates which type of resource or object should be counted if the PointStrategy is
     *                         “CountResource”, for the other PointStrategy it is null
     * @param PointStrategyCount indicates for “CountResource” card how many resources or objects should be counted to
     *                           get the points
     * @param Kingdom indicates the kingdom which the card belongs
     * @param Corner the array contains the corner's type of the card
     * @param CornerBack the array contains the corner's type of the back side of the card
     * @param PermRes the array contains the permanent resources of the card
     * @param ConditionCount the array contains the requirements of the card that should be satisfied before the
     *                       placement.
     */
    public NonObjCardFactory(String cardType,int ID, int Value, String PointStrategy, String PointStrategyType,
                             int PointStrategyCount, String Kingdom, String[] Corner,
                                String[] CornerBack, int[] PermRes, int[] ConditionCount){
        this.CardType=cardType;
        this.ID=ID;
        this.Value=Value;
        this.PointStrategy=PointStrategy;
        this.PointStrategyType=PointStrategyType;
        this.PointStrategyCount=PointStrategyCount;
        this.Kingdom=Kingdom;
        this.Corner=Corner;
        this.CornerBack=CornerBack;
        this.PermRes=PermRes;
        this.ConditionCount=ConditionCount;
    }

    public static ArrayList<NonObjCardFactory> setNonObjCardArray() {
        // Initialize the ArrayList to store the cards
        ArrayList<NonObjCardFactory> NonObjCards = new ArrayList<>();
        // Initialize the ObjectMapper object needed to unpack the JSON file
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file: resource cards, gold cards and starting cards.
            String fileContentResource = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/it/polimi/ingsw/am32/model/deck/ResourceCards.json")));
            String fileContentGold = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/it/polimi/ingsw/am32/model/deck/GoldCards.json")));
            String fileContentStarting = new String(Files.readAllBytes(
                    Paths.get("src/main/resources/it/polimi/ingsw/am32/model/deck/StartingCards.json")));
            // Perform the unpacking
            JsonNode[] jsonNode= new JsonNode[3];
            jsonNode[0]=objectMapper.readTree(fileContentResource);
            jsonNode[1]=objectMapper.readTree(fileContentGold);
            jsonNode[2]=objectMapper.readTree(fileContentStarting);
            // Iterate over the JSON array
            for (int i = 0; i< 3; i++) {
                if (jsonNode[i].isArray()) {
                    for (JsonNode currentNode : jsonNode[i]) {
                        // Build the card object
                        NonObjCardFactory card = new NonObjCardFactory(
                                currentNode.get("CardType").asText(),
                                currentNode.get("ID").asInt(),
                                currentNode.get("Value").asInt(),
                                currentNode.get("PointStrategy").asText(),
                                currentNode.get("PointStrategy_Type").asText(),
                                currentNode.get("PointStrategy_Count").asInt(),
                                currentNode.get("Kingdom").asText(),
                                new String[]{
                                        currentNode.get("TopLeft").asText(),
                                        currentNode.get("TopRight").asText(),
                                        currentNode.get("BottomLeft").asText(),
                                        currentNode.get("BottomRight").asText()
                                },
                                new String[]{
                                        currentNode.get("TopLeftBack").asText(),
                                        currentNode.get("TopRightBack").asText(),
                                        currentNode.get("BottomLeftBack").asText(),
                                        currentNode.get("BottomRightBack").asText()
                                },
                                new int[]{
                                        currentNode.get("PermRes.PLANT").asInt(),
                                        currentNode.get("PermRes.FUNGI").asInt(),
                                        currentNode.get("PermRes.ANIMAL").asInt(),
                                        currentNode.get("PermRes.INSECT").asInt(),
                                        currentNode.get("PermRes.QUILL").asInt(),
                                        currentNode.get("PermRes.INKWELL").asInt()
                                },
                                new int[]{
                                        currentNode.get("ConditionCount.PLANT").asInt(),
                                        currentNode.get("ConditionCount.FUNGI").asInt(),
                                        currentNode.get("ConditionCount.ANIMAL").asInt(),
                                        currentNode.get("ConditionCount.INSECT").asInt(),
                                        currentNode.get("ConditionCount.QUILL").asInt(),
                                        currentNode.get("ConditionCount.INKWELL").asInt(),
                                        currentNode.get("ConditionCount.MANUSCRIPT").asInt()
                                });
                        // Add the card to the ArrayList
                        NonObjCards.add(card);
                    }
                }
            }
        } catch (Exception e) {
            throw new MissingJSONException("Unable to locate JSON file.");
        }
        // Return the ArrayList
        return NonObjCards;
    }
    //--- getters ----

    /**
     * @return the kingdom which the card belongs
     */
    public String getKingdom() {
        return Kingdom;
    }
    /**
     * @return the type of the card
     */
    public String getType() {
        return CardType;
    }
    /**
     * @return the ID of the card
     */
    public int getID() {
        return ID;
    }
    /**
     * @return the value of the card
     */
    public int getValue() {
        return Value;
    }
    /**
     * @return the strategy of the card to get the points
     */
    public String getPointStrategy() {
        return PointStrategy;
    }
    /**
     * @return the type of resource or object should be counted if the PointStrategy is “CountResource”
     */
    public String getPointStrategyType() {
        return PointStrategyType;
    }
    /**
     * @return the number of resources or objects should be counted to get the points
     */
    public int getPointStrategyCount() {
        return PointStrategyCount;
    }
    /**
     * @return the corner's type of the card
     */
    public String[] getCorner() {
        return Corner;
    }
    /**
     * @return the corner's type of the back side of card
     */
    public String[] getCornerBack() {
        return CornerBack;
    }
    /**
     * @return the permanent resources of the card
     */
    public int[] getPermRes() {
        return PermRes;
    }
    /**
     * @return the requirements of the card that should be satisfied before the placement
     */
    public int[] getConditionCount() {
        return ConditionCount;
    }

}
