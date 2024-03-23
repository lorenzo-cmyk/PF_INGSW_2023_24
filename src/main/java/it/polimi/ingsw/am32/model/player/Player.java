package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Player {

    //---------------------------------------------------------------------------------------------
    // Variables and Constants

    private String nickname;
    private Field gameField;
    private Card chosenSecretObj;
    private Colour colour;
    private int points;
    private ArrayList<Card> hand;
    private Card[] tempSecretObj;


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
        this.hand = new ArrayList<Card>();
        this.colour = null;
    }


    //---------------------------------------------------------------------------------------------
    // Methods

    /**
     * Place the initial card in the hand of the player
     *
     * @param initialCard is the initial card assigned by the match
     * @return true if the assignment was successful, false if the hand wasn't empty
     */
    public boolean assignStartingCard(Card initialCard) {

        if(!hand.isEmpty())
            return false;

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

        if (gameField != null)
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
        if(hand.getFirst().getId() == id) {
            secretObjective = hand.getFirst();
            hand.clear();
        } else if (hand.get(1).getId() == id){
            secretObjective = hand.get(1);
            hand.clear();
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
        if(!hand.isEmpty())
            return false;

        hand.addLast(firstCard);
        hand.addLast(secondCard);
        return true;
    }



    public boolean putCardInHand(Card newCard) {
        hand.addLast(newCard);
        return false;
    }

    public boolean performMove(NonObjectiveCard card, int x, int y, boolean isUp) {
        // TODO
        return false;
    }

    public boolean updatePointsForObjectives(Card[] objectiveCards) {
        // TODO
        return false;
    }

    public int calculatePoints(Card card, int x, int y) {
        // TODO
        return 0;
    }


    //---------------------------------------------------------------------------------------------
    // Getters

    public String getNickname() {
        return nickname;
    }
    public Field getField() {
        return gameField;
    }
    public Card getSecretObjective() {
        return secretObjective;
    }

    public Colour getColour() {
        return colour;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Card> getHand() {
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
    }


    //---------------------------------------------------------------------------------------------
    // Setters

    // TODO check if used
    public boolean setPoints(int points) {
        this.points = points;
        // TODO Return statement
        return false;
    }


    //---------------------------------------------------------------------------------------------
    // ToString

    // TODO parsing method
}