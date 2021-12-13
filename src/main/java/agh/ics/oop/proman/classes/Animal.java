package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.enums.MapDirection;
import agh.ics.oop.proman.enums.MoveDirection;
import agh.ics.oop.proman.interfaces.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.proman.enums.MapDirection.*;

public class Animal extends AbstractWorldMapElement {
    private final AbstractWorldMap map;
    private final int energy;
    private final Genome genome;
    private MapDirection orientation;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(AbstractWorldMap map, int genesCount) {
        super(map.pickRandomPosition(true));
        this.map = map;
        this.energy = map.startEnergy;
        this.genome = new Genome(genesCount);
        this.orientation = getOrientation();
        this.addObserver(map);
    }

    // Copulation constructor
    public Animal(AbstractWorldMap map, Vector2d initialPosition, int startEnergy, Genome genome) {
        super(initialPosition);
        this.map = map;
        this.energy = startEnergy;
        this.genome = genome;
        this.orientation = getOrientation();
        this.addObserver(this.map);
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    @Override
    public String toString() {
        return switch (this.orientation) {
            case NORTH -> "^";
            case NORTHEAST -> "^>";
            case EAST -> ">";
            case SOUTHEAST -> "v>";
            case SOUTH -> "v";
            case SOUTHWEST -> "<v";
            case WEST -> "<";
            case NORTHWEST -> "<^";
        };
    }

    public void move(MoveDirection direction) {
        switch (direction) {
            case RIGHT -> this.orientation = this.orientation.next();
            case LEFT -> this.orientation = this.orientation.previous();
            case FORWARD, BACKWARD -> {
                Vector2d unitVector = this.orientation.toUnitVector();
                if (direction == MoveDirection.BACKWARD)
                    unitVector = unitVector.opposite();
                Vector2d newPosition = this.position.add(unitVector);

                if (this.map instanceof UnboundedWorldMap)
                    newPosition = ((UnboundedWorldMap)this.map).positionToUnboundedPosition(newPosition);

                if (this.map.canMoveTo(newPosition)) {
                    positionChanged(this.position, newPosition);
                    this.position = newPosition;
                }
            }
        }
    }

    private void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers)
            observer.positionChanged(oldPosition, newPosition);
    }

    public void chooseOrientation() {
        this.orientation = MapDirection.fromInteger(this.genome.getRandomGene().getValue());
    }
}
