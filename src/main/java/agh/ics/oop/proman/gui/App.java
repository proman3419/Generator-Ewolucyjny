package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.AbstractWorldMap;
import agh.ics.oop.proman.classes.BoundedWorldMap;
import agh.ics.oop.proman.classes.SimulationEngine;
import agh.ics.oop.proman.classes.UnboundedWorldMap;
import agh.ics.oop.proman.enums.SimulationParameter;
import agh.ics.oop.proman.interfaces.IStartButtonClickObserver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class App extends Application implements IStartButtonClickObserver {
    private Stage primaryStage;
    private Menu menu = new Menu(this);
    private SimulationDisplayer leftSimulationDisplayer;
    private SimulationDisplayer rightSimulationDisplayer;
    private Thread leftSimulationThread;
    private Thread rightSimulationThread;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(new Scene(this.menu));
        this.primaryStage.show();
    }

    private void initSimulations(int mapWidth, int mapHeight, int startEnergy, int moveEnergy,
                                 int plantEnergy, double jungleRatio, int animalsCount) {
        AbstractWorldMap boundedMap = new BoundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount);
        SimulationEngine boundedSimulationEngine = new SimulationEngine(boundedMap);
        this.leftSimulationDisplayer = new SimulationDisplayer(boundedSimulationEngine, boundedMap);
        this.leftSimulationThread = new Thread(this.leftSimulationDisplayer);

        AbstractWorldMap unboundedMap = new UnboundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount);
        SimulationEngine unboundedSimulationEngine = new SimulationEngine(unboundedMap);
        this.rightSimulationDisplayer = new SimulationDisplayer(unboundedSimulationEngine, unboundedMap);
        this.rightSimulationThread = new Thread(this.rightSimulationDisplayer);
    }

    private void placeSimulations() {
        GridPane gridPane = new GridPane();
        gridPane.add(this.leftSimulationDisplayer, 0, 0, 1, 1);
        gridPane.add(this.rightSimulationDisplayer, 1, 0, 1, 1);
        this.primaryStage.setScene(new Scene(gridPane));
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
        placeSimulations();
        startSimulations();
    }
}
