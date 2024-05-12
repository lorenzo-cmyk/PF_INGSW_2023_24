package it.polimi.ingsw.am32.client;

import java.util.ArrayList;

/**
 * Use this class to store the player's public information that is shared with other players, which means that in this
 * class contains all the information that the player can request to know about the other players.
 */
public class PlayerPub {
    private final String colour;
    private int points;
    private final ArrayList<CardPlacedView> field;
    private int[]resources;

    public PlayerPub( String colour, int points,ArrayList<CardPlacedView> field,int[]resources) {
        this.colour = colour;
        this.points = points;
        this.field = field;
        this.resources = resources;
    }
    public String getColour() {

        return colour;
    }
    public int getPoints() {

        return points;
    }
    public ArrayList<CardPlacedView> getField() {

        return field;
    }
    public int[] getResources(){
        return resources;
    }

    /**
     * update the resources of the player with the new resources received from the message.
     * @param resources the new version of the resources.
     */
    public void updateResources(int[] resources){
        this.resources = resources.clone();
    }
    /**
     * Update the points of the player with the points updated received from the message.
     */
    public void updatePoints(int points){
        this.points = points;
    }
    /**
     * Add the card placed on the field to the field of the player.
     * @param card the card placed on the field.
     */
    public void addToField(CardPlacedView card){
        this.field.add(card);
    }
}
