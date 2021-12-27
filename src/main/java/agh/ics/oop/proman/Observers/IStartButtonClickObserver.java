package agh.ics.oop.proman.Observers;

import agh.ics.oop.proman.Settings.IParameter;
import agh.ics.oop.proman.Settings.SimulationParameter;
import agh.ics.oop.proman.Settings.GuiParameter;

import java.util.LinkedHashMap;

public interface IStartButtonClickObserver {
    void startButtonClicked(LinkedHashMap<IParameter, String> parameterToString);
}
