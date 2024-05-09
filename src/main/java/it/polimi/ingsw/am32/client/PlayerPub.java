package it.polimi.ingsw.am32.client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Use this class to store the player's public information that is shared with other players, which means that in this
 * class contains all the information that the player can request to know about the other players.
 */
public class PlayerPub {
    private final String nickname;
    private final String colour;
    private final int points;
    private final ArrayList<CardPlacedView> field;
    private final String [][] board; //TODO: not sure if this is useful also for GUI
    private final int[]resources;

    public PlayerPub(String nickname, String colour, int points, String[][] board,ArrayList<CardPlacedView> field,int[]resources) {
        this.nickname = nickname;
        this.colour = colour;
        this.points = points;
        this.board = board;
        this.field = field;
        this.resources = resources;
    }
    public String getNickname() {

        return nickname;
    }
    public String getColour() {

        return colour;
    }
    public int getPoints() {

        return points;
    }
    public String[][] getBoard() {
        return board;
    }
    public ArrayList<CardPlacedView> getField() {

        return field;
    }
    public int[] getResources(){
        return resources;
    }
}
