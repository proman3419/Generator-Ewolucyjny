package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GuiContainer extends GridPane {
    private final Label title;
    private final GridPane guiElement;

    // Assumes that guiElement is a GridPane
    public GuiContainer(IGuiContainable guiElement) {
        this.title = new Label(guiElement.getContainerTitle());
        this.title.setFont(Font.font(GuiConstants.fontFamily, FontWeight.BOLD,
                                     GuiConstants.guiContainterTitleFontSize));
        this.title.setPadding(new Insets(0,0,5,0));
        assert guiElement instanceof GridPane : "guiElement is not a GridPane";
        this.guiElement = (GridPane) guiElement;
        positionElements();
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.title, 0, 0, 1, 1);
            this.add(this.guiElement, 0, 1, 1, 1);
        });
    }
}
