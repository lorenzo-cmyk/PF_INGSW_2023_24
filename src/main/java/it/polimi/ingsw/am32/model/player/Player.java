package it.polimi.ingsw.am32.model.player;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private Field gameField;
    private Card secretObjective;
    private Colour colour;
    private int points;
    private ArrayList<Card> hand;

    public Player(String nickname) {
        this.nickname = nickname;
        this.Field = new Field();
        this.points = 0;
        this.hand = new ArrayList<Card>();
    }

    public String getNickname() {
        return nickname;
    }
    public Field getField() {
        return gameField;
    }
    public int receiveSecretObjective(Card[] choices, int choice_number) {
        // TODO
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
    public boolean setPoints(int points) {
        this.points = points;
        // TODO Return statement
    }
    public boolean putCardInHand(Card newCard) {
        hand.add(newCard);
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public boolean performMove(Card card, int x, int y, boolean isUp) {
        // TODO
    }
    public boolean updatePointsForObjectives(Card[] objectiveCards) {
        // TODO
    }
    public int calculatePoints(Card card, int x, int y) {
        // TODO
    }
}
