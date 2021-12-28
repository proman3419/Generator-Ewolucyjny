package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.Gui.GuiElementBox;
import agh.ics.oop.proman.Gui.IGuiContainable;
import agh.ics.oop.proman.MapElements.AbstractWorldMapElement;
import agh.ics.oop.proman.MapElements.Animal.Animal;
import agh.ics.oop.proman.MapElements.Plant;
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
    private final int guiElementBoxSize;

    public MapDisplayer(AbstractWorldMap map, int appWidth, int appHeight) {
        this.map = map;
        int width = (int) (GuiConstants.mapDisplayerToAppWidthRatio * appWidth);
        int height = (int) (GuiConstants.mapDisplayerToAppHeightRatio * appHeight);
        this.guiElementBoxSize = Math.min(width / this.map.getWidth(), height / this.map.getHeight());
    }

    private void formatGrid(int maxX, int maxY) {
        for (int y = 0; y <= maxY; y++)
            this.getRowConstraints().add(new RowConstraints(this.guiElementBoxSize));

        for (int x = 0; x <= maxX; x++)
            this.getColumnConstraints().add(new ColumnConstraints(this.guiElementBoxSize));

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
                Vector2d position = new Vector2d(mapX, mapY);
                AbstractWorldMapElement objectAt = this.map.objectAt(position);
                addGuiRepresentation(x, y, objectAt, position, true);
                if (objectAt != null)
                    addGuiRepresentation(x, y, objectAt, position, false);
            }
        }

        formatGrid(maxX, maxY);
    }

    private void addGuiRepresentation(int x, int y, AbstractWorldMapElement objectAt,
                                      Vector2d position, boolean background) {
        GuiElementBox guiElementBox = new GuiElementBox(objectAt, this.guiElementBoxSize);
        VBox vBox = null;

        if (background)
            vBox = guiElementBox.getGuiRepresentation(this.map.isInsideJungle(position));
        else {
            if (objectAt instanceof Animal)
                vBox = guiElementBox.getGuiRepresentation(((Animal) objectAt).getEnergy(), this.map.getStartEnergy());
            else if (objectAt instanceof Plant)
                vBox = guiElementBox.getGuiRepresentation(this.map.getPlantEnergy(), this.map.getStartEnergy());
        }

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
