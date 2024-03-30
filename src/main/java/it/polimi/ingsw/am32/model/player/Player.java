package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private String nickname;
    private Field gameField;
    private Card secretObjective;
    private Colour colour;
    private int points;
    private ArrayList<NonObjectiveCard> hand;
    private Card[] tmpSecretObj;

    private final static int secObjOptions = 2;


    //---------------------------------------------------------------------------------------------
    // Constructors

    /**
     * Initialize the player
     *
     * @param nickname name of the player
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.gameField = null;
        this.points = 0;
        this.secretObjective = null;
        this.hand = null;
        this.colour = null;
        this.tmpSecretObj = new Card[secObjOptions];
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Place the initial card in the hand of the player
     *
     * @param initialCard is the initial card assigned by the match
     * @return true if the assignment was successful, false if the hand wasn't empty and the assignment failed
     */
    public boolean assignStartingCard(NonObjectiveCard initialCard) {

        if(hand != null)
            return false;

        hand = new ArrayList<NonObjectiveCard>();
        hand.addLast(initialCard);
        return true;
    }


    /**
     * Create the field and place the initial card in it
     *
     * @param isUp denote the side of the card chosen by the player
     * @return true if the process was successful, false if the method was already executed
     */
    public boolean initializeGameField(boolean isUp) {

        if (gameField != null || hand == null)
            return false;

        gameField = new Field(hand.getFirst(), isUp);
        hand.clear();
        return true;
    }


    /**
     * Search in the player hand for the card that has to be selected and saved as the secret objective
     *
     * @param id of the card chosen by the player
     * @return true if the card was successfully chosen, false if the card wasn't in the hand of the player
     */
    public boolean secretObjectiveSelection(int id) {
        if(tmpSecretObj[0].getId() == id) {
            secretObjective = tmpSecretObj[0];
        } else if (tmpSecretObj[1].getId() == id){
            secretObjective = tmpSecretObj[1];
        } else
            return false;

        return true;
    }


    /**
     * Place the two card given in the parameters in the hand so that that one of them can be chosen as secret objective
     *
     * @param firstCard is the first choice for the secret objective
     * @param secondCard is the second choice for the secret objective
     * @return true if the cards where successfully saved for later selection, false if not
     */
    public boolean receiveSecretObjective(Card firstCard, Card secondCard) {

        if(tmpSecretObj[0] != null || tmpSecretObj[1] != null)
            return false;

        tmpSecretObj[0] = firstCard;
        tmpSecretObj[1] = secondCard;
        return true;
    }


    /**
     * tries to put a card in the hand of the player
     *
     * @param newCard is the card that has to be added to the hand of the player
     * @return true if successfully added, false if not
     */
    public boolean putCardInHand(NonObjectiveCard newCard) {

        if(hand == null || hand.size() >= 3)
            return false;

        hand.addLast(newCard);
        return true;
    }

    // TODO Javadoc
    public boolean performMove(int id, int x, int y, boolean isUp) {

        if(gameField == null)
            return false;

        NonObjectiveCard nonObjectiveCard = null;
        int tmpVar = -1;

        for (int i = 0; i < hand.size(); i++)
            if(hand.get(i).getId() == id) {
                nonObjectiveCard = hand.get(i);
                tmpVar = i;
                break;
            }
        
        if(nonObjectiveCard == null)
            return false;
        
        boolean result = gameField.placeCardInField(nonObjectiveCard, x, y, isUp);

        if(!result)
            return false;

        hand.remove(tmpVar);
        
        PointStrategy pointStrategy = nonObjectiveCard.getPointStrategy();

        // TODO check if next line can fail
        int occurrences = pointStrategy.calculateOccurences(gameField, x, y);

        points = points + occurrences * nonObjectiveCard.getValue();

        return true;
    }

    /**
     * Use the given cards to calculate the extra points that the player gains from the common objectives and add them
     * to the current personal points
     *
     * @param objectiveCards are the common objectives used to calculate the extra points
     * @return true if the points were successfully calculated and added, false if not
     */
    public boolean updatePointsForObjectives(Card[] objectiveCards) {

        if(objectiveCards[0].getPointStrategy() == null || objectiveCards[1].getPointStrategy() == null)
            return false;

        PointStrategy pointStrategy1 = objectiveCards[0].getPointStrategy();
        PointStrategy pointStrategy2 = objectiveCards[1].getPointStrategy();

        int multiplierPoints1 = pointStrategy1.calculateOccurences(gameField, 0,0);
        int multiplierPoints2 = pointStrategy2.calculateOccurences(gameField, 0,0);

        int value1 = objectiveCards[0].getValue();
        int value2 = objectiveCards[1].getValue();

        points = points + value1 * multiplierPoints1 + value2 * multiplierPoints2;

        return true;
    }

    // TODO is it actually used?
    public int calculatePoints(Card card, int x, int y) {
        // TODO
        return 0;
    }


    //---------------------------------------------------------------------------------------------
    // Getters

    /**
     * Getter:
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter:
     *
     * @return the field if initialized, null if not
     */
    public Field getField() {
        return gameField;
    }

    /**
     * Getter:
     *
     * @return a Card if the secretObjective has already been selected, null if not
     */
    public Card getSecretObjective() {
        return secretObjective;
    }

    /**
     * Getter:
     *
     * @return the color of the player
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Getter:
     *
     * @return the current points of the player
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter:
     *
     * @return the hand of the player
     */
    public ArrayList<NonObjectiveCard> getHand() {
        return hand;
    }

    /**
     * Find the initial card, if assigned, and returns it
     *
     * @return The initial Card, if it was non already assigned returns null
     */
    public Card getInitialCard() {
        if( gameField != null)
            return gameField.getCardFromPosition(0,0);
        else {
            try {
                return hand.getFirst();
            } catch (NoSuchElementException e){
                return null;
            }
        }

        // TODO check if it can be done better
    }


    //---------------------------------------------------------------------------------------------
    // Setters

    /**
     * Setter
     *
     * @param points is the value to be set as number of points of the player
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * assign a colour to the player if it doesn't have already one
     *
     * @param colour is the colour of the player
     * @return true if successfully assigned, false if not
     */
    public boolean setColour(Colour colour) {
        if(this.colour != null)
            return false;

        this.colour = colour;
        return true;
    }

    //---------------------------------------------------------------------------------------------
    // ToString

    // TODO parsing method
}