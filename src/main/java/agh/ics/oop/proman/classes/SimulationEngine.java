package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IEpochEndObserver;

import java.util.LinkedList;
import java.util.List;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map;
    private final List<IEpochEndObserver> mapChangeObservers = new LinkedList<>();
    private final int moveDelay = 1400;
    private int epoch = 0;

    public SimulationEngine(AbstractWorldMap map) {
        this.map = map;
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

        for (IEpochEndObserver mapChangeObserver : this.mapChangeObservers)
            mapChangeObserver.epochEnded(this.epoch, this.map.getAnimalsCount(), this.map.getPlantsCount());

        try {
            Thread.sleep(this.moveDelay);
        } catch (InterruptedException e) {
            System.out.println("The simulation has stopped");
        }
    }

    public int getAnimalsCount() {
        return this.map.getAnimalsCount();
    }
}
