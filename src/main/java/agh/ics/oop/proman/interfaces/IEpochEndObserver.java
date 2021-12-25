package agh.ics.oop.proman.interfaces;

import agh.ics.oop.proman.classes.Genome;

public interface IEpochEndObserver {
    void epochEnded(int epoch, int animalsCount, int plantsCount, double averageAnimalEnergy,
                    double averageAnimalLifespan, double averageChildrenCount, Genome dominantGenome);
}
