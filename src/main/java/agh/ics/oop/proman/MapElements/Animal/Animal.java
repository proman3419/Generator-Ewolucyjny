package agh.ics.oop.proman.MapElements.Animal;

import agh.ics.oop.proman.MapElements.AbstractWorldMapElement;
import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Maps.UnboundedWorldMap;
import agh.ics.oop.proman.Entities.Vector2d;
import agh.ics.oop.proman.Settings.SimulationConstants;
import agh.ics.oop.proman.Enums.MapDirection;
import agh.ics.oop.proman.Enums.MoveDirection;
import agh.ics.oop.proman.Observers.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class Animal extends AbstractWorldMapElement {
    private final AbstractWorldMap map;
    private int energy;
    private int age = 0;
    private int childrenCount = 0;
    private final Genome genome;
    private MapDirection orientation;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int startEnergy) {
        super(initialPosition);
        this.map = map;
        this.energy = startEnergy;
        this.genome = new Genome(SimulationConstants.genesInGenomeCount);
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

    //region Animal's activities ---------------------------------------------------------------------------------------
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

    public void chooseOrientation() {
        this.orientation = MapDirection.fromInteger(this.genome.getRandomGene().getValue());
    }

    // Assumes that other.energy <= this.energy
    public Animal breed(Animal other) {
        int childEnergy = (int) (SimulationConstants.energyPassOnBreedRatio * (this.energy + other.energy));
        this.energy -= SimulationConstants.energyPassOnBreedRatio * this.energy;
        other.energy -= SimulationConstants.energyPassOnBreedRatio * other.energy;

        double otherGenesRatio = (double) other.energy / ((double) (other.energy + this.energy));
        Genome childGenome = this.genome.combine(other.genome, otherGenesRatio);

        return new Animal(this.map, this.position, childEnergy, childGenome);
    }

    public void eat(int energyGain) {
        this.energy += energyGain;
    }

    public void becomeOlder() {
        this.age++;
    }

    public void increaseChildrenCount() {
        this.childrenCount++;
    }
    //endregion Animal's activities ------------------------------------------------------------------------------------

    //region IPositionChangeObserver related ---------------------------------------------------------------------------
    private void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition,
                                 AbstractWorldMapElement objectAtOldPosition) {
        for (IPositionChangeObserver observer : this.observers)
            observer.positionChanged(oldPosition, newPosition, objectAtOldPosition);
    }
    //endregion IPositionChangeObserver related ------------------------------------------------------------------------

    //region IMapElement implementation --------------------------------------------------------------------------------
    @Override
    public String getRepresentationImagePath() {
        return switch (this.orientation) {
            case NORTH -> "src/main/resources/north.png";
            case NORTHEAST -> "src/main/resources/northeast.png";
            case EAST -> "src/main/resources/east.png";
            case SOUTHEAST -> "src/main/resources/southeast.png";
            case SOUTH -> "src/main/resources/south.png";
            case SOUTHWEST -> "src/main/resources/southwest.png";
            case WEST -> "src/main/resources/west.png";
            case NORTHWEST -> "src/main/resources/northwest.png";
        };
    }

    @Override
    public String toLabelString() {
        return this + Integer.toString(this.energy);
    }
    //endregion IMapElement implementation -----------------------------------------------------------------------------

    //region Getters ---------------------------------------------------------------------------------------------------
    public int getEnergy() {
        return this.energy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public Genome getGenome() {
        return genome;
    }
    //endregion Getters ------------------------------------------------------------------------------------------------
}
