package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.card.pointstrategy.PointStrategy;
import it.polimi.ingsw.am32.model.exceptions.*;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class is responsible for managing the player's actions and status.
 * @author Matteo
 */
public class Player {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private final String nickname;
    private Field gameField;
    private Card secretObjective;
    private Colour colour;
    private int points;
    private ArrayList<NonObjectiveCard> hand;
    private final Card[] tmpSecretObj;
    private int pointsGainedFromObjectives = 0;
    private final boolean[] objectivePointsState = new boolean[]{false, false};

    private final static int secObjOptions = 2;

    private int oldPoints;

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
        this.oldPoints = 0;
        this.secretObjective = null;
        this.hand = null;
        this.colour = null;
        this.tmpSecretObj = new Card[secObjOptions];
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Initializes the player hand and places the initial card in the player's hand
     *
     * @param initialCard is the initial card assigned by the match
     * @exception NonEmptyHandException Hand was not empty when we tried to assign the starting card
     */
    public void assignStartingCard(NonObjectiveCard initialCard) {

        if(hand != null)
            throw new NonEmptyHandException("Attempted to assign starting card with non-empty hand.");

        hand = new ArrayList<>();
        hand.addLast(initialCard);
    }


    /**
     * Create the field and place the initial card in it
     *
     * @param isUp denote the side of the card chosen by the player
     * @exception NonNullFieldException Field existed already (was not null)
     * @exception NullHandException Hand was null when tried to assign the starting card
     */
    public void initializeGameField(boolean isUp) {

        if (gameField != null)
            throw new NonNullFieldException("Attempted to initialize a non-null gameField");
        if (hand == null)
            throw new NullHandException("Attempted to place the starting card with empty hand.");

        gameField = new Field(hand.getFirst(), isUp);
        hand.clear();
    }


    /**
     * Search in the player hand for the card that has to be selected and saved as the secret objective
     *
     * @param id of the card chosen by the player
     * @throws InvalidSelectionException Tried to choose a non-valid secret objective card
     */
    public void secretObjectiveSelection(int id) throws InvalidSelectionException {
        if(tmpSecretObj[0].getId() == id) {
            secretObjective = tmpSecretObj[0];
        } else if (tmpSecretObj[1].getId() == id) {
            secretObjective = tmpSecretObj[1];
        } else
            throw new InvalidSelectionException("Attempted to select a card that was not in the options.");
    }


    /**
     * Place the two card given in the parameters in the hand so that that one of them can be chosen as secret objective
     *
     * @param firstCard is the first choice for the secret objective
     * @param secondCard is the second choice for the secret objective
     * @exception SecretObjectiveCardException Tried to receive secret objective when already received
     */
    public void receiveSecretObjective(Card firstCard, Card secondCard) {

        if(tmpSecretObj[0] != null || tmpSecretObj[1] != null)
            throw new SecretObjectiveCardException("Attempted to receive secret objective when already received.");

        tmpSecretObj[0] = firstCard;
        tmpSecretObj[1] = secondCard;
    }


    /**
     * tries to put a card in the hand of the player
     *
     * @param newCard is the card that has to be added to the hand of the player
     * @exception NullHandException Tried to add a card to a null hand
     * @exception InvalidHandSizeException Hand size was somehow greater than 3
     */
    public void putCardInHand(NonObjectiveCard newCard) {

        if (hand == null)
            throw new NullHandException("Attempted to add a card to a null hand.");
        if (hand.size() >= 3)
            throw new InvalidHandSizeException("Invalid hand size: " + hand.size());

        hand.addLast(newCard);
    }

    /**
     * Tries to take a card from the hand of the player and place it in the field. If the card is placed successfully
     * calculate the points gained from its placement and add them to those of the player
     *
     * @param id is the id of the card in the hand of the player that has to be placed
     * @param x is the horizontal coordinate of the position
     * @param y is the vertical coordinate of the position
     * @param isUp is the side of the card that is going to be visible when placed
     * @throws InvalidSelectionException Tried to place a card that was not in the player's hand
     * @throws MissingRequirementsException Tried to place a card in a position that was not empty
     * @throws InvalidPositionException Tried to place a card in a position that was not valid
     * @exception  NullFieldException Tried to place a card in a null field
     */
    public void performMove(int id, int x, int y, boolean isUp) throws InvalidSelectionException, MissingRequirementsException, InvalidPositionException {

        if(gameField == null)
            throw new NullFieldException("Attempted to place a card in a null field.");

        NonObjectiveCard nonObjectiveCard = null;
        int tmpVar = -1;

        for (int i = 0; i < hand.size(); i++)
            if(hand.get(i).getId() == id) {
                nonObjectiveCard = hand.get(i);
                tmpVar = i;
                break;
            }
        
        if(nonObjectiveCard == null)
            throw new InvalidSelectionException("Attempted to place a card that was not in the player's hand.");
        
        gameField.placeCardInField(nonObjectiveCard, x, y, isUp);

        hand.remove(tmpVar);

        // All the placeable cards: Gold, Resource and Starting cannot give
        // points to the player if they are placed with their back-up.

        // Backup the current points in case we need to revert the move later
        oldPoints = points;

        if(isUp){
            PointStrategy pointStrategy = nonObjectiveCard.getPointStrategy();

            int occurrences = pointStrategy.calculateOccurrences(gameField, x, y);

            points += occurrences * nonObjectiveCard.getValue();
        }
    }

