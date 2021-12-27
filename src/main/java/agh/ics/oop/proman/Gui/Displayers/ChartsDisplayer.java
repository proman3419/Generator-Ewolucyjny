package agh.ics.oop.proman.Gui.Displayers;

import agh.ics.oop.proman.Entities.Epoch;
import agh.ics.oop.proman.Gui.Chart;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class ChartsDisplayer extends GridPane {
    private final Chart animalsCountChart = new Chart(0, "Animals count");
    private final Chart plantsCountChart = new Chart(0, "Plants count");
    private final Chart averageAnimalEnergyChart = new Chart(0, "Avg animal energy");
    private final Chart averageAnimalLifespanChart = new Chart(0, "Avg animal lifespan");
    private final Chart averageChildrenCountChart = new Chart(0, "Avg children count");

    public ChartsDisplayer() {
        positionElements();
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.animalsCountChart.getChart(), 0, 0, 1, 1);
            this.add(this.plantsCountChart.getChart(), 0, 1, 1, 1);
            this.add(this.averageAnimalEnergyChart.getChart(), 0, 2, 1, 1);
            this.add(this.averageAnimalLifespanChart.getChart(), 0, 3, 1, 1);
            this.add(this.averageChildrenCountChart.getChart(), 0, 4, 1, 1);
        });
    }

    public void update(Epoch epoch) {
        Platform.runLater(() -> {
            this.animalsCountChart.update(epoch.getId(), epoch.getAnimalsCount());
            this.plantsCountChart.update(epoch.getId(), epoch.getPlantsCount());
            this.averageAnimalEnergyChart.update(epoch.getId(), epoch.getAverageAnimalEnergy());
            this.averageAnimalLifespanChart.update(epoch.getId(), epoch.getAverageAnimalLifespan());
            this.averageChildrenCountChart.update(epoch.getId(), epoch.getAverageChildrenCount());
        });
    }
}
