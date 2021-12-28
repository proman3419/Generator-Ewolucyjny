package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Classes.ReportGenerator;
import agh.ics.oop.proman.Classes.SimulationEngine;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SimulationControlPanel extends GridPane {
    private final SimulationEngine simulationEngine;
    private final Button pauseButton;
    private final Button saveReportButton;
    private final ReportGenerator reportGenerator;

    public SimulationControlPanel(SimulationEngine simulationEngine, ReportGenerator reportGenerator) {
        this.simulationEngine = simulationEngine;
        this.pauseButton = new Button("PAUSE");
        this.saveReportButton = new Button("SAVE REPORT");
        this.reportGenerator = reportGenerator;
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

        this.saveReportButton.setOnAction(click -> {
            if (this.simulationEngine.isPaused() || this.simulationEngine.isFinished()) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    file = forceExtension(file, ".csv");
                    this.reportGenerator.saveReport(file);
                }
            }
        });
    }

    private File forceExtension(File file, String extension) {
        if (!file.getName().endsWith(extension))
            return new File(file.getAbsolutePath() + extension);

        return file;
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.pauseButton, 0, 0, 1, 1);
            this.add(this.saveReportButton, 1, 0, 1, 1);
        });
    }
}
