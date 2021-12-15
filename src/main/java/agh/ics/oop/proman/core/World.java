package agh.ics.oop.proman.core;

import agh.ics.oop.proman.classes.AbstractWorldMap;
import agh.ics.oop.proman.classes.BoundedWorldMap;
import agh.ics.oop.proman.classes.SimulationEngine;
import agh.ics.oop.proman.classes.UnboundedWorldMap;

import java.util.ArrayList;

public class World {
    public static void main(String[] args) {
        int mapWidth = 100;
        int mapHeight = 30;
        int startEnergy = 10;
        int moveEnergy = 1;
        int plantEnergy = 2;
        double jungleRatio = 0.25;
        int animalsCount = 10;

        ArrayList<AbstractWorldMap> maps = new ArrayList<>();
        maps.add(new BoundedWorldMap(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount));
        maps.add(new UnboundedWorldMap(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount));

        SimulationEngine simulationEngine = new SimulationEngine(maps);
        simulationEngine.run();
    }
}
