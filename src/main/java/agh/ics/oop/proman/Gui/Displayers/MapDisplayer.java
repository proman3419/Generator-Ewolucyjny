package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.Gui.GuiElementBox;
import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Entities.Vector2d;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class MapDisplayer extends GridPane {
    private final AbstractWorldMap map;

    public MapDisplayer(AbstractWorldMap map) {
        this.map = map;
    }

    private void formatGrid(int maxX, int maxY) {
        for (int y = 0; y <= maxY; y++)
            this.getRowConstraints().add(new RowConstraints(60));

        for (int x = 0; x <= maxX; x++)
            this.getColumnConstraints().add(new ColumnConstraints(60));

        this.setGridLinesVisible(false);
        this.setGridLinesVisible(true);
    }

    private void display() {
        Vector2d ll = this.map.getLowerLeft();
        Vector2d ur = this.map.getUpperRight();
        int maxX = ur.x - ll.x;
        int maxY = ur.y - ll.y;

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                int mapX = ll.x + x;
                int mapY = ur.y - y;

                GuiElementBox guiElementBox = new GuiElementBox(this.map.objectAt(new Vector2d(mapX, mapY)));
                VBox vBox = guiElementBox.getGuiRepresentation();
                this.add(vBox, x, y, 1, 1);
                GridPane.setHalignment(vBox, HPos.CENTER);
                GridPane.setValignment(vBox, VPos.CENTER);
            }
        }

        formatGrid(maxX, maxY);
    }

    public void update() {
        Platform.runLater(() -> {
            this.getChildren().clear();
            this.getRowConstraints().clear();
            this.getColumnConstraints().clear();
            display();
        });
    }
}
