package agh.ics.oop.proman.classes;

import java.util.LinkedList;
import java.util.List;

public class WorldMapStatistics {
    private AbstractWorldMap map;

    public WorldMapStatistics(AbstractWorldMap map) {
        this.map = map;
    }

    public int getAnimalsCount() {
        return this.map.animalsList.size();
    }

    private int getDeadAnimalsCount() {
        return this.map.animalsLifespans.size();
    }

    public int getPlantsCount() {
        return this.map.plantsList.size();
    }

    public double getAverageAnimalEnergy() {
        if (getAnimalsCount() == 0) return 0;

        int energySum = 0;
        for (Animal animal : this.map.animalsList)
            energySum += animal.getEnergy();

        return (double) energySum / getAnimalsCount();
    }

    public double getAverageAnimalLifespan() {
        if (getDeadAnimalsCount() == 0) return 0;

        int animalLifespansSum = 0;
        for (Integer animalLifespan : this.map.animalsLifespans)
            animalLifespansSum += animalLifespan;

        return (double) animalLifespansSum / getDeadAnimalsCount();
    }

    public double getAverageChildrenCount() {
        if (getAnimalsCount() == 0) return 0;

        int childrenSum = 0;
        for (Animal animal : this.map.animalsList)
            childrenSum += animal.getChildrenCount();

        return (double) childrenSum / getAnimalsCount();
    }
}
