package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.classes.Epoch;
import javafx.application.Platform;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class GraphsDisplayer extends GridPane {
    private final ScatterChart<Number, Number> animalsCountGraph;
    private final ScatterChart<Number, Number> plantsCountGraph;
    private final ScatterChart<Number, Number> averageAnimalEnergyGraph;
    private final ScatterChart<Number, Number> averageAnimalLifespanGraph;
    private final ScatterChart<Number, Number> averageChildrenCountGraph;

    public GraphsDisplayer() {
        this.animalsCountGraph = initGraph("Epoch", "Animals count");
        this.plantsCountGraph = initGraph("Epoch","Plants count");
        this.averageAnimalEnergyGraph = initGraph("Epoch","Average animal energy");
        this.averageAnimalLifespanGraph = initGraph("Epoch","Average animal lifespan");
        this.averageChildrenCountGraph = initGraph("Epoch","Average children count");
        positionElements();
    }

    private ScatterChart<Number, Number> initGraph(String xAxisName, String yAxisName) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xAxisName);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisName);

        ScatterChart<Number, Number> graph = new ScatterChart<>(xAxis, yAxis);
        graph.setLegendVisible(false);

        return graph;
    }

    private void positionElements() {
        Platform.runLater(() -> {
            this.add(this.animalsCountGraph, 0, 0, 1, 1);
            this.add(this.plantsCountGraph, 0, 1, 1, 1);
            this.add(this.averageAnimalEnergyGraph, 0, 2, 1, 1);
            this.add(this.averageAnimalLifespanGraph, 0, 3, 1, 1);
            this.add(this.averageChildrenCountGraph, 0, 4, 1, 1);
        });
    }

    private void updateGraph(ScatterChart<Number, Number> graph, Number xValue, Number yValue) {
        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.getData().add(new XYChart.Data<>(xValue, yValue));
        graph.getData().add(dataSeries);
    }

    public void update(Epoch epoch) {
        Platform.runLater(() -> {
            updateGraph(this.animalsCountGraph, epoch.getId(), epoch.getAnimalsCount());
            updateGraph(this.plantsCountGraph, epoch.getId(), epoch.getPlantsCount());
            updateGraph(this.averageAnimalEnergyGraph, epoch.getId(), epoch.getAverageAnimalEnergy());
            updateGraph(this.averageAnimalLifespanGraph, epoch.getId(), epoch.getAverageAnimalLifespan());
            updateGraph(this.averageChildrenCountGraph, epoch.getId(), epoch.getAverageChildrenCount());
        });
    }
}
