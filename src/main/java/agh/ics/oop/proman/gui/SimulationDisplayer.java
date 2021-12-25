package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.AbstractWorldMap;
import agh.ics.oop.proman.classes.Genome;
import agh.ics.oop.proman.classes.SimulationEngine;
import agh.ics.oop.proman.core.Constants;
import agh.ics.oop.proman.interfaces.IEpochEndObserver;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class SimulationDisplayer extends GridPane implements IEpochEndObserver, Runnable {
    private final SimulationEngine simulationEngine;
    private final Thread simulationEngineThread;
    private final MapDisplayer mapDisplayer;
    private final GraphsDisplayer graphsDisplayer = new GraphsDisplayer();
    private final GenomeDisplayer dominantGenomeDisplayer = new GenomeDisplayer();

    public SimulationDisplayer(SimulationEngine simulationEngine, AbstractWorldMap map) {
        this.simulationEngine = simulationEngine;
        this.simulationEngine.addMapChangeObserver(this);
        this.simulationEngineThread = new Thread(this.simulationEngine);
        this.mapDisplayer = new MapDisplayer(map);
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.mapDisplayer, 0, 0, 1, 1);
            this.add(this.graphsDisplayer, 1, 0, 1, 1);
            this.add(this.dominantGenomeDisplayer, 0, 1, 1, 1);
        });
    }

    @Override
    public void run() {
        this.mapDisplayer.update();
        this.graphsDisplayer.update(0, 0, 0, 0, 0, 0);
        this.dominantGenomeDisplayer.update(new Genome(Constants.genesInGenomeCount));
        positionElements();
        this.simulationEngineThread.start();
    }

    @Override
    public void epochEnded(int epoch, int animalsCount, int plantsCount, double averageAnimalEnergy,
                           double averageAnimalLifespan, double averageChildrenCount, Genome dominantGenome) {
        Platform.runLater(() -> {
            this.mapDisplayer.update();
            this.graphsDisplayer.update(epoch, animalsCount, plantsCount, averageAnimalEnergy,
                                        averageAnimalLifespan, averageChildrenCount);
            this.dominantGenomeDisplayer.update(dominantGenome);
        });
    }
}
