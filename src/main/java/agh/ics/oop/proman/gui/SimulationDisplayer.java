package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.AbstractWorldMap;
import agh.ics.oop.proman.classes.SimulationEngine;
import agh.ics.oop.proman.interfaces.IEpochEndObserver;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class SimulationDisplayer extends GridPane implements IEpochEndObserver, Runnable {
    private SimulationEngine simulationEngine;
    private Thread simulationEngineThread;
    private MapDisplayer mapDisplayer;

    public SimulationDisplayer(SimulationEngine simulationEngine, AbstractWorldMap map) {
        this.simulationEngine = simulationEngine;
        this.simulationEngine.addMapChangeObserver(this);
        this.simulationEngineThread = new Thread(this.simulationEngine);
        this.mapDisplayer = new MapDisplayer(map);
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.mapDisplayer, 0, 0, 1, 1);
        });
    }

    @Override
    public void run() {
        this.mapDisplayer.update();
        positionElements();
        this.simulationEngineThread.start();
    }

    @Override
    public void epochEnded() {
        Platform.runLater(() -> {
            this.mapDisplayer.update();
        });
    }

    public SimulationEngine getSimulationEngine() {
        return simulationEngine;
    }
}