    /**
     * Use the given cards to calculate the extra points that the player gains from the common objectives and add them
     * to the current personal points
     *
     * @param objectiveCards are the common objectives used to calculate the extra points
     * @exception NullPointStrategyException Found a card with a null PointStrategy
     * @throws AlreadyComputedPointsException Tried to calculate points when they were already calculated
     */
    public void updatePointsForObjectives(Card[] objectiveCards) throws AlreadyComputedPointsException {

        if(objectiveCards[0].getPointStrategy() == null || objectiveCards[1].getPointStrategy() == null)
            throw new NullPointStrategyException("Objective cards have invalid strategies for calculating points");

        // Check if the points have already been calculated for the objectives
        if(objectivePointsState[0])
            throw new AlreadyComputedPointsException("Already calculated points from common objectives");

        PointStrategy pointStrategy1 = objectiveCards[0].getPointStrategy();
        PointStrategy pointStrategy2 = objectiveCards[1].getPointStrategy();

        int multiplierPoints1 = pointStrategy1.calculateOccurrences(gameField, 0,0);
        int multiplierPoints2 = pointStrategy2.calculateOccurrences(gameField, 0,0);

        int value1 = objectiveCards[0].getValue();
        int value2 = objectiveCards[1].getValue();

        int tmpGain = value1 * multiplierPoints1 + value2 * multiplierPoints2;

        pointsGainedFromObjectives += tmpGain;

        objectivePointsState[0] = true;

        points += tmpGain;
    }

    /**
     * Calculate the points gained by the player for the secret objective and add them to his current points
     *
     * @exception NullPointStrategyException Found a card with a null PointStrategy
     * @throws AlreadyComputedPointsException Tried to calculate points when they were already calculated
     */
    public void updatePointsForSecretObjective() throws AlreadyComputedPointsException {

        PointStrategy pointStrategy = secretObjective.getPointStrategy();

        if(pointStrategy == null)
            throw new NullPointStrategyException("Objective cards have invalid strategies for calculating points");

        if(objectivePointsState[1])
            throw new AlreadyComputedPointsException("Already calculated points from secret objectives");

        int multiplierPoints = pointStrategy.calculateOccurrences(gameField, 0,0);

        int value = secretObjective.getValue();

        int tmpGain = value * multiplierPoints;

        pointsGainedFromObjectives += tmpGain;

        objectivePointsState[1] = true;

        points += tmpGain;
    }

    /**
     * This method is used to roll back the last move performed by the player.
     * It restores the game field and the player's points to their previous state.
     *
     * @throws RollbackException if the game field is null, indicating that no move has been made yet.
     */
    public void rollbackMove() throws RollbackException {
        // If the field is null, we cannot roll back the move. Throw a NullFieldException exception.
        if(gameField == null)
            throw new NullFieldException("Attempted to rollback a move with a null field.");
        // If the field is not null, perform the rollback.
        NonObjectiveCard card = gameField.rollback();
        // If the card is not null, restore it to the player's hand.
        hand.addLast(card);
        // Restore the old points.
        points = oldPoints;
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
    public NonObjectiveCard getInitialCard() {
        if( gameField != null)
            return gameField.getCardFromPosition(0,0);
        else {
            try {
                return hand.getFirst();
            } catch (NoSuchElementException e){
                return null;
            }
        }
    }

    /**
     * Getter:
     *
     * @return the objective cards received by player.
     */
    public Card[] getTmpSecretObj() {
        return tmpSecretObj;
    }

    /**
     * Getter:
     *
     * @return the points gained from the objectives
     */
    public int getPointsGainedFromObjectives() {
        return pointsGainedFromObjectives;
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
     * Assign a colour to the player if it doesn't have already one
     *
     * @param colour is the colour of the player
     * @exception NonNullColourException Try to set the colour when it was already set.
     */
    public void setColour(Colour colour) {
        if(this.colour != null)
            throw new NonNullColourException("Attempted to assign a colour to a player that already has one.");

        this.colour = colour;
    }

}