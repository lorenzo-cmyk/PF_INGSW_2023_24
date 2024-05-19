package it.polimi.ingsw.am32.client;

import java.util.ArrayList;

/**
 * It's a client-side cardPlaced class.
 * Use this class just to store the information of the card placed on the field, if the player wants to know the details
 * of the card placed on the field, through the information stored in this class, the player can know the ID of the card,
 * the x and y coordinates of the card and the side of the card.
 * @param ID the ID of the card placed.
 * @param x the x coordinate of the card placed.
 * @param y the y coordinate of the card placed.
 * @param side the side of the card placed.
 */
public record CardPlacedView(int ID, ArrayList<String>cardImage, int x, int y, boolean side) {
}
