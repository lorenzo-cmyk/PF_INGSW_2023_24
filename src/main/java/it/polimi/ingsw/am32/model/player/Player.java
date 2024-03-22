package it.polimi.ingsw.am32.model.player;

import it.polimi.ingsw.am32.model.card.Card;
import it.polimi.ingsw.am32.model.card.NonObjectiveCard;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Field gameField;
    private Card chosenSecretObj;
    private Colour colour;
    private int points;
    private ArrayList<Card> hand;
    private Card[] tempSecretObj;

    public Player(String nickname, Field gameField, Card chosenSecretObj, Colour colour, int points, ArrayList<Card> hand, Card[] tempSecretObj) {
        this.nickname = nickname;
        this.gameField = gameField;
        this.chosenSecretObj = chosenSecretObj;
        this.colour = colour;
        this.points = points;
        this.hand = hand;
        this.tempSecretObj = tempSecretObj;
        // TODO: MAKE THIS CONSTRUCTOR USABLE ACCORDING TO GAME LOGIC
    }

    public String getNickname() {
        return nickname;
    }
    public boolean initializeGameField() {
        return false;
    }
    public Field getField() {
        return gameField;
    }
    public int receiveSecretObjective(Card[] choices, int choice_number) {
        // TODO
        return 0;
    }
    public Card getChosenSecretObj() {
        return chosenSecretObj;
    }
    public Colour getColour() {
        return colour;
    }
    public int getPoints() {
        return points;
    }
    public boolean setPoints(int points) {
        this.points = points;
        // TODO Return statement
        return false;
    }
    public boolean putCardInHand(Card newCard) {
        hand.add(newCard);
        return false;
    }
    public ArrayList<Card> getHand() {
        return hand;
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
}
