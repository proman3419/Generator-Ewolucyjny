package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Settings.GuiConstants;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class Chart {
    private final NumberAxis xAxis;
    private final ScatterChart<Number, Number> chart;
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();

    // Only x axis is restricted
    public Chart(double xAxisStartValue, String chartTitle) {
        this.xAxis = new NumberAxis();
        this.xAxis.setAutoRanging(false);
        this.xAxis.setLowerBound(xAxisStartValue);
        this.xAxis.setUpperBound(xAxisStartValue + GuiConstants.chartXAxisBoundariesSpan);

        this.chart = new ScatterChart<>(this.xAxis, new NumberAxis());
        this.chart.getData().add(this.series);
        this.chart.setLegendVisible(false);
        this.chart.setTitle(chartTitle);
    }

    public void update(Number x, Number y) {
        if (this.series.getData().size() >= GuiConstants.chartXAxisBoundariesSpan) {
            this.series.getData().remove(0);
            double lowerBound = this.xAxis.getLowerBound();
            lowerBound++;
            this.xAxis.setLowerBound(lowerBound);
            this.xAxis.setUpperBound(lowerBound + GuiConstants.chartXAxisBoundariesSpan);
        }
        this.series.getData().add(new XYChart.Data<>(x, y));
    }

    //region Getters ---------------------------------------------------------------------------------------------------
    public ScatterChart<Number, Number> getChart() {
        return chart;
    }
    //endregion Getters ------------------------------------------------------------------------------------------------
}
