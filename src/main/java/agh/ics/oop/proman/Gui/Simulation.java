package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Gui.Displayers.GenomeDisplayer;
import agh.ics.oop.proman.Gui.Displayers.ChartsDisplayer;
import agh.ics.oop.proman.Gui.Displayers.MapDisplayer;
import agh.ics.oop.proman.Gui.Displayers.MagicBreedingDisplayer;
import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Entities.Epoch;
import agh.ics.oop.proman.Classes.ReportGenerator;
import agh.ics.oop.proman.Classes.SimulationEngine;
import agh.ics.oop.proman.Observers.IEpochEndObserver;
import agh.ics.oop.proman.Observers.IMagicBreedingObserver;
import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class Simulation extends GridPane implements Runnable, IEpochEndObserver, IMagicBreedingObserver {
    private final SimulationEngine simulationEngine;
    private final Thread simulationEngineThread;
    private final SimulationControlPanel simulationControlPanel;
    private final MapDisplayer mapDisplayer;
    private final ChartsDisplayer chartsDisplayer = new ChartsDisplayer();
    private final GenomeDisplayer dominantGenomeDisplayer = new GenomeDisplayer("Dominant genome");
    private final MagicBreedingDisplayer magicBreedingDisplayer = new MagicBreedingDisplayer();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    // GuiContainers
    private final GuiContainer mapDisplayerContainer;
    private final GuiContainer dominantGenomeDisplayerContainer;
    private final GuiContainer magicBreedingDisplayerContainer;

    public Simulation(SimulationEngine simulationEngine, AbstractWorldMap map) {
        this.simulationEngine = simulationEngine;
        this.simulationEngine.addEpochEndObserver(this);
        this.simulationEngine.addMagicBreedingObserver(this);
        this.simulationEngine.epochEndedNotify(); // Init epoch ended
        this.simulationEngineThread = new Thread(this.simulationEngine);
        this.simulationControlPanel = new SimulationControlPanel(this.simulationEngine, reportGenerator);
        this.mapDisplayer = new MapDisplayer(map);

        this.mapDisplayerContainer = new GuiContainer(this.mapDisplayer);
        this.dominantGenomeDisplayerContainer = new GuiContainer(this.dominantGenomeDisplayer);
        this.magicBreedingDisplayerContainer = new GuiContainer(this.magicBreedingDisplayer);
    }

    private void styleElements() {
        this.magicBreedingDisplayerContainer.setPadding(new Insets(0,0,0,25));
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.simulationControlPanel, 0, 2, 1, 1);
            this.add(this.mapDisplayerContainer, 0, 0, 1, 1);
            this.add(this.chartsDisplayer, 1, 0, 1, 1);
            this.add(this.dominantGenomeDisplayerContainer, 0, 1, 1, 1);
            this.add(this.magicBreedingDisplayerContainer, 1, 1, 1, 1);
        });
    }

    @Override
    public void run() {
        styleElements();
        positionElements();
        this.simulationEngineThread.start();
    }

    @Override
    public void epochEnded(Epoch epoch) {
        Platform.runLater(() -> {
            this.mapDisplayer.update();
            this.chartsDisplayer.update(epoch);
            this.dominantGenomeDisplayer.update(epoch.getDominantGenome());
            this.reportGenerator.feed(epoch);
        });
    }

    @Override
    public void magicBreedingHappened(String notificationMessage) {
        this.magicBreedingDisplayer.update(notificationMessage);
    }
}
