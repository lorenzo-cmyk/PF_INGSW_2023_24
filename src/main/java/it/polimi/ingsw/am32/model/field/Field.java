package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;

import java.util.ArrayList;

public class Field {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private final ArrayList<CardPlaced> fieldCards;
    private final int[] activeRes;
    private static final int resourcesSize = 7;


    //---------------------------------------------------------------------------------------------
    // Constructors

    /**
     * Initialize the field, put resources counter to 0 and insert the initial card
     *
     * @param initialCard id the card that was assigned
     * @param isUp denote the side of the card chosen by the player
     */
    public Field(NonObjectiveCard initialCard, boolean isUp) {

        this.activeRes = new int[resourcesSize];
        this.fieldCards = new ArrayList<>();

        CardPlaced cardPlaced = new CardPlaced(initialCard, 0, 0, isUp);
        fieldCards.addFirst(cardPlaced);

        int[] resToAdd = resourcesObtained(initialCard, isUp);

        activeRes[0] = resToAdd[0];
        activeRes[1] = resToAdd[1];
        activeRes[2] = resToAdd[2];
        activeRes[3] = resToAdd[3];
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    // TODO Javadoc
    public boolean placeCardInField(NonObjectiveCard nonObjectiveCard, int x, int y, boolean side) {

        if( x > 40 || x < -40 || y > 40 || y < -40)
            return false;

        // Checking resource requirements for placement
        // TODO parlare modifica CornderType e ObjectType per idea

        if (!checkResRequirements(activeRes, nonObjectiveCard.getConditionCount()))
            return false;

        // Find possible diagonal cards

        CardPlaced[] tmpCardsPlaced = new CardPlaced[4];

        for (CardPlaced cardPlaced : fieldCards) {

            int tmpX = cardPlaced.getX();
            int tmpY = cardPlaced.getY();


            if (tmpX == x && tmpY == y)
                return false;

            if (tmpX == x - 1 && tmpY == y + 1)
                tmpCardsPlaced[0] = cardPlaced;
            if (tmpX == x + 1 && tmpY == y + 1)
                tmpCardsPlaced[1] = cardPlaced;
            if (tmpX == x - 1 && tmpY == y - 1)
                tmpCardsPlaced[2] = cardPlaced;
            if (tmpX == x + 1 && tmpY == y - 1)
                tmpCardsPlaced[3] = cardPlaced;
        }

        // Check if exist at least one
        // TODO it might exist a better algorithm

        CornerType[] cornerType = new CornerType[4];

        boolean tmpFilled = false;

        for(int i = 0; i < 4; i++)
            if (tmpCardsPlaced[i] != null) {
                tmpFilled = true;
                break;
            }

        if(!tmpFilled)
            return false;

        if (tmpCardsPlaced[0] != null && tmpCardsPlaced[0].getIsUp()) {
            cornerType[0] = tmpCardsPlaced[0].getNonObjectiveCard().getBottomRight();
            if (cornerType[0] == CornerType.NON_COVERABLE)
                return false;
        }
        if (tmpCardsPlaced[1] != null && tmpCardsPlaced[1].getIsUp()) {
            cornerType[1] = tmpCardsPlaced[1].getNonObjectiveCard().getBottomRight();
            if (cornerType[1] == CornerType.NON_COVERABLE)
                return false;
        }
        if (tmpCardsPlaced[2] != null && tmpCardsPlaced[2].getIsUp()) {
            cornerType[2] = tmpCardsPlaced[2].getNonObjectiveCard().getBottomRight();
            if (cornerType[2] == CornerType.NON_COVERABLE)
                return false;
        }
        if (tmpCardsPlaced[3] != null && tmpCardsPlaced[3].getIsUp()) {
            cornerType[3] = tmpCardsPlaced[3].getNonObjectiveCard().getBottomRight();
            if (cornerType[3] == CornerType.NON_COVERABLE)
                return false;
        }

        // Place card in field

        CardPlaced newCardPlaced = new CardPlaced(nonObjectiveCard, x, y, side);

        fieldCards.addFirst(newCardPlaced);

        // Add gained resources
        // TODO risorse perse/guadagnate

        int[] resToAdd = resourcesObtained(nonObjectiveCard, side);

        activeRes[0] = activeRes[0] + resToAdd[0];
        activeRes[1] = activeRes[1] + resToAdd[1];
        activeRes[2] = activeRes[2] + resToAdd[2];
        activeRes[3] = activeRes[3] + resToAdd[3];
        activeRes[4] = activeRes[4] + resToAdd[4];
        activeRes[5] = activeRes[5] + resToAdd[5];
        activeRes[6] = activeRes[6] + resToAdd[6];

        // Subtract lost resources

        int[] resToSub = resourceCornersConverter(cornerType[0], cornerType[1], cornerType[2], cornerType[3]);

        activeRes[0] = activeRes[0] - resToSub[0];
        activeRes[1] = activeRes[1] - resToSub[1];
        activeRes[2] = activeRes[2] - resToSub[2];
        activeRes[3] = activeRes[3] - resToSub[3];
        activeRes[4] = activeRes[4] - resToSub[4];
        activeRes[5] = activeRes[5] - resToSub[5];
        activeRes[6] = activeRes[6] - resToSub[6];

        return true;
    }

    /**
     * Returns the card at the given position if available.
     *
     * @param x X position of the card in the field to return
     * @param y Y position of the card in the field to return
     * @return NonObjectiveCard at given coordinates if present in the field, else null.
     */
    public NonObjectiveCard getCardFromPosition(int x, int y) {

        for (CardPlaced i : fieldCards) {
            if (i.getX() == x && i.getY() == y) {
                return i.getNonObjectiveCard();
            }
        }

        return null;
    }

    /**
     * Verify if a specified space in the field is not already occupied by a card
     *
     * @param x is the coordinate on the horizontal axis for the space searched
     * @param y is the coordinate on the vertical axis for the space searched
     * @return true if the space is available, false if not
     */
    public boolean availableSpace(int x, int y) {

        for (CardPlaced fieldCard : fieldCards)
            if (fieldCard.getX() == x && fieldCard.getY() == y)
                return false;
        return true;

        //TODO manca controllo sulle carte addiacenti
    }

    /**
     * Converts the given corners to an array of integers (of size 7) containing the number of occurrences of the 7
     * resources in the card corners. the array is order following the convention used for ObjectType
     *
     * @param first  is the specific corner of the card
     * @param second is the specific corner of the card
     * @param third  is the specific corner of the card
     * @param forth  is the specific corner of the card
     * @return the array of integer
     */
    public static int[] resourceCornersConverter(CornerType first, CornerType second, CornerType third, CornerType forth){

        int[] results = new int[7];
        CornerType[] tmpCorners = new CornerType[]{first, second, third, forth};

        for(CornerType cornerType: tmpCorners) {
            if (cornerType == CornerType.PLANT)
                results[CornerType.PLANT.getValue()]++;
            if(cornerType == CornerType.FUNGI)
                results[CornerType.FUNGI.getValue()]++;
            if(cornerType == CornerType.ANIMAL)
                results[CornerType.ANIMAL.getValue()]++;
            if(cornerType == CornerType.INSECT)
                results[CornerType.INSECT.getValue()]++;
            if(cornerType == CornerType.QUILL)
                results[CornerType.QUILL.getValue()]++;
            if(cornerType == CornerType.INKWELL)
                results[CornerType.INKWELL.getValue()]++;
            if(cornerType == CornerType.MANUSCRIPT)
                results[CornerType.MANUSCRIPT.getValue()]++;
        }

        return results;
    }

    /**
     * Given the two arrays check if the values of the first array are grater then those of the second array in the
     * same index for every position of the second array
     *
     * @param resources is the array that contain the current resources
     * @param requirements is the array of the requirements for the resources
     * @return true if the check result is positive, false if the length of the second array is grater than that of
     * the first array or if the checking process resulted negatively
     */
    public static boolean checkResRequirements(int[] resources, int[] requirements){

        if(requirements.length >= resources.length)
            return false;

        for (int i = 0; i < requirements.length; i++)
            if(resources[i] < requirements[i])
                return false;

        return true;
    }

    // TODO Javadoc
    public static int[] resourcesObtained(NonObjectiveCard nonObjectiveCard, boolean isUp){

        int[] result = new int[7];

        if(isUp){

            int[] tmpAdders = resourceCornersConverter(nonObjectiveCard.getTopLeft(), nonObjectiveCard.getTopRight(),
                    nonObjectiveCard.getBottomLeft(), nonObjectiveCard.getBottomRight());

            result[0] = tmpAdders[0];
            result[1] = tmpAdders[1];
            result[2] = tmpAdders[2];
            result[3] = tmpAdders[3];
            result[4] = tmpAdders[4];
            result[5] = tmpAdders[5];
            result[6] = tmpAdders[6];


        }else{

            int[] tmpBases = nonObjectiveCard.getPermRes();

            int[] tmpAdders = resourceCornersConverter(nonObjectiveCard.getTopLeftBack(), nonObjectiveCard.getTopRightBack(),
                    nonObjectiveCard.getBottomLeftBack(), nonObjectiveCard.getBottomRightBack());

            result[0] = tmpBases[0] + tmpAdders[0];
            result[1] = tmpBases[1] + tmpAdders[1];
            result[2] = tmpBases[2] + tmpAdders[2];
            result[3] = tmpBases[3] + tmpAdders[3];
            result[4] = tmpAdders[4];
            result[5] = tmpAdders[5];
            result[6] = tmpAdders[6];

        }

        return result;
    }

    //---------------------------------------------------------------------------------------------
    // Getters

    /**
     * Getter
     *
     * @return the structure containing the all the placed cards
     */
    public ArrayList<CardPlaced> getFieldCards() {
        return fieldCards;
    }

    /**
     * Getter
     *
     * @param type is the nature of the resource requested
     * @return the amount of occurrences in the field of the specified resource
     */
    public int getActiveRes(ObjectType type) {
        return activeRes[type.getValue()];
    }

    /**
     * Getter
     *
     * @return the array containing the amount of each resource of the field
     */
    public int[] getAllRes(){
        return activeRes;
    }
}
