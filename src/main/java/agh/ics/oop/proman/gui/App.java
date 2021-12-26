package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.AbstractWorldMap;
import agh.ics.oop.proman.classes.BoundedWorldMap;
import agh.ics.oop.proman.classes.SimulationEngine;
import agh.ics.oop.proman.classes.UnboundedWorldMap;
import agh.ics.oop.proman.enums.SimulationParameter;
import agh.ics.oop.proman.interfaces.IStartButtonClickObserver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class App extends Application implements IStartButtonClickObserver {
    private Stage primaryStage;
    private Simulation leftSimulation;
    private Thread leftSimulationThread;
    private Simulation rightSimulation;
    private Thread rightSimulationThread;
    private int width = 1920;
    private int height = 1080;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(new Scene(new Menu(this), this.width, this.height));
        this.primaryStage.show();
    }

    private void initSimulations(int mapWidth, int mapHeight, int startEnergy, int moveEnergy,
                                 int plantEnergy, double jungleRatio, int animalsCount) {
        AbstractWorldMap unboundedMap = new UnboundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount);
        SimulationEngine unboundedSimulationEngine = new SimulationEngine(unboundedMap);
        this.leftSimulation = new Simulation(unboundedSimulationEngine, unboundedMap);
        this.leftSimulationThread = new Thread(this.leftSimulation);

        AbstractWorldMap boundedMap = new BoundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount);
        SimulationEngine boundedSimulationEngine = new SimulationEngine(boundedMap);
        this.rightSimulation = new Simulation(boundedSimulationEngine, boundedMap);
        this.rightSimulationThread = new Thread(this.rightSimulation);
    }

    private void positionElements() {
        GridPane gridPane = new GridPane();
        gridPane.add(this.leftSimulation, 0, 0, 1, 1);
        gridPane.add(this.rightSimulation, 1, 0, 1, 1);
        this.primaryStage.setScene(new Scene(gridPane, this.width, this.height));
    }

    private void startSimulations() {
        this.leftSimulationThread.start();
        this.rightSimulationThread.start();
    }

    @Override
    public void startButtonClicked(LinkedHashMap<SimulationParameter, String> simulParamToString) {
        int mapWidth = Integer.parseInt(simulParamToString.get(SimulationParameter.MAP_WIDTH));
        int mapHeight = Integer.parseInt(simulParamToString.get(SimulationParameter.MAP_HEIGHT));
        int startEnergy = Integer.parseInt(simulParamToString.get(SimulationParameter.START_ENERGY));
        int moveEnergy = Integer.parseInt(simulParamToString.get(SimulationParameter.MOVE_ENERGY));
        int plantEnergy = Integer.parseInt(simulParamToString.get(SimulationParameter.PLANT_ENERGY));
        double jungleRatio = Double.parseDouble(simulParamToString.get(SimulationParameter.JUNGLE_RATIO));
        int animalsCount = Integer.parseInt(simulParamToString.get(SimulationParameter.ANIMALS_COUNT));

        initSimulations(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount);
        positionElements();
        startSimulations();
    }
}
