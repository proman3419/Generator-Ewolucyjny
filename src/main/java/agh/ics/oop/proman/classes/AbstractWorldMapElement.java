package agh.ics.oop.proman.classes;

public abstract class AbstractWorldMapElement {
    protected Vector2d position = new Vector2d(0,0); // Default position, the only one that is guaranteed to be present on any map

    public AbstractWorldMapElement(Vector2d position) {
        this.position = position;
    }
}
