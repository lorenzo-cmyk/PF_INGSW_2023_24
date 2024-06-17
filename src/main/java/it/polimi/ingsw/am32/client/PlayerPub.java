package it.polimi.ingsw.am32.client;

import java.util.ArrayList;

/**
 * Use this class to store the player's public information that is shared with other players, which means that in this
 * class contains all the information that the player can request to know about the other players.
 */
public class PlayerPub {
    /**
     * The colour of the player.
     */
    private String colour;
    /**
     * The points of the player.
     */
    private int points;
    /**
     * The field of the player that contains the information of the cards placed on the field.
     */
    private final ArrayList<CardPlacedView> field;
    /**
     * The resources of the player in the field with order: PLANT, FUNGI, ANIMAL, INSECT, QUILL, INKWELL, MANUSCRIPT.
     */
    private int[]resources;
    /**
     * The boolean value that indicates if the player is online or offline.
     */
    private boolean isOnline;

    /**
     * The constructor of the class that initializes the player's public information with the given parameters.
     * @param colour The colour of the player.
     * @param points The points of the player.
     * @param field The field of the player that contains the information of the cards placed on the field.
     * @param resources The resources of the player in the field.
     * @param isOnline The boolean value that indicates if the player is online or offline.
     */
    public PlayerPub( String colour, int points,ArrayList<CardPlacedView> field,int[]resources,boolean isOnline){
        this.colour = colour;
        this.points = points;
        this.field = field;
        this.resources = resources;
        this.isOnline = isOnline;
    }

    /**
     * The getter method for the colour of the player.
     * @return The colour of the player.
     */
    public String getColour() {
        return colour;
    }

    /**
     * The getter method for the points of the player.
     * @return The points of the player.
     */
    public int getPoints() {
        return points;
    }

    /**
     * The getter method for the field of the player.
     * @return The field of the player.
     */
    public ArrayList<CardPlacedView> getField() {
        return field;
    }

    /**
     * The getter method for the resources of the player.
     * @return The array of integers that stores the resources of the player in the field.
     */
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
     * @param points the new version of the points.
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

    /**
     * The setter method for the colour of the player to update the colour of the player.
     * @param colour the new colour of the player.
     */
    public void updateColour(String colour){
        this.colour = colour;
    }

    /**
     * The setter method for the boolean value that indicates if the player is online or offline.
     * @param isOnline the current status of the player.
     */
    public void updateOnline(boolean isOnline){
        this.isOnline = isOnline;
    }
}
