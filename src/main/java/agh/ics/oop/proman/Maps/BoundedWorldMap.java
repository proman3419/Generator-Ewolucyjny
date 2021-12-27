package agh.ics.oop.proman.Maps;

public class BoundedWorldMap extends AbstractWorldMap {
    public BoundedWorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio,
                            int animalsCount, boolean isMagicBreedingAllowed) {
        super(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, animalsCount, isMagicBreedingAllowed);
    }
}
