package it.polimi.ingsw.am32.model.match;

import java.util.ArrayList;

public class Match implements ModelInterface{
    private Deck starterCardsDeck;
    private Deck objectiveCardsDeck;
    private Deck secretObjectiveCardsDeck;
    private Deck resourceCardsDeck;
    private Deck goldCardsDeck;
    private Card[] currentResourceCards;
    private Card[] currentGoldCards;
    private Card[] commonObjectives;
    private ArrayList<Player> players;
    private String currentPlayerID;
    private boolean isGameTerminating;
    private boolean isLastTurn;
    private int currentTurnNumber;

    Match() {
        // TODO
    }

    public boolean shuffleDeck(int deckNum) {
        // TODO
    }
    public Card drawFromDeck(int deckNum) {
       // TODO
    }
    public Card drawFromCurrentResourceCards(int cardNum) {
        // TODO
    }
    public Card drawFromCurrentGoldCards(int cardNum) {
        // TODO
    }
    public Card[] getCommonObjectives() {
        return commonObjectives;
    }
    public boolean addPlayer(String nickname) {
        // TODO
    }
    public boolean deletePlayer(String nickname) {
        // TODO
    }
    public Colour pickRandomColour() {
        // TODO
    }
    public Player getPlayer(String nickname) {
        // TODO
    }
    public boolean updateCurrentPlayer() {
        // TODO
    }
    public boolean checkifGameIsTerminating() {
        return isGameTerminating;
    }
    public boolean setGameIsTerminating() {
        this.isGameTerminating = true;
    }
    public boolean checkIfTurnIsLast() {
        return isLastTurn;
    }
    public boolean setTurnIsLast() {
        this.isLastTurn = true;
    }
    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }
    public boolean increaseCurrentTurnNumber() {
        currentTurnNumber++;
    }
}
