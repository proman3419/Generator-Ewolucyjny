package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.MapElements.Animal.Genome;
import agh.ics.oop.proman.Core.Constants;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class GenomeDisplayer extends GridPane {
    private void display(Genome genome) {
        for (int i = 0; i < Constants.genesInGenomeCount; i++) {
            String labelContent = genome == null ? "-" : genome.getGene(i).toString();
            Label label = new Label(labelContent);
            label.setFont(new Font(24));
            this.add(label, i, 0, 1, 1);
            this.getColumnConstraints().add(new ColumnConstraints(22));
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
