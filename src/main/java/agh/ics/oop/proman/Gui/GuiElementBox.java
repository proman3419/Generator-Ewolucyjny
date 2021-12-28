package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.MapElements.IMapElement;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GuiElementBox {
    private final IMapElement mapElement;
    private final int size;

    public GuiElementBox(IMapElement mapElement, int size) {
        this.mapElement = mapElement;
        this.size = size;
    }

    public VBox getGuiRepresentation(int energy, int maxEnergy) {
        ImageView imageView = new ImageView(ImagesHoarder.getImage(this.mapElement.getRepresentationImagePath()));
        imageView.setFitWidth(GuiConstants.guiElementBoxImageSizeRatio * this.size);
        imageView.setFitHeight(GuiConstants.guiElementBoxImageSizeRatio * this.size);

        VBox vBox = new VBox(imageView, makeEnergyBar(energy, maxEnergy));
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    public VBox getGuiRepresentation(boolean isJungle) {
        Rectangle tile = new Rectangle();
        Color color = isJungle ? Color.web("#1c9c2f") : Color.web("#c48b10");
        tile.setWidth(GuiConstants.guiElementBoxBackgroundSizeRatio * this.size);
        tile.setHeight(GuiConstants.guiElementBoxBackgroundSizeRatio * this.size);
        tile.setFill(color);

        return new VBox(tile);
    }

    private ProgressBar makeEnergyBar(int energy, int maxEnergy) {
        ProgressBar energyBar = new ProgressBar();
        double energyRatio = (double) energy / maxEnergy;
        energyBar.setProgress(energyRatio > 1 ? 1 : energyRatio);

        return energyBar;
    }
}
