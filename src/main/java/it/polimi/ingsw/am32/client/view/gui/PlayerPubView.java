package it.polimi.ingsw.am32.client.view.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerPubView {
    private final ImageView colour;
    private final Label nickname;
    private final Label points;
    //private final field
    private final Label[] resourceLabels;

    public PlayerPubView(Label nickname, ImageView colour, Label points, Label[] resourceLabels) {
        this.colour = colour;
        this.points = points;
        this.nickname = nickname;
        this.resourceLabels = resourceLabels;
    }

    public ImageView getColour() {
        return colour;
    }
    public Label getNickname() {
        return nickname;
    }
    public void setNickname(String nickname){
        this.nickname.setText(nickname);
    }
    public Label getPoints() {
        return points;
    }

    public Label[] getResourceLabels() {
        return resourceLabels;
    }

    public void setColour(Image colour) {
        this.colour.setImage(colour);
    }

    public void setPoints(int points) {
        this.points.setText(String.valueOf(points));
    }

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
