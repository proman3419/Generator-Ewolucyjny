package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IMapElement;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position = new Vector2d(0,0); // Default position, the only one that is guaranteed to be present on any map

    public AbstractWorldMapElement(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return this.position;
    }
}
