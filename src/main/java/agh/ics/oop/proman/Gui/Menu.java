package agh.ics.oop.proman.Gui;

import agh.ics.oop.proman.Settings.GuiConstants;
import agh.ics.oop.proman.Settings.IParameter;
import agh.ics.oop.proman.Settings.SimulationParameter;
import agh.ics.oop.proman.Settings.GuiParameter;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedHashMap;

public class Menu extends GridPane {
    private final App app;
    private final LinkedHashMap<IParameter, Control> parameterToControl = new LinkedHashMap<>();
    private int currRow = 0;

    public Menu(App app) {
        this.app = app;
        addElements();
    }

    private void addElements() {
        addSectionTitleLabel("Simulation settings");
        addElements(SimulationParameter.values());
        addSectionTitleLabel("App settings");
        addElements(GuiParameter.values());
        addStartButton();
    }

    private void addSectionTitleLabel(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(GuiConstants.fontFamily, FontWeight.BOLD,
                                     GuiConstants.menuSectionTitleFontSize));
        titleLabel.setPadding(new Insets(10,0,5,5));

        this.add(titleLabel, 0, this.currRow, 1, 1);
        this.currRow++;
    }

    private void addControlTitleLabel(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(GuiConstants.fontFamily, GuiConstants.menuControlFontSize));
        titleLabel.setPadding(new Insets(5,10,5,10));

        this.add(titleLabel, 0, this.currRow, 1, 1);
    }

    private void addElements(IParameter[] parameters) {
        for (IParameter parameter : parameters) {
            switch (parameter.getInputGuiControlType()) {
                case TEXT_FIELD: {addTextField(parameter); break;}
                case CHECK_BOX: {addCheckBox(parameter); break;}
                case NONE: continue;
            }
            addControlTitleLabel(parameter.toString());
            this.currRow++;
        }
    }

    private void addCheckBox(IParameter parameter) {
        CheckBox checkBox = new CheckBox();
        checkBox.setIndeterminate(Boolean.parseBoolean(parameter.getDefaultValue()));
        this.add(checkBox, 1, this.currRow, 1, 1);
        this.parameterToControl.put(parameter, checkBox);
    }

    private void addTextField(IParameter parameter) {
        TextField textField = new TextField(parameter.getDefaultValue());
        textField.setFont(Font.font(GuiConstants.fontFamily, GuiConstants.menuControlFontSize));
        this.add(textField, 1, this.currRow, 1, 1);
        this.parameterToControl.put(parameter, textField);
    }

    private void addStartButton() {
        Button startButton = new Button("Simulate");
        this.add(startButton, 1, this.currRow, 1, 1);

        startButton.setOnAction(click -> {
            this.app.startButtonClicked(parseInputs());
        });
    }

    private LinkedHashMap<IParameter, String> parseInputs() {
        LinkedHashMap<IParameter, String> parameterToString = new LinkedHashMap<>();

        for (IParameter parameter : this.parameterToControl.keySet()) {
            String parameterValue = "";
            switch (parameter.getInputGuiControlType()) {
                case TEXT_FIELD: {parameterValue = ((TextField) this.parameterToControl.get(parameter)).getText();
                                  break;}
                case CHECK_BOX: {parameterValue = ((CheckBox) this.parameterToControl.get(parameter)).isSelected()
                                                 ? "true" : "false";
                                  break;}
                case NONE: continue;
            }
            parameterToString.put(parameter, parameterValue);
        }

        return parameterToString;
    }
}
