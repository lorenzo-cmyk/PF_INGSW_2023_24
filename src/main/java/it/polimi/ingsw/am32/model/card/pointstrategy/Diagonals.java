package it.polimi.ingsw.am32.model.card.pointstrategy;

import it.polimi.ingsw.am32.model.field.CardPlaced;
import it.polimi.ingsw.am32.model.field.Field;

import java.util.*;

/**
 * Diagonals is one of the classes that implement the PointStrategy interface used to calculate the
 * objective cards, which count 3 cards of the same kingdom on the diagonal line y=x or y=-x.
 * @author Jie
 */
public class Diagonals implements PointStrategy {
    /**
     * type: the kingdom requested by objective card.
     */
    private final ObjectType type;
    /**
     * leftToRight: the type of the diagonal line which returns true for diagonal type y=x and returns false for diagonal type y=-x.
     */
    private final boolean leftToRight;

    /**
     * Calculate how many times three cards of the same kingdom are placed on the diagonal line.
     *
     * @param field Field of play where the card placed.
     * @param x The x coordinate of the card whose points are being calculated.
     * @param y the y coordinates of the card whose points are being calculated.
     * @return Number of times that objective card has been fulfilled in this field.
     */
    public int calculateOccurrences(Field field, int x, int y) {
        int times = 0;
        List<CardPlaced> dominantKingdom = new ArrayList<>(); // create Arraylist to store all cards that belong the Kingdom requested by objective Card.
        for (CardPlaced i : field.getFieldCards()) {
            if (i.getNonObjectiveCard().getKingdom() == this.type) {
                dominantKingdom.add(i);
            }
        } // add a card to the dominantKingdom list if it has same kingdom type as the objective card's kingdom.
        Comparator<CardPlaced> comparator = (c1, c2) -> {
            if (c1.getX() > c2.getX()) {
                return 1;
            } else if (c1.getX() < c2.getX()) {
                return -1;
            } else
                return Integer.compare(c1.getY(), c2.getY());
        };
        dominantKingdom.sort(comparator);// sort arraylist in ascending order
        while (!dominantKingdom.isEmpty()) {
            int xLoc;
            int yLoc;
            xLoc = dominantKingdom.getFirst().getX();
            yLoc = dominantKingdom.getFirst().getY();
            CardPlaced e1;
            boolean condition1 = false; // condition1 marks the existence of the card in the middle of the three diagonal cards
            for (int i = 1; i < dominantKingdom.size() && !condition1; i++) {
                if (this.leftToRight) { // case of diagonal y=x.
                    if (dominantKingdom.get(i).getX() == (xLoc + 1) && dominantKingdom.get(i).getY() == (yLoc + 1)) {
                        e1 = dominantKingdom.get(i);
                        if (dominantKingdom.removeIf(e -> (e.getX() == (xLoc + 2)) && (e.getY() == (yLoc + 2)))) {
                            dominantKingdom.remove(e1);
                            times++;
                            condition1 = true;
                        }
                    }
                } // check the existence of the cards, then if they existed move them from the list.
                else { // case of diagonal y=-x.
                    if (dominantKingdom.get(i).getX() == (xLoc + 1) && dominantKingdom.get(i).getY() == (yLoc - 1)) {
                        e1 = dominantKingdom.get(i);
                        if (dominantKingdom.removeIf(e -> (e.getX() == (xLoc + 2)) && (e.getY() == (yLoc - 2)))) {
                            dominantKingdom.remove(e1);
                            times++;
                            condition1 = true;
                        }
                    }
                }
            }
            dominantKingdom.remove(dominantKingdom.getFirst());
        }
        return times;
    }

    /**
     * Constructor of the Diagonals strategy.
     *
     * @param type  ObjectType to search for.
     * @param leftToRight Determine the type of diagonal line.
     */
    public Diagonals(ObjectType type, boolean leftToRight) {
        this.type = type;
        this.leftToRight = leftToRight;
    }

    /**
     * Get the type of the resource (ObjectType) that the card requires.
     *
     * @return ObjectType needed.
     */
    public ObjectType getType() {
        return type;
    }

    /**
     * Get the type of the diagonal line.
     *
     * @return true for diagonal type y=x and false for diagonal type y=-x.
     */
    public boolean getLeftToRight() {
        return leftToRight;
    }
}
