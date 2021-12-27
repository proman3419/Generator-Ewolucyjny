package agh.ics.oop.proman.Observers;

import agh.ics.oop.proman.Enums.SimulationParameter;

import java.util.LinkedHashMap;

public interface IStartButtonClickObserver {
    void startButtonClicked(LinkedHashMap<SimulationParameter, String> simulParamToString);
}
