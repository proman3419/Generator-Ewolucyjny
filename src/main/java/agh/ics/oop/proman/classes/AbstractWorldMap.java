package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IPositionChangeObserver;
import agh.ics.oop.proman.interfaces.IWorldMap;

import java.util.LinkedHashMap;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final LinkedHashMap<Vector2d, AbstractWorldMapElement> mapElements = new LinkedHashMap<>();
    protected final int width;
    protected final int height;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected final Vector2d lowerLeftJungle;
    protected final Vector2d upperRightJungle;

    public AbstractWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);

        int jungleWidth = (int)(width * jungleRatio);
        int jungleHeight = (int)(height * jungleRatio);
        int jungleXStart = (width - jungleWidth) / 2;
        int jungleYStart = (height - jungleHeight) / 2;
        this.lowerLeftJungle = new Vector2d(jungleXStart, jungleYStart);
        this.upperRightJungle = new Vector2d(jungleXStart + jungleWidth, jungleYStart + jungleHeight);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();

        if (this.lowerLeft.precedes(position) && this.upperRight.follows(position)) {
            this.mapElements.put(animal.getPosition(), animal);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.mapElements.get(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        return this.mapElements.get(position);
    }

    @Override
    public String toString() {
        return "Implement me!";
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        AbstractWorldMapElement mapElement = this.mapElements.remove(oldPosition);
        this.mapElements.put(newPosition, mapElement);
    }
}
