package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IEpochEndObserver;

import java.util.LinkedList;
import java.util.List;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map;
    private final WorldMapStatistics mapStatistics;
    private final List<IEpochEndObserver> mapChangeObservers = new LinkedList<>();
    private final int moveDelay = 300;
    private int epoch = 0;

    public SimulationEngine(AbstractWorldMap map) {
        this.map = map;
        this.mapStatistics = new WorldMapStatistics(map);
    }

    @Override
    public void run() {
        while (map.isAnyAnimalAlive()) {
            this.map.removeDeadAnimals();
            this.map.animalsMove();
            this.map.animalsEat();
            this.map.animalsBreed();
            this.map.growPlants();
            epochEnded();
        }
    }

    public void addMapChangeObserver(IEpochEndObserver mapChangeObserver) {
        this.mapChangeObservers.add(mapChangeObserver);
    }

    public void epochEnded() {
        this.epoch++;
        ageAnimals();

        for (IEpochEndObserver mapChangeObserver : this.mapChangeObservers)
            mapChangeObserver.epochEnded(this.epoch,
                                         this.mapStatistics.getAnimalsCount(),
                                         this.mapStatistics.getPlantsCount(),
                                         this.mapStatistics.getAverageAnimalEnergy(),
                                         this.mapStatistics.getAverageAnimalLifespan(),
                                         this.mapStatistics.getAverageChildrenCount());
        try {
            Thread.sleep(this.moveDelay);
        } catch (InterruptedException e) {
            System.out.println("The simulation has stopped");
        }
    }

    private void ageAnimals() {
        for (Animal animal : this.map.animalsList)
            animal.becomeOlder();
    }
}
