package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.MapElements.IMapElement;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private final IMapElement mapElement;

    public GuiElementBox(IMapElement mapElement) {
        this.mapElement = mapElement;
    }

    public VBox getGuiRepresentation(int energy, int maxEnergy) {
        VBox vBox = new VBox(makeImageView(), makeEnergyBar(energy, maxEnergy));
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    public VBox getGuiRepresentation(boolean isJungle) {
        Rectangle tile = new Rectangle();
        Color color = isJungle ? Color.web("#1c9c2f") : Color.web("#c48b10");
        tile.setWidth(GuiConstants.guiElementBoxSize);
        tile.setHeight(GuiConstants.guiElementBoxSize);
        tile.setFill(color);

        return new VBox(tile);
    }

    private ImageView makeImageView() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(this.mapElement.getRepresentationImagePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GuiConstants.guiElementBoxImageSize);
        imageView.setFitHeight(GuiConstants.guiElementBoxImageSize);

        return imageView;
    }

    private ProgressBar makeEnergyBar(int energy, int maxEnergy) {
        ProgressBar energyBar = new ProgressBar();
        double energyRatio = (double) energy / maxEnergy;
        energyBar.setProgress(energyRatio > 1 ? 1 : energyRatio);

        return energyBar;
    }
}
