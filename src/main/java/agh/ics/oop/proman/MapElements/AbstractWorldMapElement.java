package agh.ics.oop.proman.MapElements;

import agh.ics.oop.proman.Entities.Vector2d;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position;

    public AbstractWorldMapElement(Vector2d position) {
        this.position = position;
    }

    //region IMapElement implementation --------------------------------------------------------------------------------
    @Override
    public String getRepresentationImagePath() {
        return "";
    }

    @Override
    public String toLabelString() {
        return "";
    }
    //endregion IMapElement implementation -----------------------------------------------------------------------------

    public Vector2d getPosition() {
        return position;
    }
}
