package agh.ics.oop.proman.gui;

import agh.ics.oop.proman.enums.SimulationParameter;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedHashMap;

public class Menu extends GridPane {
    private final App app;
    private final LinkedHashMap<SimulationParameter, CheckBox> simulParamToCheckBox = new LinkedHashMap<>();
    private final LinkedHashMap<SimulationParameter, TextField> simulParamToTextField = new LinkedHashMap<>();
    private int currRow = 0;

    public Menu(App app) {
        this.app = app;
        addElements();
    }

    private void addElements() {
        for (SimulationParameter simulParam : SimulationParameter.values()) {
            this.add(new Label(simulParam.toString()), 0, this.currRow, 1, 1);

            if (simulParam == SimulationParameter.IS_MAGIC_BREEDING_ALLOWED_UM ||
                simulParam == SimulationParameter.IS_MAGIC_BREEDING_ALLOWED_BM)
                addCheckBox(simulParam);
            else
                addTextField(simulParam);

            this.currRow++;
        }

        Button startButton = new Button("Simulate");
        this.add(startButton, 1, this.currRow, 1, 1);

        startButton.setOnAction(click -> {
            this.app.startButtonClicked(parseInputs());
        });
    }

    private void addCheckBox(SimulationParameter simulParam) {
        CheckBox checkBox = new CheckBox();
        checkBox.setIndeterminate(Boolean.parseBoolean(simulParam.getDefaultValue()));
        this.add(checkBox, 1, this.currRow, 1, 1);
        this.simulParamToCheckBox.put(simulParam, checkBox);
    }

    private void addTextField(SimulationParameter simulParam) {
        TextField textField = new TextField(simulParam.getDefaultValue());
        this.add(textField, 1, this.currRow, 1, 1);
        this.simulParamToTextField.put(simulParam, textField);
    }

    private LinkedHashMap<SimulationParameter, String> parseInputs() {
        LinkedHashMap<SimulationParameter, String> simulParamToString = new LinkedHashMap<>();

        for (SimulationParameter simulParam : this.simulParamToCheckBox.keySet()) {
            boolean value = this.simulParamToCheckBox.get(simulParam).isSelected();
            simulParamToString.put(simulParam, value ? "true" : "false");
        }

        for (SimulationParameter simulParam : this.simulParamToTextField.keySet())
            simulParamToString.put(simulParam, this.simulParamToTextField.get(simulParam).getText());

        return simulParamToString;
    }
}
