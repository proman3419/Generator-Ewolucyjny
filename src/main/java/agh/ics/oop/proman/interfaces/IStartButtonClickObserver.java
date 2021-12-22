package agh.ics.oop.proman.interfaces;

import agh.ics.oop.proman.enums.SimulationParameter;

import java.util.LinkedHashMap;

public interface IStartButtonClickObserver {
    void startButtonClicked(LinkedHashMap<SimulationParameter, String> simulParamToString);
}
