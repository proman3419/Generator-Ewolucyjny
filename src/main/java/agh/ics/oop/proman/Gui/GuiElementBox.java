package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.MapElements.IMapElement;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private final IMapElement mapElement;

    public GuiElementBox(IMapElement mapElement) {
        this.mapElement = mapElement;
    }

    public VBox getGuiRepresentation(int energy, int maxEnergy) {
        if (this.mapElement == null)
            return new VBox();

        Image image = null;
        try {
            image = new Image(new FileInputStream(this.mapElement.getRepresentationImagePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(GuiConstants.guiElementBoxImageSize);
        imageView.setFitHeight(GuiConstants.guiElementBoxImageSize);

        ProgressBar energyBar = new ProgressBar();
        double energyRatio = (double) energy / maxEnergy;
        energyBar.setProgress(energyRatio > 1 ? 1 : energyRatio);

        VBox vBox = new VBox(imageView, energyBar);
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }
}
