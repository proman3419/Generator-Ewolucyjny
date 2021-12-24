package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.core.Constants;
import agh.ics.oop.proman.enums.MoveDirection;
import agh.ics.oop.proman.interfaces.IPositionChangeObserver;
import agh.ics.oop.proman.interfaces.IWorldMap;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final int width;
    protected final int height;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected Vector2d lowerLeftJungle;
    protected Vector2d upperRightJungle;
    protected final LinkedHashMap<Vector2d, List<Animal>> animals = new LinkedHashMap<>();
    protected final List<Integer> animalsLifespans = new LinkedList<>();
    protected final List<Animal> animalsList = new LinkedList<>();
    protected final LinkedHashMap<Vector2d, Plant> plants = new LinkedHashMap<>();
    protected final List<Plant> plantsList = new LinkedList<>();
    protected final Set<Vector2d> freePositionsSteppe = new HashSet<>();
    protected final Set<Vector2d> freePositionsJungle = new HashSet<>();

    public AbstractWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                            int animalsCount) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);

        initJungle(jungleRatio);
        noteFreePositions();
        spawnAnimalsAtRandomFreePositions(animalsCount, true);
    }

    private void initJungle(double jungleRatio) {
        int jungleWidth = (int)(width * jungleRatio);
        int jungleHeight = (int)(height * jungleRatio);
        int jungleXStart = (width - jungleWidth) / 2;
        int jungleYStart = (height - jungleHeight) / 2;
        this.lowerLeftJungle = new Vector2d(jungleXStart, jungleYStart);
        this.upperRightJungle = new Vector2d(jungleXStart + jungleWidth, jungleYStart + jungleHeight);
    }

    private void noteFreePositions() {
        for (int x = this.lowerLeft.x; x < this.upperRight.x; x++)
            for (int y = this.lowerLeft.y; y < this.upperRight.y; y++)
                notePositionIfFree(new Vector2d(x, y));
    }

    private void spawnAnimalsAtRandomFreePositions(int animalsCount, boolean withinJungle) {
        for (int i = 0; i < animalsCount; i++) {
            Vector2d position = chooseRandomFreePosition(withinJungle);
            if (position == null) break;
            Animal animal = new Animal(this, position);
            place(animal);
        }
    }

    /* v IWorldMap implementation v -------------------------------------------------------------------------- */
    @Override
    public boolean canMoveTo(Vector2d position) {
        return this.lowerLeft.precedes(position) && this.upperRight.follows(position);
    }

    @Override
    public boolean place(Animal animal) {
        if (canMoveTo(animal.position)) {
            addAnimal(animal, animal.position);
            return true;
        }

        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (this.plants.get(position) != null) return this.plants.get(position);
        if (this.animals.get(position) != null)  {
            if (this.animals.get(position).size() > 0)
                return this.animals.get(position).get(0);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Implement me!";
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object objectAtOldPosition) {
        if (objectAtOldPosition instanceof Animal animal) {
            removeAnimal(animal, oldPosition);
            addAnimal(animal, newPosition);
        }
    }
    /* ^ IWorldMap implementation ^ -------------------------------------------------------------------------- */

    /* v Simulation related v -------------------------------------------------------------------------------- */
    public void removeDeadAnimals() {
        for (int i = 0; i < this.animalsList.size(); i++) {
            Animal animal = this.animalsList.get(i);
            if (animal.getEnergy() < this.moveEnergy) {
                removeAnimal(animal, animal.position);
                this.animalsLifespans.add(animal.getAge());
            }
        }
    }

    public void animalsMove() {
        for (int i = 0; i < this.animalsList.size(); i++)
            this.animalsList.get(i).move(MoveDirection.getRandomMoveDirection(), this.moveEnergy);
    }

    public void animalsEat() {
        List<Plant> plantsToRemove = new LinkedList<>();

        for (Plant plant : this.plantsList) {
            List<Animal> strongestAnimalsAtPosition = getStrongestAnimalsAtPositionEqualEnergy(plant.position);
            int strongestAnimalsAtPositionCount = strongestAnimalsAtPosition.size();

            for (Animal animal : strongestAnimalsAtPosition)
                animal.eat(this.plantEnergy / strongestAnimalsAtPositionCount);

            if (strongestAnimalsAtPositionCount > 0)
                plantsToRemove.add(plant);
        }

        for (Plant plant : plantsToRemove)
            removePlant(plant);
    }

    public void animalsBreed() {
        for (Vector2d position : this.animals.keySet()) {
            if (this.animals.get(position).size() > 1) {
                this.animals.get(position).sort(Helper.animalEnergyComparator);
                Animal strongerParent = this.animals.get(position).get(0);
                Animal weakerParent = this.animals.get(position).get(1);

                if (weakerParent.getEnergy() >= Constants.minEnergyToBreed) {
                    Animal child = strongerParent.breed(weakerParent);
                    place(child);
                    strongerParent.increaseChildrenCount();
                    weakerParent.increaseChildrenCount();
                }
            }
        }
    }

    public void growPlants() {
        for (int jungleSpawned = 0; jungleSpawned < Constants.dailyPlantsSpawnCountJungle; jungleSpawned++) {
            Vector2d position = chooseRandomFreePosition(false);
            if (position == null) break;
            addPlant(new Plant(position));
        }

        for (int steppeSpawned = 0; steppeSpawned < Constants.dailyPlantsSpawnCountSteppe; steppeSpawned++) {
            Vector2d position = chooseRandomFreePosition(true);
            if (position == null) break;
            addPlant(new Plant(position));
        }
    }
    /* ^ Simulation related ^ -------------------------------------------------------------------------------- */

    /* v Others v -------------------------------------------------------------------------------------------- */
    public Vector2d chooseRandomFreePosition(boolean withinJungle) {
        if (withinJungle)
            return (Vector2d) Helper.getRandomElementFromSet(this.freePositionsJungle);
        else
            return (Vector2d) Helper.getRandomElementFromSet(this.freePositionsSteppe);
    }

    protected void addAnimal(Animal animal, Vector2d position) {
        this.animalsList.add(animal);

        // If empty add a new container
        this.animals.computeIfAbsent(position, k -> new ArrayList<>());
        this.animals.get(position).add(animal);

        notePositionIfOccupied(position);
    }

    protected void removeAnimal(Animal animal, Vector2d position) {
        this.animalsList.remove(animal);

        this.animals.get(position).remove(animal);
        // If empty remove the container
        if (this.animals.get(position).size() == 0)
            this.animals.remove(position);

        notePositionIfFree(position);
    }

    protected boolean addPlant(Plant plant) {
        if (!isOccupied(plant.position)) {
            this.plantsList.add(plant);
            this.plants.put(plant.position, plant);
            notePositionIfOccupied(plant.position);

            return true;
        }

        return false;
    }

    protected void removePlant(Plant plant) {
        this.plantsList.remove(plant);
        this.plants.remove(plant.position);
        notePositionIfFree(plant.position);
    }

    protected void notePositionIfOccupied(Vector2d position) {
        if (isOccupied(position)) {
            if (isInsideJungle(position))
                this.freePositionsJungle.remove(position);
            else
                this.freePositionsSteppe.remove(position);
        }
    }

    protected void notePositionIfFree(Vector2d position) {
        if (!isOccupied(position)) {
            if (isInsideJungle(position))
                this.freePositionsJungle.add(position);
            else
                this.freePositionsSteppe.add(position);
        }
    }

    protected List<Animal> getStrongestAnimalsAtPositionEqualEnergy(Vector2d position) {
        List<Animal> strongestAnimalsAtPosition = new LinkedList<>();
        List<Animal> animalsAtPosition = this.animals.get(position);

        if (animalsAtPosition != null) {
            animalsAtPosition.sort(Helper.animalEnergyComparator);

            for (Animal animal : animalsAtPosition) {
                if (Helper.animalEnergyComparator.compare(animal, animalsAtPosition.get(0)) == 0)
                    strongestAnimalsAtPosition.add(animal);
                else
                    break;
            }
        }

        return strongestAnimalsAtPosition;
    }

    private boolean isInsideJungle(Vector2d position) {
        return this.lowerLeftJungle.precedes(position) && this.upperRightJungle.follows(position);
    }

    public boolean isAnyAnimalAlive() {
        return this.animals.size() > 0;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }
    /* ^ Others ^ -------------------------------------------------------------------------------------------- */
}
