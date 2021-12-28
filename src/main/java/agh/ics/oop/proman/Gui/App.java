package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Maps.AbstractWorldMap;
import agh.ics.oop.proman.Maps.BoundedWorldMap;
import agh.ics.oop.proman.Classes.SimulationEngine;
import agh.ics.oop.proman.Maps.UnboundedWorldMap;
import agh.ics.oop.proman.Settings.IParameter;
import agh.ics.oop.proman.Settings.SimulationParameter;
import agh.ics.oop.proman.Observers.IStartButtonClickObserver;
import agh.ics.oop.proman.Settings.GuiParameter;
import javafx.application.Application;
import javafx.geometry.Insets;
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
    private int width;
    private int height;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setScene(new Scene(new Menu(this)));
        this.primaryStage.show();
    }

    private void initSimulations(int mapWidth, int mapHeight, int startEnergy, int moveEnergy,
                                 int plantEnergy, double jungleRatio, int animalsCount,
                                 boolean isMagicBreedingAllowedUM, boolean isMagicBreedingAllowedBM) {
        AbstractWorldMap unboundedMap = new UnboundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, isMagicBreedingAllowedUM);
        SimulationEngine unboundedSimulationEngine = new SimulationEngine(unboundedMap);
        this.leftSimulation = new Simulation(unboundedSimulationEngine, unboundedMap, this.width, this.height);
        this.leftSimulationThread = new Thread(this.leftSimulation);

        AbstractWorldMap boundedMap = new BoundedWorldMap(mapWidth, mapHeight,
                startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, isMagicBreedingAllowedBM);
        SimulationEngine boundedSimulationEngine = new SimulationEngine(boundedMap);
        this.rightSimulation = new Simulation(boundedSimulationEngine, boundedMap, this.width, this.height);
        this.rightSimulationThread = new Thread(this.rightSimulation);
    }

    private void styleElements() {
        this.leftSimulation.setPadding(new Insets(0,20,0,0));
        this.rightSimulation.setPadding(new Insets(0,0,0,20));
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
    public void startButtonClicked(LinkedHashMap<IParameter, String> parameterToString) {
        int mapWidth = Integer.parseInt(parameterToString.get(SimulationParameter.MAP_WIDTH));
        int mapHeight = Integer.parseInt(parameterToString.get(SimulationParameter.MAP_HEIGHT));
        int startEnergy = Integer.parseInt(parameterToString.get(SimulationParameter.START_ENERGY));
        int moveEnergy = Integer.parseInt(parameterToString.get(SimulationParameter.MOVE_ENERGY));
        int plantEnergy = Integer.parseInt(parameterToString.get(SimulationParameter.PLANT_ENERGY));
        double jungleRatio = Double.parseDouble(parameterToString.get(SimulationParameter.JUNGLE_RATIO));
        int animalsCount = Integer.parseInt(parameterToString.get(SimulationParameter.ANIMALS_COUNT));
        boolean isMagicBreedingAllowedUM = Boolean.parseBoolean(
                parameterToString.get(SimulationParameter.IS_MAGIC_BREEDING_ALLOWED_UM));
        boolean isMagicBreedingAllowedBM = Boolean.parseBoolean(
                parameterToString.get(SimulationParameter.IS_MAGIC_BREEDING_ALLOWED_BM));

        this.width = Integer.parseInt(parameterToString.get(GuiParameter.APP_WIDTH));
        this.height = Integer.parseInt(parameterToString.get(GuiParameter.APP_HEIGHT));

        initSimulations(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount,
                        isMagicBreedingAllowedUM, isMagicBreedingAllowedBM);
        styleElements();
        positionElements();
        startSimulations();
    }
}
