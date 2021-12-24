package agh.ics.oop.proman.gui;

import javafx.application.Platform;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

public class GraphsDisplayer extends GridPane {
    private final ScatterChart<Number, Number> animalsCountGraph;
    private final ScatterChart<Number, Number> plantsCountGraph;
//    private final ScatterChart<Number, Double> averageAnimalEnergyGraph;
//    private final ScatterChart<Number, Double> averageAnimalLifespanGraph;
//    private final ScatterChart<Number, Double> averageChildrenCountGraph;

    public GraphsDisplayer() {
        this.animalsCountGraph = initGraph("Epoch", "Animals count");
        this.plantsCountGraph = initGraph("Epoch","Plants count");
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
        });
    }

    public void update(int epoch, int animalsCount, int plantsCount) {
        updateGraph(this.animalsCountGraph, epoch, animalsCount);
        updateGraph(this.plantsCountGraph, epoch, plantsCount);
    }

    private void updateGraph(ScatterChart<Number, Number> graph, int xValue, int yValue) {
        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.getData().add(new XYChart.Data<>( xValue, yValue));
        graph.getData().add(dataSeries);
    }
}
