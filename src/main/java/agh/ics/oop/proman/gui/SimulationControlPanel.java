package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.SimulationEngine;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SimulationControlPanel extends GridPane {
    private final SimulationEngine simulationEngine;
    private final Button pauseButton;

    public SimulationControlPanel(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
        this.pauseButton = new Button("PAUSE");
        addOnClickEvents();
        positionElements();
    }

    private void addOnClickEvents() {
        this.pauseButton.setOnAction(click -> {
            if (this.simulationEngine.isPaused()) {
                this.simulationEngine.resume();
                this.pauseButton.setText("PAUSE");
            }
            else {
                this.simulationEngine.pause();
                this.pauseButton.setText("RESUME");
            }
        });
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(pauseButton, 0, 0, 1, 1);
        });
    }
}
