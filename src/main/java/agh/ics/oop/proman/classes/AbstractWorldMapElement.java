package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IMapElement;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position;

    public AbstractWorldMapElement(Vector2d position) {
        this.position = position;
    }

    @Override
    public String getRepresentationImagePath() {
        return "";
    }

    @Override
    public String toLabelString() {
        return "";
    }
}
