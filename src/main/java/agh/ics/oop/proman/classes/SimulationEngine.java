package agh.ics.oop.proman.classes;

import agh.ics.oop.proman.interfaces.IEpochEndObserver;
import agh.ics.oop.proman.interfaces.IEventObserver;

import java.util.LinkedList;
import java.util.List;

public class SimulationEngine implements Runnable, IEventObserver {
    private final AbstractWorldMap map;
    private final WorldMapStatistics mapStatistics;
    private final List<IEpochEndObserver> epochEndObservers = new LinkedList<>();
    private final List<IEventObserver> eventObservers = new LinkedList<>();
    private final int moveDelay = 300;
    private Epoch epoch;
    private boolean isPaused = false;
    private boolean isFinished = false;

    public SimulationEngine(AbstractWorldMap map) {
        this.map = map;
        this.mapStatistics = new WorldMapStatistics(map);
        this.map.addEventObserver(this);
        this.epoch = getCurrentEpoch(-1);
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
        this.isFinished = true;
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

    public boolean isFinished() {
        return isFinished;
    }

    private Epoch getCurrentEpoch(int prevEpochId) {
        return new Epoch(prevEpochId + 1,
                         this.mapStatistics.getAnimalsCount(),
                         this.mapStatistics.getPlantsCount(),
                         this.mapStatistics.getAverageAnimalEnergy(),
                         this.mapStatistics.getAverageAnimalLifespan(),
                         this.mapStatistics.getAverageChildrenCount(),
                         this.mapStatistics.getDominantGenome());
    }

    public void addEpochEndObserver(IEpochEndObserver epochEndObserver) {
        this.epochEndObservers.add(epochEndObserver);
    }

    public void epochEndedNotify() {
        for (IEpochEndObserver epochEndObserver : this.epochEndObservers)
            epochEndObserver.epochEnded(this.epoch);
    }

    private void epochEnded() {
        this.epoch = getCurrentEpoch(this.epoch.getId());
        ageAnimals();
        epochEndedNotify();

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

    public void addEventObserver(IEventObserver eventObserver) {
        this.eventObservers.add(eventObserver);
    }

    @Override
    public void eventHappened(String description) {
        for (IEventObserver eventObserver : this.eventObservers)
            eventObserver.eventHappened(description);
    }
}
