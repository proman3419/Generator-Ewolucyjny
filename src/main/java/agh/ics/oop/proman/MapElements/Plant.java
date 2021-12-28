package agh.ics.oop.proman.MapElements;

import agh.ics.oop.proman.Classes.Helper;
import agh.ics.oop.proman.Entities.Vector2d;

public class Plant extends AbstractWorldMapElement {
    public Plant(Vector2d position) {
        super(position);
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getRepresentationImagePath() {
        return Helper.adjustPathString("src/main/resources/plant.png");
    }
}
