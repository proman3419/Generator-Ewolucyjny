package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.core.Constants;
import agh.ics.oop.proman.enums.MapDirection;
import agh.ics.oop.proman.enums.MoveDirection;
import agh.ics.oop.proman.interfaces.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class Animal extends AbstractWorldMapElement {
    private final AbstractWorldMap map;
    private int energy;
    private final Genome genome;
    private MapDirection orientation;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(AbstractWorldMap map, boolean withinJungle) {
        super(map.chooseRandomPosition(withinJungle));
        this.map = map;
        this.energy = map.startEnergy;
        this.genome = new Genome(Constants.genesCount);
        this.chooseOrientation();
        this.addObserver(map);
    }

    // Breeding constructor
    public Animal(AbstractWorldMap map, Vector2d initialPosition, int startEnergy, Genome genome) {
        super(initialPosition);
        this.map = map;
        this.energy = startEnergy;
        this.genome = genome;
        this.chooseOrientation();
        this.addObserver(map);
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

    public void move(MoveDirection direction, int moveEnergy) {
        switch (direction) {
            case RIGHT -> this.orientation = this.orientation.next();
            case LEFT -> this.orientation = this.orientation.previous();
            case FORWARD, BACKWARD -> {
                Vector2d unitVector = this.orientation.toUnitVector();
                if (direction == MoveDirection.BACKWARD)
                    unitVector = unitVector.opposite();
                Vector2d newPosition = this.position.add(unitVector);

                if (this.map instanceof UnboundedWorldMap)
                    newPosition = ((UnboundedWorldMap) this.map).positionToUnboundedPosition(newPosition);

                if (this.map.canMoveTo(newPosition)) {
                    positionChanged(this.position, newPosition, this);
                    this.position = newPosition;
                }
            }
        }
        this.energy -= moveEnergy;
    }

    private void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    private void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition, AbstractWorldMapElement objectAtOldPosition) {
        for (IPositionChangeObserver observer : this.observers)
            observer.positionChanged(oldPosition, newPosition, objectAtOldPosition);
    }

    public void chooseOrientation() {
        this.orientation = MapDirection.fromInteger(this.genome.getRandomGene().getValue());
    }

    // Assumes that other.energy <= this.energy
    public Animal breed(Animal other) {
        int childEnergy = (int) (Constants.energyPassOnBreedRatio * (this.energy + other.energy));
        this.energy -= Constants.energyPassOnBreedRatio * this.energy;
        other.energy -= Constants.energyPassOnBreedRatio * other.energy;

        double otherGenesRatio = (double) other.energy / ((double) (other.energy + this.energy));
        Genome childGenome = this.genome.combine(other.genome, otherGenesRatio);

        return new Animal(this.map, this.position, childEnergy, childGenome);
    }

    public void eat(int energyGain) {
        this.energy += energyGain;
    }

    public int getEnergy() {
        return this.energy;
    }

    @Override
    public String getRepresentationImagePath() {
        return "src/main/resources/down.png";
    }

    @Override
    public String toLabelString() {
        return this + Integer.toString(this.energy);
    }
}
