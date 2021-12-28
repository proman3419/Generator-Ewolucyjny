package agh.ics.oop.proman.Observers;

import agh.ics.oop.proman.Settings.IParameter;

import java.util.LinkedHashMap;

public interface IStartButtonClickObserver {
    void startButtonClicked(LinkedHashMap<IParameter, String> parameterToString);
}
