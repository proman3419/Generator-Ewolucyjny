package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Gui.Displayers.GenomeDisplayer;
import agh.ics.oop.proman.Gui.Displayers.GraphsDisplayer;
import agh.ics.oop.proman.Gui.Displayers.MapDisplayer;
import agh.ics.oop.proman.Gui.Displayers.TextDisplayer;
import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Entities.Epoch;
import agh.ics.oop.proman.Classes.ReportGenerator;
import agh.ics.oop.proman.Classes.SimulationEngine;
import agh.ics.oop.proman.Observers.IEpochEndObserver;
import agh.ics.oop.proman.Observers.IEventObserver;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class Simulation extends GridPane implements Runnable, IEpochEndObserver, IEventObserver {
    private final SimulationEngine simulationEngine;
    private final Thread simulationEngineThread;
    private final SimulationControlPanel simulationControlPanel;
    private final MapDisplayer mapDisplayer;
    private final GraphsDisplayer graphsDisplayer = new GraphsDisplayer();
    private final GenomeDisplayer dominantGenomeDisplayer = new GenomeDisplayer();
    private final TextDisplayer textDisplayer = new TextDisplayer();
    private final ReportGenerator reportGenerator = new ReportGenerator();

    public Simulation(SimulationEngine simulationEngine, AbstractWorldMap map) {
        this.simulationEngine = simulationEngine;
        this.simulationEngine.addEpochEndObserver(this);
        this.simulationEngine.addEventObserver(this);
        this.simulationEngine.epochEndedNotify(); // Init epoch ended
        this.simulationEngineThread = new Thread(this.simulationEngine);
        this.simulationControlPanel = new SimulationControlPanel(this.simulationEngine, reportGenerator);
        this.mapDisplayer = new MapDisplayer(map);
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.simulationControlPanel, 0, 2, 1, 1);
            this.add(this.mapDisplayer, 0, 0, 1, 1);
            this.add(this.graphsDisplayer, 1, 0, 1, 1);
            this.add(this.dominantGenomeDisplayer, 0, 1, 1, 1);
            this.add(this.textDisplayer, 0, 3, 1, 1);
        });
    }

    @Override
    public void run() {
        positionElements();
        this.simulationEngineThread.start();
    }

    @Override
    public void epochEnded(Epoch epoch) {
        Platform.runLater(() -> {
            this.mapDisplayer.update();
            this.graphsDisplayer.update(epoch);
            this.dominantGenomeDisplayer.update(epoch.getDominantGenome());
            this.reportGenerator.feed(epoch);
        });
    }

    @Override
    public void eventHappened(String description) {
        this.textDisplayer.setText(description);
    }
}
