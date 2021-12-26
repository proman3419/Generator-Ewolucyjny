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
    private boolean isPaused = false;

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
            pauseIfRequested();
        }
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
        synchronized (this) {
            notify();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void pauseIfRequested() {
        synchronized (this) {
            while (this.isPaused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                                         this.mapStatistics.getAverageChildrenCount(),
                                         this.mapStatistics.getDominantGenome());
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
