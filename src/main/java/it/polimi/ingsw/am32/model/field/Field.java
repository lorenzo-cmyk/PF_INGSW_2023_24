package it.polimi.ingsw.am32.model.field;

import it.polimi.ingsw.am32.model.card.CornerType;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.ObjectType;
import it.polimi.ingsw.am32.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.am32.model.exceptions.MissingRequirementsException;
import it.polimi.ingsw.am32.model.exceptions.RollbackException;

import java.util.ArrayList;

/**
 * Used to store information about the field of the player. It contains the cards placed and the resources available.
 * @author Matteo
 */
public class Field {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    /**
     * The list of cards placed in the field.
     */
    private final ArrayList<CardPlaced> fieldCards;
    /**
     * The resources currently owned by the player
     */
    private final int[] activeRes;
    /**
     * The number of resources in the game
     */
    private static final int resourcesSize = 7;
    /**
     * The resources owned by the player before the last card was placed; used for rollback
     */
    private final int[] oldActiveRes;

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
        this.oldActiveRes = new int[resourcesSize];
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

    /**
     * Verifies if it's possible to place the card in the field. If it is, place the card and update the available
     * resources in the field
     *
     * @param nonObjectiveCard is the card to be placed
     * @param x is the horizontal coordinate of the position
     * @param y is the vertical coordinate of the position
     * @param isUp is the side of the card
     * @throws InvalidPositionException if the position is not valid
     * @throws MissingRequirementsException if the resources are not enough
     */
    public void placeCardInField(NonObjectiveCard nonObjectiveCard, int x, int y, boolean isUp)
            throws InvalidPositionException, MissingRequirementsException {

        // Checking valid position

        if( x > 40 || x < -40 || y > 40 || y < -40)
            throw new InvalidPositionException("Attempted to place a card out of range.");

        if (Math.abs((x + y) % 2) == 1)
            throw new InvalidPositionException("Attempted to place a card in a non even position.");

        // Checking resource requirements for placement

        if (isUp && !checkResRequirements(activeRes, nonObjectiveCard.getConditionCount()))
            throw new MissingRequirementsException("Requirements not fulfilled.");

        // Find possible diagonal cards

        CardPlaced[] tmpCardsPlaced = new CardPlaced[4];

        for (CardPlaced cardPlaced : fieldCards) {

            int tmpX = cardPlaced.getX();
            int tmpY = cardPlaced.getY();


            if (tmpX == x && tmpY == y)
                throw new InvalidPositionException("Attempted to place a card in an occupied position.");

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

        CornerType[] cornerType = new CornerType[4];

        boolean tmpFilled = false;

        for(int i = 0; i < 4; i++)
            if (tmpCardsPlaced[i] != null) {
                tmpFilled = true;
                break;
            }

        if(!tmpFilled)
            throw new InvalidPositionException("Attempted to place a card in an isolated position.");

        if (tmpCardsPlaced[0] != null) {

            if (tmpCardsPlaced[0].getIsUp())
                cornerType[0] = tmpCardsPlaced[0].getNonObjectiveCard().getBottomRight();
            else
                cornerType[0] = tmpCardsPlaced[0].getNonObjectiveCard().getBottomRightBack();

            if (cornerType[0] == CornerType.NON_COVERABLE)
                throw new InvalidPositionException("Attempted to place a card in a non-coverable position.");
        }

        if (tmpCardsPlaced[1] != null) {

            if (tmpCardsPlaced[1].getIsUp())
                cornerType[1] = tmpCardsPlaced[1].getNonObjectiveCard().getBottomLeft();
            else
                cornerType[1] = tmpCardsPlaced[1].getNonObjectiveCard().getBottomLeftBack();

            if (cornerType[1] == CornerType.NON_COVERABLE)
                throw new InvalidPositionException("Attempted to place a card in a non-coverable position.");
        }

        if (tmpCardsPlaced[2] != null) {

            if (tmpCardsPlaced[2].getIsUp())
                cornerType[2] = tmpCardsPlaced[2].getNonObjectiveCard().getTopRight();
            else
                cornerType[2] = tmpCardsPlaced[2].getNonObjectiveCard().getTopRightBack();

            if (cornerType[2] == CornerType.NON_COVERABLE)
                throw new InvalidPositionException("Attempted to place a card in a non-coverable position.");
        }

        if (tmpCardsPlaced[3] != null) {

            if (tmpCardsPlaced[3].getIsUp())
                cornerType[3] = tmpCardsPlaced[3].getNonObjectiveCard().getTopLeft();
            else
                cornerType[3] = tmpCardsPlaced[3].getNonObjectiveCard().getTopLeftBack();

            if (cornerType[3] == CornerType.NON_COVERABLE)
                throw new InvalidPositionException("Attempted to place a card in a non-coverable position.");
        }

        // Place card in field

        CardPlaced newCardPlaced = new CardPlaced(nonObjectiveCard, x, y, isUp);

        fieldCards.addFirst(newCardPlaced);

        // Backup old resources in case of later rollback.
        // This is done by overwriting the oldActiveRes array with the current activeRes array
        System.arraycopy(activeRes, 0, oldActiveRes, 0, resourcesSize);

        // Add gained resources

        int[] resToAdd = resourcesObtained(nonObjectiveCard, isUp);

        activeRes[0] += resToAdd[0];
        activeRes[1] += resToAdd[1];
        activeRes[2] += resToAdd[2];
        activeRes[3] += resToAdd[3];
        activeRes[4] += resToAdd[4];
        activeRes[5] += resToAdd[5];
        activeRes[6] += resToAdd[6];

        // Subtract lost resources

        int[] resToSub = resourceCornersConverter(cornerType[0], cornerType[1], cornerType[2], cornerType[3]);

        activeRes[0] -= resToSub[0];
        activeRes[1] -= resToSub[1];
        activeRes[2] -= resToSub[2];
        activeRes[3] -= resToSub[3];
        activeRes[4] -= resToSub[4];
        activeRes[5] -= resToSub[5];
        activeRes[6] -= resToSub[6];
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
     * Verify whether a card can be freely placed at the given coordinates
     *
     * @param x is the coordinate on the horizontal axis for the space searched
     * @param y is the coordinate on the vertical axis for the space searched
     * @return true if the space is available, false if not
     *
     * @author anto, Jie
     */
    public boolean availableSpace(int x, int y) {
        if (Math.abs((x + y) % 2) == 1)
            return false; // Impossible position

        for (CardPlaced fieldCard : fieldCards)
            if (fieldCard.getX() == x && fieldCard.getY() == y)
                return false; // Found a card occupying x,y

        // Conditions for space availability:
        // - At least one card must be adjacent to positions x and y (diagonally)
        // - No adjacent card can have a non-coverable corner at x and y

        boolean adjacentCards = false; // Flag indicating whether there are no adjacent cards
        for (CardPlaced cardPlaced : fieldCards) { // Scan all cards on the field
            if (cardPlaced.getX() == x+1 && cardPlaced.getY() == y-1) { // Found card at the bottom right
                adjacentCards = true;
                if (cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getTopLeft() == CornerType.NON_COVERABLE) {
                    return false;
                } else if (!cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getTopLeftBack() == CornerType.NON_COVERABLE) {
                    return false;
                }
            }
            else if (cardPlaced.getX() == x-1 && cardPlaced.getY() == y-1) {
                adjacentCards = true;
                if (cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getTopRight() == CornerType.NON_COVERABLE) {
                    return false;
                } else if (!cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getTopRightBack() == CornerType.NON_COVERABLE) {
                    return false;
                }
            }
            else if (cardPlaced.getX() == x+1 && cardPlaced.getY() == y+1) {
                adjacentCards = true;
                if (cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getBottomLeft() == CornerType.NON_COVERABLE) {
                    return false;
                } else if (!cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getBottomLeftBack() == CornerType.NON_COVERABLE) {
                    return false;
                }
            }
            else if (cardPlaced.getX() == x-1 && cardPlaced.getY() == y+1) {
                adjacentCards = true;
                if (cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getBottomRight() == CornerType.NON_COVERABLE) {
                    return false;
                } else if(!cardPlaced.getIsUp() && cardPlaced.getNonObjectiveCard().getBottomRightBack() == CornerType.NON_COVERABLE) {
                    return false;
                }
            }
        }

        return adjacentCards;
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
    protected static int[] resourceCornersConverter(CornerType first, CornerType second, CornerType third, CornerType forth){

        int[] results = new int[ObjectType.values().length];
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
     * Given the two arrays check if the values of the first array are greater or equal then those of the second array
     * in the same index for every position of the second array
     *
     * @param resources is the array that contain the current resources
     * @param requirements is the array of the requirements for the resources
     * @return true if the check result is positive, false if the length of the second array is grater than that of
     * the first array or if the checking process resulted negatively
     */
    protected static boolean checkResRequirements(int[] resources, int[] requirements){

        if(requirements.length > resources.length)
            return false;

        for (int i = 0; i < requirements.length; i++)
            if(resources[i] < requirements[i])
                return false;

        return true;
    }

    /**
     * Given the card and the side, return an array of integer where at each position (according to the attribute
     * value of the enumeration ObjectType) is inserted the number of resources gained if the card is placed
     *
     * @param nonObjectiveCard is the card that has to be placed
     * @param isUp is the side, of the card, relevant to the play
     * @return the array of integer
     */
    protected static int[] resourcesObtained(NonObjectiveCard nonObjectiveCard, boolean isUp){

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

    /**
     * This method is used to roll back the last operation performed on the field. It removes the last card placed
     * in the field and restores the resources to their previous state.
     *
     * @return The NonObjectiveCard that was removed from the field.
     * @throws RollbackException if the field is empty or if there is only the starting card in the field.
     */
    public NonObjectiveCard rollback() throws RollbackException {
        // We can't roll back if there is only one card in the field. We can't remove the starting card.
        if (fieldCards.size() == 1) {
            throw new RollbackException("The field has only the starting card in it.");
        } else if (fieldCards.isEmpty()) {
            throw new RollbackException("The field is empty.");
        }

        // Remove the last card placed in the field.
        CardPlaced removedCard = fieldCards.removeFirst();

        // Restore resources to previous state.
        // This is done by overwriting the activeRes array with the oldActiveRes array.
        System.arraycopy(oldActiveRes, 0, activeRes, 0, resourcesSize);

        // Return the removed card.
        return removedCard.getNonObjectiveCard();
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
