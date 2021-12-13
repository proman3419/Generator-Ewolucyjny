package agh.ics.oop.proman.classes;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final List<AbstractWorldMap> maps = new ArrayList<>();

    public SimulationEngine(int mapWidth, int mapHeight, int startEnergy, int moveEnergy, int plantEnergy,
                            float jungleRatio, int animalsCount, int genesCount) {
        maps.add(new BoundedWorldMap(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, genesCount));
        maps.add(new UnboundedWorldMap(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, genesCount));
    }

    public void run() {
    }
}
