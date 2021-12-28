package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.Gui.GuiElementBox;
import agh.ics.oop.proman.Gui.IGuiContainable;
import agh.ics.oop.proman.MapElements.AbstractWorldMapElement;
import agh.ics.oop.proman.MapElements.Animal.Animal;
import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Entities.Vector2d;
import agh.ics.oop.proman.Maps.BoundedWorldMap;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class MapDisplayer extends GridPane implements IGuiContainable {
    private final AbstractWorldMap map;

    public MapDisplayer(AbstractWorldMap map) {
        this.map = map;
    }

    private void formatGrid(int maxX, int maxY) {
        for (int y = 0; y <= maxY; y++)
            this.getRowConstraints().add(new RowConstraints(GuiConstants.guiElementBoxSize));

        for (int x = 0; x <= maxX; x++)
            this.getColumnConstraints().add(new ColumnConstraints(GuiConstants.guiElementBoxSize));

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
                addGuiRepresentation(x, y, mapX, mapY);
            }
        }

        formatGrid(maxX, maxY);
    }

    private void addGuiRepresentation(int x, int y, int mapX, int mapY) {
        AbstractWorldMapElement objectAt = this.map.objectAt(new Vector2d(mapX, mapY));
        GuiElementBox guiElementBox = new GuiElementBox(objectAt);
        VBox vBox;
        if (objectAt instanceof Animal)
            vBox = guiElementBox.getGuiRepresentation(((Animal) objectAt).getEnergy(), this.map.getStartEnergy());
        else
            vBox = guiElementBox.getGuiRepresentation(this.map.getPlantEnergy(), this.map.getStartEnergy());
        this.add(vBox, x, y, 1, 1);
        GridPane.setHalignment(vBox, HPos.CENTER);
        GridPane.setValignment(vBox, VPos.CENTER);
    }

    public void update() {
        Platform.runLater(() -> {
            this.getChildren().clear();
            this.getRowConstraints().clear();
            this.getColumnConstraints().clear();
            display();
        });
    }

    @Override
    public String getContainerTitle() {
        if (this.map instanceof BoundedWorldMap)
            return "Bounded map";
        else
            return "Unbounded map";
    }
}
