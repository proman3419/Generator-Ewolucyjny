package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.Gui.IGuiContainable;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class MagicBreedingDisplayer extends GridPane implements IGuiContainable {
    private final TextField textField = new TextField();

    public MagicBreedingDisplayer() {
        this.textField.setText("None");
        this.textField.setEditable(false);
        this.textField.setFont(Font.font(GuiConstants.fontFamily, GuiConstants.magicBreedingDisplayerFontSize));
        this.textField.setAlignment(Pos.CENTER);
        positionElements();
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.textField, 0, 0, 1, 1);
        });
    }

    public void update(String notificationMessage) {
        Platform.runLater(() -> {
            this.textField.setText(notificationMessage);
        });
    }

    @Override
    public String getContainerTitle() {
        return "Magic breedings";
    }
}
