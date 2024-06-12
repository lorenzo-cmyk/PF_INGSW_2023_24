package it.polimi.ingsw.am32.client.view.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is used to store the components of javafx that are used to display the player's information in the
 * game. The class is used by the {@link GraphicalUI} class to display and update the player's information.
 */
public class PlayerPubView {
    /**
     * The ImageView that displays the player's colour
     */
    private final ImageView colour;
    /**
     * The Label that displays the player's nickname
     */
    private final Label nickname;
    /**
     * The Label that displays the player's points
     */
    private final Label points;
    /**
     * The array of Labels that display the player's resources in the fields with order: PLANT, FUNGI, ANIMAL, INSECT,
     * QUILL, INKWELL, MANUSCRIPT.
     */
    private final Label[] resourceLabels;

    /**
     * The constructor of the class that initializes the player pub view with the given parameters.
     * @param nickname The Label that displays the player's nickname
     * @param colour The ImageView that displays the player's colour
     * @param points The Label that displays the player's points
     * @param resourceLabels The array of Labels that display the player's resources
     */
    public PlayerPubView(Label nickname, ImageView colour, Label points, Label[] resourceLabels) {
        this.colour = colour;
        this.points = points;
        this.nickname = nickname;
        this.resourceLabels = resourceLabels;
    }

    /**
     * The getter method for the ImageView that displays the player's colour
     * @return The ImageView that displays the player's colour
     */
    public ImageView getColour() {
        return colour;
    }

    /**
     * The getter method for the Label that displays the player's nickname
     * @return The Label that displays the player's nickname
     */
    public Label getNickname() {
        return nickname;
    }

    /**
     * The setter method for the player's nickname
     * @param nickname the string of the nickname that should be set or updated
     */
    public void setNickname(String nickname){
        this.nickname.setText(nickname);
    }

    /**
     * The getter method for the Label that displays the player's points
     * @return The Label that displays the player's points
     */
    public Label getPoints() {
        return points;
    }

    /**
     * The getter method for the array of Labels that display the player's resources
     * @return The array of Labels that display the player's resources
     */
    public Label[] getResourceLabels() {
        return resourceLabels;
    }

    /**
     * The setter method for the ImageView that displays the player's colour
     * @param colour the new image that should be set or updated
     */
    public void setColour(Image colour) {
        this.colour.setImage(colour);
    }

    /**
     * The setter method for the player's points
     * @param points the number of points that should be set or updated
     */
    public void setPoints(int points) {
        this.points.setText(String.valueOf(points));
    }

    /**
     * The setter method for the player's resources
     * @param resourceCount the number of resources that should be set or updated
     */
    public void setResourceLabels(int[] resourceCount) {
        this.resourceLabels[0].setText(String.valueOf(resourceCount[0]));
        this.resourceLabels[1].setText(String.valueOf(resourceCount[1]));
        this.resourceLabels[2].setText(String.valueOf(resourceCount[2]));
        this.resourceLabels[3].setText(String.valueOf(resourceCount[3]));
        this.resourceLabels[4].setText(String.valueOf(resourceCount[4]));
        this.resourceLabels[5].setText(String.valueOf(resourceCount[5]));
        this.resourceLabels[6].setText(String.valueOf(resourceCount[6]));
    }
}
