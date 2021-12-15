package agh.ics.oop.proman.classes;

import java.util.List;

public class SimulationEngine {
    private final List<AbstractWorldMap> maps;

    public SimulationEngine(List<AbstractWorldMap> maps) {
        this.maps = maps;
    }

    public void run() {
        for (AbstractWorldMap map : maps) {
            map.removeDeadAnimals();
            map.animalsMove();
            map.animalsEat();
            map.animalsBreed();
            map.growGrass();
        }
    }
}
