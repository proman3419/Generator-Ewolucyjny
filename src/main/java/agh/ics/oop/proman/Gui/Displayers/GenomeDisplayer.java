package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.MapElements.Animal.Genome;
import agh.ics.oop.proman.Settings.GuiConstants;
import agh.ics.oop.proman.Settings.SimulationConstants;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class GenomeDisplayer extends GridPane {
    private void display(Genome genome) {
        for (int i = 0; i < SimulationConstants.genesInGenomeCount; i++) {
            String labelContent = genome == null ? "-" : genome.getGene(i).toString();
            Label label = new Label(labelContent);
            label.setFont(new Font(GuiConstants.genomeDisplayerFontSize));
            this.add(label, i, 0, 1, 1);
            this.getColumnConstraints().add(new ColumnConstraints(GuiConstants.genomeDisplayerSegmentWidth));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }

        this.setGridLinesVisible(false);
        this.setGridLinesVisible(true);
    }

    public void update(Genome genome) {
        Platform.runLater(() -> {
            this.getChildren().clear();
            this.getRowConstraints().clear();
            this.getColumnConstraints().clear();
            display(genome);
        });
    }
}
