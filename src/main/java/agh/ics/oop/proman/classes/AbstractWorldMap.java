package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.core.Constants;
import agh.ics.oop.proman.enums.MoveDirection;
import agh.ics.oop.proman.interfaces.IPositionChangeObserver;
import agh.ics.oop.proman.interfaces.IWorldMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final int width;
    protected final int height;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final Vector2d lowerLeft;
    protected final Vector2d upperRight;
    protected final Vector2d lowerLeftJungle;
    protected final Vector2d upperRightJungle;
    protected final LinkedHashMap<Vector2d, List<Animal>> animals = new LinkedHashMap<>();
    protected final List<Animal> animalsList = new LinkedList<>();
    protected final LinkedHashMap<Vector2d, Plant> plants = new LinkedHashMap<>();
    protected final List<Plant> plantsList = new LinkedList<>();
    protected int plantsInSteppe = 0;
    protected int plantsInJungle = 0;
    protected final int plantsInSteppeLimit;
    protected final int plantsInJungleLimit;

    public AbstractWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                            int animalsCount) {
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

        for (int i = 0; i < animalsCount; i++) {
            Animal animal;
            do {
               animal = new Animal(this, true);
            } while (this.animals.get(animal.position) != null); // Choose a new position if there's an animal already
            place(animal);
        }

        this.plantsInJungleLimit = jungleWidth * jungleHeight;
        this.plantsInSteppeLimit = width * height - this.plantsInJungleLimit;
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
            if (animal.getEnergy() <= 0) {
                removeAnimal(animal, animal.position);
            }
        }
    }

    public void animalsMove() {
        for (int i = 0; i < this.animalsList.size(); i++) {
            this.animalsList.get(i).move(MoveDirection.getRandomMoveDirection(), this.moveEnergy);
        }
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
                }
            }
        }
    }

    public void growPlants() {
        int steppeSpawned = 0;
        while (this.plantsInSteppe < this.plantsInSteppeLimit && steppeSpawned < Constants.dailyPlantsSpawnCountSteppe) {
            Vector2d position;
            do {
                position = chooseRandomPosition(false);
            } while(!addPlant(new Plant(position, false)));
            steppeSpawned++;
        }

        int jungleSpawned = 0;
        while (this.plantsInJungle < this.plantsInJungleLimit && jungleSpawned < Constants.dailyPlantsSpawnCountJungle) {
            Vector2d position;
            do {
                position = chooseRandomPosition(true);
            } while(!addPlant(new Plant(position, true)));
            jungleSpawned++;
        }
    }
    /* ^ Simulation related ^ -------------------------------------------------------------------------------- */

    /* v Others v -------------------------------------------------------------------------------------------- */
    public Vector2d chooseRandomPosition(boolean withinJungle) {
        Vector2d ll = withinJungle ? this.lowerLeftJungle : this.lowerLeft;
        Vector2d ur = withinJungle ? this.upperRightJungle : this.upperRight;
        Vector2d position;

        do {
            int x = Helper.getRandomIntFromRange(ll.x, ur.x+1);
            int y = Helper.getRandomIntFromRange(ll.y, ur.y+1);
            position = new Vector2d(x, y);
        } while (!withinJungle && this.lowerLeftJungle.precedes(position) && this.upperRight.follows(position));

        return position;
    }

    protected void addAnimal(Animal animal, Vector2d position) {
        this.animalsList.add(animal);

        // If empty add a new container
        this.animals.computeIfAbsent(position, k -> new ArrayList<>());
        this.animals.get(position).add(animal);
    }

    protected void removeAnimal(Animal animal, Vector2d position) {
        this.animalsList.remove(animal);

        this.animals.get(position).remove(animal);
        // If empty remove the container
        if (this.animals.get(position).size() == 0)
            this.animals.remove(position);
    }

    protected boolean addPlant(Plant plant) {
        this.plantsList.add(plant);

        if (this.plants.get(plant.position) == null) {
            this.plants.put(plant.position, plant);

            if (plant.isInJungle())
                this.plantsInJungle++;
            else
                this.plantsInSteppe++;

            return true;
        }

        return false;
    }

    protected void removePlant(Plant plant) {
        this.plantsList.remove(plant);

        this.plants.remove(plant.position);

        if (plant.isInJungle())
            this.plantsInJungle--;
        else
            this.plantsInSteppe--;
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

    public boolean isAnyAnimalAlive() {
        return getAnimalsCount() > 0;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public int getAnimalsCount() {
        return this.animalsList.size();
    }

    public int getPlantsCount() {
        return this.plantsList.size();
    }
    /* ^ Others ^ -------------------------------------------------------------------------------------------- */
}
