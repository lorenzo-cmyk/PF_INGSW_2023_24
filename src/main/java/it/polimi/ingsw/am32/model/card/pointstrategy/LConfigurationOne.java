package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
* LConfigurationOne is one of the classes that implement the PointStrategy interface used to calculate the
* objective cards, which in the bottom right has a Plant card and in the top left corner has a Fungi card
* and then above that card has another Fungi card.
 *
 * @author Jie
*/
public class LConfigurationOne implements PointStrategy {
    /**
     *  Calculate how many times the L configuration is fulfilled in the player's field, where in the bottom right is a
     *  Plant card, and in the left top found two Fungi cards.
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y The y coordinates of the card whose points are being calculated.
     * @return Number of times that objective card has been fulfilled in this field.
     * @author Jie
     */
    public int calculateOccurences(Field field, int x, int y) {
        int times = 0;
        List<CardPlaced> plantKingdom = new ArrayList<>(); // create Arraylist to store all cards of Plant Kingdom visible in field.
        for (CardPlaced i : field.getFieldCards()) {
            if (i.getNonObjectiveCard().getKingdom() == ObjectType.PLANT) {
                plantKingdom.add(i);
            }
        } // add a card to the plantKingdom list if its type is Plant.
        List<CardPlaced> fungiKingdom = new ArrayList<>(); // create Arraylist to store all cards of Fungi Kingdom visible in field.
        for (CardPlaced i : field.getFieldCards()) {
            if (i.getNonObjectiveCard().getKingdom() == ObjectType.FUNGI) {
                fungiKingdom.add(i);
            }
        } // add a card to the fungiKingdom list if its type is Fungi.
        Comparator<CardPlaced> comparatorOne = (c1, c2) -> {
            if (c1.getX() > c2.getX()) {
                return 1;
            } else if (c1.getX() < c2.getX()) {
                return -1;
            } else
                return Integer.compare(c1.getY(), c2.getY());
        };
        plantKingdom.sort(comparatorOne);
        fungiKingdom.sort(comparatorOne);

        while (!plantKingdom.isEmpty()) {
            int xLoc;
            int yLoc;
            xLoc = plantKingdom.getFirst().getX();
            yLoc = plantKingdom.getFirst().getY();
            CardPlaced e1;
            for(int j=0; j<fungiKingdom.size()-1&& fungiKingdom.get(j).getX()<xLoc; j++){ // search for Fungi cards that connect to the specific Plant card.
                if (fungiKingdom.get(j).getX() == (xLoc - 1) && fungiKingdom.get(j).getY() == (yLoc + 1)) {
                    e1 = fungiKingdom.get(j); // a Fungi card found in the top left corner of the specific Plant card.
                    if (fungiKingdom.get(j + 1).getX() == e1.getX() && fungiKingdom.get(j + 1).getY() == e1.getY() + 2) {
                        fungiKingdom.remove(fungiKingdom.get(j + 1));
                        times++;
                    }
                    fungiKingdom.remove(e1);
                }
            } // remove elements validated to reduce the search time.
            plantKingdom.remove(plantKingdom.getFirst());
        }
        fungiKingdom.clear();
        return times;
    }
}
