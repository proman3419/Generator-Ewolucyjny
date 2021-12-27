package agh.ics.oop.proman.Maps;

import agh.ics.oop.proman.MapElements.AbstractWorldMapElement;
import agh.ics.oop.proman.MapElements.Animal.Animal;
import agh.ics.oop.proman.MapElements.Plant;
import agh.ics.oop.proman.Classes.Helper;
import agh.ics.oop.proman.Entities.Vector2d;
import agh.ics.oop.proman.Core.Constants;
import agh.ics.oop.proman.Enums.MoveDirection;
import agh.ics.oop.proman.Observers.IEventObserver;
import agh.ics.oop.proman.Observers.IPositionChangeObserver;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final int width;
    protected final int height;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final boolean isMagicBreedingAllowed;
    protected int magicBreedingCount = 0;
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
    protected final List<IEventObserver> eventObservers = new LinkedList<>();

    public AbstractWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                            int animalsCount, boolean isMagicBreedingAllowed) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.isMagicBreedingAllowed = isMagicBreedingAllowed;
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
            Vector2d position = getRandomFreePosition(withinJungle);
            if (position == null) break;
            Animal animal = new Animal(this, position);
            place(animal);
        }
    }

    private void spawnAnimalsAtRandomFreePositions(List<Animal> animalsList) {
        for (Animal animal : animalsList) {
            boolean withinJungle = Helper.getRandomBoolean(0.5);
            Vector2d position = getRandomFreePosition(withinJungle);
            if (position == null) break;

            Animal animalCopy = new Animal(this, position, this.startEnergy, animal.getGenome());
            place(animalCopy);
        }
    }

    /* v IWorldMap implementation v -------------------------------------------------------------------------- */
    @Override
    public boolean canMoveTo(Vector2d position) {
        return this.lowerLeft.precedes(position) && this.upperRight.follows(position);
    }

    @Override
    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            addAnimal(animal, animal.getPosition());
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
                removeAnimal(animal, animal.getPosition());
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
            List<Animal> strongestAnimalsAtPosition = getStrongestAnimalsAtPositionEqualEnergy(plant.getPosition());
            int strongestAnimalsAtPositionCount = strongestAnimalsAtPosition.size();

            for (Animal animal : strongestAnimalsAtPosition)
                animal.eat(this.plantEnergy / strongestAnimalsAtPositionCount);

            if (strongestAnimalsAtPositionCount > 0)
                plantsToRemove.add(plant);
        }

        for (Plant plant : plantsToRemove)
            removePlant(plant);
    }

    private void animalsBreedNormal() {
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

    protected void animalsBreedMagic() {
        List<Animal> animalsListCopy = new LinkedList<>(this.animalsList);
        spawnAnimalsAtRandomFreePositions(animalsListCopy);
        this.magicBreedingCount++;
        eventHappened(String.format("Magic breeding [%d/%d]", this.magicBreedingCount, Constants.maxMagicBreedingCount));
    }

    public void animalsBreed() {
        boolean isMagic = Helper.getRandomBoolean(Constants.magicBreedingProbability);
        if (this.isMagicBreedingAllowed && isMagic && this.magicBreedingCount < Constants.maxMagicBreedingCount)
            animalsBreedMagic();
        else
            animalsBreedNormal();
    }

    public void growPlants() {
        for (int jungleSpawned = 0; jungleSpawned < Constants.dailyPlantsSpawnCountJungle; jungleSpawned++) {
            Vector2d position = getRandomFreePosition(false);
            if (position == null) break;
            addPlant(new Plant(position));
        }

        for (int steppeSpawned = 0; steppeSpawned < Constants.dailyPlantsSpawnCountSteppe; steppeSpawned++) {
            Vector2d position = getRandomFreePosition(true);
            if (position == null) break;
            addPlant(new Plant(position));
        }
    }
    /* ^ Simulation related ^ -------------------------------------------------------------------------------- */

    /* v Others v -------------------------------------------------------------------------------------------- */
    public Vector2d getRandomFreePosition(boolean withinJungle) {
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
        if (!isOccupied(plant.getPosition())) {
            this.plantsList.add(plant);
            this.plants.put(plant.getPosition(), plant);
            notePositionIfOccupied(plant.getPosition());

            return true;
        }

        return false;
    }

    protected void removePlant(Plant plant) {
        this.plantsList.remove(plant);
        this.plants.remove(plant.getPosition());
        notePositionIfFree(plant.getPosition());
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

    public void addEventObserver(IEventObserver eventObserver) {
        this.eventObservers.add(eventObserver);
    }

    private void eventHappened(String description) {
        for (IEventObserver eventObserver : this.eventObservers)
            eventObserver.eventHappened(description);
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public List<Animal> getAnimalsList() {
        return animalsList;
    }
    /* ^ Others ^ -------------------------------------------------------------------------------------------- */
}
