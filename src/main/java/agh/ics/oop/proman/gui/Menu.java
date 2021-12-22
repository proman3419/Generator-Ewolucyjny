package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.enums.SimulationParameter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;

public class Menu extends GridPane {
    private final App app;
    private final LinkedHashMap<SimulationParameter, TextField> simulParamToInputField = new LinkedHashMap<>();

    public Menu(App app) {
        this.app = app;

        int currRow = 0;
        for (SimulationParameter simulParam : SimulationParameter.values()) {
            this.add(new Label(simulParam.toString()), 0, currRow, 1, 1);

            TextField inputField = new TextField(simulParam.getDefaultValue());
            this.add(inputField, 1, currRow, 1, 1);
            this.simulParamToInputField.put(simulParam, inputField);

            currRow++;
        }

        Button startButton = new Button("Simulate");
        this.add(startButton, 1, currRow, 1, 1);

        startButton.setOnAction(click -> {
            this.app.startButtonClicked(parseInputs());
        });
    }

    private LinkedHashMap<SimulationParameter, String> parseInputs() {
        LinkedHashMap<SimulationParameter, String> simulParamToString = new LinkedHashMap<>();
        for (SimulationParameter simulParam : SimulationParameter.values())
            simulParamToString.put(simulParam, this.simulParamToInputField.get(simulParam).getText());

        return simulParamToString;
    }
}
